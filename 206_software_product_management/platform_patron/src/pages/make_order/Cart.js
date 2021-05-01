import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { navigate, updateCart, getMenu, makeOrder } from '../../redux/reducers/main'
import { findInList, isFalse } from '../../common/functions'
import ButtonCard from '../../components/ButtonCard'
import Modal from 'react-modal';
 
const customStyles = {
  content : {
    top                   : '50%',
    left                  : '50%',
    right                 : 'auto',
    bottom                : 'auto',
    marginRight           : '-50%',
    transform             : 'translate(-50%, -50%)'
  }
};

const Cart = (props) => {
  const { cart, menus } = props.redux.main;
  const [modalDescription, setModalDescription] = useState({ text: "", style: {}, image: "" })
  const [modalIsOpen,setIsOpen] = useState(false);
  
  const openModal = () => { setIsOpen(true); }
  const afterOpenModal = () => {}
  const closeModal = () => { setIsOpen(false); }
  const setModal = (type) => {
    if (type == 'success') {
      setModalDescription({
        text: "Order Sent",
        style: { color: 'green' },
        image: "https://upload.wikimedia.org/wikipedia/commons/thumb/7/73/Flat_tick_icon.svg/1200px-Flat_tick_icon.svg.png"
      })
    } else if (type == 'loggedout') {
      setModalDescription({
        text: "Not Logged In",
        style: { color: 'red' },
        image: "https://www.google.com/url?sa=i&url=https%3A%2F%2Ficons-for-free.com%2Fcancel%2Bdanger%2Berror%2Bexit%2Bfault%2Bproblem%2Bicon-1320086092655159088%2F&psig=AOvVaw0e3JCatOebqQrCYup5VbX-&ust=1617686951871000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNDYmdWv5u8CFQAAAAAdAAAAABAD"
      })
    }
  }

  useEffect(() => {
    const { localStorage } = window;
    let storageCart = localStorage.getItem('cs206_cart')
    storageCart = storageCart == null ? [] : JSON.parse(storageCart)
    if (storageCart != null) {
      for (let i = 0; i < storageCart.length; i++) {
        if (isFalse(findInList(cart, "menuid", storageCart[i].menuid))) {
          props.updateCart({
            type: "EDIT_CART",
            data: storageCart[i]
          })
        }
      }
    }
  }, [cart, menus])

  const getMenuItem = (uen, id) => {
    let menuItem = findInList(menus, "_id", id)
    if (!isFalse(menuItem)) return menus[menuItem]
    props.getMenu({ uen, id })
  }

  return (
    <div>
      <Modal
        isOpen={modalIsOpen}
        onAfterOpen={afterOpenModal}
        onRequestClose={closeModal}
        style={customStyles}
        contentLabel=""
      >

        <h2 style={modalDescription.style}>{modalDescription.text}</h2>
        <button onClick={closeModal}>close</button>
        <image src={modalDescription.image} />
      </Modal>

      {
        cart && cart.map((ele) => {
          let menuItem = getMenuItem(ele.uen, ele.menuid)
          if (menuItem == null) return <></>
          return (
            <ButtonCard
              image={menuItem.image}
              title={menuItem.name}
              badge={ele.quantity}yap
              line1={`$${Math.round(ele.quantity*menuItem.price * 100)/ 100}`}
              b1={() => props.navigate(`/menu?_id=${ele.menuid}&uen=${ele.uen}`)}
              b1Name="Edit"
              b2={() => {
                const { localStorage } = window;
                let storageCart = JSON.parse(localStorage.getItem('cs206_cart'))
                localStorage.setItem('cs206_cart',
                  JSON.stringify([ ...storageCart.filter((listItem) => listItem.menuid != ele.menuid) ])
                )
                props.updateCart({
                  type: "REMOVE_CART",
                  data: ele
                })
              }}
              b2Name="Remove"
            />
          )
        })
      }

    <div>Total: ${cart.reduce((a, v) => a = Math.round((a + v.quantity * getMenuItem(v.uen, v.menuid).price) * 100) / 100, 0)   }</div>

      <button
        type="button"
        className="btn btn-outline-primary"
        onClick={() => {
          if (props.redux.main.user.loggedin) {
            props.makeOrder()
            setModal('success')
            openModal()
          } else {
            setModal('loggedout')
            openModal()
          }
        }}
      >
        Order Now
      </button>
    </div>
  )
}

const mapStateToProps = redux => ({ redux })
const mapDispatchToProps = dispatch => {
  return bindActionCreators({ navigate, updateCart, getMenu, makeOrder }, dispatch)
}


export default connect(mapStateToProps, mapDispatchToProps)(Cart);
