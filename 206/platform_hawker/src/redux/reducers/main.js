import axios from 'axios'
import { push } from 'connected-react-router'
import initState from './initState'

export default function(state = initState, action) {
  switch (action.type) {
    case 'LOGIN':
      return {
        ...state,
        user: {
          ...state.user,
          ...action.data
        }
      };
    case 'REMOVE_CART':
      return {
        ...state,
        cart: state.cart.filter((ele) => ele.menuid != action.data.menuid)
      }
    case 'EDIT_CART':
      return {
        ...state,
        cart: [
          ...state.cart.filter((ele) => ele.menuid != action.data.menuid),
          action.data
        ]
      }
    case 'ADD_HAWKER':
      return {
        ...state,
        hawkers: [
          ...state.hawkers.filter((ele) => ele._id != action.data._id),
          action.data
        ]
      }
    case 'ADD_STALL':
      return {
        ...state,
        stalls: [
          ...state.stalls.filter((ele) => ele.uen != action.data.uen),
          action.data
        ]
      }
    case 'ADD_MENU':
      return {
        ...state,
        menus: [
          ...state.menus.filter((ele) => ele._id != action.data._id),
          action.data
        ]
      }
    case 'ADD_ORDERS':
      return {
        ...state,
        orders: action.data
      }
    case 'DELETE_CART':
      return {
        ...state,
        cart: null
      }
    default:
      return state
  }
}

const update = (dispatch, type, data, url=false) => {
  dispatch({ type, data });
  if (url) { dispatch(push(url)); }
}

export const navigate = (url) => {
  return (dispatch) => {
    dispatch(push(url));
  }
}

export const login = (form) => {
  return (dispatch, getState) => {
    if (getState().main.backend) {
      axios({
        method: 'post',
        url: 'https://jiak-api.vitaverify.me/api/v1/customer/auth/login',
        data: form,
        withCredentials: true
      })
        .then(function (res) {
          console.log(res);
          update(dispatch, "LOGIN", form, "/")
        })
        .catch(function (error) {
          console.log(error);
        })
    } else { update(dispatch, "LOGIN", form, "/") }
  }
}

export const register = (form) => {
  return (dispatch, getState) => {
    if (getState().main.backend) {
      axios({
        method: 'post',
        url: 'https://jiak-api.vitaverify.me/api/v1/customer/auth/register',
        data: form,
        withCredentials: true
      })
        .then(function (res) {
          console.log(res);
          update(dispatch, "LOGIN", form, "/")
        })
        .catch(function (error) {
          console.log(error);
        })
    } else { update(dispatch, "LOGIN", form, "/") }
  }
}

export const updateCart = (form) => {
  return (dispatch) => {
    dispatch({ type: form.type, data: form.data });
  }
}

export const getAllCenters = () => {
  return (dispatch, getState) => {
    if (getState().main.backend) {
      axios({
        method: 'get',
        url: `https://jiak-api.vitaverify.me/api/v1/customer/hawkercenter/retrieve`,
        withCredentials: true
      })
        .then(function (res) {
          console.log(res)
          for (let k = 0; k < res.data.length; k++) {
            dispatch({ type: 'ADD_HAWKER', data: res.data[k] });
            const map_id = res.data[k]._id
            for (let i = 0; i < res.data[k].stalls.length; i++) {
              const uen = res.data[k].stalls[i].uen
              dispatch({ type: 'ADD_STALL', data: { ...res.data[k].stalls[i], map_id } });
              for (let j = 0; j < res.data[k].stalls[i].menu.length; j++) {
                dispatch({ type: 'ADD_MENU', data: { ...res.data[k].stalls[i].menu[j], uen } });
              }
            }
          }
        })
        .catch(function (error) {
          console.log(error);
        })
    } else {
      // do nothing
    }
  }
}

