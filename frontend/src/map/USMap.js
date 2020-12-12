import React from 'react';
import $ from 'jquery';
import districtGeoJson from './data/districts-geojson.json';
import stateGeoJson from './data/states-geojson.json';
import pennsylvaniaHeatMap from './data/PennsylvaniaHeatMap.json'
import lousianaHeatMap from './data/LouisianaHeatMap.json'
import californiaHeatMap from './data/CaliforniaHeatMap.json'
import L from 'leaflet';
import 'leaflet.heat';
import './data/map.css'
import SummaryData from './SummaryData';
window.$ = $;

var map;
var states;
var realDistrict;
var extremeDistrict;
var averageDistrict;
var heatMap;
var mapLegend;
var precinctHover;
var laPrecinct;
var caPrecinct;
var paPrecinct;
class USMap extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            paPrecinct: '',
            caPrecinct: '',
            laPrecinct: '',
            currentState: 'none'
        }
    }

    componentDidMount() {
        map = L.map('map-id').setView([40.0, -98], 5);
        L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
            attribution: '...',
            id: 'mapbox/light-v9',
            tileSize: 512,
            zoomOffset: -1,
            // Setting min/max zoom here causes map to grey on state click doing below instead
            scrollWheelZoom: false,
            dragging: false,
            accessToken: 'pk.eyJ1IjoidGVuemlubG9kZW4iLCJhIjoiY2tmZ3F5YmgzMDA5MDMybGF1dHNnN2JxNiJ9.7lGyZksjGSE669Hsufhtjg'
        }).addTo(map);
        map.setMaxZoom(5);
        map.setMinZoom(5);

        states = L.geoJson(stateGeoJson, { style: this.stateStyle, onEachFeature: this.stateOnEachFeature }).addTo(map);

        if (this.props.districtsIsSet && this.props.currentIsSet && !map.hasLayer(realDistrict)) {
            realDistrict = L.geoJson(districtGeoJson, { style: this.realDistrictStyle }).addTo(map);
            this.setState({
                realDistrictCurrentlyOn:true
            });
        } else if ((!this.props.districtsIsSet || !this.props.currentIsSet) && map.hasLayer(realDistrict)) {
            map.removeLayer(realDistrict);
        }
        if (this.props.districtsIsSet && this.props.averageIsSet && this.props.averageMap !== "" && !map.hasLayer(averageDistrict)) {
            averageDistrict = L.geoJson(this.props.averageMap, { style: this.averageDistrictStyle }).addTo(map);
        } else if ((!this.props.districtsIsSet || !this.props.averageIsSet || this.props.averageMap === "") && map.hasLayer(averageDistrict)) {
            map.removeLayer(averageDistrict);
        }

        if (this.props.districtsIsSet && this.props.extremeIsSet && this.props.extremeMap !== "" && !map.hasLayer(extremeDistrict)) {
            extremeDistrict = L.geoJson(this.props.extremeMap, { style: this.extremeDistrictStyle }).addTo(map);
        } else if ((!this.props.districtsIsSet || !this.props.extremeIsSet || this.props.extremeMap === "") && map.hasLayer(extremeDistrict)) {
            map.removeLayer(extremeDistrict);
        }

        if (this.props.heatMapIsSet && this.props.precinctsIsSet && !map.hasLayer(heatMap) && this.props.selectedState !== "none"){
            heatMap =  L.heatLayer(this.loadHeatMapData(),  {gradient: {0.2: 'blue', 0.5: 'lime', 0.8: 'red'}, radius: 25}).addTo(map);
            //heatMap.bringToFront();
            this.addHeatMapLegend()
            
        } else if (this.props.heatMapIsSet && this.props.precinctsIsSet && map.hasLayer(heatMap) && this.props.selectedState !== "none") {
            heatMap.setLatLngs(this.loadHeatMapData());
        } else if ((!this.props.heatMapIsSet || !this.props.precinctsIsSet || this.props.heatMap === "" || this.props.selectedState === "none") && map.hasLayer(heatMap)) {
            map.removeLayer(heatMap);
            //map.removeControl(heatMapLegend);
            this.removeHeatMapLegend();
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

    stateOnEachFeature = (feature, layer, e) => {
        layer.on({
            click: this.onStateClick.bind(this)
        });
    }

    /*onMouseOut = (e) => {
        this.hover =  '<h4> Hover over a precinct </h4>';
    }

    onMouseOver = (e) => {
        this.hover =  '<p> test </p>';
    }*/

    loadHeatMapData() {
        var state;
        var length;
        if (this.props.selectedState === "LA") {
            console.log("LouisianaHeatMap")
            state = lousianaHeatMap;
            length = 3688;
        } else if (this.props.selectedState === "PA") {
            console.log("PennsylvaniaHeatMap")
            state = pennsylvaniaHeatMap;
            length = 9080;
        } else if (this.props.selectedState === "CA"){
            console.log("CaliforniaHeatMap")
            state = californiaHeatMap;
            length = 16775;
        } else {
            return;
        }
        var points = [];
        for (var i = 0; i < length; i++) {
            var data = [];
            data.push(Number(state[i].Long));
            data.push(Number(state[i].Lat));
            var pop = 0;
            var total = state[i].total_pop;
            if (this.props.blackIsSet){
                pop += state[i].black_pop;
                if (this.props.nativeIsSet) {
                    pop += state[i].native_and_black_vap;
                }
            }  
            if (this.props.whiteIsSet){
                pop += state[i].white_pop;
                if (this.props.nativeIsSet) {
                    pop += state[i].native_and_white_vap;
                }
                if (this.props.asianIsSet) {
                    pop += state[i].asian_and_white_vap;
                }
                if (this.props.blackIsSet) {
                    pop += state[i].black_and_white_vap;
                }
            }
            if (this.props.asianIsSet){
                pop += state[i].asian_pop;
            }
            if (this.props.hawaiianIsSet){
                pop += state[i].hawaiian_pop;
            }
            if (this.props.nativeIsSet){
                pop += state[i].native_pop;
            }
            if (this.props.hispanicIsSet){
                pop += state[i].hispanic_pop;
            }
            var intensity= pop / total;
            data.push(intensity);
            points.push(data);
        }            
        return points;
    }

    addMapLegend() {
        var legend = L.control({position: 'bottomright'});
        legend.onAdd = function (map) {
           var div = L.DomUtil.create('div', 'info legend');
            // loop through our density intervals and generate a label with a colored square for each interval
            div.innerHTML = '<b> Legend </b> <br>' +
            '<i style="background: darkcyan;"></i> District Boundary<br>' +
            '<i style="background: orangered;"></i> Precinct Boundary<br>';
            return div;
        };
        mapLegend = legend.addTo(map);
    }

    addHeatMapLegend() {
        mapLegend._container.innerHTML += '<br><b> Heat Map </b> <br>'
        mapLegend._container.innerHTML += '<i style="background: #FF0000;"></i> ' + 80 + '% <br>' 
        mapLegend._container.innerHTML += '<i style="background: #00FF00;"></i> ' + 50 + '% <br>' 
        mapLegend._container.innerHTML += '<i style="background: #0000FF;"></i> ' + 20 + '% <br>' 
    }

    removeHeatMapLegend(){
        if(mapLegend){
            mapLegend._container.innerHTML = '<b> Legend </b> <br>' +
                '<i style="background: darkcyan;"></i> District Boundary<br>' +
                '<i style="background: orangered;"></i> Precinct Boundary<br>';
        }
    }

    addPrecinctHover(props) {
        console.log("add precinctHover")
        var info = L.control({position: 'topright'});
        info.onAdd = function (map) {
            var div = L.DomUtil.create('div', 'info'); // create a div with a class "info"
            div.innerHTML = '<h4>Hover over a precinct</h4>'
        return div;
        };
        // method that we will use to update the control based on feature properties passed
        precinctHover = info.addTo(map);
    }

    precinctOnEachFeature = (feature, layer, e) => {
        layer.on({
            mouseover: this.highlightFeature,
			mouseout: this.resetHighlight
        });
    }

    highlightFeature(e) {
        var layer = e.target;
        var props = layer.feature.properties;
        console.log(precinctHover)
        console.log(props)
        precinctHover._container.innerHTML = 
                '<b>Precinct ID:</b> '+ props.VTDST10 +  '<br />' +
                '<b>County ID:</b> '+ props.COUNTYFP10 +  '<br />' +
                '<b>Total Population:</b> '+ props.total_pop + '<br />' +
                '<b>Total VAP:</b> '+ props.total_vap + '<br />' +
                '<b>White Pop.:</b> '+ props.white_pop + '<br />' +
                '<b>White VAP:</b> '+ props.white_vap + '<br />' +
                '<b>African American Pop.:</b> '+ props.black_pop + '<br />' +
                '<b>African American VAP:</b> '+ props.black_vap + '<br />' +
                '<b>Hispanic/Latino Pop.:</b> '+ props.hispanic_pop + '<br />' +
                '<b>Hispanic/Latino VAP:</b> '+ props.hispanic_vap + '<br />' +
                '<b>Asian Pop.:</b> '+ props.asian_pop + '<br />' +
                '<b>Asian VAP:</b> '+ props.asian_vap + '<br />' +
                '<b>Native American Pop.:</b> '+ props.native_pop + '<br />' +
                '<b>Native American VAP:</b> '+ props.native_vap + '<br />' +
                '<b>Hawaiian/Pacific Pop.:</b> '+ props.hawaiian_pop + '<br />' +
                '<b>Hawaiian/Pacific VAP:</b> '+ props.hawaiian_vap + '<br />';

    }

    resetHighlight(e) {
        precinctHover._container.innerHTML = '<h4>Hover over a precinct</h4>'
    }

    componentDidUpdate() {
        if (this.props.districtsIsSet && this.props.currentIsSet && !map.hasLayer(realDistrict)) {
            realDistrict = L.geoJson(districtGeoJson, { style: this.realDistrictStyle }).addTo(map);
        } else if ((!this.props.districtsIsSet || !this.props.currentIsSet) && map.hasLayer(realDistrict)) {
            map.removeLayer(realDistrict);
        }
        console.log(this.props.districtsIsSet && this.props.averageIsSet && this.props.averageMap !== "" && !map.hasLayer(averageDistrict));
        if (this.props.districtsIsSet && this.props.averageIsSet && this.props.averageMap !== "" && !map.hasLayer(averageDistrict)) {
            averageDistrict = L.geoJson(this.props.averageMap, { style: this.averageDistrictStyle }).addTo(map);
        } else if ((!this.props.districtsIsSet || !this.props.averageIsSet || this.props.averageMap === "") && map.hasLayer(averageDistrict)) {
            map.removeLayer(averageDistrict);
        }

        if (this.props.districtsIsSet && this.props.extremeIsSet && this.props.extremeMap !== "" && !map.hasLayer(extremeDistrict)) {
            extremeDistrict = L.geoJson(this.props.extremeMap, { style: this.extremeDistrictStyle }).addTo(map);
        } else if ((!this.props.districtsIsSet || !this.props.extremeIsSet || this.props.extremeMap === "") && map.hasLayer(extremeDistrict)) {
            map.removeLayer(extremeDistrict);
        }

        if (this.props.heatMapIsSet && this.props.precinctsIsSet && !map.hasLayer(heatMap) && this.props.selectedState !== "none"){
            heatMap =  L.heatLayer(this.loadHeatMapData(),  {gradient: {0.2: '#0000FF', 0.5: '#00FF00',  0.8: '#FF0000'}, radius: 25}).addTo(map);
            //heatMap.bringToFront();
            this.addHeatMapLegend();
            
        } else if (this.props.heatMapIsSet && this.props.precinctsIsSet && map.hasLayer(heatMap) && this.props.selectedState !== "none") {
            heatMap.setLatLngs(this.loadHeatMapData());
        } else if ((!this.props.heatMapIsSet || !this.props.precinctsIsSet || this.props.heatMap === "" || this.props.selectedState === "none") && map.hasLayer(heatMap)) {
            map.removeLayer(heatMap);
           // map.removeControl(heatMapLegend);
           this.removeHeatMapLegend();
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
        if(this.state.currentState !== this.props.selectedState) {
            map.setMaxZoom(15);
            map.setMinZoom(6.35);
            map.setView([37.5, -119], 6.35);
            map.setMaxBounds(L.latLngBounds(L.latLng(42.2,-105.4),L.latLng(32,-130.8)));
            
            map.scrollWheelZoom.enable();
            map.dragging.enable();

            this.setState({
                currentState:"CA"
            });
        }
        if(!mapLegend){
            this.addMapLegend();
        }
        if(map.hasLayer(caPrecinct)){
            caPrecinct.bringToFront();
        }
        
        if (this.state.caPrecinct === "") {
            this.loadCAPrecincts();
        }
        if(this.state.caPrecinct !== "" && this.props.precinctsIsSet && !map.hasLayer(caPrecinct)) {
            this.addPrecinctHover();
            caPrecinct = L.geoJson(this.state.caPrecinct, { style: this.precinctStyle, onEachFeature: this.precinctOnEachFeature }).addTo(map);
            caPrecinct.bringToFront();
        } else if((this.state.caPrecinct === "" || !this.props.precinctsIsSet) && map.hasLayer(caPrecinct)){
            map.removeLayer(caPrecinct);
            map.removeControl(precinctHover);
        }  
    }

    zoomToPA() {
        if(this.state.currentState !== this.props.selectedState) {
            map.setMaxZoom(15);
            map.setMinZoom(8);
            map.setView([41.0, -77.5], 8);
            map.setMaxBounds(L.latLngBounds(L.latLng(42.5,-73.45),L.latLng(39.4, -81.5)));

            map.scrollWheelZoom.enable();
            map.dragging.enable();
            this.setState({
                currentState:"PA"
            });
        }
        if(!mapLegend){
            this.addMapLegend();
        }
        if(map.hasLayer(paPrecinct)){
            paPrecinct.bringToFront();
        }
        if (this.state.paPrecinct === "") {
            this.loadPAPrecincts();
        }
        if(this.state.paPrecinct !== "" && this.props.precinctsIsSet && !map.hasLayer(paPrecinct)) {
            this.addPrecinctHover();
            paPrecinct = L.geoJson(this.state.paPrecinct, { style: this.precinctStyle, onEachFeature: this.precinctOnEachFeature}).addTo(map);
            paPrecinct.bringToFront();
        } else if((this.state.paPrecinct === "" || !this.props.precinctsIsSet) && map.hasLayer(paPrecinct)){
            map.removeLayer(paPrecinct);
            map.removeControl(precinctHover);
        }  
    }

    zoomToLA() {
        if(this.state.currentState !== this.props.selectedState) {
            map.setMaxZoom(15);
            map.setMinZoom(7.45);
            map.setView([32.0, -91], 7.45);
            map.setMaxBounds(L.latLngBounds(L.latLng(33.5, -85),L.latLng(28, -97)));

            map.scrollWheelZoom.enable();
            map.dragging.enable();
            this.setState({
                currentState:"LA"
            });
        }
        if(!mapLegend){
            this.addMapLegend();
        }
        if(map.hasLayer(laPrecinct)){
            laPrecinct.bringToFront();
        }
        if (this.state.laPrecinct === "") {
            this.loadLAPrecincts();
        }
        if(this.state.laPrecinct !== "" && this.props.precinctsIsSet && !map.hasLayer(laPrecinct)) {
            this.addPrecinctHover();
            laPrecinct = L.geoJson(this.state.laPrecinct, { style: this.precinctStyle, onEachFeature: this.precinctOnEachFeature }).addTo(map);
            laPrecinct.bringToFront();
        } else if((this.state.laPrecinct === "" || !this.props.precinctsIsSet) && map.hasLayer(laPrecinct)){
            map.removeLayer(laPrecinct);
            map.removeControl(precinctHover);
        }  
    }

    zoomToMap() {
        if(this.state.currentState !== this.props.selectedState) {
            map.setMaxZoom(5);
            map.setMinZoom(5);
            map.setView([40.0, -98], 5);
            map.setMaxBounds(L.latLngBounds(L.latLng(51.5, -65.6),L.latLng(26.2, -130.3)));

            map.scrollWheelZoom.disable();
            map.dragging.disable();
            this.setState({
                currentState:"none"
            });
        }
        states.bringToFront();
        if(mapLegend){
            map.removeControl(mapLegend);
            mapLegend = null;
        }
        if(map.hasLayer(caPrecinct)){
            map.removeLayer(caPrecinct);
            if(precinctHover){
                map.removeControl(precinctHover);
            }
        }
        if(map.hasLayer(paPrecinct)) {
            map.removeLayer(paPrecinct);
            if(precinctHover){
                map.removeControl(precinctHover);
            }
        }
        if(map.hasLayer(laPrecinct)) {
            map.removeLayer(laPrecinct);
            if(precinctHover){
                map.removeControl(precinctHover);
            }
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