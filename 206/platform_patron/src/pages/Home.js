import React, { useState } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { navigate } from '../redux/reducers/main'


const Home = (props) => {
  const [form, setForm] = useState({ phone: "", password: "" })
  // redirect to a list of all hawker centres
  const handleListOfHawkerCentres = () => props.navigate("/hawkers")

  // redirect to the patron's previous order(s)
  const handlePatronsPreviousOrders = () => props.navigate("/orders")

  return (
    <div class="all-bg">
      <button type="button" className="btn btn-primary btn-lg btn1-main hero-image" style={{ color: "#FFFFFF", fontWeight: 700, fontSize: '16px'}} onClick={handleListOfHawkerCentres}><img src="/HawkerCentres.jpeg" width="120px" height="120px"  style={{borderRadius: '100px', marginLeft: '0px', marginTop: '0px'}}></img> <div style={{marginTop: '10px'}}>  Hawker Centres </div></button>
      <button type="button" className="btn btn-primary btn-lg btn2-main hero-image" style={{ color: "#FFFFFF", fontWeight: 700, fontSize: '16px'}} onClick={handlePatronsPreviousOrders}><img src="/previous.svg" width="120px" height="120px"  style={{borderRadius: '20px', marginLeft: '0px', marginTop: '0px'}}></img> <div style={{marginTop: '10px'}}>  Previous Orders </div></button>

    </div>
  )
}

const mapStateToProps = redux => ({ redux })
const mapDispatchToProps = dispatch => {
  return bindActionCreators({ navigate }, dispatch)
}


export default connect(mapStateToProps, mapDispatchToProps)(Home);