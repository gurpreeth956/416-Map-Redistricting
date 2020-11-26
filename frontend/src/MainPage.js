import React from 'react';
import $ from 'jquery';
import Sidebar from './sidebar/Sidebar.js';
window.$ = $;

class MainPage extends React.Component {

    constructor(props) {
        super(props);
        this.loadAverageMap = this.loadAverageMap.bind(this);
        this.loadExtremeMap = this.loadExtremeMap.bind(this);
        this.state = {
            averageMap: "",
            extremeMap: ""
        };
    }

    loadAverageMap(job) {
		console.log(job);
		const data = {stateId : job.averageStateId}
		const url = 'http://localhost:8080/getDistricting'
	    $.ajax({
	      url:url,
	      type:"POST",
	      data: data,
	      success: (data) => {
	      	this.setState({
	      		averageMap: data
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
	      success: (data) => {
	      	this.setState({
	      		extremeMap: data
		    });
	      }
	    });
    }

    render() {
        return(
            <div>
                <Sidebar loadAverageMap={this.loadAverageMap}
                loadExtremeMap={this.loadExtremeMap}></Sidebar>
            </div>
        )
    }
}

export default MainPage;