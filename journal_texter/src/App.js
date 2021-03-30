import './css/App.css';
import React, { Component } from "react";
import {
  BrowserRouter as Router,
  Route,
  Switch,
  Link,
  Redirect
} from "react-router-dom";

//Pages
import LandingPage from "./landing_login/LandingPage"
import LoginPage from "./landing_login/LoginPage"
import SignUpPage from "./landing_login/SignUpPage";
import Dashboard from "./dashboard/Dashboard";
import JournallerPage from "./journaller/JournallerPage"
import NotFoundPage from "./general_components/NotFoundPage";
import OldJournalEntry from "./journaller/OldJournalEntry";

function App() {
  return (
    <div className="App">
      <Router>
        {/*All our Routes goes here!*/}
        <Switch>
          <Route exact path="/" component={LandingPage} />
          <Route exact path="/login" component={LoginPage} />
          <Route exact path="/signup" component={SignUpPage} />
          <Route exact path="/dashboard" component={Dashboard} />
          <Route exact path="/journaller" component={JournallerPage} />
          <Route exact path="/journalentry" component={OldJournalEntry}/>
          <Route exact path="/404" component={NotFoundPage} />
          {/*This next line lets us default to the 404 page otherwise*/}
          <Redirect to="/404" />
        </Switch>
      </Router>
    </div>
  );
}

export default App;
