import React from 'react';
import $ from 'jquery';
import L from 'leaflet';
import stateGeoJson from './states-geojson.json';
import SummaryData from './SummaryData.js';
import { Map, TileLayer, GeoJSON } from 'react-leaflet';

window.$ = $;

class USMap extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            PA: '',
            CA: '',
            LA: '',
            zoomedOut: true
        }
        this.map = React.createRef();
    }

    componentDidMount() {
        //Load in districting geojson here
        console.log(this.state);
    }

    loadCAPrecincts() {
        console.log("ca");
        var data = {stateAbbreviation: 'CA'};
        const url = 'http://localhost:8080/getState'
        $.ajax({
            url:url,
            type:"GET",
            data: data,
            success: (data) => {
                this.setState({CA: data});
            }
        });
    }

    loadPAPrecincts() {
        var data = {stateAbbreviation: 'PA'};
        const url = 'http://localhost:8080/getState'
        $.ajax({
            url:url,
            type:"GET",
            data: data,
            success: (data) => {
                this.setState({PA: data});
            }
        });
    }

    loadLAPrecincts() {
        var data = {stateAbbreviation: 'LA'};
        const url = 'http://localhost:8080/getState'
        $.ajax({
            url:url,
            type:"GET",
            data: data,
            success: (data) => {
                this.setState({LA: data});
            }
        });
    }

    onEachFeature = (feature, layer, e) => {
        layer.on({
            mouseover: this.highlightFeature.bind(this),
            mouseout: this.resetHighlight.bind(this),
            click: this.onStateClick.bind(this)
        });
    }

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
        // geojson.resetStyle(e.target);
    }

    onStateClick(e) {
        var precinctLayer;
        if (e.target.feature.properties.name === "California") {
            console.log(e);
            this.map.current.leafletElement.setMaxZoom(6.5);
            this.map.current.leafletElement.setMaxZoom(6.5);
            this.map.current.leafletElement.flyToBounds(e.target.getBounds());
            if(this.state.CA !== "") {
                this.loadCAPrecincts();
                this.setState({
                    zoomedOut:false
                });
            }
            
            // precinctLayer = L.geoJSON().addTo(this.map.current.leafletElement);
            // precinctLayer.addData(JSON.parse(this.state.CA));
          }
          else if (e.target.feature.properties.name === "Pennsylvania") {
            this.map.current.leafletElement.setMaxZoom(8);
            this.map.current.leafletElement.setMinZoom(8);
            this.map.current.leafletElement.flyToBounds(e.target.getBounds());
            if(this.state.PA !== "") {
                this.loadPAPrecincts();
                this.setState({
                    zoomedOut:false
                });
            }
            // precinctLayer = L.geoJSON().addTo(this.map.current.leafletElement);
            // precinctLayer.addData(JSON.parse(this.state.PA));
          } else if(e.target.feature.properties.name === "Louisiana") {
            this.map.current.leafletElement.setMaxZoom(7.5);
            this.map.current.leafletElement.setMinZoom(7.5);
            this.map.current.leafletElement.flyToBounds(e.target.getBounds());
            if(this.state.LA !== "") {
                this.loadLAPrecincts();
                this.setState({
                    zoomedOut:false
                });
            }
            this.loadLAPrecincts();
            // precinctLayer = L.geoJSON().addTo(this.map.current.leafletElement);
            // precinctLayer.addData(JSON.parse(this.state.LA));
          } else { // if you click any of the other states than you are sent back to the starting position
            if(this.map.current.leafletElement.hasLayer(precinctLayer)) {
                console.log("detected");
                this.map.current.leafletElement.removeLayer(precinctLayer);
            }
            this.map.current.leafletElement.setMaxZoom(5);
            this.map.current.leafletElement.setMinZoom(5);
            this.map.current.leafletElement.setView([40.0, -98], 5);
            this.setState({
                zoomedOut: true
            });
          }
    }

    render() {
        return(
            <div class="col bg-white" id="body-col">
                <div id = "test"></div>
            <Map ref = {this.map} center={[40.0, -98]} zoom={5} scrollWheelZoom={false} minZoom={5} maxZoom={5} dragging={false} doubleClickZoom={false}>
                <TileLayer
                    attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://api.mapbox.com/styles/v1/mapbox/light-v9/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoidGVuemlubG9kZW4iLCJhIjoiY2tmZ3F5YmgzMDA5MDMybGF1dHNnN2JxNiJ9.7lGyZksjGSE669Hsufhtjg"
                />
                <GeoJSON data={stateGeoJson} onMouseOver={this.onMouseOver} onEachFeature={this.onEachFeature}></GeoJSON>
                {this.state.CA !== "" && !this.state.zoomedOut ? 
					<GeoJSON data={this.state.CA}></GeoJSON> :
					null
				}
                {this.state.LA !== "" && !this.state.zoomedOut ? 
					<GeoJSON data={this.state.LA}></GeoJSON> :
					null
				}
                {this.state.PA !== "" && !this.state.zoomedOut ? 
					<GeoJSON data={this.state.PA}></GeoJSON> :
					null
				}
            </Map>
            <SummaryData/>
            </div>
        );
    }
}

export default USMap;