import CanvasJSReact from './resources/canvasjs.react';
import React from 'react';
import lousianaData from './map/data/LouisianaDistrictData.json';
import pennsylvaniaData from './map/data/PennsylvaniaDistrictData.json';
import californiaData from './map/data/CaliforniaDistrictData.json';
var CanvasJSChart = CanvasJSReact.CanvasJSChart;

class BoxWhisker extends React.Component {

    loadGeneratedData() {
        var boxes = [];
        for (var i = 0; i < this.props.plot.length; i++) {
            var box = { label: "District " + (i + 1), y: [this.props.plot[i].minimum, this.props.plot[i].quartile1, this.props.plot[i].quartile3, this.props.plot[i].maximum, this.props.plot[i].median] };
            boxes.push(box);
        }
        return boxes;
    }

    loadRealLifeData(districtCount) {
        var state;
        if (districtCount === 6) {
            state = lousianaData;
        } else if (districtCount === 18) {
            state = pennsylvaniaData;
        } else {
            state = californiaData;
        }
        var dots = [];
        var data = [];
        for (var i = 0; i < districtCount; i++) {
            var vap = 0;
            if (this.props.ethnicities.includes("WHITE")) {
                vap += state[i].white_vap;
                if (this.props.ethnicities.includes("AMERICAN_INDIAN")) {
                    vap += state[i].native_and_white_vap;
                }
                if (this.props.ethnicities.includes("ASIAN")) {
                    vap += state[i].asian_and_white_vap;
                }
                if (this.props.ethnicities.includes("BLACK_OR_AFRICAN_AMERICAN")) {
                    vap += state[i].black_and_white_vap;
                }
            } 
            if (this.props.ethnicities.includes("BLACK_OR_AFRICAN_AMERICAN")) {
                vap += state[i].black_vap;
                if (this.props.ethnicities.includes("AMERICAN_INDIAN")) {
                    vap += state[i].native_and_black_vap;
                }
            }
            if (this.props.ethnicities.includes("HISPANIC_OR_LATINO")) {
                vap += state[i].hispanic_vap;
            } 
            if (this.props.ethnicities.includes("ASIAN")) {
                vap += state[i].asian_vap;
            } 
            if (this.props.ethnicities.includes("AMERICAN_INDIAN")) {
                vap += state[i].native_vap;
            } 
            if (this.props.ethnicities.includes("NATIVE_HAWAIIAN_AND_OTHER_PACIFIC")) {
                vap += state[i].hawaiian_vap;
            }
            data.push((vap / state[i].total_vap) * 100);
        }
        data.sort(function (a, b) {
            return a - b;
        });
        console.log(data);
        for (var i = 0; i < districtCount; i++) {
            var dot = { label: "Districts " + (i + 1), y: data[i] };
            dots.push(dot);
        }
        return dots;
    }

    render() {
        var boxes = this.loadGeneratedData();
        var dots = this.loadRealLifeData(boxes.length);
        const options = {
            animationEnabled: true,
            title: { // Title of the plot
                text: "Generated District VAP for Selected Ethnicities in Job"
            },
            axisY: { // Y axis details
                title: "VAP (in %)",
                interval: 10
            },
            data: [{
                type: "boxAndWhisker",
                name: "Calculated VAP for Redistricting",
                upperBoxColor: "#FFC28D",
                lowerBoxColor: "#9ECCB8",
                color: "black",
                dataPoints: boxes
            },
            { 
                type: "scatter",
                name: "Real Life Median VAP",
                dataPoints: dots
            }
            ]
        }
        setTimeout(1000);
        return (<div>
            <CanvasJSChart options={options} />
        </div>);
    }
}

export default BoxWhisker;