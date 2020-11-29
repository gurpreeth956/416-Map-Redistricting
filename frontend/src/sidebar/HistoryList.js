import React from 'react';
import ReactDOM from 'react-dom';
import $ from 'jquery';
import BoxWhisker from "../BoxWhisker.js";
const modalRoot = document.getElementById('modal-root');

window.$ = $;

class HistoryList extends React.Component {

	constructor(props) {
		super(props);
		this.el = document.createElement('div');
	}

	componentDidMount() {
        modalRoot.appendChild(this.el);
    }

    componentWillUnmount() {
        modalRoot.removeChild(this.el);
	}
	
	render() {
		const jobs = this.props.jobs;
		var loaded_id = null;
		if(this.props.loadedJob != null && this.props.loadedJob != undefined) {
			loaded_id = this.props.loadedJob.id;
		}
		return(
			<div class="list-group" id="list-tab" role="tablist">
				{jobs && jobs.map((job, index) => {
					if(job.jobStatus === "COMPLETED") {
						return(
							<a key = {job.id} class="list-group-item list-group-item-action history-item" data-toggle="modal" href={"#history-modal-"+job.id} role="tab">
							<div class="d-flex h-100 justify-content-between">
								<h6>ID# {job.id} </h6>
								<small> {job.jobStatus} </small>
							</div>
							<p>State: {job.abbreviation}</p>
							<p>Maps to Generate: {job.numberOfMaps}</p>
							<p>Compactness: {job.userCompactness}</p>
							<p>Population Limit Difference: {job.populationDifferenceLimit}</p>
							<div class= "btn-group">
								{loaded_id != null && loaded_id != undefined && loaded_id === job.id ? 
									<button type="button" class="btn btn-primary history-item-button text-nowrap" disabled onClick={(e) => {e.stopPropagation(); this.props.loadJob(index); }}>Load Job</button> :
									<button type="button" class="btn btn-primary history-item-button text-nowrap" onClick={(e) => {e.stopPropagation(); this.props.loadJob(index); }}>Load Job</button>}
								<button type="button" class="btn btn-danger history-item-button text-nowrap" onClick={(e) => {e.stopPropagation(); this.props.deleteJob(index); }}>Delete Job</button>
							</div>
							{ReactDOM.createPortal(this.generateModal(job), this.el)}
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

	generateModal(job) {
		return(
			<div class="modal fade" id={"history-modal-"+job.id} tabindex="-1" role="dialog" aria-labelledby="historyTitle" aria-hidden="true">
				<div class="modal-dialog modal-lg" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title" id="historyTitle">Job ID# {job.id}</h5>

						</div>
						<div class="modal-body">
							<div>
								<BoxWhisker plot={job.boxWhiskers}/>
							</div>
							{/* <div class="summary-section">
							
							
								<h1 style ={{paddingBottom: "50px", paddingTop: "20px"}}>Summary</h1>
								<p class = "summary-paragraph">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>
								<p class = "summary-paragraph">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>
								<p class = "summary-paragraph">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>
								<p class = "summary-paragraph">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>
								
							</div> */}
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
						</div>
					</div>
				</div>

			</div>
		);
	}
}

export default HistoryList;