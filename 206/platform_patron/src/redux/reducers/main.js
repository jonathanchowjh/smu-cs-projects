import axios from 'axios'
import { push, goBack } from 'connected-react-router'
import initState from './initState'

import { findInList } from '../../common/functions'

export default function(state = initState, action) {
  switch (action.type) {
    case 'LOGIN':
      return {
        ...state,
        user: {
          ...state.user,
          ...action.data,
        }
      };
    case 'LOGGED_IN':
      return {
        ...state,
        user: {
          loggedin: action.data
        }
      }
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
        orders: [
          ...state.orders,
          ...action.data
        ]
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

export const navigate = (url) => {
  return (dispatch) => {
    dispatch(push(url));
  }
}

export const back = () => {
  return (dispatch) => {
    dispatch(goBack());
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
          dispatch({ type: "LOGIN", form });
          dispatch({ type: 'LOGGED_IN', data: true });
          dispatch(push("/"));
        })
        .catch(function (error) {
          console.log(error);
        })
    } else {
      dispatch({ type: "LOGIN", form });
      dispatch({ type: 'LOGGED_IN', data: true });
      dispatch(push("/"));
    }
  }
}

export const logout = () => {
  return (dispatch, getState) => {
    if (getState().main.backend) {
      axios({
        method: 'post',
        url: 'https://jiak-api.vitaverify.me/api/v1/customer/auth/logout',
        withCredentials: true
      })
        .then(function (res) {
          console.log(res)
          dispatch({ type: 'LOGGED_IN', data: false });
        })
        .catch(function (error) {
          console.log(error);
        })
    } else {

    }
  }
}

export const gateway = () => { // isLoggedIn
  return (dispatch, getState) => {
    if (getState().main.backend) {
      axios({
        method: 'get',
        url: 'https://jiak-api.vitaverify.me/api/v1/customer/auth/gateway',
        withCredentials: true
      })
        .then(function (res) {
          console.log(res)
          dispatch({ type: 'LOGGED_IN', data: true });
        })
        .catch(function (error) {
          console.log(error);
        })
    } else {

    }
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
          dispatch({ type: "LOGIN", form });
          dispatch({ type: 'LOGGED_IN', data: true });
          dispatch(push("/"));
        })
        .catch(function (error) {
          console.log(error);
        })
    } else {
      dispatch({ type: "LOGIN", form });
      dispatch({ type: 'LOGGED_IN', data: true });
      dispatch(push("/"));
    }
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
        // ADD STALLS for each order if not already in redux
        for (let i = 0; i < res.data.length; i++) {
          if (res.data[i] == null || res.data[i].length == 0) continue
          const stall = findInList(getState().main.stalls, "uen", res.data[i][0].uen)
          if (!stall) {
            console.log(i)
            dispatch(getStalls({ uen: res.data[i][0].uen }))
          }
        }
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
      // get sorted cart
      let cart = getState().main.cart
      cart = cart.sort((a, b) => b.uen - a.uen)
      // for each cart item, if same uen, oreder together, else finish order and make new order
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
            let temp = [cart.slice(start, i + 1)]
            for (let i=0; i<temp.length; i++) {
              if (temp[i] == null) continue
              for (let j=0; j<temp[i].length; j++) {
                temp[i][j] = { ...temp[i][j], status: "Order made" }
              }
            }
            dispatch({ type: 'ADD_ORDERS', data: temp })
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
          let temp = [cart.slice(start, cart.length + 1)]
          for (let i=0; i<temp.length; i++) {
            if (temp[i] == null) continue
            for (let j=0; j<temp[i].length; j++) {
              temp[i][j] = { ...temp[i][j], status: "Order made" }
            }
          }
          dispatch({ type: 'ADD_ORDERS', data: temp })
        })
        .catch(function (error) {
          console.log(error);
          return
        })
      }

      dispatch(retrieveOrders())
      dispatch({ type: 'DELETE_CART' })
      const { localStorage } = window;
      localStorage.removeItem('cs206_cart')
    }
  }
}