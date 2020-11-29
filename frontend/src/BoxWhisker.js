import CanvasJSReact from './resources/canvasjs.react';
import React from 'react';
var CanvasJSChart = CanvasJSReact.CanvasJSChart;

class BoxWhisker extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            dataPoints: []
        };
    }

    componentDidMount() {
        var boxes = [];
        for(var i = 0; i<this.props.plot.length; i++) {
            var box = {label: "District " + i, y: [this.props.plot[i].minimum, this.props.plot[i].quartile1, this.props.plot[i].quartile3, this.props.plot[i].maximum, this.props.plot[i].median]};
            boxes.push(box);
        }
        this.setState({
            dataPoints:boxes
        }); 
    }

    render() {
        const options = {
            animationEnabled: true,
            title: { // Title of the plot
                text: "BVAP"
            },
            axisY: { // Y axis details
            title: "BVAP (in %)",
            interval: 50000
            },
            data: [{
            type: "boxAndWhisker",
            name: "Calculated VAP for Redistricting",
            showInLegend:true,
            upperBoxColor: "#FFC28D",
            lowerBoxColor: "#9ECCB8",
            color: "black",
            dataPoints: this.state.dataPoints
            },
            { //Dummy Data for now will have to replace with dynamic vap data
                type: "scatter",
                name: "Real Life Median VAP",
                showInLegend: true,
                dataPoints: [
                    {label: "District 1", y: 100000 },
                    {label: "District 2", y: 100000 },
                    {label: "District 3", y: 100000 },
                    {label: "District 4", y: 100000 },
                    {label: "District 5", y: 100000 },
                    {label: "District 6", y: 100000 }
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