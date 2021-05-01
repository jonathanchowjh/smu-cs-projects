import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { navigate, getHawkers } from '../../redux/reducers/main'
import { findInList, getParam, isFalse, openingHours, isOpen } from '../../common/functions'
import HCard from '../../components/HCard'


const Hawker = (props) => {
  const [hawker, setHawker] = useState(0);

  useEffect(() => {
    let num = null;
    num = findInList(props.redux.main.hawkers, "_id", getParam(props.redux.router.location.pathname, "/hawker/"));
    if (isFalse(num)) {
      // get hawker
      props.getHawkers({ id: getParam(props.redux.router.location.pathname, "/hawker/") })
    }
    setHawker(num);
  }, [props.redux.main.hawkers])

  return (
    <div>
      <div className="headers">Stalls: {`${props.redux.main.hawkers[hawker] && props.redux.main.hawkers[hawker].name}`}</div>
      {
        props.redux.main.hawkers[hawker] && props.redux.main.hawkers[hawker].stalls.map((ele, num) => {
          const openHours = openingHours(ele.operatinghours);
          var openHoursString = ""
          
          if (openHours === null) {
            openHoursString = ""
          } else if (isOpen(openHours)) {
            openHoursString = ""
          } else {
              if (openHours.open == 0) {
                openHoursString = ` - Opens at 12AM`
              } 
              else if (openHours.open < 12) {
                openHoursString = ` - Opens at ${openHours.open}AM`
              } else {
                openHoursString = ` - Opens at ${openHours.open}PM`
              }

            }

          console.log(ele)
          return (
            <HCard
              image={ele.image}
              onClick={() => props.navigate(`/stall/${ele.uen}`)}
              title={ele.name}
              line1={`⭐ ${ele.menu.reduce((prev, curr, index) => {
                return index == 0 ? curr.name : prev + ', ' + curr.name;
                }, '')}...`}
              line2={`${isOpen(openHours) ? "OPEN" : "CLOSED"}${openHoursString}`}
              l2Class={isOpen(openHours) ? "text-success" : "text-danger"}
            />
          )
        })
      }
    </div>
  )
}

const mapStateToProps = redux => ({ redux })
const mapDispatchToProps = dispatch => {
  return bindActionCreators({ navigate, getHawkers }, dispatch)
}


export default connect(mapStateToProps, mapDispatchToProps)(Hawker);
