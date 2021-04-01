import React, {useState} from "react";
import JournalTextBox_Button from "./JournalTextBox_Button";
import {useHistory} from "react-router-dom";

function JournallerPage() {
    const [currentLine, setCurrentLine] = useState("");
    const history = useHistory();

    function handleClick() {
        history.push('/');
    }

    function handleSignOut() {
        history.push("/login");
    }

    const userInput = () => {
        let journalHistoryDiv = document.getElementById("journalHistory")
        // Sadly I don't know enough regex to actually parse this
        let filteredExpression = currentLine.replaceAll('<', '');
        filteredExpression = filteredExpression.replaceAll('>', '');
        filteredExpression = filteredExpression.replaceAll(';', '');

        if (filteredExpression !== '') {
            journalHistoryDiv.innerHTML += "<div style=\"float: right; border-style: solid;"
                + "padding: 8px; margin-top: 10px;"
                + "max-width: 600px; word-wrap: break-word\">"
                + filteredExpression + "</div>"
                + "<div style=\"float: right; height: 1px; width: 1000px;\"/>"
        }

        setCurrentLine("");

    }

    const journallerPrompt = () => {
        let journalHistoryDiv = document.getElementById("journalHistory")

        // We will post request to get the retrieved question from the backend in the future
        let retrievedQuestion = "How are you doing?"

        journalHistoryDiv.innerHTML += "<div style=\"float: left; border-style: solid;"
            + "padding: 8px; margin-top: 10px;"
            + "max-width: 600px; word-wrap: break-word\">"
            + retrievedQuestion + "</div>"
            + "<div style=\"float: left; height: 1px; width: 1000px; \"/>"
    }

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
            <nav>
                <div>
                    <h1 id="logo" onClick={handleClick}>JournalTexter</h1>
                </div>
                <div id="signout" onClick={handleSignOut}>
                    Sign Out
                </div>
            </nav>

            <hr style={{backgroundColor: 'black', height: '1px'}}/>

            <br/>
            <div id="journalHistory" className="journalHistory" style={historyStyle}/>
            <JournalTextBox_Button id="inputBox" typeText={setCurrentLine} sendClick={userInput}
                                   promptClick={journallerPrompt} type="text"/>
        </div>
    );
}

export default JournallerPage;