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
    const [pastEntries, setPastEntries] = useState([]);

    /**
     * Get's the user's past journal entries from the backend.
     *
     * @returns {Promise<void>} Returns the user's past journal entries.
     */
    async function getUserJournals() {
        const toSend = JSON.parse(localStorage.getItem('token'));

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/handleUserHistorySummary",
            toSend,
            config
        )
            .then(response => {
                let entries = response.data["entries"]["values"];

                console.log(entries);

                for (let i = entries.length - 1; i >= 0; i--) {
                    let year = response.data["entries"]["values"][i]["nameValuePairs"]["date"]["year"];
                    let month = response.data["entries"]["values"][i]["nameValuePairs"]["date"]["month"];
                    let day = response.data["entries"]["values"][i]["nameValuePairs"]["date"]["day"];

                    let entryId = response.data["entries"]["values"][i]["nameValuePairs"]["entryId"]
                    let date = month + "/" + day + "/" + year;

                    pastEntries.push(<OldJournalEntryBox date={date} entryID={entryId}/>);
                    setPastEntries(pastEntries.concat(<div></div>));
                }

                let tempEntries = entries.data["date"];
                console.log(tempEntries);

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
            <NavBar/>
            <div className="dashboard-entries">
                <NewJournalBox title="Create New Entry" link="journaller"/>
                {pastEntries}
            </div>
        </div>
    );
}