import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import Modal from 'react-modal';
import { navigate, retrieveOrders, getStalls, getMenu } from '../../redux/reducers/main'
import { findInList } from '../../common/functions'
import HCard from '../../components/HCard'

const customStyles = {
  content : {
    top                   : '50%',
    left                  : '50%',
    right                 : 'auto',
    bottom                : 'auto',
    marginRight           : '-50%',
    transform             : 'translate(-50%, -50%)',
    maxHeight             : '90%',
    maxWidth              : '90%'
  }
};

const Hawkers = (props) => {
  const { orders, menus, stalls } = props.redux.main
  useEffect(() => {
    if (orders.length == 0) props.retrieveOrders()
  }, [orders])

  const [modalDescription, setModalDescription] = useState({ stall: null, orders: [], total: 0 })
  const [modalIsOpen,setIsOpen] = useState(false);
  
  const openModal = () => { setIsOpen(true); }
  const afterOpenModal = () => {}
  const closeModal = () => { setIsOpen(false); }
  

  return (
    <div>
      <div className="headers">Orders</div>
      {/* // have current order, show curr order */}
      <Modal
        isOpen={modalIsOpen}
        onAfterOpen={afterOpenModal}
        onRequestClose={closeModal}
        style={customStyles}
        contentLabel=""
      >

        <h2 style={modalDescription.style}>{modalDescription.stall && modalDescription.stall.name}</h2>
        <div style={{
          maxHeight: "50%",
          overflow: "scroll"
        }}>
          {
            modalDescription.orders.map((ele) => {
              if (ele == null || ele == undefined || ele.length == 0) return <></>
              if (!menus[findInList(menus, "_id", ele.menuId)]) {
                props.getMenu({ uen: ele.uen, id: ele.menuId })
              }
              return (
                <HCard
                  title={`${menus[findInList(menus, "_id", ele.menuId)] && menus[findInList(menus, "_id", ele.menuId)].name}`}
                  line2={`Price: S$${menus[findInList(menus, "_id", ele.menuId)] && menus[findInList(menus, "_id", ele.menuId)].price} * ${ele.quantity} item(s) = S$${
                    menus[findInList(menus, "_id", ele.menuId)] && (menus[findInList(menus, "_id", ele.menuId)].price * ele.quantity)
                  }`}
                  image={menus[findInList(menus, "_id", ele.menuId)] && menus[findInList(menus, "_id", ele.menuId)].image}
                />
              )
            })
          }
        </div>
        <button onClick={closeModal}>close</button>
      </Modal>
      {
        orders.map((ele) => { // show all orders
          if (ele == null || ele.length == 0) return <></>
          return (
            <div>
              <HCard
                title={`${stalls[findInList(stalls, "uen", ele[0].uen)] && stalls[findInList(stalls, "uen", ele[0].uen)].name}`}
                line1={`${ele[0].status}`}
                line2={`${ele[0].uen}`}
                l1Class={ele[0].status == "Order made" ? "text-success" : "text-danger"}
                image={stalls[findInList(stalls, "uen", ele[0].uen)] && stalls[findInList(stalls, "uen", ele[0].uen)].image}
                onClick={() => {
                  setModalDescription({
                    stall: stalls[findInList(stalls, "uen", ele[0].uen)],
                    orders: ele
                  })
                  openModal()
                }}
              />
            </div>
            
          )
        })
      }
    </div>
  )
}

const mapStateToProps = redux => ({ redux })
const mapDispatchToProps = dispatch => {
  return bindActionCreators({ navigate, retrieveOrders, getStalls, getMenu }, dispatch)
}


export default connect(mapStateToProps, mapDispatchToProps)(Hawkers);
