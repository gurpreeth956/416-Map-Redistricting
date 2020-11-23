'use strict';
const e = React.createElement;

class Filters extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			job_loaded: false,
			white: true,
			black: false,
			asian: false,
			hispanic: false,
			native_american: false,
			hawaiian: false
		};
	}

	render() {
		const {job_loaded, white, black, asian, hispanic, native_american, hawaiian} = this.state;

		var map_filters;
		if(job_loaded) {
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
							{white || black || asian || hispanic || native_american || hawaiian ? 
							<label class="mr-sm-2" for="inlineFormCustomSelect">Race/Ethnicity</label> : null }
							{white ? (<label class="form-check">
									<input class="form-check-input" type="checkbox" value=""/>
									<span class="form-check-label">
										White
									</span>
								</label>) : null}
							{black ? (<label class="form-check">
									<input class="form-check-input" type="checkbox" value=""/>
									<span class="form-check-label">
										Black/African American
									</span>
								</label>) : null}
							{asian ? (<label class="form-check">
									<input class="form-check-input" type="checkbox" value=""/>
									<span class="form-check-label">
										Asian
									</span>
								</label>) : null}
							{hispanic ? (<label class="form-check">
									<input class="form-check-input" type="checkbox" value=""/>
									<span class="form-check-label">
										Hispanic or Latino
									</span>
								</label>) : null}
							{native_american ? (<label class="form-check">
									<input class="form-check-input" type="checkbox" value=""/>
									<span class="form-check-label">
										American Indian or Alaskan Native
									</span>
								</label>) : null}
							{hawaiian ? (<label class="form-check">
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

const domContainer = document.querySelector('#Filters');
ReactDOM.render(e(Filters), domContainer);