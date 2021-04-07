import React, {useState} from "react";
import JournalTextBoxAndButtons from "./JournalTextBoxAndButtons";
import {useHistory} from "react-router-dom";
import ToggleButton from '@material-ui/lab/ToggleButton';
import ToggleButtonGroup from '@material-ui/lab/ToggleButtonGroup';
import {AwesomeButton} from "react-awesome-button";

function JournallerPage() {
    const [currentLine, setCurrentLine] = useState("");
    const [tags, setTags] = useState(() => ['weather']);
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

    const handleTags = (event, newTags) => {
        setTags(newTags);
        /**
         * Filler code on what we'll do with the tags, if we do anything here
         */
        for (let selectedTag of newTags) {
            console.log(selectedTag);
        }
    }

    const saveEntry = () => {
        // Have a post request here that will send journalHistory div HTML to the backend
        let historyHTML = document.getElementById("journalHistory").innerHTML;

        /**
         * Filler code, must replace later
         */
        console.log(historyHTML)
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
            <JournalTextBoxAndButtons id="inputBox" typeText={setCurrentLine} sendClick={userInput}
                                      promptClick={journallerPrompt} type="text"/>
            <br/>
            <br/>
            <ToggleButtonGroup value={tags} onChange={handleTags}>
                <ToggleButton value="work">
                    work
                </ToggleButton>
                <ToggleButton value="school">
                    school
                </ToggleButton>
                <ToggleButton value="pet">
                    pets
                </ToggleButton>
                <ToggleButton value="relationship">
                    relationships
                </ToggleButton>
                <ToggleButton value="fitness">
                    fitness
                </ToggleButton>
                <ToggleButton value="weather">
                    weather
                </ToggleButton>
            </ToggleButtonGroup>

            <div style={{width: 1000, padding: 10, margin: 'auto',}}>
            <AwesomeButton id="saveButton" type="secondary" onPress={saveEntry}
                           style={{float: 'left'}}>Save Entry</AwesomeButton>
            </div>
        </div>
    );
}

export default JournallerPage;