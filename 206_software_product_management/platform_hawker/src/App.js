import React from 'react';
import { 
  Switch,
  Route,
  BrowserRouter as Router
} from "react-router-dom";

import NavBar from './components/NavBar'
import Login from './pages/Login'
import Menu from './pages/Menu'
import StallDetails from './pages/StallDetails'
import Orders from './pages/Orders'

import './App.css';

const App = () => {
  return (
    // <Provider store={store}>
      <Router>
        <div className="App">
          <NavBar />
          <Switch>
            <Route path="/login"><Login /></Route>
            <Route path="/menu"><Menu /></Route>
            <Route path="/orders"><Orders /></Route>
          </Switch>
        </div>
      </Router>
    // </Provider>
  );
}

export default App;
