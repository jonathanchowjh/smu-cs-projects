import React, { useEffect } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { Link } from "react-router-dom";
import { logout, gateway } from '../redux/reducers/main'

const NavBar = (props) => {
  useEffect(() => {
    props.gateway()
  }, [])
  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <div className="container-fluid justify-content-between">
        <Link to="/" className="navbar-brand">
          <img src="/jiakpnglogo.png"
          alt="" width="40" height="50" className="d-inline-block"/>
        &nbsp;&nbsp;&nbsp;<span style={{color: "#EE693F", fontWeight: 800, fontSize: '24px', fontFamily: 'Montserrat', verticalAlign: 'bottom'}}>Jiak.PNG</span>
        </Link>
        <div>
          <Link to="/cart" className="p-3">
            <img src="/cart.svg" alt="" width="25" height="25" className="d-inline-block align-text-top" />
          </Link>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNavDropdown">
            <ul className="navbar-nav">
              {
                props.redux.main.user.loggedin ? (
                  <li style={{color: "#EE693F", fontWeight: 800, fontSize: '24px', fontFamily: 'Montserrat'}} onClick={() => props.logout()}>Logout </li>
                ) : (
                  <>
                    <li className="nav-item">
                      <Link to="/registration"><a className="nav-link" href="#">Register</a></Link>
                    </li>
                    <li className="nav-item">
                      <Link to="/login"><a className="nav-link" href="#">Login</a></Link>
                    </li>
                  </>
                )
              }
            </ul>
          </div>
        </div>
      </div>
    </nav>
  )
}

const mapStateToProps = redux => ({ redux })
const mapDispatchToProps = dispatch => {
  return bindActionCreators({ logout, gateway }, dispatch)
}


export default connect(mapStateToProps, mapDispatchToProps)(NavBar);