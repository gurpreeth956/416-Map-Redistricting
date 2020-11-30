import React from 'react';
import $ from 'jquery';
import Sidebar from './sidebar/Sidebar.js';
import USMap from './map/USMap.js';
window.$ = $;

var currentMapChecked = true;
var averageMapChecked = false;
var extremeMapChecked = false;
var black = false;
var white = false;
var asian = false;
var hispanic = false;
var hawaiian = false;
var native = false;
var districts = true;
var precincts = true;
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
			selectedState: "none"
		};
	}

	setDefaultFilters() {
		currentMapChecked = true;
		averageMapChecked = false;
		extremeMapChecked = false;
		black = false;
		white = false;
		asian = false;
		hispanic = false;
		hawaiian = false;
		native = false;
		districts = true;
		precincts = true;
		this.forceUpdate();
	}

	updateFilters(box) {
		if (box === "current") {
			currentMapChecked = !currentMapChecked;
		} else if (box === "average") {
			averageMapChecked = !averageMapChecked;
		} else if (box === "extreme") {
			extremeMapChecked = !extremeMapChecked;
		} else if (box === "black") {
			black = !black;
		} else if (box === "white") {
			white = !white;
		} else if (box === "asian") {
			asian = !asian;
		} else if (box === "hispanic") {
			hispanic = !hispanic;
		} else if (box === "native") {
			native = !native;
		} else if (box === "hawaiian") {
			hawaiian = !hawaiian;
		} else if (box === "district") {
			districts = !districts;
		} else if (box === "precinct") {
			precincts = !precincts;
		}
		this.forceUpdate();
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
		} else{
			const data = { jobId: job.averageStateId }
			const url = 'http://localhost:8080/getDistricting'
			$.ajax({
				url: url,
				type: "GET",
				data: data,
				success: (data) => {
					this.setState({
						averageMap: data[0],
						extremeMap: data[1]
					});
					console.log(data);
				}
			});
		}
	}

	loadBoxWhisker(job) {
		console.log(job);
		if(job == null) {
			this.setState({
				boxWhisker: []
			});
		} else{
			this.setState({
				boxWhisker: job.boxWhiskers
			});
		}
	}

	render() {
		return (
			<div>
				<div class="row" id="body-row">
					<Sidebar loadMaps={this.loadMaps} loadBoxWhisker={this.loadBoxWhisker}
						updateFilters={this.updateFilters} setDefaultFilters={this.setDefaultFilters}
						updateSelectedState={this.updateSelectedState} selectedState={this.state.selectedState}></Sidebar>
					<USMap averageMap={this.state.averageMap} extremeMap={this.state.extremeMap} boxWhisker={this.state.boxWhisker}
						currentIsSet={currentMapChecked} averageIsSet={averageMapChecked}
						extremeIsSet={extremeMapChecked} blackIsSet={black}
						whiteIsSet={white} asianIsSet={asian}
						hispanicIsSet={hispanic} hawaiianIsSet={hawaiian}
						nativeIsSet={native} districtsIsSet={districts}
						precinctsIsSet={precincts} 
						updateSelectedState={this.updateSelectedState} selectedState={this.state.selectedState}/>
				</div>
			</div>
		)
	}
}

export default MainPage;