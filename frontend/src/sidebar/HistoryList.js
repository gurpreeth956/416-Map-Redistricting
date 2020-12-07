import React from 'react';
import ReactDOM from 'react-dom';
import $ from 'jquery';
import BoxWhisker from "../BoxWhisker.js";
const historyModalRoot = document.getElementById('history-modal-root');

window.$ = $;

class HistoryList extends React.Component {

	constructor(props) {
		super(props);
		this.el = document.createElement('div');
	}

	componentDidMount() {
		historyModalRoot.appendChild(this.el);
	}

	componentWillUnmount() {
		historyModalRoot.removeChild(this.el);
	}

	gatherEthnicities(jobEthnicities) {
		var ethnicities = [];
		for (var i = 0; i<jobEthnicities.length; i++) {
			ethnicities.push(jobEthnicities[i].primaryKey.ethnicity);
		}
		return ethnicities;
	}

	render() {
		const jobs = this.props.jobs;
		var loaded_id = null;
		if (this.props.loadedJob != null && this.props.loadedJob != undefined) {
			loaded_id = this.props.loadedJob.id;
		}
		return (
			<div class="list-group" id="list-tab" role="tablist">
				{jobs && jobs.map((job, index) => {
					if (job.jobStatus === "COMPLETED") {
						return (
							<a key={job.id} class="list-group-item list-group-item-action history-item" data-toggle="modal" href={"#history-modal-" + job.id} role="tab">
								<div class="d-flex h-100 justify-content-between">
									<h6>ID# {job.id} </h6>
									<small> {job.jobStatus} </small>
								</div>
								<p>State: {job.abbreviation}</p>
								<p>Maps to Generate: {job.numberOfMaps}</p>
								<p>Compactness: {this.props.getCompactnessFromInt(job.userCompactness)}</p>
								<p>Population Limit Difference: {job.populationDifferenceLimit + "%"}</p>
								<div class="btn-group">
									{loaded_id != null && loaded_id != undefined && loaded_id === job.id ?
										<button type="button" class="btn btn-primary history-item-button text-nowrap" disabled onClick={(e) => { e.stopPropagation(); this.props.loadJob(index); }}>Load Job</button> :
										<button type="button" class="btn btn-primary history-item-button text-nowrap" onClick={(e) => { e.stopPropagation(); this.props.loadJob(index); }}>Load Job</button>}
									{loaded_id === job.id && loaded_id != null && loaded_id != undefined ? 
										<button type="button" class="btn btn-secondary history-item-button text-nowrap" onClick={(e) => { e.stopPropagation(); this.props.loadJob(-1); }}>Unload Job</button> :
										null
									}
									<button type="button" class="btn btn-danger history-item-button text-nowrap" onClick={(e) => { e.stopPropagation(); this.props.deleteJob(index); }}>Delete Job</button>
								</div>
								{ReactDOM.createPortal(this.generateModal(job), this.el)}
							</a>
						)
					} else {
						return (
							<div key={job.id} class="list-group-item history-item" role="tab">
								<div class="d-flex h-100 justify-content-between">
									<h6>ID# {job.id} </h6>
									<small> {job.jobStatus} </small>
								</div>
								<p>State: {job.abbreviation}</p>
								<p>Maps to Generate: {job.numberOfMaps}</p>
								<p>Compactness: {this.props.getCompactnessFromInt(job.userCompactness)}</p>
								<p>Population Limit Difference: {job.populationDifferenceLimit + "%"}</p>
								<div class="btn-group">
									{job.jobStatus === "WAITING" || job.jobStatus === "RUNNING" && job.seaWulfId !== -1 && job.jobStatus !== "PROCESSING" ?
										<button type="button" class="btn btn-secondary history-item-button text-nowrap enabled" onClick={(e) => this.props.cancelJob(index)}>Cancel Job</button> :
										null
									}
									{job.jobStatus === "CANCELLED" || job.seaWulfId !== -1 && job.jobStatus !== "PROCESSING" ?
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
		var ethnicities = this.gatherEthnicities(job.ethnicities);
		return (
			<div class="modal fade" id={"history-modal-" + job.id} tabindex="-1" role="dialog" aria-labelledby="historyTitle" aria-hidden="true">
				<div class="modal-dialog modal-lg" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title" id="historyTitle">Job ID# {job.id}</h5>

						</div>
						<div class="modal-body">
							<div>
								<BoxWhisker plot={job.boxWhiskers} ethnicities={ethnicities}/>
							</div>
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