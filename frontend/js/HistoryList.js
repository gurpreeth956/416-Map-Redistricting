'use strict';
const e = React.createElement;

class HistoryList extends React.Component {

	constructor(props) {
		super(props);
		this.cancelJob = this.cancelJob.bind(this);
		this.deleteJob = this.deleteJob.bind(this);
		this.state = {
			jobs: []
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

	loadAverageMap(index) {
		var job = this.state.jobs[index];
		console.log(job);
		const data = {stateId : job.averageStateId}
		const url = 'http://localhost:8080/getDistricting'
	    $.ajax({
	      url:url,
	      type:"POST",
	      data: data,
	      success: () => {
	      	// Load in geojson
	      }
	    });
	}

	loadExtremeMap(index) {
		var job = this.state.jobs[index];
		console.log(job);
		const data = {stateId : job.extremeStateId}
		const url = 'http://localhost:8080/getDistricting'
	    $.ajax({
	      url:url,
	      type:"POST",
	      data: data,
	      success: () => {
	      	// Load in geojson
	      }
	    });
	}

	cancelJob(index) {
		var job = this.state.jobs[index];
		console.log(job);
		const data = {jobId : job.id}
		const url = 'http://localhost:8080/cancelJob'
	    $.ajax({
	      url:url,
	      type:"POST",
	      data: data,
	      success: () => {
	      	this.setState({
		      jobs: [
		         ...this.state.jobs.slice(0,index),
		         Object.assign({}, this.state.jobs[index], {jobStatus: "CANCELLED"}),
		         ...this.state.jobs.slice(index+1)
		      ]
		    });
	      }
	    });
	}

	deleteJob(index) {
		var job = this.state.jobs[index];
		console.log(job);
		const data = {jobId : job.id}
		const url = 'http://localhost:8080/deleteJob'
	    $.ajax({
	      url:url,
	      type:"POST",
	      data: data,
	      success: () => {
	      	this.setState({
	      		jobs:[
			      ...this.state.jobs.slice(0, index),
			      ...this.state.jobs.slice(index+1)
			    ]
		    });
	      }
	    });
	}

	render() {
		const {jobs} = this.state;
		console.log(this.state);
		return(
			<div class="list-group" id="list-tab" role="tablist">
				{jobs.map((job, index) => {
					if(job.jobStatus === "WAITING" || job.jobStatus === "RUNNING") {
						return(
							<a key = {job.id} class="list-group-item list-group-item-action history-item" data-toggle="modal" href="#history-modal" role="tab">
							<div class="d-flex h-100 justify-content-between">
								<h6>ID# {job.id} </h6>
								<small> {job.jobStatus} </small>
							</div>
							<p>State: {job.abbreviation}</p>
							<p>Maps to Generate: {job.numberOfMaps}</p>
							<p>Compactness: {job.userCompactness}</p>
							<p>Population Limit Difference: {job.populationDifferenceLimit}</p>
							<button type="button" class="btn btn-secondary" onClick={(e) => this.cancelJob(index)}>Cancel Job</button>
							<button type="button" class="btn btn-danger" onClick={(e) => this.deleteJob(index)}>Delete Job</button>
							</a>
							)
					} else if(job.jobStatus === "COMPLETED") {
						return(
							<a key = {job.id} class="list-group-item list-group-item-action history-item" data-toggle="modal" href="#history-modal" role="tab">
							<div class="d-flex h-100 justify-content-between">
								<h6>ID# {job.id} </h6>
								<small> {job.jobStatus} </small>
							</div>
							<p>State: {job.abbreviation}</p>
							<p>Maps to Generate: {job.numberOfMaps}</p>
							<p>Compactness: {job.userCompactness}</p>
							<p>Population Limit Difference: {job.populationDifferenceLimit}</p>
							<button type="button" class="btn btn-primary" onClick={() => {loadAverageMap(index); loadExtremeMap(index); }}>Load Job</button>
							<button type="button" class="btn btn-danger" onClick={(e) => this.deleteJob(index)}>Delete Job</button>
							</a>
							)
					} else {
						return(
							<a key = {job.id} class="list-group-item list-group-item-action history-item" data-toggle="modal" href="#history-modal" role="tab">
							<div class="d-flex h-100 justify-content-between">
								<h6>ID# {job.id} </h6>
								<small> {job.jobStatus} </small>
							</div>
							<p>State: {job.abbreviation}</p>
							<p>Maps to Generate: {job.numberOfMaps}</p>
							<p>Compactness: {job.userCompactness}</p>
							<p>Population Limit Difference: {job.populationDifferenceLimit}</p>
							<button type="button" class="btn btn-danger" onClick={(e) => this.deleteJob(index)}>Delete Job</button>
							</a>

							)
					}
					
				})}
			</div>		
		);
	}
}

const domContainer = document.querySelector('#HistoryList');
ReactDOM.render(e(HistoryList), domContainer);