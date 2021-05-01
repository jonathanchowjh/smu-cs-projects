import React from 'react';
import {
  Switch,
  Route
} from "react-router-dom";

import { Provider } from 'react-redux'
import { applyMiddleware, compose, createStore } from 'redux'
import thunk from 'redux-thunk'
import createRootReducer from './redux/reducers'
import { routerMiddleware, ConnectedRouter as Router } from 'connected-react-router'
import { createBrowserHistory } from 'history'

import NavBar from './components/NavBar'
import Login from './pages/Login'
import Registration from './pages/Registration'
import Home from './pages/Home'
import Hawkers from './pages/make_order/Hawkers'
import Hawker from './pages/make_order/Hawker'
import Stall from './pages/make_order/Stall'
import Menu from './pages/make_order/Menu'
import Cart from './pages/make_order/Cart'
import Map from './pages/Map'
import Orders from './pages/make_order/Orders'
import Error from './pages/404'

import './App.css';

const history = createBrowserHistory()
const store = createStore(
  createRootReducer(history), // root reducer with router state
  {},
  compose(
    applyMiddleware(
      routerMiddleware(history), // for dispatching history actions
      thunk
    ),
  ),
)

function App() {
  return (
    <Provider store={store}>
      <Router history={history}>
        <div className="App">
          <NavBar />
          <Switch>
            <Route path="/registration"><Registration /></Route>
            <Route path="/login"><Login /></Route>
            <Route path="/hawkers"><Hawkers /></Route>
            <Route path="/hawker/:id"><Hawker /></Route>
            <Route path="/stall/:id"><Stall /></Route>
            <Route path="/menu"><Menu /></Route>
            <Route path="/cart"><Cart /></Route>
            <Route path="/map/:id"><Map /></Route>
            <Route path="/orders"><Orders /></Route>
            <Route exact path="/404"><Error /></Route>
            <Route exact path="/"><Home /></Route>
          </Switch>
        </div>
      </Router>
    </Provider>
  );
}

export default App;
