import React from "react";
import {useHistory} from "react-router-dom";
import '../css/NavBar.css';

/**
 * Creates a navbar for when the user is logged in.
 *
 * @returns {JSX.Element} The navbar to display when the user is logged in.
 * @constructor
 */
export default function NavBar() {
    const history = useHistory();

    /**
     * Deletes the users token, sends them to the login page, and reloads the page.
     */
    function handleSignOut() {
        localStorage.removeItem('token');
        sessionStorage.removeItem('token');

        history.push('/');

        window.location.reload(false);
    }

    return (
        <nav className="nav-bar">
            <h1 id="home-button" className="noselect">JournalTexter</h1>
            <div id="signout-button" onClick={handleSignOut}>
                <div>
                    Sign Out
                </div>
            </div>
        </nav>
    )
}