import React from "react";
import {useHistory} from "react-router-dom";

function OldJournalEntry() {
    const history = useHistory();

    function handleClick() {
        history.push('/');
    }

    function handleSignOut() {
        localStorage.removeItem('token');
        sessionStorage.removeItem('token');

        history.push('/login');

        window.location.reload(false);
    }

    const historyStyle = {
        height: 400,
        width: 1000,
        margin: 'auto',
        border: '2px solid black',
        overflow: 'auto',
        padding: 10,
    }

    return (
        <div>
            <nav className="dashboard-nav">
                <h1 id="logo" onClick={handleClick}>JournalTexter</h1>
                <div id="signout" onClick={handleSignOut}>
                    Sign Out
                </div>
            </nav>

            <hr style={{backgroundColor: 'black', height: '1px'}}/>

            <br/>
            <div id="journalHistory" className="journalHistory" style={historyStyle}/>
        </div>
    );
}

export default OldJournalEntry;