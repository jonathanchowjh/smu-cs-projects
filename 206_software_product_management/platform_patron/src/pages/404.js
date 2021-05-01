import React from 'react'
import { Link } from "react-router-dom";

const ErrorPage = () => {
  return (
    <div className="container text-center">
      <div className="row">
        <div className="col-md-12">
          <div className="error-template">
              <h1 className="m-3">Oops!</h1>
              <h2>404 Not Found</h2>
              <div className="error-details">Sorry, an error has occured, Requested page not found!</div>
              <div className="error-actions">
                <Link to="/">
                  <div className="btn btn-primary btn-lg m-3">
                    Take Me Home
                  </div>
                </Link>
                <div
                  className="btn btn-default btn-lg m-3"
                  onClick={() => window.history.back()}
                >
                  Back
                </div>
              </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ErrorPage
