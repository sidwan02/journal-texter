import React from 'react';
import '../css/Dashboard.css'
import {useHistory} from "react-router-dom";

/**
 * Component represents old journal entry link.
 *
 * @param props Takes in the text to display in the box and the link to go to.
 * @returns {JSX.Element} The old journal entry tile.
 * @constructor
 */
function OldJournalEntryBox(props) {
    const history = useHistory();

    function handleClick() {
        history.push({
            pathname: '/journalentry',
            state: {entryID: props.entryID}
        })
    }

    return (
        <div className="dashboardElement" onClick={handleClick}>
            <p className="dashboardElementText dashboard-element-text-title">{props.entryTitle}</p>
            {/*<p className="dashboardElementText dashboard-element-text-body">- - -</p>*/}
            <p className="dashboardElementText dashboard-element-text-body">{props.date}</p>
        </div>
    );
}

export default OldJournalEntryBox;