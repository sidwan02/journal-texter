import React, {useEffect, useState} from 'react';
import '../css/Dashboard.css'
import {useHistory} from "react-router-dom";
import OldJournalEntryBox from "./OldJournalEntryBox";
import NewJournalBox from "./NewJournalBox";
import axios from "axios";

export default function Dashboard() {
    const history = useHistory();
    let entries = [];
    const [pastEntries, setPastEntries] = useState([]);

    async function getUserJournals() {
        const toSend = JSON.parse(localStorage.getItem('token'));

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/dashboard",
            toSend,
            config
        )
            .then(response => {
                entries = response.data["entries"];

                console.log(entries);
                setPastEntries(entries.map(entry =>
                    <OldJournalEntryBox date={entry}/>
                ));
            })
            .catch(error => {
                console.log(error.response);
                return null;
            });
    }

    useEffect(() => {
        getUserJournals().then(() => undefined);
    }, [])

    function handleReturnToHome() {
        history.push('/');
    }

    function handleSignOut() {
        localStorage.removeItem('token');
        sessionStorage.removeItem('token');

        history.push('/login');

        window.location.reload(false);
    }

    return (
        <div className="dashboard">
            <nav>
                <div>
                    <h1 id="logo" onClick={handleReturnToHome}>JournalTexter</h1>
                </div>
                <div id="signout" onClick={handleSignOut}>
                    Sign Out
                </div>
            </nav>
            <div className="entries">
                <NewJournalBox title="Create New Entry" link="journaller"/>
                { pastEntries }
            </div>
        </div>
    );
}