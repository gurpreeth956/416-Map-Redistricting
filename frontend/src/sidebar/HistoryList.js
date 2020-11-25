import React from 'react';
import $ from 'jquery';
window.$ = $;

class HistoryList extends React.Component {

	constructor(props) {
		super(props);
	}

	render() {
		const jobs = this.props.jobs;
		return(
			<div class="list-group" id="list-tab" role="tablist">
				{jobs && jobs.map((job, index) => {
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
								<button type="button" class="btn btn-primary history-item-button text-nowrap" onClick={(e) => {e.stopPropagation(); this.props.loadJob(index); }}>Load Job</button>
								<button type="button" class="btn btn-danger history-item-button text-nowrap" onClick={(e) => {e.stopPropagation(); this.props.deleteJob(index); }}>Delete Job</button>
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
							{job.jobStatus === "WAITING" || job.jobStatus === "RUNNING" && job.onSeaWulf !== -1 ? 
								<button type="button" class="btn btn-secondary history-item-button text-nowrap enabled" onClick={(e) => this.props.cancelJob(index)}>Cancel Job</button> :
								null
							}
							{job.jobStatus === "CANCELLED" || job.onSeaWulf !== -1 ?
								<button type="button" class="btn btn-danger history-item-button text-nowrap enabled" onClick={(e) => this.props.deleteJob(index)}>Delete Job</button> :
								null
							}
							</div>
							</div>
							)
					}
					
				})}
			</div>		
		);
	}
}

export default HistoryList;