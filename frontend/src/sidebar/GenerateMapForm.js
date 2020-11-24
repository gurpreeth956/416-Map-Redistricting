import React from 'react';
import $ from 'jquery';
window.$ = $;

class GenerateMapForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      state: '',
      numMaps: '',
      black: false,
      hispanic: false,
      asian: false,
      native: false,
      hawaiin: false,
      popDiff: '',
      compactness: ''
    };
  }

  handleStateChange = event => {
		this.setState({
			state: event.target.value
    })
	}

  handleNumMapsChange = event => {
		this.setState({
			numMaps: event.target.value
		})
	}

  handleEthnicityChange = event => {
    const target = event.target;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    const name = target.name;
    this.setState({
         [name]: value
    });
  }

  handlePopDiffChange = event => {
		this.setState({
			popDiff: event.target.value
		})
	}

  handleCompactnessChange = event => {
		this.setState({
			compactness: event.target.value
		})
	}

  handleSubmit = event => {
		alert(`
      ${this.state.state}
      ${this.state.numMaps}
      Black/African American: ${this.state.black}
      Hispanic or Latino: ${this.state.hispanic}
      Asian: ${this.state.asian}
      American Indian or Alaskan Native: ${this.state.native}
      Native Hawaiian or Other Pacfic Islander: ${this.state.hawaiin}
      ${this.state.popDiff}
      ${this.state.compactness}
      `)
		event.preventDefault()
    var ethnicity = []
    if(this.state.black){
      ethnicity.push("BLACK_OR_AFRICAN_AMERICAN")
    }
    if(this.state.hispanic){
      ethnicity.push("HISPANIC_OR_LATINO")
    }
    if(this.state.asian){
      ethnicity.push("ASIAN")
    }
    if(this.state.native){
      ethnicity.push("AMERICAN_INDIAN")
    }
    if(this.state.hawaiin){
      ethnicity.push("NATIVE_HAWAIIAN_AND_OTHER_PACIFIC")
    }
    console.log(this.state.state)
    console.log(this.state.compactness)
    console.log(this.state.popDiff)
    console.log(ethnicity.toString())
    console.log(this.state.numMaps)
    console.log(ethnicity.toString())
    const data = {stateName: this.state.state, userCompactness: this.state.compactness,
    populationDifferenceLimit: this.state.popDiff, ethnicities: ethnicity.toString(),
    numberOfMaps: this.state.numMaps}
    const url = 'http://localhost:8080/initializeJob'
    $.ajax({
      url:url,
      type:"POST",
      data: data,
      success: (job) => {
        // addJob(job)
        console.log("Job Created Id: " + job.jobId)
      }
    });
	}

  render() {
      const { state, numMaps, ethnicity, popDiff, compactness } = this.state
      return (
        <div class="generate-content">
        							<div class="card-body">
        								<form>
        									<label class="font-weight-bold">State</label>
        									<div class="form-row align-items-center">
        										<div class="col-auto my-1">
        											<select class="custom-select mr-sm-2" id="inlineFormCustomSelect"
                              onChange={this.handleStateChange}>
        												<option selected>State</option>
        												<option value="CA">California</option>
        												<option value="LA">Lousiana</option>
        												<option value="PA">Pennslyvania</option>
        											</select>
        										</div>
        									</div>
        								</form>

        								<div class="form-group">
        									<label class="font-weight-bold">Number of Maps to Generate</label>
        									<input class="form-control" id="exampleInputPassword1" placeholder="Number of Maps"
                           value={numMaps} onChange={this.handleNumMapsChange}/>
        								</div>

        								<form>
        									<label class="font-weight-bold">Race/Ethnicity</label>
        									<label class="form-check">
        										<input class="form-check-input" type="checkbox" value=""
                            name="black" onChange={this.handleEthnicityChange}   checked={this.state.black}/>
        										<span class="form-check-label">
        											Black/African American
        										</span>
        									</label>
        									<label class="form-check">
        										<input class="form-check-input" type="checkbox" value=""
                            name="hispanic" onChange={this.handleEthnicityChange} checked={this.state.hispanic} />
                            <span class="form-check-label">
        											Hispanic or Latino
        										</span>
        									</label>
        									<label class="form-check">
        										<input class="form-check-input" type="checkbox" value=""
                            name="asian" onChange={this.handleEthnicityChange} checked={this.state.asian}/>
        										<span class="form-check-label">
        											Asian
        										</span>
        									</label>
        									<label class="form-check">
        										<input class="form-check-input" type="checkbox" value=""
                            name="native" onChange={this.handleEthnicityChange} checked={this.state.native}/>
        										<span class="form-check-label">
        											American Indian or Alaskan Native
        										</span>
        									</label>
        									<label class="form-check">
        										<input class="form-check-input" type="checkbox" value=""
                            name="hawaiin" onChange={this.handleEthnicityChange} checked={this.state.hawaiin}/>
        										<span class="form-check-label">
        											Native Hawaiian or Other Pacfic Islander
        										</span>
        									</label>
        								</form>

        								<div class="form-group">
        									<label class="font-weight-bold">Population Difference Limit</label>
        									<input class="form-control" id="exampleInputPassword1" placeholder="Difference Limit"
                          value={popDiff} onChange={this.handlePopDiffChange} />
        								</div>

        								<label class="font-weight-bold">Compactness Treshold</label>
                        <div class="btn-group">
          								<div class="form-check form-check-inline">
          									<input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="2"
                            onChange={this.handleCompactnessChange}/>
          									<label class="form-check-label" for="inlineRadio1">Very Compact</label>
          								</div>
          								<div class="form-check form-check-inline">
          									<input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="1"
                            onChange={this.handleCompactnessChange}/>
          									<label class="form-check-label" for="inlineRadio2">Somewhat Compact</label>
          								</div>
          								<div class="form-check form-check-inline">
          									<input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio3" value="0"
                            onChange={this.handleCompactnessChange}/>
          									<label class="form-check-label" for="inlineRadio3">Not Compact</label>
          								</div>
                        </div>

        								<button type="button" class="btn btn-primary" data-toggle="modal" href="#generation-modal" onClick={this.handleSubmit}>
                        Generate Maps</button>
        							</div>
        						</div>  )};
}

export default GenerateMapForm;

// const domContainer = document.querySelector('#GenerateMapForm');
// ReactDOM.render(e(GenerateMapForm), domContainer);
