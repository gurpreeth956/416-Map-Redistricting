import React from 'react';
import $ from 'jquery';
import districtGeoJson from './data/districts-geojson.json';
import stateGeoJson from './data/states-geojson.json';
import L from 'leaflet';
import SummaryData from './SummaryData';
window.$ = $;

var map;
var states;
var realDistrict;
var extremeDistrict;
var averageDistrict;
var laPrecinct;
var caPrecinct;
var paPrecinct;
class USMap extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            paPrecinct: '',
            caPrecinct: '',
            laPrecinct: ''
        }
    }

    componentDidMount() {
        map = L.map('map-id').setView([42.0, -96], 5);
        L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
            attribution: '...',
            id: 'mapbox/light-v9',
            tileSize: 512,
            zoomOffset: -1,
            scrollWheelZoom: false,
            dragging: false,
            accessToken: 'pk.eyJ1IjoidGVuemlubG9kZW4iLCJhIjoiY2tmZ3F5YmgzMDA5MDMybGF1dHNnN2JxNiJ9.7lGyZksjGSE669Hsufhtjg'
        }).addTo(map);

        states = L.geoJson(stateGeoJson, { style: this.stateStyle, onEachFeature: this.onEachFeature }).addTo(map);

        if (this.props.districtsIsSet && this.props.currentIsSet && !map.hasLayer(realDistrict)) {
            realDistrict = L.geoJson(districtGeoJson, { style: this.realDistrictStyle }).addTo(map);
        } else if ((!this.props.districtsIsSet || !this.props.currentIsSet) && map.hasLayer(realDistrict)) {
            map.removeLayer(realDistrict);
        }
        if (this.props.districtsIsSet && this.props.averageIsSet && this.state.averageMap !== "" && !map.hasLayer(averageDistrict)) {
            averageDistrict = L.geoJson(this.state.averageMap, { style: this.averageDistrictStyle }).addTo(map);
        } else if ((!this.props.districtsIsSet || !this.props.averageIsSet || this.state.averageMap === "") && map.hasLayer(averageDistrict)) {
            map.removeLayer(averageDistrict);
        }

        if (this.props.districtsIsSet && this.props.extremeIsSet && this.state.extremeMap !== "" && !map.hasLayer(extremeDistrict)) {
            extremeDistrict = L.geoJson(this.state.extremeMap, { style: this.extremeDistrictStyle }).addTo(map);
        } else if ((!this.props.districtsIsSet || !this.props.extremeIsSet || this.state.extremeMap === "") && map.hasLayer(extremeDistrict)) {
            map.removeLayer(extremeDistrict);
        }

        if (this.props.selectedState === "CA") {
            this.zoomToCA();
        } else if (this.props.selectedState === "PA") {
            this.zoomToPA();
        } else if (this.props.selectedState === "LA") {
            this.zoomToLA();
        } else if (this.props.selectedState === "none") {
            this.zoomToMap();
        }
    }

    loadCAPrecincts() {
        var data = { stateAbbreviation: 'CA' };
        const url = 'http://localhost:8080/getState'
        $.ajax({
            url: url,
            type: "GET",
            data: data,
            success: (data) => {
                this.setState({ caPrecinct: JSON.parse(data) });
            }
        });
    }

    loadPAPrecincts() {
        var data = { stateAbbreviation: 'PA' };
        const url = 'http://localhost:8080/getState'
        $.ajax({
            url: url,
            type: "GET",
            data: data,
            success: (data) => {
                this.setState({ paPrecinct: JSON.parse(data) });
            }
        });
    }

    loadLAPrecincts() {
        console.log("loading lA")
        var data = { stateAbbreviation: 'LA' };
        const url = 'http://localhost:8080/getState'
        $.ajax({
            url: url,
            type: "GET",
            data: data,
            success: (data) => {
                this.setState({ laPrecinct: JSON.parse(data) });
                console.log("la data set");
            }
        });
    }

    getColor(name, level) { //Set color of our three states
        if (name === "California" || name === "Louisiana" || name === "Pennsylvania" && level === "state") {
            return 'black';
        } else if (name === "realDistrict") {
            return 'darkcyan';
        } else if (name === "averageDistrict") {
            return 'purple';
        } else if (name === "extremeDistrict") {
            return 'goldenrod';
        } else if (name === "precinct") {
            return 'orangered';
        }
    }

    stateStyle = (feature) => { //styles all of the US states
        return {
            // fillColor: this.getColor(feature.properties.name),
            weight: 4,
            opacity: 1,
            color: this.getColor(feature.properties.name, "state"),
            fillOpacity: 0.0
        };
    }

    realDistrictStyle = (feature) => { //styles all of the US states
        return {
            // fillColor: this.getColor(feature.properties.name),
            weight: 3,
            opacity: 1,
            color: this.getColor("realDistrict", "district"),
            fillOpacity: 0.0
        };
    }

    averageDistrictStyle = (feature) => { //styles all of the US states
        return {
            // fillColor: this.getColor(feature.properties.name),
            weight: 3,
            opacity: 1,
            color: this.getColor("averageDistrict", "district"),
            fillOpacity: 0.0
        };
    }

    extremeDistrictStyle = (feature) => { //styles all of the US states
        return {
            // fillColor: this.getColor(feature.properties.name),
            weight: 3,
            opacity: 1,
            color: this.getColor("extremeDistrict", "district"),
            fillOpacity: 0.0
        };
    }

    precinctStyle = (feature) => { //styles all of the US states
        return {
            // fillColor: this.getColor(feature.properties.name),
            weight: 1,
            opacity: 1,
            color: this.getColor("precinct", "precinct"),
            fillOpacity: 0.0
        };
    }

    onEachFeature = (feature, layer, e) => {
        layer.on({
            mouseover: this.highlightFeature.bind(this),
            mouseout: this.resetHighlight.bind(this),
            click: this.onStateClick.bind(this)
        });
    }

    /*onMouseOut = (e) => {
        this.hover =  '<h4> Hover over a precinct </h4>';
    }

    onMouseOver = (e) => {
        this.hover =  '<p> test </p>';
    }*/

    highlightFeature(e) { //when mouse hovers on one of the 3 states, border is outlined and hover info in top right is updated
        if (e.target.feature.properties.name === "California" ||
            e.target.feature.properties.name === "Louisiana" ||
            e.target.feature.properties.name === "Pennsylvania") {
            var layer = e.target;
            layer.setStyle({
                weight: 5,
                color: '#666',
                dashArray: '',
                fillOpacity: 0.7
            });

        }
    }

    resetHighlight(e) { // listener for when mouse stops hovering state
        states.resetStyle(e.target);
    }

    componentDidUpdate() {
        if (this.props.districtsIsSet && this.props.currentIsSet && !map.hasLayer(realDistrict)) {
            realDistrict = L.geoJson(districtGeoJson, { style: this.realDistrictStyle }).addTo(map);
        } else if ((!this.props.districtsIsSet || !this.props.currentIsSet) && map.hasLayer(realDistrict)) {
            map.removeLayer(realDistrict);
        }
        if (this.props.districtsIsSet && this.props.averageIsSet && this.state.averageMap !== "" && !map.hasLayer(averageDistrict)) {
            averageDistrict = L.geoJson(this.state.averageMap, { style: this.averageDistrictStyle }).addTo(map);
        } else if ((!this.props.districtsIsSet || !this.props.averageIsSet || this.state.averageMap === "") && map.hasLayer(averageDistrict)) {
            map.removeLayer(averageDistrict);
        }

        if (this.props.districtsIsSet && this.props.extremeIsSet && this.state.extremeMap !== "" && !map.hasLayer(extremeDistrict)) {
            extremeDistrict = L.geoJson(this.state.extremeMap, { style: this.extremeDistrictStyle }).addTo(map);
        } else if ((!this.props.districtsIsSet || !this.props.extremeIsSet || this.state.extremeMap === "") && map.hasLayer(extremeDistrict)) {
            map.removeLayer(extremeDistrict);
        }

        if (this.props.selectedState === "CA") {
            this.zoomToCA();
        } else if (this.props.selectedState === "PA") {
            this.zoomToPA();
        } else if (this.props.selectedState === "LA") {
            this.zoomToLA();
        } else if (this.props.selectedState === "none") {
            this.zoomToMap();
        }
    }

    onStateClick(e) {
        if (e.target.feature.properties.name === "California") {
            this.zoomToCA();
            this.props.updateSelectedState("CA");
        } else if (e.target.feature.properties.name === "Pennsylvania") {
            this.zoomToPA();
            this.props.updateSelectedState("PA");

        } else if (e.target.feature.properties.name === "Louisiana") {
            this.zoomToLA();
            this.props.updateSelectedState("LA");

        } else { // if you click any of the other states than you are sent back to the starting position
            this.zoomToMap();
            this.props.updateSelectedState("none");

        }
    }

    zoomToCA() {
        map.setView([37.0, -119], 6.35);
        map.setMinZoom(6.35);
        map.setMaxZoom(6.35);
        map.scrollWheelZoom.enable();
        map.dragging.enable();
        if (this.state.caPrecinct === "") {
            this.loadCAPrecincts();
        }
        caPrecinct = L.geoJson(this.state.caPrecinct, { style: this.precinctStyle }).addTo(map);
        caPrecinct.bringToFront();
    }

    zoomToPA() {
        map.setMaxZoom(8);
        map.setMinZoom(8);
        map.setView([41.0, -77], 8);
        map.scrollWheelZoom.enable();
        map.dragging.enable();
        if (this.state.paPrecinct === "") {
            this.loadPAPrecincts();
        }
        paPrecinct = L.geoJson(this.state.paPrecinct, { style: this.precinctStyle }).addTo(map);
        paPrecinct.bringToFront();
    }

    zoomToLA() {
        map.setMaxZoom(7.5);
        map.setMinZoom(7.5);
        map.setView([31.0, -92], 8);
        map.scrollWheelZoom.enable();
        map.dragging.enable();
        if (this.state.laPrecinct === "") {
            this.loadLAPrecincts();
        }
        laPrecinct = L.geoJson(this.state.laPrecinct, { style: this.precinctStyle }).addTo(map);
        laPrecinct.bringToFront();
    }

    zoomToMap() {
        map.setMaxZoom(5);
        map.setMinZoom(5);
        map.setView([40.0, -98], 5);
        states.bringToFront();
        map.scrollWheelZoom.disable();
        map.dragging.disable();
        if(states.hasLayer(caPrecinct)){
            map.removeLayer(caPrecinct);
        } else if(states.hasLayer(paPrecinct)) {
            map.removeLayer(paPrecinct);
        } else if(states.hasLayer(laPrecinct)) {
            map.removeLayer(laPrecinct);
        }
    }

    render() {
        console.log(this.props);

        return (
            <div class="col bg-white" id="body-col">
                <div id="map-id"></div>
                {this.props.boxWhisker.length !== 0 ? <SummaryData plot={this.props.boxWhisker} ethnicities={this.props.ethnicities} /> : null}
            </div>
        );
    }
}

export default USMap;