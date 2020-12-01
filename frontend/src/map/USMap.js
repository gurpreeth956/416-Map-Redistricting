import React from 'react';
import $ from 'jquery';
import districtGeoJson from './districts-geojson.json';
import stateGeoJson from './states-geojson.json';
import L from 'leaflet';
import { Map, TileLayer, GeoJSON } from 'react-leaflet';
import BoxWhisker from '../BoxWhisker';
import PrecinctHover from "./PrecinctHover";

window.$ = $;

class USMap extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            paPrecinct: '',
            caPrecinct: '',
            laPrecinct: ''        
        }
        this.map = React.createRef();
        this.stateGeoJson = React.createRef();
        this.realDistricts = React.createRef();
    }

    componentDidMount() {
        var districts = L.geoJson(stateGeoJson);
        console.log(districts);
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
        } else if (name === "extreme District") {
            return 'yellow';
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
            // mouseover: this.highlightFeature.bind(this),
            // mouseout: this.resetHighlight.bind(this),
            click: this.onStateClick.bind(this)
        });
    }  

    /*onMouseOut = (e) => {
        this.hover =  '<h4> Hover over a precinct </h4>';
    }

    onMouseOver = (e) => {
        this.hover =  '<p> test </p>';
    }*/

    // highlightFeature(e) { //when mouse hovers on one of the 3 states, border is outlined and hover info in top right is updated
    //     if (e.target.feature.properties.name === "California" ||
    //         e.target.feature.properties.name === "Louisiana" ||
    //         e.target.feature.properties.name === "Pennsylvania") {
    //         console.log(e.target);
    //         var layer = e.target;
    //         layer.setStyle({
    //             weight: 5,
    //             color: '#666',
    //             dashArray: '',
    //             fillOpacity: 0.7
    //         });

    //     }
    // }

    // resetHighlight(e) { // listener for when mouse stops hovering state
    //     // console.log(this.stateGeoJson);
    //     // this.stateGeoJson.leafletElement.resetStyle(e.target);
    // }

    componentDidUpdate() {
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
        this.map.current.leafletElement.setMaxZoom(6.35);
        this.map.current.leafletElement.setMaxZoom(6.35);
        this.map.current.leafletElement.setView([37.0, -119], 6.35);
        if (this.state.caPrecinct === "") {
            this.loadCAPrecincts();
        }
    }

    zoomToPA() {
        this.map.current.leafletElement.setMaxZoom(8);
        this.map.current.leafletElement.setMinZoom(8);
        this.map.current.leafletElement.setView([41.0, -77], 8);
        if (this.state.paPrecinct === "") {
            this.loadPAPrecincts();
        }
    }

    zoomToLA() {
        this.map.current.leafletElement.setMaxZoom(7.5);
        this.map.current.leafletElement.setMinZoom(7.5);
        this.map.current.leafletElement.setView([31.0, -92], 8);
        if (this.state.laPrecinct === "") {
            this.loadLAPrecincts();
        }
    }

    zoomToMap() {
        this.map.current.leafletElement.setMaxZoom(5);
        this.map.current.leafletElement.setMinZoom(5);
        this.map.current.leafletElement.setView([40.0, -98], 5);
    }

    render() {
        console.log(this.props);
        var mapClass;
        var precinctClass;
        if (this.state.zoomedOut == true) {
            mapClass = "front-geojson-class";
            precinctClass = "back-geojson-class";
        } else {
            precinctClass = "front-geojson-class"
            mapClass = "back-geojson-class";
        }
        return (
            <div class="col bg-white" id="body-col">
                <Map ref={this.map} center={[40.0, -98]} zoom={5} scrollWheelZoom={false} minZoom={5} maxZoom={5} dragging={false} doubleClickZoom={false}>
                    <TileLayer
                        attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                        url="https://api.mapbox.com/styles/v1/mapbox/light-v9/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoidGVuemlubG9kZW4iLCJhIjoiY2tmZ3F5YmgzMDA5MDMybGF1dHNnN2JxNiJ9.7lGyZksjGSE669Hsufhtjg"
                    />

                    {/* GeoJSON for IRL Precincts */}
                    {this.props.selectedState === "CA" && this.state.caPrecinct !== "" && this.props.precinctsIsSet ?
                        <GeoJSON data={this.state.caPrecinct} className={precinctClass} style={this.precinctStyle}></GeoJSON> :
                        null
                    }
                    {this.props.selectedState === "LA" && this.state.laPrecinct !== "" && this.props.precinctsIsSet ?
                        <GeoJSON data={this.state.laPrecinct} className={precinctClass} style={this.precinctStyle}></GeoJSON> :
                        null
                    }

                    {this.props.selectedState === "PA" && this.state.paPrecinct !== "" && this.props.precinctsIsSet ?
                        <GeoJSON data={this.state.paPrecinct} className={precinctClass} style={this.precinctStyle}></GeoJSON> :
                        null
                    }
                    {/* GeoJSON for State */}
                    <GeoJSON ref={this.stateGeoJson} data={stateGeoJson} className={mapClass} style={this.stateStyle} onEachFeature={this.onEachFeature}>
                        {/* GeoJSON for IRL Districts */}
                        {this.props.districtsIsSet && this.props.currentIsSet ?
                            <GeoJSON ref={this.realDistricts} data={districtGeoJson} style={this.realDistrictStyle} className={"back-geojson-class"}></GeoJSON> :
                            null
                        }
                        {/* GeoJSON for job maps */}
                        {this.props.averageMap !== "" && this.props.districtsIsSet && this.props.averageIsSet ?
                            <GeoJSON data={this.props.averageMap} style={this.averageDistrictStyle}></GeoJSON> :
                            null
                        }
                        {this.state.extremeMap !== "" && this.props.districtsIsSet && this.props.extremeIsSet ?
                            <GeoJSON data={this.props.extremeMap} style={this.extremeDistrictStyle}></GeoJSON> :
                            null
                        }
                    </GeoJSON>
                </Map>
                {this.props.boxWhisker.length !== 0 ? <BoxWhisker plot={this.props.boxWhisker} /> : null}
            </div>
        );
    }
}

export default USMap;