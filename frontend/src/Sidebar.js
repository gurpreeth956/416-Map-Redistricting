import React from 'react';
import Filters from "./sidebar/Filters.js"
import HistoryList from "./sidebar/HistoryList.js"
import GenerateMapForm from "./sidebar/GenerateMapForm.js"
import $ from 'jquery';
window.$ = $;

class Sidebar extends React.Component {
  constructor(props) {
    super(props);
    this.loadJob = this.loadJob.bind(this);
    this.loadAverageMap = this.loadAverageMap.bind(this);
    this.loadExtremeMap = this.loadExtremeMap.bind(this);
    this.cancelJob = this.cancelJob.bind(this);
    this.deleteJob = this.deleteJob.bind(this);
    this.addJob = this.addJob.bind(this);
    this.state = {
      jobs:[],
      loadedJob:null
    };
  }

  componentDidMount() {
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
			}
		)
  }
  
  loadAverageMap(job) {
		console.log(job);
		const data = {stateId : job.averageStateId}
		const url = 'http://localhost:8080/getDistricting'
	    $.ajax({
	      url:url,
	      type:"POST",
	      data: data,
	      success: () => {
	      	this.setState({
	      		loadedJob: data
		    });
	      }
	    });
	}

	loadExtremeMap(job) {
		const data = {stateId : job.extremeStateId}
		const url = 'http://localhost:8080/getDistricting'
	    $.ajax({
	      url:url,
	      type:"POST",
	      data: data,
	      success: () => {
	      	this.setState({
	      		jobs: data
		    });
	      }
	    });
  }
  
  loadJob(index) {
    var job = this.state.jobs[index];
    console.log(job);
    this.setState({
      loadedJob: job
    });
    console.log(this.state);
    // this.loadAverageMap(job);
    // this.loadExtremeMap(job);
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
    console.log(index);
    console.log(this.state.jobs);
    console.log(this.state.jobs[index]);
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
	      }
	    });
  }
  
  addJob(job) {
    this.setState({
      jobs: [
        ...this.state.jobs,
        job
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
            <GenerateMapForm addJob={this.addJob}></GenerateMapForm>
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
          <Filters loadedJob={this.state.loadedJob}></Filters>
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
            deleteJob={this.deleteJob} jobs={this.state.jobs}></HistoryList>
          </article>
        </div>
      </ul>
    </div>
    );
  }
}

export default Sidebar;
