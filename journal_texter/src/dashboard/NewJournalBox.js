import React from 'react';
import '../css/Dashboard.css'
import {useHistory} from "react-router-dom";

/**
 * Component represents new journal entry link.
 *
 * @param props Takes in the text to display in the box and the link to go to.
 * @returns {JSX.Element} The new journal entry tile.
 * @constructor
 */
function NewJournalBox(props) {
    const history = useHistory();

    /**
     * Handles the elements being clicked on and redirects to the specified link.
     */
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