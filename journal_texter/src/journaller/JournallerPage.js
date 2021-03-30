import React, {useState} from "react";
import JournalTextBox from "./JournalTextBox";
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
        journalHistoryDiv.innerHTML += "<div style=\"float: right; border-style: solid; padding: 10px; margin: 10px\">"
            + currentLine + "</div> <br/><div style=\"height: 50px\"/><br/>"

    }

    const historyStyle = {
        height: 400,
        width: 1000,
        margin: 'auto',
        border: '2px solid black',
        overflow: 'auto'
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
            <br/>
            <div id="entryComponents">
            <JournalTextBox id="inputBox" change={setCurrentLine} type="text"/>
            <button onClick={userInput}>Send</button>
            </div>
        </div>
    );
}

export default JournallerPage;