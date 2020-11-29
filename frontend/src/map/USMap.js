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
            paPrecinct: '',
            caPrecinct: '',
            laPrecinct: '',
            paDistrict: '',
            caDistrict: '',
            laDistrict: '',
            zoomedOut: true
        }
        this.map = React.createRef();
        this.stateGeoJson = React.createRef();
    }

    componentDidMount() {
        //Load in districting geojson here
        // var data = {stateId: 0};
        // const url = 'http://localhost:8080/getDistricting'
        // $.ajax({
        //     url:url,
        //     type:"GET",
        //     data: data,
        //     success: (data) => {
        //         this.setState({caDistrict: JSON.parse(data)});
        //     }
        // });

        // data = {stateId: 1};
        // const url = 'http://localhost:8080/getDistricting'
        // $.ajax({
        //     url:url,
        //     type:"GET",
        //     data: data,
        //     success: (data) => {
        //         this.setState({paDistrict: JSON.parse(data)});
        //     }
        // });

        // data = {stateId: 2};
        // const url = 'http://localhost:8080/getDistricting'
        // $.ajax({
        //     url:url,
        //     type:"GET",
        //     data: data,
        //     success: (data) => {
        //         this.setState({laDistrict: JSON.parse(data)});
        //     }
        // });
    }

    loadCAPrecincts() {
        var data = {stateAbbreviation: 'CA'};
        const url = 'http://localhost:8080/getState'
        $.ajax({
            url:url,
            type:"GET",
            data: data,
            success: (data) => {
                this.setState({caPrecinct: JSON.parse(data)});
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
                this.setState({paPrecinct: JSON.parse(data)});
            }
        });
    }

    loadLAPrecincts() {
        console.log("loading lA")
        var data = {stateAbbreviation: 'LA'};
        const url = 'http://localhost:8080/getState'
        $.ajax({
            url:url,
            type:"GET",
            data: data,
            success: (data) => {
                this.setState({laPrecinct: JSON.parse(data)});
            }
        });
        console.log("finished loading La");
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
        // console.log(this.stateGeoJson);
        // this.stateGeoJson.leafletElement.resetStyle(e.target);
    }

    onStateClick(e) {
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
          } else if(e.target.feature.properties.name === "Louisiana") {
            this.map.current.leafletElement.setMaxZoom(7.5);
            this.map.current.leafletElement.setMinZoom(7.5);
            this.map.current.leafletElement.flyToBounds(e.target.getBounds());
            console.log(this.state);
            if(this.state.LA !== "") {
                this.loadLAPrecincts();
                this.setState({
                    zoomedOut:false
                });
            }
            this.loadLAPrecincts();
          } else { // if you click any of the other states than you are sent back to the starting position
            this.map.current.leafletElement.setMaxZoom(5);
            this.map.current.leafletElement.setMinZoom(5);
            this.map.current.leafletElement.setView([40.0, -98], 5);
            this.setState({
                zoomedOut: true
            });
          }
    }

    render() {
        console.log(this.props);
        return(
            <div class="col bg-white" id="body-col">
                <div id = "test"></div>
            <Map ref = {this.map} center={[40.0, -98]} zoom={5} scrollWheelZoom={false} minZoom={5} maxZoom={5} dragging={false} doubleClickZoom={false}>
                <TileLayer
                    attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://api.mapbox.com/styles/v1/mapbox/light-v9/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoidGVuemlubG9kZW4iLCJhIjoiY2tmZ3F5YmgzMDA5MDMybGF1dHNnN2JxNiJ9.7lGyZksjGSE669Hsufhtjg"
                />
                <GeoJSON ref = {this.stateGeoJson} data={stateGeoJson} onMouseOver={this.onMouseOver} onEachFeature={this.onEachFeature}></GeoJSON>
                {this.state.caPrecinct !== "" && !this.state.zoomedOut && this.props.precinctsIsSet ? 
					<GeoJSON data={this.state.caPrecinct}></GeoJSON> :
					null
				}
                {this.state.laPrecinct !== "" && !this.state.zoomedOut && this.props.precinctsIsSet ? 
					<GeoJSON data={this.state.laPrecinct}></GeoJSON> :
                    null
                }
                {this.state.paPrecinct !== "" && !this.state.zoomedOut && this.props.precinctsIsSet ? 
					<GeoJSON data={this.state.paPrecinct}></GeoJSON> :
					null
				}
                {this.state.caDistrict !== "" && this.props.districtsIsSet ? 
					<GeoJSON data={this.state.caDistrict}></GeoJSON> :
					null
				}
                {this.state.laDistrict !== "" && this.props.districtsIsSet ? 
					<GeoJSON data={this.state.laDistrict}></GeoJSON> :
                    null
                }
                {this.state.paDistrict !== "" && this.props.districtsIsSet ? 
					<GeoJSON data={this.state.paDistrict}></GeoJSON> :
					null
				}
            </Map>
            <SummaryData/>
            </div>
        );
    }
}

export default USMap;