import React from 'react'

/**
 * 
 * @param {string} image
 * @param {string} title
 * @param {string} badge
 * @param {string} classname optional
 * @param {string} b1 optional
 * @param {string} b2 optional
 * @param {string} b1Name optional
 * @param {string} b2Name optional
 * @param {string} line1 optional
 * @param {string} line2 optional
 * @param {string} l1Class classname optional
 * @param {string} l2Class classname optional
 */

const cardCss = {
  margin: "1em",
}

const ButtonCard = (props) => {
  return (
    <div
      className={`card flex-row flex-wrap m-3 ${props.classname}`}
    >
      <div className="card-header border-0">
        <img
          alt=""
          style={{
            height: "6em",
            width: "6em",
            backgroundRepeat: "no-repeat",
            backgroundSize: "6em 6em",
            backgroundImage: `url(${props.image}), url(https://martialartsplusinc.com/wp-content/uploads/2017/04/default-image-620x600.jpg)`
          }}
        />
      </div>
      <div className="card-block text-start text-truncate p-3">
        <div className="card-title em-12">
          {props.title}
          {
            props.badge ? <span class="badge bg-secondary mx-2">{props.badge}</span> : <></>
          }
        </div>
        <div className={`card-text ${props.l1Class ? props.l1Class : ""}`}>{props.line1}</div>
        <div className={`card-text ${props.l2Class ? props.l2Class : ""}`}>{props.line2}</div>
        <br />
        {
          !props.b1 || !props.b1Name ? <></> : (
            <button
              type="button"
              className="btn btn-primary btn-sm mr-2 px-4"
              onClick={props.b1}
            >
              {props.b1Name}
            </button>
          )
        }
        {
          !props.b2 || !props.b2Name ? <></> : (
            <button
              type="button"
              className="btn btn-primary btn-sm mx-2 px-4"
              onClick={props.b2}
            >
              {props.b2Name}
            </button>
          )
        }
      </div>
      <div className="w-100"></div>
      {/* <div className="card-footer w-100 text-muted">FOOTER</div> */}
    </div>
  )
}

export default ButtonCard
