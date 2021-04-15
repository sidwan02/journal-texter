import React, {useEffect, useState} from 'react';
import '../css/Dashboard.css'
import {useHistory} from "react-router-dom";
import OldJournalEntryBox from "./OldJournalEntryBox";
import NewJournalBox from "./NewJournalBox";
import axios from "axios";

/**
 * Renders the dashboard for the UI.
 *
 * @returns {JSX.Element} The dashboard to render.
 * @constructor
 */
export default function Dashboard() {
    const history = useHistory();
    let entries = [];
    const [pastEntries, setPastEntries] = useState([]);

    /**
     * Get's the user's past journal entries from the backend.
     *
     * @returns {Promise<void>} Returns the user's past journal entries.
     */
    async function getUserJournals() {
        const toSend = JSON.parse(localStorage.getItem('token'));

        console.log(toSend);

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/handleUserHistorySummary",
            // "http://localhost:4567/dashboard",
            toSend,
            config
        )
            .then(response => {
                entries = response.data["entries"];

                // console.log(entries);
                // setPastEntries(entries.map(entry =>
                //     <OldJournalEntryBox date={entry}/>
                // ));
            })
            .catch(error => {
                console.log(error.response);
                return null;
            });
    }

    /**
     * Loads in user data when it is retrieved from the backend.
     */
    useEffect(() => {
        getUserJournals().then(() => undefined);
    }, [])

    /**
     * Deletes the users token, sends them to the login page, and reloads the page.
     */
    function handleSignOut() {
        localStorage.removeItem('token');
        sessionStorage.removeItem('token');

        history.push('/login');

        window.location.reload(false);
    }

    return (
        <div className="dashboard">
            <nav className="dashboard-nav">
                <h1 id="logo">JournalTexter</h1>
                <div id="signout" onClick={handleSignOut}>
                    Sign Out
                </div>
            </nav>
            <div className="entries">
                <NewJournalBox title="Create New Entry" link="journaller"/>
                {pastEntries}
            </div>
        </div>
    );
}