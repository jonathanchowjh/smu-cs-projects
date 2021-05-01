import React, { useRef } from 'react';
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { navigate, retrieveOrders, getStalls, getMenu } from '../redux/reducers/main'

const Map = (props) => {
  const c = useRef(null);
  const draw = () => {
    console.log(c)
    var ctx = c.current.getContext("2d");
    ctx.beginPath();
    ctx.arc(95, 50, 40, 0, 2 * Math.PI);
    ctx.stroke();
  }

  return (
    <div>
      Map
      <canvas ref={c} />
      <button onClick={draw}>DRAW</button>
    </div>
  )
}

const mapStateToProps = redux => ({ redux })
const mapDispatchToProps = dispatch => {
  return bindActionCreators({ navigate, retrieveOrders, getStalls, getMenu }, dispatch)
}


export default connect(mapStateToProps, mapDispatchToProps)(Map);
