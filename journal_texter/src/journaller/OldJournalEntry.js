import '../css/JournallerPage.css';
import NavBar from "../dashboard/NavBar";
import React, {useEffect, useState} from "react";
import '../css/OldJournalEntry.css';
import {useHistory} from "react-router-dom";
import axios from "axios";

export default function OldJournalEntry(props) {
    const [texts, setTexts] = useState([]);
    const user = JSON.parse(localStorage.getItem('token'))['token'];
    const entryID = props.location.state.entryID;
    const history = useHistory()

    let [title, setTitle] = useState("");

    function addUserText(userResponse) {
        console.log(userResponse);
        setTexts(texts =>
            [...texts, <div className="journal-entry-text-container align-right">
                <div className="journal-entry-text">{userResponse}</div>
            </div>]);
    }

    function addPromptText(prompt) {
        console.log(prompt);
        setTexts(texts =>
            [...texts, <div className="journal-entry-text-container align-left">
                <div className="journal-entry-text">{prompt}</div>
            </div>]);
    }

    /**
     * Loads the journal entry upon load
     */
    const loadEntry = async () => {
        const toSend = {
            entryID: entryID,
            userID: user
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/handleUserHistoryRequest",
            toSend,
            config
        ).then(response => {
            setTitle(response.data["entryTitle"]);

            let questionsList = response.data["questions"]
            let responsesList = response.data["responses"]
            let i;
            for (i = 0; i < questionsList.length; i++) {
                let question = questionsList[i]
                if (question !== "") {
                    addPromptText(question)
                }

                if (i < responsesList.length) {
                    let j;
                    for (j = 0; j < responsesList[i].length; j++) {
                        addUserText(responsesList[i][j])
                    }
                }
            }
        })
    }

    useEffect(() => {
        loadEntry()
    }, [])


    /**
     * Function that allows the user to delete the entry
     */
    const deleteEntry = () => {
        const toSend = {
            entryID: entryID
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/handleDeletionRequest",
            toSend,
            config
        ).then(() => {
            history.push('/');
        })
    }

    /**
     * Function that confirms whether the user wants to delete this entry
     */
    const confirmDeleteEntry = () => {
        let delEntry = window.confirm("Would you like to delete this entry?");
        if (delEntry) {
            deleteEntry();
        }
    }

    return (
        <div className="old-journal-entry">
            <NavBar/>
            <div className="old-journal-entry-title">
                <div className="old-journal-entry-title-text">
                    {title}
                </div>
            </div>
            <div className="old-journal-entry-body">
                {texts}
            </div>
            <div className="journal-entry-button-container">
                <button className="delete-journal-entry-button old-journal-entry-button" onClick={confirmDeleteEntry}>Delete
                    Journal Entry
                </button>
                <button className="return-to-home-button old-journal-entry-button"
                        onClick={() => history.push('/')}>Return to Dashboard
                </button>
            </div>
        </div>
    );
}