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
        // history.push('/journalentry');
        history.push({
            pathname: '/journalentry',
            state: {entryID: props.entryID}
        })

        /*
        history.push({
            pathname: '/test',
            state: {entryID: props.entryID}
        })
         */
    }

    return (
        <div className="dashboardElement" onClick={handleClick}>
            <p className="dashboardElementText">{props.date}</p>
            <p className="dashboardElementText">{props.entryID}</p>
            {/*<p className="dashboardElementText">{props.text}</p>*/}
        </div>
    );
}

export default OldJournalEntryBox;