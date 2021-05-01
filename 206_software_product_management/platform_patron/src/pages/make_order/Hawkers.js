import React, { useEffect } from 'react';
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { navigate, getAllCenters } from '../../redux/reducers/main'
import HCard from '../../components/HCard'

const Hawkers = (props) => {

  useEffect(() => {
    if (props.redux.main.hawkers.length < 1) {
      props.getAllCenters();
    }
  }, [])

  return (
    <div>
      <div className="headers" > Hawker Centres </div>
      {
        props.redux.main.hawkers.map((ele) => {
          return (
            <HCard
              image={ele.image}
              onClick={() => props.navigate(`/hawker/${ele._id}`)}
              title={ele.name}
              line1={`⭐ ${ele.stalls.reduce((prev, curr, index) => {
                return index == 0 ? curr.name : prev + ', ' + curr.name;
                }, '')}...`}
            />
          )
        })
      }
    </div>
  )
}

const mapStateToProps = redux => ({ redux })
const mapDispatchToProps = dispatch => {
  return bindActionCreators({ navigate, getAllCenters }, dispatch)
}


export default connect(mapStateToProps, mapDispatchToProps)(Hawkers);
