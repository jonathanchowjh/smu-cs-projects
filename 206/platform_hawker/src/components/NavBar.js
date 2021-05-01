import React from 'react'
import { Link } from "react-router-dom";

const NavBar = () => {
  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <div className="container-fluid justify-content-between">
        <Link to="/" className="navbar-brand">
          <img src="data:image/png;base64,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"
           alt="" width="30" height="24" className="d-inline-block align-text-top" />
          Jiak.png
        </Link>
        <div> 
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNavDropdown">
            <ul className="navbar-nav">
              <li className="nav-item">
                <Link to="/login"><a className="nav-link active" aria-current="page" href="#">Login</a></Link>
              </li>
              <li className="nav-item">
                <Link to="/menu"><a className="nav-link active" aria-current="page" href="#">Menu</a></Link>
              </li>
              <li className="nav-item">
                <Link to="/orders"><a className="nav-link" href="#">Orders</a></Link>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </nav>
  )
}

export default NavBar