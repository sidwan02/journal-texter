import "./index.css";
import React from "react";
import {
    BrowserRouter as Router,
    Route,
    Switch,
    Redirect,
} from "react-router-dom";

//Pages
import LandingPage from "./landing_login/LandingPage"
import LoginPage from "./landing_login/LoginPage"
import SignUpPage from "./landing_login/SignUpPage";
import Dashboard from "./dashboard/Dashboard";
import JournallerPage from "./journaller/JournallerPage"
import NotFoundPage from "./general_components/NotFoundPage";
import OldJournalEntry from "./journaller/OldJournalEntry";
import useToken from './useToken';
import JournallerTest from "./journaller/JournallerTest";

/**
 * The component that all the other components run under.
 *
 * @returns {JSX.Element} The component that contains all the other components.
 * @constructor
 */
function App() {
    const {token, setToken} = useToken();

    if (!token) {
        return (
            <div className="App">
                <Router>
                    <Switch>
                        <Route exact path="/" component={LandingPage}/>
                        <Route exact path="/login">
                            <LoginPage setToken={setToken}/>
                        </Route>
                        <Route exact path="/signup">
                            <SignUpPage setToken={setToken}/>
                        </Route>
                        <Route exact path="/dashboard" component={LoginPage}/>

                        <Route exact path="/journaller">
                            <Redirect to="/login"/>
                        </Route>
                        <Route exact path="/journalentry">
                            <Redirect to="/login"/>
                        </Route>

                        <Route exact path="/404" component={NotFoundPage}/>
                        <Redirect to="/404"/>
                    </Switch>
                </Router>
            </div>
        );
    } else {
        return (
            <div className="App">
                <Router>
                    {/*All our Routes goes here!*/}
                    <Switch>
                        <Route exact path="/" component={Dashboard}/>
                        <Route exact path="/login">
                            <Redirect to="/"/>
                        </Route>
                        <Route exact path="/signup">
                            <Redirect to="/"/>
                        </Route>
                        {/*<Route exact path="/dashboard" component={Dashboard}/>*/}
                        <Route exact path="/journaller" component={JournallerPage}/>
                        <Route exact path="/journalentry" component={OldJournalEntry}/>
                        <Route exact path="/404" component={NotFoundPage}/>
                        {/*This next line lets us default to the 404 page otherwise*/}

                        <Route exact path="/test" component={JournallerTest} />

                        <Redirect to="/404"/>
                    </Switch>
                </Router>
            </div>
        );
    }
}

export default App;
