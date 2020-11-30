import React from 'react';
import ReactDOM from 'react-dom';
import $ from 'jquery';
window.$ = $;
const jobModalRoot = document.getElementById('job-modal-root');

var jobId = -1;
class GenerateMapForm extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			numMaps: '',
			white: false,
			black: false,
			hispanic: false,
			asian: false,
			native: false,
			hawaiian: false,
			popDiff: '',
			compactness: '1',
			popDiffError: false,
			numMapError: false,
			ethnicityError: false,
			stateError: false,
			loading: false
		};
		this.el = document.createElement('div');
	}

	componentDidMount() {
		jobModalRoot.appendChild(this.el);
	}

	componentWillUnmount() {
		jobModalRoot.removeChild(this.el);
	}

	handleStateChange = event => {
		this.props.updateSelectedState(event.target.value);
	}

	handleNumMapsChange = event => {
		this.setState({
			numMaps: event.target.value
		})
	}

	handleEthnicityChange = event => {
		const target = event.target;
		const value = target.type === 'checkbox' ? target.checked : target.value;
		const name = target.name;
		this.setState({
			[name]: value
		});
	}

	handlePopDiffChange = event => {
		this.setState({
			popDiff: event.target.value
		})
	}

	handleCompactnessChange = event => {
		this.setState({
			compactness: event.target.value
		})
	}

	handleSubmit = event => {
		jobId = -1;
		event.preventDefault()
		var ethnicity = this.gatherEthnicities();

		var numMapError = false;
		var popDiffError = false;
		var ethnicityError = false;
		var stateError = false;
		if (this.props.selectedState === "none") {
			stateError = true;
		}
		if (this.state.numMaps > 10000 || this.state.numMaps < 1 || this.state.numMaps === '') {
			numMapError = true;
		}
		if (this.state.popDiff < 0.0 || this.state.popDiff === '') {
			popDiffError = true;
		}
		if (ethnicity.length === 0) {
			ethnicityError = true;
		}

		this.setState({
			numMapError: numMapError,
			popDiffError: popDiffError,
			ethnicityError: ethnicityError,
			stateError: stateError
		});
		if (!numMapError && !popDiffError && !ethnicityError && !stateError) {
			const data = {
				stateName: this.props.selectedState, userCompactness: this.state.compactness,
				populationDifferenceLimit: this.state.popDiff, ethnicities: ethnicity.toString(),
				numberOfMaps: this.state.numMaps
			}
			console.log(data);
			const url = 'http://localhost:8080/initializeJob'
			this.setState({
				loading: true
			});
			$.ajax({
				url: url,
				type: "POST",
				data: data,
				success: (job) => {
					console.log(job);
					jobId = job.id;
					this.setState({
						loading: false
					});
					this.props.addJob(job);
				}
			});
		}
	}

	gatherEthnicities() {
		var ethnicity = [];
		if (this.state.black) {
			ethnicity.push("BLACK_OR_AFRICAN_AMERICAN")
		}
		if (this.state.white) {
			ethnicity.push("WHITE")
		}
		if (this.state.hispanic) {
			ethnicity.push("HISPANIC_OR_LATINO")
		}
		if (this.state.asian) {
			ethnicity.push("ASIAN")
		}
		if (this.state.native) {
			ethnicity.push("AMERICAN_INDIAN")
		}
		if (this.state.hawaiin) {
			ethnicity.push("NATIVE_HAWAIIAN_AND_OTHER_PACIFIC")
		}
		return ethnicity;
	}

	render() {
		const { numMaps, ethnicity, popDiff } = this.state
		return (
			<div class="generate-content">
				<div class="card-body">
					<form>
						<label class="font-weight-bold">State</label>
						<div class="form-row align-items-center">
							<div class="col-auto my-1">
								<select class="custom-select mr-sm-2" id="inlineFormCustomSelect"
									onChange={this.handleStateChange}>
									<option value="none" selected={this.props.selectedState === "none"}>State</option>
									<option value="CA" selected={this.props.selectedState === "CA"}>California</option>
									<option value="LA" selected={this.props.selectedState === "LA"}>Lousiana</option>
									<option value="PA" selected={this.props.selectedState === "PA"}>Pennslyvania</option>
								</select>
							</div>
						</div>
					</form>

					<div class="form-group">
						<label class="font-weight-bold">Number of Maps to Generate</label>
						<input class="form-control" id="exampleInputPassword1" placeholder="Number of Maps"
							value={numMaps} onChange={this.handleNumMapsChange} />
					</div>

					<form>
						<label class="font-weight-bold">Race/Ethnicity</label>
						<label class="form-check">
							<input class="form-check-input" type="checkbox" value=""
								name="white" onChange={this.handleEthnicityChange} checked={this.state.white} />
							<span class="form-check-label">
								White
        										</span>
						</label>
						<label class="form-check">
							<input class="form-check-input" type="checkbox" value=""
								name="black" onChange={this.handleEthnicityChange} checked={this.state.black} />
							<span class="form-check-label">
								Black/African American
        										</span>
						</label>
						<label class="form-check">
							<input class="form-check-input" type="checkbox" value=""
								name="hispanic" onChange={this.handleEthnicityChange} checked={this.state.hispanic} />
							<span class="form-check-label">
								Hispanic or Latino
        										</span>
						</label>
						<label class="form-check">
							<input class="form-check-input" type="checkbox" value=""
								name="asian" onChange={this.handleEthnicityChange} checked={this.state.asian} />
							<span class="form-check-label">
								Asian
        										</span>
						</label>
						<label class="form-check">
							<input class="form-check-input" type="checkbox" value=""
								name="native" onChange={this.handleEthnicityChange} checked={this.state.native} />
							<span class="form-check-label">
								American Indian or Alaskan Native
        										</span>
						</label>
						<label class="form-check">
							<input class="form-check-input" type="checkbox" value=""
								name="hawaiin" onChange={this.handleEthnicityChange} checked={this.state.hawaiin} />
							<span class="form-check-label">
								Native Hawaiian or Other Pacfic Islander
        										</span>
						</label>
					</form>

					<div class="form-group">
						<label class="font-weight-bold">Population Difference Limit</label>
						<input class="form-control" id="exampleInputPassword1" placeholder="Difference Limit"
							value={popDiff} onChange={this.handlePopDiffChange} />
					</div>

					<label class="font-weight-bold">Compactness Treshold</label>
					<div class="btn-group">
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="2"
								onChange={this.handleCompactnessChange} checked={this.state.compactness === '2'}/>
							<label class="form-check-label" for="inlineRadio1">Very Compact</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="1"
								onChange={this.handleCompactnessChange} checked={this.state.compactness === '1'} />
							<label class="form-check-label" for="inlineRadio2">Somewhat Compact</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio3" value="0"
								onChange={this.handleCompactnessChange} checked={this.state.compactness === '0'}/>
							<label class="form-check-label" for="inlineRadio3">Not Compact</label>
						</div>
					</div>

					<button type="button" style={{ marginTop: "5%" }} class="btn btn-primary" data-toggle="modal" href="#job-modal" onClick={this.handleSubmit}>
						Generate Maps</button>
				</div>
				{ReactDOM.createPortal(this.generateModal(), this.el)}
			</div>)
	};

	generateModal() {
		var ethnicities = this.gatherEthnicities();
		return (
			<div class="modal fade" id="job-modal" tabindex="-1" role="dialog" aria-labelledby="jobTitle" aria-hidden="true">
				<div class="modal-dialog modal-sm" role="document">
					<div class="modal-content">
						<div class="modal-header">
							{
								this.state.stateError || this.state.numMapError || this.state.popDiffError || this.state.ethnicityError && jobId === -1 ?
									<h5 class="modal-title" id="jobTitle">ERROR: Please review the error below.</h5> :
									null
							}
							{
								jobId !== -1 && !this.state.loading && !this.state.stateError && !this.state.numMapError && !this.state.popDiffError && !this.state.ethnicityError ?
									<h5 class="modal-title" id="jobTitle">Job ID# {jobId}</h5> :
									null

							}
							{jobId === -1 && this.state.loading && !this.state.stateError && !this.state.numMapError && !this.state.popDiffError && !this.state.ethnicityError ?
								<h5 class="modal-title" id="jobTitle">Loading Job</h5> :
								null
							}
						</div>
						<div class="modal-body">
							{this.state.stateError ? <p>Invalid State Error: <br/> Please select a state from the dropdown menu.</p> : null}
							{this.state.numMapError ? <p>Invalid Number of Maps: <br/> Please enter a number between 1 - 20,000.</p> : null}
							{this.state.popDiffError ? <p>Invalid Population Difference Limit: <br/> Please enter a percent greater than 0.0</p> : null}
							{this.state.ethnicityError ? <p>Invalid Ethnicities: <br/> Please select at least one ethnicity.</p> : null}
							{!this.state.loading && jobId !== -1 && !this.state.stateError && !this.state.ethnicityError && !this.state.ethnicityError && !this.state.ethnicityError ?
								<div>
									<p>Your job as been successfully created with the following:</p>
									<p>Job ID#: {jobId}</p>
									<p>State: {this.props.selectedState}</p>
									<p>Maps to Generate: {this.state.numMaps}</p>
									<p>Ethnicities: </p>
									<ul>
										{ethnicities.map((ethnicity, index) => (
											<li key={index}>{ethnicity}</li>
										))}
									</ul>
									<p>Compactness: {this.props.getCompactnessFromInt(this.state.compactness)}</p>
									<p>Population Limit Difference: {this.state.popDiff + "%"}</p>
								</div> : null}
							{jobId === -1 && this.state.loading ? <p>Loading...</p> : null}
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

export default GenerateMapForm;

