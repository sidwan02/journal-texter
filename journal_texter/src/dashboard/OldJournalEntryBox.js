import React from 'react';
import '../css/Dashboard.css'
import {useHistory} from "react-router-dom";

function OldJournalEntryBox(props) {
    const history = useHistory();

    function handleClick() {
        history.push('/journalentry');
    }

    return (
        <div className="dashboardElement" onClick={handleClick}>
            <p className="dashboardElementText">{props.date}</p>
            {/*<p className="dashboardElementText">{props.name}</p>*/}
            {/*<p className="dashboardElementText">{props.text}</p>*/}
        </div>
    );
}

export default OldJournalEntryBox;