import CanvasJSReact from './resources/canvasjs.react';
import React from 'react';
var CanvasJSChart = CanvasJSReact.CanvasJSChart;

class BoxWhisker extends React.Component {

    loadData() {
        var boxes = [];
        for(var i = 0; i<this.props.plot.length; i++) {
            var box = {label: "District " + (i+1), y: [this.props.plot[i].minimum, this.props.plot[i].quartile1, this.props.plot[i].quartile3, this.props.plot[i].maximum, this.props.plot[i].median]};
            boxes.push(box);
        }
        return boxes;
    }

    render() {
        var boxes = this.loadData();
        console.log(boxes);
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
            }
            // { //Dummy Data for now will have to replace with dynamic vap data
            //     type: "scatter",
            //     name: "Real Life Median VAP",
            //     showInLegend: true,
            //     dataPoints: [
            //         {label: "District 1", y: 50 },
            //         {label: "District 2", y: 50 },
            //         {label: "District 3", y: 50 },
            //         {label: "District 4", y: 50 },
            //         {label: "District 5", y: 50 },
            //         {label: "District 6", y: 50 }
            //     ]
            // }
        ]
        }
        setTimeout(1000);
        return( <div>
            <CanvasJSChart options= {options}/>
        </div>);
    }
}

export default BoxWhisker;