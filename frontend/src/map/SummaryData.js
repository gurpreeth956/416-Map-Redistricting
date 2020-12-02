import React from 'react';
import $ from 'jquery';
import BoxWhisker from '../BoxWhisker';
window.$ = $;

class SummaryData extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <BoxWhisker plot={this.props.plot} ethnicities={this.props.ethnicities} />
                <div>
                    <h5>Ethnicities Selected: <span style={{marginTop:"10px", fontWeight:"normal", fontSize:"16px"}}>{this.props.ethnicities.join(', ')}</span> </h5>
                    
                    {/* <ul>
                        {this.props.ethnicities.map((ethnicity, index) => (
                            <li key={index}>{ethnicity}</li>
                        ))}
                    </ul> */}
                </div>
            </div>
        );
    }
}

export default SummaryData; 