export const getHawkers = (form) => {
  return (dispatch, getState) => {
    if (getState().main.backend) {
      axios({
        method: 'get',
        url: `https://jiak-api.vitaverify.me/api/v1/customer/hawkercenter/get?id=${form.id}`,
        withCredentials: true
      })
        .then(function (res) {
          console.log(res)
          const map_id = res.data._id
          dispatch({ type: 'ADD_HAWKER', data: res.data });
          for (let i = 0; i < res.data.stalls.length; i++) {
            const uen = res.data.stalls[i].uen
            dispatch({ type: 'ADD_STALL', data: { ...res.data.stalls[i], map_id } });
            for (let j = 0; j < res.data.stalls[i].menu.length; j++) {
              dispatch({ type: 'ADD_MENU', data: { ...res.data.stalls[i].menu[j], uen } });
            }
          }
        })
        .catch(function (error) {
          console.log(error);
        })
    } else {
      // do nothing
    }
  }
}

export const getStalls = (form) => {
  return (dispatch, getState) => {
    if (getState().main.backend) {
      axios({
        method: 'get',
        url: `https://jiak-api.vitaverify.me/api/v1/customer/stall?uen=${form.uen}`,
        withCredentials: true
      })
        .then(function (res) {
          console.log(res);
          const uen = res.data.uen
          dispatch({ type: 'ADD_STALL', data: res.data });
          for (let j = 0; j < res.data.menu.length; j++) {
            dispatch({ type: 'ADD_MENU', data: { ...res.data.menu[j], uen } });
          }
        })
        .catch(function (error) {
          console.log(error);
        })
    } else {
      // do nothing
    }
  }
}

export const getMenu = (form) => {
  return (dispatch, getState) => {
    if (getState().main.backend) {
      axios({
        method: 'get',
        url: `https://jiak-api.vitaverify.me/api/v1/customer/dish?uen=${form.uen}&menuId=${form.id}`,
        withCredentials: true
      })
        .then(function (res) {
          console.log(res);
          dispatch({ type: 'ADD_MENU', data: res.data });
        })
        .catch(function (error) {
          console.log(error);
        })
    } else {
      // do nothing
    }
  }
}

export const retrieveOrders = () => {
  return (dispatch, getState) => {
    if (getState().main.backend) {
      axios({
        method: 'get',
        url: `https://jiak-api.vitaverify.me/api/v1/customer/order`,
        withCredentials: true
      })
      .then(function (res) {
        console.log(res);
        dispatch({ type: 'ADD_ORDERS', data: res.data })
      })
      .catch(function (error) {
        console.log(error);
      })
    }
  }
}

export const makeOrder = () => {
  return (dispatch, getState) => {
    if (getState().main.backend) {
      let cart = getState().main.cart
      cart = cart.sort((a, b) => b.uen - a.uen)
      let start = 0
      for (let i = 0; i < cart.length - 1; i++) {
        if (cart[i].uen != cart[i + 1].uen) {
          axios({
            method: 'post',
            url: `https://jiak-api.vitaverify.me/api/v1/customer/order`,
            data: {
              uen: cart[start].uen,
              orderList: cart.slice(start, i + 1)
            },
            withCredentials: true
          })
          .then(function (res) {
            console.log(res);
          })
          .catch(function (error) {
            console.log(error);
            return
          })
          console.log(cart.slice(start, i + 1))
          start = i + 1
        }
      }
      if (start != cart.length) {
        axios({
          method: 'post',
          url: `https://jiak-api.vitaverify.me/api/v1/customer/order`,
          data: {
            uen: cart[start].uen,
            orderList: cart.slice(start, cart.length + 1)
          },
          withCredentials: true
        })
        .then(function (res) {
          console.log(res);
        })
        .catch(function (error) {
          console.log(error);
          return
        })
      }
      dispatch({ type: 'DELETE_CART' })
      const { localStorage } = window;
      localStorage.removeItem('cs206_cart')
    }
  }
}