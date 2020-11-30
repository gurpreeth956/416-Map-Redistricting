import React from 'react';
import $ from 'jquery';
window.$ = $;

class Filters extends React.Component {

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

	
	updateFiltersOnChange(e, recordedValue) {
		console.log("updating filters")
		if(recordedValue !== e.target.checked) {
			this.props.updateFilters(e.target.name);
			console.log(e.target.name);
		}
	}


	render() {
		console.log(this.props);
		const loaded_job = this.props.loadedJob;
		var ethnicities = [];
		var map_filters;

		if(loaded_job !== null) {
			ethnicities = this.getEthnicities(loaded_job);
			map_filters = 
			<form>
				<label class="mr-sm-2" for="inlineFormCustomSelect">Maps</label>
				<label class="form-check">
					<input onChange={(e) => {this.updateFiltersOnChange(e, this.props.currentIsSet)}} name="current" class="form-check-input" type="checkbox" checked={this.props.currentIsSet}/>
					<span class="form-check-label">
						Current Map
					</span>
				</label>
				<label class="form-check">
					<input onChange={(e) => {this.updateFiltersOnChange(e, this.props.averageIsSet)}} name="average" class="form-check-input" type="checkbox" checked={this.props.averageIsSet}/>
					<span class="form-check-label">
						Average Map
					</span>
				</label>
				<label class="form-check">
					<input onChange={(e) => {this.updateFiltersOnChange(e, this.props.extremeIsSet)}} name="extreme" class="form-check-input" type="checkbox" checked={this.props.extremeIsSet}/>
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
						<input onChange={(e) => {this.updateFiltersOnChange(e, this.props.currentIsSet)}} name="current" class="form-check-input" type="checkbox" checked={this.props.currentIsSet}/>
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
							{ethnicities.length !== 0 && this.props.heatMapIsSet ?
								<label class="mr-sm-2" for="inlineFormCustomSelect">Race/Ethnicity</label>: null}
							{ethnicities.includes("WHITE") && this.props.heatMapIsSet ? (<label class="form-check">
									<input onChange={(e) => {this.updateFiltersOnChange(e, this.props.whiteIsSet)}} name="white" class="form-check-input" type="checkbox" checked={this.props.whiteIsSet}/>
									<span class="form-check-label">
										White
									</span>
								</label>) : null}
							{ethnicities.includes("BLACK_OR_AFRICAN_AMERICAN") && this.props.heatMapIsSet ? (<label class="form-check">
									<input onChange={(e) => {this.updateFiltersOnChange(e, this.props.blackIsSet)}} name="black" class="form-check-input" type="checkbox" checked={this.props.blackIsSet}/>
									<span class="form-check-label">
										Black/African American
									</span>
								</label>) : null}
							{ethnicities.includes("ASIAN") && this.props.heatMapIsSet ? (<label class="form-check">
									<input onChange={(e) => {this.updateFiltersOnChange(e, this.props.asianIsSet)}} name="asian" class="form-check-input" type="checkbox" checked={this.props.asianIsSet}/>
									<span class="form-check-label">
										Asian
									</span>
								</label>) : null}
							{ethnicities.includes("HISPANIC_OR_LATINO") && this.props.heatMapIsSet ? (<label class="form-check">
									<input onChange={(e) => {this.updateFiltersOnChange(e, this.props.hispanicIsSet)}} name="hispanic" class="form-check-input" type="checkbox" checked={this.props.hispanicIsSet}/>
									<span class="form-check-label">
										Hispanic or Latino
									</span>
								</label>) : null}
							{ethnicities.includes("AMERICAN_INDIAN") && this.props.heatMapIsSet ? (<label class="form-check">
									<input onChange={(e) => {this.updateFiltersOnChange(e, this.props.nativeIsSet)}} name="native" class="form-check-input" type="checkbox" checked={this.props.nativeIsSet}/>
									<span class="form-check-label">
										American Indian or Alaskan Native
									</span>
								</label>) : null}
							{ethnicities.includes("NATIVE_HAWAIIAN_AND_OTHER_PACIFIC") && this.props.heatMapIsSet ? (<label class="form-check">
									<input onChange={(e) => {this.updateFiltersOnChange(e, this.props.hawaiianIsSet)}} name="hawaiian" class="form-check-input" type="checkbox" checked={this.props.hawaiianIsSet}/>
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
							<input onChange={(e) => {this.updateFiltersOnChange(e, this.props.districtsIsSet)}} name="district" class="form-check-input" type="checkbox" checked={this.props.districtsIsSet}/>
							<span class="form-check-label">
								District Boundaries
							</span>
						</label>
						<label class="form-check">
							<input onChange={(e) => {this.updateFiltersOnChange(e, this.props.precinctsIsSet)}} name="precinct" class="form-check-input" type="checkbox" checked={this.props.precinctsIsSet}/>
							<span class="form-check-label">
								Precinct Boundaries
							</span>
						</label>
						<label class="form-check">
							<input onChange={(e) => {this.updateFiltersOnChange(e, this.props.heatMapIsSet)}} name="heatmap" class="form-check-input" type="checkbox" checked={this.props.heatMapIsSet}/>
							<span class="form-check-label">
								Heat Map
							</span>
						</label>
					</div>
				</div>	
			</article>
		);
	}
}

export default Filters;