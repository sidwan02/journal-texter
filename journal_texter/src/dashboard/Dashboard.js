import React from 'react';
import '../css/Dashboard.css'
import { useHistory} from "react-router-dom";
import OldJournalEntryBox from "./OldJournalEntryBox";
import NewJournalBox from "./NewJournalBox";

export default function Dashboard() {
    const history = useHistory();

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
                <OldJournalEntryBox date="[ Date ]" name="[ Entry Name ]" text="[ Custom Note ]" link="journaller"/>
                <OldJournalEntryBox date="[ Date ]" name="[ Entry Name ]" text="[ Custom Note ]" link="journaller"/>
                <OldJournalEntryBox date="[ Date ]" name="[ Entry Name ]" text="[ Custom Note ]" link="journaller"/>
                <OldJournalEntryBox date="[ Date ]" name="[ Entry Name ]" text="[ Custom Note ]" link="journaller"/>
                <OldJournalEntryBox date="[ Date ]" name="[ Entry Name ]" text="[ Custom Note ]" link="journaller"/>
            </div>
        </div>
    );
}