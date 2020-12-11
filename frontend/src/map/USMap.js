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
        if(map.hasLayer(caPrecinct)){
            caPrecinct.bringToFront();
        }
        
        if (this.state.caPrecinct === "") {
            this.loadCAPrecincts();
        }
        if(this.state.caPrecinct !== "" && this.props.precinctsIsSet && !map.hasLayer(caPrecinct)) {
            caPrecinct = L.geoJson(this.state.caPrecinct, { style: this.precinctStyle }).addTo(map);
            caPrecinct.bringToFront();
        } else if((this.state.caPrecinct === "" || !this.props.precinctsIsSet) && map.hasLayer(caPrecinct)){
            map.removeLayer(caPrecinct);
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
        if(map.hasLayer(paPrecinct)){
            paPrecinct.bringToFront();
        }
        if (this.state.paPrecinct === "") {
            this.loadPAPrecincts();
        }
        if(this.state.paPrecinct !== "" && this.props.precinctsIsSet && !map.hasLayer(paPrecinct)) {
            paPrecinct = L.geoJson(this.state.paPrecinct, { style: this.precinctStyle }).addTo(map);
            paPrecinct.bringToFront();
        } else if((this.state.paPrecinct === "" || !this.props.precinctsIsSet) && map.hasLayer(paPrecinct)){
            map.removeLayer(paPrecinct);
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
        if(map.hasLayer(laPrecinct)){
            laPrecinct.bringToFront();
        }
        if (this.state.laPrecinct === "") {
            this.loadLAPrecincts();
        }
        if(this.state.laPrecinct !== "" && this.props.precinctsIsSet && !map.hasLayer(laPrecinct)) {
            laPrecinct = L.geoJson(this.state.laPrecinct, { style: this.precinctStyle }).addTo(map);
            laPrecinct.bringToFront();
        } else if((this.state.laPrecinct === "" || !this.props.precinctsIsSet) && map.hasLayer(laPrecinct)){
            map.removeLayer(laPrecinct);
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

        if(map.hasLayer(caPrecinct)){
            map.removeLayer(caPrecinct);
        }
        if(map.hasLayer(paPrecinct)) {
            map.removeLayer(paPrecinct);
        }
        if(map.hasLayer(laPrecinct)) {
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