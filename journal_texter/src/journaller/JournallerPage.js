import React, {useEffect, useState} from "react";
import JournalTextBoxAndButtons from "./JournalTextBoxAndButtons";
import {useHistory} from "react-router-dom";
import ToggleButton from '@material-ui/lab/ToggleButton';
import ToggleButtonGroup from '@material-ui/lab/ToggleButtonGroup';
import {AwesomeButton} from "react-awesome-button";
import axios from "axios";

function JournallerPage() {
    // currentLine - the current input in the textbox
    const [currentLine, setCurrentLine] = useState("");
    // currentResponse - all of the currentLine before the user clicks "Request Questions"
    const [currentResponse, setCurrentResponse] = useState("");
    const [selectedQuestion, setSelectedQuestion] = useState("");
    const [question1, setQuestion1] = useState("");
    const [question2, setQuestion2] = useState("");
    const [question3, setQuestion3] = useState("");
    const [question4, setQuestion4] = useState("");
    const [question5, setQuestion5] = useState("");

    const history = useHistory();

    function handleClick() {
        history.push('/');
    }

    function handleSignOut() {
        history.push("/login");
    }

    const userInput = () => {
        let journalHistoryDiv = document.getElementById("journalHistory")
        let filteredExpression = currentLine.replaceAll('<', '');
        filteredExpression = filteredExpression.replaceAll('>', '');
        filteredExpression = filteredExpression.replaceAll(';', '');

        if (filteredExpression !== '') {
            journalHistoryDiv.innerHTML += "<div style=\"float: right; border-style: solid;"
                + "padding: 8px; margin-top: 10px;"
                + "max-width: 600px; word-wrap: break-word\">"
                + filteredExpression + "</div>"
                + "<div style=\"float: right; height: 1px; width: 1000px;\"/>"

            setCurrentResponse(currentResponse + " " + filteredExpression);
            setCurrentLine("");
        }
    }

    // Request questions from the backend
    // TODO: Currently only an outline
    const requestQuestions = () => {
        const toSend = {
            text: currentResponse
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        // TODO: Insert post request here for "/handleUserResponse"
        /*
        axios.post(
            "http://localhost:4567/handleUserResponse",
            toSend,
            config
        ).then(response => {

        })
         */

        // This is a save and request button
        // Needs userID

        // Send only the most recent history to backend for processing
        // "text" is the key

        // Returns immutable map that contains a list of strings
        // That list of strings is a list of questions
        // "questions" is the key
        // Said immutable map contains a list of tags that is a list of strings
        // "tags" is the key for that
        // Also get a "sentiment" - a double
        // localhost 4567, /handleUserResponse

        // setCurrentResponse("");
    }

    const chooseQuestion = () => {
        let journalHistoryDiv = document.getElementById("journalHistory")

        // TODO: Insert post request here for "/handleSelectedQuestion"

        if (selectedQuestion !== "" && currentResponse !== '') {
            journalHistoryDiv.innerHTML += "<div style=\"float: left; border-style: solid;"
                + "padding: 8px; margin-top: 10px;"
                + "max-width: 600px; word-wrap: break-word\">"
                + selectedQuestion + "</div>"
                + "<div style=\"float: left; height: 1px; width: 1000px; \"/>"

            setSelectedQuestion("");
            setQuestion1("");
            setQuestion2("");
            setQuestion3("");
            setQuestion4("");
            setQuestion5("");
        }
    }

    const handleQuestions = (event, newQuestion) => {
        setSelectedQuestion(newQuestion);
    }

    const firstQuestionLoad = async () => {
        let journalHistoryDiv = document.getElementById("journalHistory")

        // TODO: Replace what's below this line with Axios post request stuff
        let firstQuestion = "How are you doing?"
        journalHistoryDiv.innerHTML += "<div style=\"float: left; border-style: solid;"
            + "padding: 8px; margin-top: 10px;"
            + "max-width: 600px; word-wrap: break-word\">"
            + firstQuestion + "</div>"
            + "<div style=\"float: left; height: 1px; width: 1000px; \"/>"
    }

    useEffect(() => {
        firstQuestionLoad()
    }, [])


    const saveEntry = () => {
        // TODO: Axios Post request stuff here for "/handleSaveUserEntry"
    }

    // TODO: Delete this and its corresponding button later, only for testing
    const quickGenerateQuestions = () => {

        setCurrentResponse("");

        let questionArray = ["Tell me about your day", "How is school going?",
            "What interesting things have you done today?", "How is the weather today?",
            "Are you feeling well?"]
        setQuestion1(questionArray[0]);
        setQuestion2(questionArray[1]);
        setQuestion3(questionArray[2]);
        setQuestion4(questionArray[3]);
        setQuestion5(questionArray[4]);
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
                                      promptClick={requestQuestions} confirmClick={chooseQuestion}
                                      type="text"/>
            <br/>
            <br/>
            <ToggleButtonGroup value={selectedQuestion} orientation="vertical"
                               exclusive onChange={handleQuestions}>
                <ToggleButton value={question1}>
                    {question1}
                </ToggleButton>
                <ToggleButton value={question2}>
                    {question2}
                </ToggleButton>
                <ToggleButton value={question3}>
                    {question3}
                </ToggleButton>
                <ToggleButton value={question4}>
                    {question4}
                </ToggleButton>
                <ToggleButton value={question5}>
                    {question5}
                </ToggleButton>
            </ToggleButtonGroup>

            <div style={{width: 1000, padding: 10, margin: 'auto',}}>
            <AwesomeButton id="saveButton" type="secondary" onPress={saveEntry}
                           style={{float: 'left'}}>Save Entry</AwesomeButton>
            <AwesomeButton type="secondary" onPress={quickGenerateQuestions}
                           style={{float: 'left'}}>Test Generate</AwesomeButton>
            </div>
        </div>
    );
}

export default JournallerPage;