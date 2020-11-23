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
	      success: (data) => {
	      	this.setState({
	      		jobs: data
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
	      success: (data) => {
	      	this.setState({
	      		jobs: data
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
					if(job.jobStatus === "COMPLETED") {
						return(
							<a key = {job.id} class="list-group-item list-group-item-action history-item" data-toggle="collapse" href={"#expand-item-"+job.id} role="tab">
							<div class="d-flex h-100 justify-content-between">
								<h6>ID# {job.id} </h6>
								<small> {job.jobStatus} </small>
							</div>
							<p>State: {job.abbreviation}</p>
							<p>Maps to Generate: {job.numberOfMaps}</p>
							<p>Compactness: {job.userCompactness}</p>
							<p>Population Limit Difference: {job.populationDifferenceLimit}</p>
							<div class= "btn-group">
								<button type="button" class="btn btn-primary history-item-button text-nowrap" onClick={(e) => {e.stopPropagation(); this.loadAverageMap(index); this.loadExtremeMap(index); }}>Load Job</button>
								<button type="button" class="btn btn-danger history-item-button text-nowrap" onClick={(e) => {e.stopPropagation(); this.deleteJob(index); }}>Delete Job</button>
							</div>
							<div class="collapse multi-collapse" id={"expand-item-"+job.id}>
								<div class="card card-body">
            						<h4>Summary</h4>
            						<p class = "summary-paragraph">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>
								</div>
							</div>
							</a>
							)
					} else {
						return(
							<div key = {job.id} class="list-group-item history-item" role="tab">
							<div class="d-flex h-100 justify-content-between">
								<h6>ID# {job.id} </h6>
								<small> {job.jobStatus} </small>
							</div>
							<p>State: {job.abbreviation}</p>
							<p>Maps to Generate: {job.numberOfMaps}</p>
							<p>Compactness: {job.userCompactness}</p>
							<p>Population Limit Difference: {job.populationDifferenceLimit}</p>
							<div class= "btn-group">
							{job.jobStatus === "WAITING" || job.jobStatus === "RUNNING" ? 
								<button type="button" class="btn btn-secondary history-item-button text-nowrap enabled" onClick={(e) => this.cancelJob(index)}>Cancel Job</button> :
								null
							}
							<button type="button" class="btn btn-danger history-item-button text-nowrap enabled" onClick={(e) => this.deleteJob(index)}>Delete Job</button>
							</div>
							</div>
							)
					}
					
				})}
			</div>		
		);
	}
}

const domContainer = document.querySelector('#HistoryList');
ReactDOM.render(e(HistoryList), domContainer);