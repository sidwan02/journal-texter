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
            let questionsList = response.data["questions"]
            let responsesList = response.data["responses"]
            let i;
            for (i = 0; i < questionsList.length; i++) {
                let question = questionsList[i].substring(1);
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
            </div>
            <div className="journal-entry-button-container">
                <button className="delete-journal-entry-button old-journal-entry-button" onClick={deleteEntry}>Delete Journal Entry</button>
                <button className="return-to-home-button old-journal-entry-button" onClick={() => history.push('/')}>Return to Dashboard</button>
            </div>
        </div>
    );
}