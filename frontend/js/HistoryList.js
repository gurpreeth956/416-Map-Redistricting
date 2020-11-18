'use strict';
const e = React.createElement;

class HistoryList extends React.Component {

	constructor(props) {
		super(props);
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

	render() {
		const {jobs} = this.state;
		return(
			<div class="list-group" id="list-tab" role="tablist">
				{jobs.map(job => {
					console.log(job)
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
							<button type="button" class="btn btn-secondary">Cancel Job</button>
							<button type="button" class="btn btn-danger">Delete Job</button>
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
							<button type="button" class="btn btn-primary">Load Map</button>
							<button type="button" class="btn btn-danger">Delete Job</button>
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
							<button type="button" class="btn btn-danger">Delete Job</button>
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