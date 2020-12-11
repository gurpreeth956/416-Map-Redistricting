import React from 'react';
import $ from 'jquery';
import Sidebar from './sidebar/Sidebar.js';
import USMap from './map/USMap.js';
window.$ = $;

class MainPage extends React.Component {

	constructor(props) {
		super(props);
		this.loadMaps = this.loadMaps.bind(this);
		this.loadBoxWhisker = this.loadBoxWhisker.bind(this);
		this.setDefaultFilters = this.setDefaultFilters.bind(this);
		this.updateFilters = this.updateFilters.bind(this);
		this.updateSelectedState = this.updateSelectedState.bind(this);
		this.state = {
			averageMap: "",
			extremeMap: "",
			boxWhisker: [],
			ethnicities: [],
			selectedState: "none",
			currentMapChecked: true,
			averageMapChecked: false,
			extremeMapChecked: false,
			black: false,
			white: false,
			asian: false,
			hispanic: false,
			hawaiian: false,
			native: false,
			districts: true,
			precincts: false,
			heatMap: false,
		};
	}

	setDefaultFilters() {
		this.setState({
			currentMapChecked: true,
			averageMapChecked: false,
			extremeMapChecked: false,
			black: false,
			white: false,
			asian: false,
			hispanic: false,
			hawaiian: false,
			native: false,
			districts: true,
			precincts: false,
			heatMap: false
		});
	}

	updateFilters(box) {
		if (box === "current") {
			this.setState({
				currentMapChecked: !this.state.currentMapChecked
			});
		} else if (box === "average") {
			this.setState({
				averageMapChecked: !this.state.averageMapChecked
			});
		} else if (box === "extreme") {
			this.setState({
				extremeMapChecked: !this.state.extremeMapChecked
			});
		} else if (box === "black") {
			this.setState({
				black: !this.state.black
			});
		} else if (box === "white") {
			this.setState({
				white: !this.state.white
			});
		} else if (box === "asian") {
			this.setState({
				asian: !this.state.asian
			});
		} else if (box === "hispanic") {
			this.setState({
				hispanic: !this.state.hispanic
			});
		} else if (box === "native") {
			this.setState({
				native: !this.state.native
			});
		} else if (box === "hawaiian") {
			this.setState({
				hawaiian: !this.state.hawaiian
			});
		} else if (box === "district") {
			this.setState({
				districts: !this.state.districts
			});
		} else if (box === "precinct") {
			this.setState({
				precincts: !this.state.precincts
			});
		} else if (box === "heatmap") {
			this.setState({
				heatMap: !this.state.heatMap
			});
		}
	}

	updateSelectedState(state) {
		this.setState({
			selectedState: state
		});
	}

	loadMaps(job) {
		console.log(job);
		if (job == null) {
			this.setState({
				averageMap: "",
				extremeMap: "",
			});
		} else {
			const data = { jobId: job.id }
			const url = 'http://localhost:8080/getDistricting'
			$.ajax({
				url: url,
				type: "GET",
				data: data,
				success: (data) => {
					data = JSON.parse(data)
					this.setState({
						averageMap: data.Districtings[0].data,
						extremeMap: data.Districtings[1].data
					});
				}
			});
		}
	}

	loadBoxWhisker(job) {
		console.log(job);
		if (job == null) {
			this.setState({
				boxWhisker: [],
				ethnicities: []
			});
		} else {
			var ethnicities = [];
			for (var i = 0; i < job.ethnicities.length; i++) {
				ethnicities.push(job.ethnicities[i].primaryKey.ethnicity);
			}
			this.setState({
				boxWhisker: job.boxWhiskers,
				ethnicities: ethnicities
			});
		}
	}

	render() {
		return (
			<div>
				<div class="row" id="body-row">
					<Sidebar loadMaps={this.loadMaps} loadBoxWhisker={this.loadBoxWhisker}
						updateFilters={this.updateFilters} setDefaultFilters={this.setDefaultFilters}
						currentIsSet={this.state.currentMapChecked} averageIsSet={this.state.averageMapChecked}
						extremeIsSet={this.state.extremeMapChecked} blackIsSet={this.state.black}
						whiteIsSet={this.state.white} asianIsSet={this.state.asian}
						hispanicIsSet={this.state.hispanic} hawaiianIsSet={this.state.hawaiian}
						nativeIsSet={this.state.native} districtsIsSet={this.state.districts}
						precinctsIsSet={this.state.precincts} heatMapIsSet={this.state.heatMap}
						updateSelectedState={this.updateSelectedState} selectedState={this.state.selectedState}></Sidebar>
					<USMap averageMap={this.state.averageMap} extremeMap={this.state.extremeMap} boxWhisker={this.state.boxWhisker}
						currentIsSet={this.state.currentMapChecked} averageIsSet={this.state.averageMapChecked}
						extremeIsSet={this.state.extremeMapChecked} blackIsSet={this.state.black}
						whiteIsSet={this.state.white} asianIsSet={this.state.asian}
						hispanicIsSet={this.state.hispanic} hawaiianIsSet={this.state.hawaiian}
						nativeIsSet={this.state.native} districtsIsSet={this.state.districts}
						precinctsIsSet={this.state.precincts} heatMapIsSet={this.state.heatMap}
						updateSelectedState={this.updateSelectedState} selectedState={this.state.selectedState}
						ethnicities={this.state.ethnicities} />
				</div>
			</div>
		)
	}
}

export default MainPage;