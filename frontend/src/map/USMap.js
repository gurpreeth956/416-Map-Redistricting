import React from 'react';
import { MapContainer, TileLayer, GeoJSON } from "react-leaflet";
import $ from 'jquery';
import geoJson from './us-states.json';
import SummaryData from './SummaryData.js';

window.$ = $;

class USMap extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            PA: '',
            CA: '',
            LA: ''
        }
    }

    componentDidMount() {
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

        data = {stateAbbreviation: 'PA'};

        $.ajax({
            url:url,
            type:"GET",
            data: data,
            success: (data) => {
                this.setState({PA: data});
            }
        });

        data = {stateAbbreviation: 'CA'};

        $.ajax({
            url:url,
            type:"GET",
            data: data,
            success: (data) => {
                this.setState({CA: data});
            }
        });

    }

    render() {
        console.log(this.state);
        return(
            <div class="col bg-white" id="body-col">
            <MapContainer center={[40.0, -98]} zoom={5} scrollWheelZoom={false} minZoom={5} maxZoom={5} dragging={false}>
                <TileLayer
                    attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://api.mapbox.com/styles/v1/mapbox/light-v9/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoidGVuemlubG9kZW4iLCJhIjoiY2tmZ3F5YmgzMDA5MDMybGF1dHNnN2JxNiJ9.7lGyZksjGSE669Hsufhtjg"
                />
                <GeoJSON data={geoJson}></GeoJSON>
            </MapContainer>
            <SummaryData/>
            </div>
        );
    }
}

export default USMap;