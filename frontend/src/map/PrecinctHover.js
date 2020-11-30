import React from 'react';
import { MapControl, withLeaflet } from "react-leaflet";
import L from "leaflet";

class PrecinctHover extends MapControl {
  createLeafletElement(props) {}

  componentDidMount() { 
    const precinctHover = L.control({ position: "topright" });

    precinctHover.onAdd = () => {
      const div = L.DomUtil.create("div", "info legend");
      div.innerHTML = 'Hover over a precinct';
      return div;
    };

    const { map } = this.props.leaflet;
    precinctHover.addTo(map);
  }

  

}

export default withLeaflet(PrecinctHover);

