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

	render() {
		const loaded_job = this.props.loadedJob;
		console.log(this.props.loadedJob);
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
					<input class="form-check-input" type="checkbox" value="" defaultChecked/>
					<span class="form-check-label">
						Current Map
					</span>
				</label>
				<label class="form-check">
					<input class="form-check-input" type="checkbox" value=""/>
					<span class="form-check-label">
						Average Map
					</span>
				</label>
				<label class="form-check">
					<input class="form-check-input" type="checkbox" value=""/>
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
						<input class="form-check-input" type="checkbox" value="" defaultChecked/>
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
									<input class="form-check-input" type="checkbox" value=""/>
									<span class="form-check-label">
										White
									</span>
								</label>) : null}
							{ethnicities.includes("BLACK_OR_AFRICAN_AMERICAN") ? (<label class="form-check">
									<input class="form-check-input" type="checkbox" value=""/>
									<span class="form-check-label">
										Black/African American
									</span>
								</label>) : null}
							{ethnicities.includes("ASIAN") ? (<label class="form-check">
									<input class="form-check-input" type="checkbox" value=""/>
									<span class="form-check-label">
										Asian
									</span>
								</label>) : null}
							{ethnicities.includes("HISPANIC_OR_LATINO") ? (<label class="form-check">
									<input class="form-check-input" type="checkbox" value=""/>
									<span class="form-check-label">
										Hispanic or Latino
									</span>
								</label>) : null}
							{ethnicities.includes("AMERICAN_INDIAN") ? (<label class="form-check">
									<input class="form-check-input" type="checkbox" value=""/>
									<span class="form-check-label">
										American Indian or Alaskan Native
									</span>
								</label>) : null}
							{ethnicities.includes("NATIVE_HAWAIIAN_AND_OTHER_PACIFIC") ? (<label class="form-check">
									<input class="form-check-input" type="checkbox" value=""/>
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
							<input class="form-check-input" type="checkbox" value=""/>
							<span class="form-check-label">
								District Boundaries
							</span>
						</label>
						<label class="form-check">
							<input class="form-check-input" type="checkbox" value=""/>
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