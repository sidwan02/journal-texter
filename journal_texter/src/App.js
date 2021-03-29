import './App.css';
import React, { Component } from "react";
//Import all needed Component for this tutorial
import {
  BrowserRouter as Router,
  Route,
  Switch,
  Link,
  Redirect
} from "react-router-dom";

//Pages
import MainPage from "./MainPage"
import LoginPage from "./LoginPage"
import SignInPage from "./SignInPage";
import JournallerPage from "./JournallerPage"
import NotFoundPage from "./NotFoundPage";

function App() {
  return (
    <div className="App">
      <Router>
        {/*All our Routes goes here!*/}
        <Switch>
          <Route exact path="/" component={MainPage} />
          <Route exact path="/login" component={LoginPage} />
          <Route exact path="/signin" component={SignInPage} />
          <Route exact path="/journaller" component={JournallerPage} />
          <Route exact path="/404" component={NotFoundPage} />
          {/*This next line lets us default to the 404 page otherwise*/}
          <Redirect to="/404" />
        </Switch>
      </Router>
    </div>
  );
}

export default App;
