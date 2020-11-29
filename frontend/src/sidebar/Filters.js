import React from 'react';
import $ from 'jquery';
window.$ = $;

class Filters extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			current: true,
			average: false,
			extreme: false,
			black: false,
			white: false,
			asian: false,
			hispanic: false,
			native: false,
			hawaiian: false,
			district: true,
			precinct: true
		};
	}

	setDefaultCheckboxes() {
		this.setState({
			current: true,
			average: false,
			extreme: false,
			black: false,
			white: false,
			asian: false,
			hispanic: false,
			native: false,
			hawaiian: false,
			district: true,
			precinct: true
		});
	}

	getEthnicities(job) {
		var ethnicities = [];
		var i = 0;
		if(job === null) return;
		while(i < job.ethnicities.length) {
			ethnicities.push(job.ethnicities[i].primaryKey.ethnicity);
			i++;
		}
		return ethnicities;
	}

	render() {
		const loaded_job = this.props.loadedJob;
		var ethnicities = [];
		var map_filters;

		if(loaded_job !== null) {
			ethnicities = this.getEthnicities(loaded_job);
			console.log(ethnicities);
			console.log(ethnicities.includes("WHITE"));
			map_filters = 
			<form>
				<label class="mr-sm-2" for="inlineFormCustomSelect">Maps</label>
				<label class="form-check">
					<input onChange={e => { this.setState({current: !this.state.current}); this.props.updateCheckboxes("current") }} class="form-check-input" type="checkbox" checked={this.state.current}/>
					<span class="form-check-label">
						Current Map
					</span>
				</label>
				<label class="form-check">
					<input onChange={e => { this.setState({average: !this.state.average}); this.props.updateCheckboxes("average") }}class="form-check-input" type="checkbox" checked={this.state.average}/>
					<span class="form-check-label">
						Average Map
					</span>
				</label>
				<label class="form-check">
					<input onChange={e => { this.setState({extreme: !this.state.extreme}); this.props.updateCheckboxes("extreme") }} class="form-check-input" type="checkbox" checked={this.state.extreme}/>
					<span class="form-check-label">
						Extreme Map
					</span>
				</label>
			</form>
		} else {
			map_filters = 
				<form>
					<label class="mr-sm-2" for="inlineFormCustomSelect">Maps</label>
					<label class="form-check">
						<input onChange={e => { this.setState({current: !this.state.current}); this.props.updateCheckboxes("current") }} class="form-check-input" type="checkbox" checked={this.state.current}/>
						<span class="form-check-label">
							Current Map
						</span>
					</label>
				</form>
		}

		return(
			<article class="card-group-item">
				<header class="card-header">
					<h6 class="title">Map Data</h6>
				</header>
				<div class="map-filter-content">
					<div class="card-body">
						{map_filters}

						<form>
							{ethnicities.length !== 0 ? 
							<label class="mr-sm-2" for="inlineFormCustomSelect">Race/Ethnicity</label> : null }
							{ethnicities.includes("WHITE") ? (<label class="form-check">
									<input onChange={e => { this.setState({white: !this.state.white}); this.props.updateCheckboxes("white") }} class="form-check-input" type="checkbox" checked={this.state.white}/>
									<span class="form-check-label">
										White
									</span>
								</label>) : null}
							{ethnicities.includes("BLACK_OR_AFRICAN_AMERICAN") ? (<label class="form-check">
									<input onChange={e => { this.setState({black: !this.state.black}); this.props.updateCheckboxes("black") }} class="form-check-input" type="checkbox" checked={this.state.black}/>
									<span class="form-check-label">
										Black/African American
									</span>
								</label>) : null}
							{ethnicities.includes("ASIAN") ? (<label class="form-check">
									<input onChange={e => { this.setState({asian: !this.state.asian}); this.props.updateCheckboxes("asian") }} class="form-check-input" type="checkbox" checked={this.state.asian}/>
									<span class="form-check-label">
										Asian
									</span>
								</label>) : null}
							{ethnicities.includes("HISPANIC_OR_LATINO") ? (<label class="form-check">
									<input onChange={e => { this.setState({hispanic: !this.state.hispanic}); this.props.updateCheckboxes("hispanic") }} class="form-check-input" type="checkbox" checked={this.state.hispanic}/>
									<span class="form-check-label">
										Hispanic or Latino
									</span>
								</label>) : null}
							{ethnicities.includes("AMERICAN_INDIAN") ? (<label class="form-check">
									<input onChange={e => { this.setState({native: !this.state.native}); this.props.updateCheckboxes("native") }} class="form-check-input" type="checkbox" checked={this.state.native}/>
									<span class="form-check-label">
										American Indian or Alaskan Native
									</span>
								</label>) : null}
							{ethnicities.includes("NATIVE_HAWAIIAN_AND_OTHER_PACIFIC") ? (<label class="form-check">
									<input onChange={e => { this.setState({hawaiian: !this.state.hawaiian}); this.props.updateCheckboxes("hawaiian") }} class="form-check-input" type="checkbox" checked={this.state.hawaiian}/>
									<span class="form-check-label">
										Native Hawaiian or Other Pacfic Islander
									</span>
								</label>) : null}
						</form>
					</div>
				</div>
				<div class="generated-map-filter-content">
					<header class="card-header">
						<h6 class="title">Boundary Filter</h6>
					</header>
					<div class="card-body">
						<label class="form-check">
							<input onChange={e => { this.setState({district: !this.state.district}); this.props.updateCheckboxes("district") }} class="form-check-input" type="checkbox" checked={this.state.district}/>
							<span class="form-check-label">
								District Boundaries
							</span>
						</label>
						<label class="form-check">
							<input onChange={e => { this.setState({precinct: !this.state.precinct}); this.props.updateCheckboxes("precinct") }} class="form-check-input" type="checkbox" checked={this.state.precinct}/>
							<span class="form-check-label">
								Precinct Boundaries
							</span>
						</label>
					</div>
				</div>	
			</article>
		);
	}
}

export default Filters;