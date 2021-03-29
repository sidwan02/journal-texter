import React, {useState, useEffect} from 'react';
import './css/Dashboard.css'
import {Link, useHistory} from "react-router-dom";
import JournalEntry from "./JournalEntry";
import NewJournal from "./NewJournal";

function Dashboard() {
    const history = useHistory();

    function handleClick() {
        history.push('/');
    }

    function handleSignOut() {
        history.push("/login");
    }

    return (
        <div className="dashboard">
            <nav>
                <div>
                    <h1 id="logo" onClick={handleClick}>JournalTexter</h1>
                </div>
                <div id="signout" onClick={handleSignOut}>
                    Sign Out
                </div>
            </nav>
            <div className="entries">
                <NewJournal title="Create New Entry" link="journaller"/>
                <JournalEntry date="[ Date ]" name="[ Entry Name ]" text="[ Custom Note ]" link="journaller"/>
                <JournalEntry date="[ Date ]" name="[ Entry Name ]" text="[ Custom Note ]" link="journaller"/>
                <JournalEntry date="[ Date ]" name="[ Entry Name ]" text="[ Custom Note ]" link="journaller"/>
                <JournalEntry date="[ Date ]" name="[ Entry Name ]" text="[ Custom Note ]" link="journaller"/>
                <JournalEntry date="[ Date ]" name="[ Entry Name ]" text="[ Custom Note ]" link="journaller"/>
            </div>
        </div>
    );
}

export default Dashboard;