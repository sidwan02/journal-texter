import '../css/JournallerPage.css';
import NavBar from "../dashboard/NavBar";
import React, {useEffect, useState} from "react";
import './OldJournalEntryTest.css';
import {useHistory} from "react-router-dom";

export default function OldJournalEntryTest(props) {
    const [texts, setTexts] = useState([]);
    const user = JSON.parse(localStorage.getItem('token'))['token'];
    const entryID = props.location.state.entryID;
    const history = useHistory()

    useEffect(() => {
        // TODO Load in past journal entries
        // TODO Text is not updating correctly.  ONLY ADDS ONE MAX. WORKS WHEN USING BUTTON THOUGH.
        addUserText("TEST");
        addUserText("TEST");
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

    const deleteEntry = () => {
        //TODO DELETE ENTRY ID
        alert("Entry ID = " + entryID);
        history.push('/');
    }

    return (
        <div className="old-journal-entry">
            <NavBar/>
            <div className="old-journal-entry-body">
                {texts}
                {/*TODO FOR SOME REASON THIS WORKS WHEN ADDING USER TEXT*/}
                <button onClick={() => addUserText("Test")}>Add User Text Test</button>
            </div>
            <div className="journal-entry-button-container">
                <button className="delete-journal-entry-button old-journal-entry-button" onClick={deleteEntry}>Delete Journal Entry</button>
                <button className="return-to-home-button old-journal-entry-button" onClick={() => history.push('/')}>Return to Dashboard</button>
            </div>
        </div>
    );
}