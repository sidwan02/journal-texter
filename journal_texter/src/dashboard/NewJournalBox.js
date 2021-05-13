import React from 'react';
import '../css/Dashboard.css'
import {useHistory} from "react-router-dom";
import axios from "axios";

/**
 * Component represents new journal entry link.
 *
 * @param props Takes in the text to display in the box and the link to go to.
 * @returns {JSX.Element} The new journal entry tile.
 * @constructor
 */
function NewJournalBox(props) {
    const user = JSON.parse(localStorage.getItem('token'))['token'];
    const history = useHistory();

    /**
     * Handles the elements being clicked on and redirects to the specified link.
     */
    function handleClick() {
        // history.push('/' + props.link);
        generateEntry()
    }

    /**
     * Generates the entryID
     */
    const generateEntry = () => {
        const toSend = {
            userID: user
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        const API_URL = "https://journaltexter-api.herokuapp.com/"
        return axios.post(
            API_URL + "handleCreateEntry",
            // "http://localhost:4567/handleCreateEntry",
            toSend,
            config
        ).then(response => {
            let generatedEntryID = response.data["entryId"];
            history.push({
                pathname: '/' + props.link,
                state: {entryID: generatedEntryID}
            })
            }
        )
    }

    return (
        <div id="newEntry" className="dashboardElement" onClick={handleClick}>
            <p className="newEntryTitle">{props.title}</p>
        </div>
    );
}

export default NewJournalBox;