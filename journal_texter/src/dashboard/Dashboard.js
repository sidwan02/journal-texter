import React, {useEffect, useState} from 'react';
import '../css/Dashboard.css'
import OldJournalEntryBox from "./OldJournalEntryBox";
import NewJournalBox from "./NewJournalBox";
import axios from "axios";
import NavBar from "./NavBar";

/**
 * Renders the dashboard for the UI.
 *
 * @returns {JSX.Element} The dashboard to render.
 * @constructor
 */
export default function Dashboard() {
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

                console.log(entries);

                setPastEntries(entries.map(entry =>
                    <OldJournalEntryBox date={entry["date"]} entryID={entry["entryId"]}/>
                ));

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

    return (
        <div className="dashboard">
            <NavBar />
            <div className="dashboard-entries">
                <NewJournalBox title="Create New Entry" link="journaller"/>
                {pastEntries}
            </div>
        </div>
    );
}