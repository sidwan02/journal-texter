import React, {useEffect} from "react";
import {useHistory} from "react-router-dom";
import axios from "axios";
import NavBar from "../dashboard/NavBar";

function OldJournalEntry(props) {
    const user = JSON.parse(localStorage.getItem('token'))['token'];
    const entryID = props.location.state.entryID;

    // TODO: Send a post request to "/handleUserHistoryRequest"
    const loadEntry = async () => {

        const toSend = {
            entryID: entryID, // TODO: Replace with actual entryID
            userID: user
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        // TODO: Fill out/Load the response
        // variables = ImmutableMap.of(
        //           "questions", questions, -> list
        //           "responses", accumulatedResponses, -> list of lists
        //           "date", entry.getDate(),
        //           "tags", entry.getTags(),
        //           "sentiment", entry.getSentiment());
        axios.post(
            "http://localhost:4567/handleUserHistoryRequest",
            toSend,
            config
        ).then(response => {
            let questionsList = response.data["questions"]
            let responsesList = response.data["responses"]

            let journalHistoryDiv = document.getElementById("journalHistory")

            let i;
            for (i = 0; i < questionsList.length; i++) {
                let question = questionsList[i].substring(1);
                if (question !== "") {
                    journalHistoryDiv.innerHTML += "<div style=\"float: left; border-style: solid;"
                        + "padding: 8px; margin-top: 10px;"
                        + "max-width: 600px; word-wrap: break-word\">"
                        + question + "</div>"
                        + "<div style=\"float: left; height: 1px; width: 1000px; \"/>"
                }

                if (i < responsesList.length) {
                    journalHistoryDiv.innerHTML += "<div style=\"float: right; border-style: solid;"
                        + "padding: 8px; margin-top: 10px;"
                        + "max-width: 600px; word-wrap: break-word\">"
                        + responsesList[i] + "</div>"
                        + "<div style=\"float: right; height: 1px; width: 1000px;\"/>"
                }
            }

            // Hopefully this works...

        })
    }

    useEffect(() => {
        loadEntry()
    }, [])

    const historyStyle = {
        height: 400,
        width: 1000,
        margin: 'auto',
        border: '2px solid black',
        overflow: 'auto',
        padding: 10,
    }

    return (
        <div>
            <NavBar />

            <hr style={{backgroundColor: 'black', height: '1px'}}/>

            <br/>
            <div id="journalHistory" className="journalHistory" style={historyStyle}/>
        </div>
    );
}

export default OldJournalEntry;