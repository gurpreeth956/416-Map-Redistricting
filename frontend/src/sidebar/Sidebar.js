import React from 'react';
import Filters from "./Filters.js"
import HistoryList from "./HistoryList.js"
import GenerateMapForm from "./GenerateMapForm.js"
import $ from 'jquery';
window.$ = $;

class Sidebar extends React.Component {
  constructor(props) {
    super(props);
    this.loadJob = this.loadJob.bind(this);
    this.cancelJob = this.cancelJob.bind(this);
    this.deleteJob = this.deleteJob.bind(this);
    this.addJob = this.addJob.bind(this);
    this.getCompactnessFromInt = this.getCompactnessFromInt.bind(this);
    this.state = {
      jobs:[],
      loadedJob:null
    };
  }

  componentDidMount() {
    this.interval = setInterval(() =>
      fetch("http://localhost:8080/getJobHistory", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*"
        }
      })
      .then(response  => response.json())
      .then(
        (result) => {
          this.setState({
            jobs: result
          });
          console.log(result);
        }
      ), 5000
    );
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  getCompactnessFromInt(compactness) {
    if(compactness === 0) {
      return "Not Compact";
    } else if(compactness === 1) {
      return "Somewhat Compact";
    } else{
      return "Very Compact";
    }
  }
  
  loadJob(index) {
    var job;
    if(index === -1) {
      job = null;
      this.setState({
        loadedJob: null
      });
    } else {
      job = this.state.jobs[index];
      this.setState({
        loadedJob: job
      });
    }
    this.props.setDefaultFilters();
    this.props.loadMaps(job);
    this.props.loadBoxWhisker(job);
  }

	cancelJob(index) {
		var job = this.state.jobs[index];
		const data = {jobId : job.id}
		const url = 'http://localhost:8080/cancelJob'
	    $.ajax({
	      url:url,
	      type:"POST",
	      data: data,
	      success: (data) => {
	      	this.setState({
	      		jobs: data
		    });
	      }
	    });
	}

	deleteJob(index) {
		var job = this.state.jobs[index];
		const data = {jobId : job.id}
		const url = 'http://localhost:8080/deleteJob'
	    $.ajax({
	      url:url,
	      type:"POST",
	      data: data,
	      success: (data) => {
	      	this.setState({
	      		jobs: data
          });
          if(this.state.loadedJob != null && this.state.loadedJob != undefined && job.id === this.state.loadedJob.id) {
            this.loadJob(-1);
          }
	      }
	    });
  }
  
  addJob(job) {
    this.setState({
      jobs: [
        job,
        ...this.state.jobs
      ]
    });
  }

  render() {
    return(
    <div id="sidebar-container" class="sidebar-expanded d-none d-md-block">
      <ul class="list-group sidebar-list-expanded overflow-auto" id="sidebar-list">
        <li class="list-group-item sidebar-separator-title text-muted d-flex align-items-center menu-collapsed">
          <small>MAIN MENU</small>
        </li>
        <a href="#" data-toggle="sidebar-collapse"
          class="bg-light list-group-item list-group-item-action d-flex align-items-center">
          <div class="d-flex w-100 justify-content-start align-items-center">
            <span id="collapse-icon" class="fa fa-2x mr-3"></span>
            <span id="collapse-text" class="menu-collapsed">Collapse</span>
          </div>
        </a>
        <a href="#home-submenu" data-toggle="collapse" aria-expanded="false"
          class="bg-light list-group-item list-group-item-action flex-column align-items-start"
          id="generate-sidebar-button">
          <div class="d-flex w-100 justify-content-start align-items-center">
            <span class="fa fa-plus fa-fw mr-3"></span>
            <span class="menu-collapsed">Generate</span>
            <span class="submenu-icon ml-auto"></span>
          </div>
        </a>
        <div id='home-submenu' class="collapse sidebar-submenu card">
          <article class="card-group-item">
            <header class="card-header">
              <h6 class="title">Generate Maps</h6>
            </header>
            <GenerateMapForm addJob={this.addJob} updateSelectedState={this.props.updateSelectedState} 
            selectedState={this.props.selectedState} getCompactnessFromInt={this.getCompactnessFromInt}></GenerateMapForm>
          </article>
        </div>
        <a href="#filter-submenu" data-toggle="collapse" aria-expanded="false"
          class="bg-light list-group-item list-group-item-action flex-column align-items-start">
          <div class="d-flex w-100 justify-content-start align-items-center">
            <span class="fa fa-filter fa-fw mr-3"></span>
            <span class="menu-collapsed">Filters</span>
            <span class="submenu-icon ml-auto"></span>
          </div>
        </a>
        <div id='filter-submenu' class="collapse sidebar-submenu card">
          <Filters loadedJob={this.state.loadedJob} updateFilters={this.props.updateFilters}
          currentIsSet={this.props.currentIsSet} averageIsSet={this.props.averageIsSet}
          extremeIsSet={this.props.extremeIsSet} blackIsSet={this.props.blackIsSet}
          whiteIsSet={this.props.whiteIsSet} asianIsSet={this.props.asianIsSet}
          hispanicIsSet={this.props.hispanicIsSet} hawaiianIsSet={this.props.hawaiianIsSet}
          nativeIsSet={this.props.nativeIsSet} districtsIsSet={this.props.districtsIsSet}
          precinctsIsSet={this.props.precinctsIsSet} heatMapIsSet={this.props.heatMapIsSet}></Filters>
        </div>
        <a href="#history-submenu" data-toggle="collapse" aria-expanded="false"
          class="bg-light list-group-item list-group-item-action flex-column align-items-start"
          id="history-sidebar-button">
          <div class="d-flex w-100 justify-content-start align-items-center">
            <span class="fa fa-history fa-fw mr-3"></span>
            <span class="menu-collapsed">History</span>
            <span class="submenu-icon ml-auto"></span>
          </div>
        </a>
        <div id='history-submenu' class="collapse sidebar-submenu card">
          <article class="card-group-item">
            <HistoryList loadJob={this.loadJob} cancelJob={this.cancelJob} 
            deleteJob={this.deleteJob} jobs={this.state.jobs} 
            loadedJob={this.state.loadedJob} getCompactnessFromInt={this.getCompactnessFromInt}></HistoryList>
          </article>
        </div>
      </ul>
    </div>
    );
  }
}

export default Sidebar;
