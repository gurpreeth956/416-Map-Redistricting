import CanvasJSReact from './resources/canvasjs.react';
import React from 'react';
var CanvasJS = CanvasJSReact.CanvasJS;
var CanvasJSChart = CanvasJSReact.CanvasJSChart;

class BoxWhisker extends React.Component {
    render() {
        const options = {
            animationEnabled: true,
            title: { // Title of the plot
                text: "BVAP"
            },
            axisY: { // Y axis details
            title: "BVAP (in %)",
            interval: 10
            },
            data: [{
            type: "boxAndWhisker",
            upperBoxColor: "#FFC28D",
            lowerBoxColor: "#9ECCB8",
            color: "black",
            dataPoints: [ // This is the data (y: [Min, Lower Q, High Q, Max, Median])
                { label: "District 1", y: [15, 20, 25, 30, 22.5] },
                { label: "District 2", y: [20, 25, 50, 52.5, 37.5] },
                { label: "District 3", y: [15, 20, 25, 30, 22.5] },
                { label: "District 4", y: [55, 70, 85, 90, 82.5] },
                { label: "District 5", y: [15, 30, 35, 50, 32.5] },
                { label: "District 6", y: [15, 20, 25, 40, 22.5] }
            ]
            }]
        }
        setTimeout(1000);
        return( <div>
            <CanvasJSChart options= {options}/>
        </div>);
    }
}

export default BoxWhisker;