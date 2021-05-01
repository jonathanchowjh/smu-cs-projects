import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { navigate, getStalls } from '../../redux/reducers/main'
import { findInList, getParam, isFalse } from '../../common/functions'
import HCard from '../../components/HCard'

const Stall = (props) => {
  const [stall, setStall] = useState(0);

  useEffect(() => {
    let num = null;
    num = findInList(props.redux.main.stalls, "uen", getParam(props.redux.router.location.pathname, "/stall/"));
    if (isFalse(num)) {
      // get hawker
      props.getStalls({ uen: getParam(props.redux.router.location.pathname, "/stall/") })
    }
    setStall(num);
  }, [props.redux.main.stalls])

  return (
    <div>
      <div className="headers">Dishes: {`${props.redux.main.stalls[stall] && props.redux.main.stalls[stall].name}`}</div>
      {
        props.redux.main.stalls[stall] && props.redux.main.stalls[stall].menu.map((ele, num) => {
          console.log(ele)
          return (
            <HCard
              image={ele.image}
              onClick={() => props.navigate(`/menu?_id=${ele._id}&uen=${props.redux.main.stalls[stall].uen}`)}
              title={ele.name}
              line1={ele.description}
              line2={`Price: S$${ele.price}`}
              l2Class="text-success"
            />
          )
        })
      }
    </div>
  )
}

const mapStateToProps = redux => ({ redux })
const mapDispatchToProps = dispatch => {
  return bindActionCreators({ navigate, getStalls }, dispatch)
}


export default connect(mapStateToProps, mapDispatchToProps)(Stall);
