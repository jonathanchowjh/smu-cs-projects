import React from 'react'

/**
 * 
 * @param {string} image
 * @param {func} onClick
 * @param {string} title
 * @param {string} classname optional
 * @param {string} line1 optional
 * @param {string} line2 optional
 * @param {string} line3 optional
 * @param {string} l1Class classname optional
 * @param {string} l2Class classname optional
 * @param {string} l3Class classname optional
 */

const cardCss = {
  margin: "1em",
}

const MenuCard = (props) => {
  return (
    <div
      className={`card flex-row m-2 em-8 ${props.classname}`}
      onClick={props.onClick}
    >
      <div className="card-header border-0">
        <img
          alt=""
          style={{
            height: "9em",
            width: "9em",
            backgroundRepeat: "no-repeat",
            backgroundSize: "9em 9em",
            borderRadius: 5,
            backgroundImage: `url(${props.image}), url(https://martialartsplusinc.com/wp-content/uploads/2017/04/default-image-620x600.jpg)
            `
          }}
        />
      </div>
      <div className="card-block text-start p-3">
        <div className="card-title elements-hawker-centres-list" style={{ color: "#000000", fontWeight: 600, fontSize: '20px', textAlign: 'left'}}> {props.title}</div>
        <div className={`card-text ${props.l3Class ? props.l3Class : ""}`}>{props.line3}</div> 
        <div className={`card-text ${props.l2Class ? props.l2Class : ""}`}>{props.line2}</div>
        <div className={`card-text ${props.l1Class ? props.l1Class : ""}`} style={{ color: "#4F4F4F", fontSize: '14px'}}>{props.line1}</div>
      </div>
      <div className="w-20"></div>
      {/* <div className="card-footer w-100 text-muted">FOOTER</div> */}
    </div>
  )
}

export default MenuCard
