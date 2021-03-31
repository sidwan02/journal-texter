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
        journalHistoryDiv.innerHTML += "<div style=\"float: right; border-style: solid; padding: 10px; margin: 10px\">"
            + filteredExpression + "</div> <br/><div style=\"height: 50px\"/><br/>"
        }

        setCurrentLine("");

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
            <JournalTextBox_Button id="inputBox" change={setCurrentLine} click={userInput} type="text"/>
        </div>
    );
}

export default JournallerPage;