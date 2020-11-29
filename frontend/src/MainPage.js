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
		this.loadAverageMap = this.loadAverageMap.bind(this);
		this.loadExtremeMap = this.loadExtremeMap.bind(this);
		this.loadBoxWhisker = this.loadBoxWhisker.bind(this);
		this.setDefaultFilters = this.setDefaultFilters.bind(this);
		this.updateFilters = this.updateFilters.bind(this);
		this.state = {
			averageMap: "",
			extremeMap: "",
			boxWhisker: []
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

	loadAverageMap(job) {
		console.log(job);
		const data = { stateId: job.averageStateId }
		const url = 'http://localhost:8080/getDistricting'
		$.ajax({
			url: url,
			type: "POST",
			data: data,
			success: (data) => {
				this.setState({
					averageMap: data
				});
			}
		});
	}

	loadExtremeMap(job) {
		const data = { stateId: job.extremeStateId }
		const url = 'http://localhost:8080/getDistricting'
		$.ajax({
			url: url,
			type: "POST",
			data: data,
			success: (data) => {
				this.setState({
					extremeMap: data
				});
			}
		});
	}

	loadBoxWhisker(job) {
		this.setState({
			boxWhisker: job.boxWhisker
		});
	}

	render() {
		return (
			<div>
				<div class="row" id="body-row">
					<Sidebar loadAverageMap={this.loadAverageMap} loadExtremeMap={this.loadExtremeMap}
						updateFilters={this.updateFilters} setDefaultFilters={this.setDefaultFilters}></Sidebar>
					<USMap averageMap={this.state.averageMap} extremeMap={this.state.extremeMap} boxWhisker={this.state.boxWhisker}
						currentIsSet={currentMapChecked} averageIsSet={averageMapChecked}
						extremeIsSet={extremeMapChecked} blackIsSet={black}
						whiteIsSet={white} asianIsSet={asian}
						hispanicIsSet={hispanic} hawaiianIsSet={hawaiian}
						nativeIsSet={native} districtsIsSet={districts}
						precinctsIsSet={precincts} />
				</div>
			</div>
		)
	}
}

export default MainPage;