import '../css/JournallerTest.css';
import NavBar from "../dashboard/NavBar";
import React, {useEffect, useState} from "react";
import './OldJournalEntryTest.css';
import {useHistory} from "react-router-dom";

export default function (props) {
    const [texts, setTexts] = useState([]);
    const user = JSON.parse(localStorage.getItem('token'))['token'];
    const entryID = props.location.state.entryID;
    const history = useHistory()

    useEffect(() => {
        // TODO Load in past journal entries
    }, [])

    /**
     * Updates the journal with user input. Sends a POST request to the backend to generate new questions
     */
    const addUserText = (userResponse) => {
        setTexts(texts.concat(
            <div className="journal-entry-text-container align-right">
                <div className="journal-entry-text">{userResponse}</div>
            </div>));
    }

    const addPromptText = (prompt) => {
        setTexts(texts.concat(
            <div className="journal-entry-text-container align-left">
                <div className="journal-entry-text">{prompt}</div>
            </div>));
    }

    return (
        <div className="old-journal-entry">
            <NavBar/>
            {/*<p>{entryID}</p>*/}
            <div className="old-journal-entry-body">
                {texts}
            </div>
        </div>
    );
}