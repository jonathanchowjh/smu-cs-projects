import React, { useState, useEffect } from 'react'
import axios from 'axios'
import OrderCard from '../components/OrderCard'
import { findInList, isFalse } from '../common/functions'
import Modal from 'react-modal';

const customStyles = {
  content: {
    top: '50%',
    left: '50%',
    right: 'auto',
    bottom: 'auto',
    marginRight: '-50%',
    transform: 'translate(-50%, -50%)',
    maxHeight: '90%',
    maxWidth: '90%'
  }
};


const Orders = (props) => {
  const [orders, setOrders] = useState([]) // array of array of orders
  const [menu, setMenu] = useState([]) // all menu items for this stall
  const [orderType, setOrderType] = useState("Order made") // "Order made", "Done", "Rejected"
  const [reload, setReload] = useState(0)

  const [modalDescription, setModalDescription] = useState({ customer: null, orders: [] }) // MODAL ITEM
  const [modalIsOpen, setIsOpen] = useState(false); // MODAL Handling

  const openModal = () => { setIsOpen(true); }
  const afterOpenModal = () => { }
  const closeModal = () => { setIsOpen(false); }

  useEffect(() => {
    axios({
      method: 'get',
      url: 'https://jiak-api.vitaverify.me/api/v1/stall/order',
      withCredentials: true
    })
      .then(function (res) {
        console.log(res)
        setOrders(res.data.bundledOrders)
      })
      .catch(function (error) {
        console.log(error)
      })
    
    axios({
      method: 'get',
      url: 'https://jiak-api.vitaverify.me/api/v1/stall/menu',
      withCredentials: true
    })
      .then(function (res) {
        console.log(res)
        setMenu(res.data)
      })
      .catch(function (error) {
        console.log(error)
      })
  }, [reload])

  const getMenuItem = (id) => {
    let menuItem = findInList(menu, "_id", id)
    if (!isFalse(menuItem)) return menu[menuItem]
    return null
  }

  const getBgColour = (status) => {
    if (status === "Done") return "bg-secondary"
    if (status === "Rejected") return "bg-danger"
    if (status === "Order made") return "bg-success"
    return ""
  }

  const getStatusName = (status) => {
    if (status === "Done") return "Done"
    if (status === "Rejected") return "Reject"
    if (status === "Order made") return "New"
    return ""
  }

  const processOrder = (orders, status) => {
    if (orders === null || orders.length === 0) {
      return
    }
    axios({
      method: 'patch',
      url: 'https://jiak-api.vitaverify.me/api/v1/stall/order',
      data: {
        "orderId": orders[0].orderId,
        "status": status
      },
      withCredentials: true
    })
      .then(function (res) {
        console.log(res)
        setReload(reload + 1)
        closeModal()
      })
      .catch(function (error) {
        console.log(error)
      })
  }

  return (
    <div style={{ textAlign: "initial", padding: "1em" }}>
      <h2 className="headers">Orders</h2>
      <div className="btn-group" role="group" aria-label="Basic mixed styles example">
        <button type="button" className="btn btn-primary" onClick={() => setOrderType("")}>All</button>
        <button type="button" className="btn btn-success" onClick={() => setOrderType("Order made")}>New</button>
        <button type="button" className="btn btn-secondary" onClick={() => setOrderType("Done")}>Completed</button>
        <button type="button" className="btn btn-danger" onClick={() => setOrderType("Rejected")}>Rejected</button>
      </div>
      <Modal
        isOpen={modalIsOpen}
        onAfterOpen={afterOpenModal}
        onRequestClose={closeModal}
        style={customStyles}
        contentLabel=""
      >
        <h2>{modalDescription.customer}</h2>
        {modalDescription.orders.map((ele) => {
          const item = getMenuItem(ele.menuId)
          if (item == null) return <></>
          return <OrderCard 
            image={item.image}
            title={item.name}
            badge={ele.quantity}
          />
        })}
        {
          modalDescription.orders[0] && modalDescription.orders[0].status === "Order made" && (
            <div className="btn-group py-1" role="group" aria-label="Basic example">
              <button type="button" className="btn btn-success btn-sm" onClick={() => {processOrder(modalDescription.orders, "Done")}}>Complete</button>
              <button type="button" className="btn btn-danger btn-sm" onClick={() => {processOrder(modalDescription.orders, "Rejected")}}>Reject</button>
            </div>
          )
        }
        <div>
          <button type="button" class="btn btn-primary btn-sm" onClick={closeModal}>Close</button>
        </div>
      </Modal>
      {
        orders && orders.filter((ele) => {
          if (orderType === "") return true
          return orderType === ele[0].status
        }).map((ele) => {
          let menuItem = getMenuItem(ele.menuId)
          if (ele.length === 0) return <></>
          return (
            <OrderCard
              title={ele[0].customerId}
              image="http://cdn.onlinewebfonts.com/svg/img_415179.png"
              badge={getStatusName(ele[0].status)}
              badgeClass={`${getBgColour(ele[0].status)}`}
              classname="mx-0"
              imageClass="d-none d-sm-block"
              b1={() => {
                openModal()
                setModalDescription({ customer: ele[0].customerId, 
                                      orders: ele})
              }}
              b1Name="View"
              
            />
          )
        })
      }
    </div>
  )
}

export default Orders
