import React, {useState} from 'react';
import '../css/Dashboard.css'
import {useHistory} from "react-router-dom";

function NewJournalBox(props) {
    const history = useHistory();

    function handleClick() {
        history.push('/' + props.link);
    }

    return (
        <div id="newEntry" className="dashboardElement" onClick={handleClick}>
            <p className="newEntryTitle">{props.title}</p>
        </div>
    );
}

export default NewJournalBox;