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
    const [currentResponse, setCurrentResponse] = useState([]);
    const [selectedQuestion, setSelectedQuestion] = useState("");
    const [question1, setQuestion1] = useState("");
    const [question2, setQuestion2] = useState("");
    const [question3, setQuestion3] = useState("");
    const [question4, setQuestion4] = useState("");
    const [question5, setQuestion5] = useState("");

    const history = useHistory();

    // TODO: Replace for final build
    //const user = "placeholder";
    const user = JSON.parse(localStorage.getItem('token'))['token'];

    function handleClick() {
        history.push('/');
    }

    function handleSignOut() {
        localStorage.removeItem('token');
        sessionStorage.removeItem('token');

        history.push('/login');

        window.location.reload(false);
    }

    /**
     * Takes what the user types in the text box and sends it into the journal history
     */
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

            setCurrentResponse(currentResponse.concat(currentLine));
            setCurrentLine("");
        }
    }

    /**
     * Requests 5 questions from the backend depending on what the currentResponse is
     */
    const requestQuestions = () => {
        if (currentResponse.length !== 0) {
            const toSend = {
                entryID: 1, //TODO: Replace this with actual entryID
                userID: user, //TODO: Replace this with actual username
                text: currentResponse,
                state: "requestQuestion"
            }

            let config = {
                headers: {
                    "Content-Type": "application/json",
                    'Access-Control-Allow-Origin': '*',
                }
            }

            // TODO: Insert post request here for "/handleRequestQuestion"
            // Response is variables = ImmutableMap.of(
            //             "questions", List<String>,
            //             "tags", List<String>,
            //             "sentiment", double);
            axios.post(
                "http://localhost:4567/handleRequestQuestion",
                toSend,
                config
            ).then(response => {
                console.log(response.data)
                let questionsList = response.data["questions"]
                console.log(questionsList)
                setQuestion1(questionsList[0])
                setQuestion2(questionsList[1])
                setQuestion3(questionsList[2])
                setQuestion4(questionsList[3])
                setQuestion5(questionsList[4])
                }
            )
            setCurrentResponse([]);
        }
    }

    /**
     * Sends the currently selected question into the journal history
     */
    const chooseQuestion = () => {
        let journalHistoryDiv = document.getElementById("journalHistory")

        if (selectedQuestion !== "") {

            const toSend = {
                entryID: 1, // TODO: Replace with actual entryID,
                question: selectedQuestion,
                userID: user,
                text: [],
                state: "saveQuestion"
            }

            let config = {
                headers: {
                    "Content-Type": "application/json",
                    'Access-Control-Allow-Origin': '*',
                }
            }

            // TODO: Insert post request here for "/handleSaveUserInputs"
            axios.post(
                "http://localhost:4567/handleSaveUserInputs",
                toSend,
                config
            ).then(response => {

            })

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

    /**
     * Handles the toggling of the generated questions
     */
    const handleQuestions = (event, newQuestion) => {
        setSelectedQuestion(newQuestion);
    }

    /**
     * Retrieves a question from the backend immediately upon loading the page
     */
    const firstQuestionLoad = async () => {
        let journalHistoryDiv = document.getElementById("journalHistory")

        // TODO: Replace what's below this line with Axios post request stuff
        const toSend = {
            entryID: 1, //TODO: Replace this with actual entryId
            userID: user,
            text: [],
            state: "start"
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        // TODO: Insert post request here for "/handleRequestQuestion"
        // Response is variables = ImmutableMap.of(
        //             "questions", List<String>,
        //             "tags", List<String>,
        //             "sentiment", double);
        axios.post(
            "http://localhost:4567/handleRequestQuestion",
            toSend,
            config
        ).then(response => {

        })

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


    /**
     * Manually saves the entry. TODO: Close the entry too
     */
    const saveEntry = () => {
        const toSend = {
            entryID: 1, // TODO: Replace with actual entryId,
            question: "",
            userID: user,
            text: currentResponse,
            state: "saveEntry"
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        // TODO: Insert post request here for "/handleSaveUserInputs"
        axios.post(
            "http://localhost:4567/handleSaveUserInputs",
            toSend,
            config
        ).then(response => {

        })
    }

    // TODO: Delete this and its corresponding button later, only for testing
    const quickGenerateQuestions = () => {
        if (currentResponse.length !== 0) {
            setCurrentResponse([]);

            let questionArray = ["Tell me about your day", "How is school going?",
                "What interesting things have you done today?", "How is the weather today?",
                "Are you feeling well?"]
            setQuestion1(questionArray[0]);
            setQuestion2(questionArray[1]);
            setQuestion3(questionArray[2]);
            setQuestion4(questionArray[3]);
            setQuestion5(questionArray[4]);
        }
    }

    const historyStyle = {
        height: 400,
        width: 1000,
        margin: 'auto',
        border: '2px solid black',
        overflow: 'auto',
        padding: 10,
    }

    const paddingStyle = {
        margin: 'auto',
        width: 400,
    }

    return (
        <div>
            <nav className="dashboard-nav">
                <h1 id="logo" onClick={handleClick}>JournalTexter</h1>
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
            <div id="toggleButtons" style={paddingStyle}>
            <ToggleButtonGroup value={selectedQuestion} orientation="vertical"
                               exclusive onChange={handleQuestions}>
                {question1 !== "" &&
                    <ToggleButton value={question1}>
                        {question1}
                    </ToggleButton>
                }
                {question2 !== "" &&
                    <ToggleButton value={question2}>
                        {question2}
                    </ToggleButton>
                }
                {question3 !== "" &&
                    <ToggleButton value={question3}>
                    {question3}
                    </ToggleButton>
                }
                {question4 !== "" &&
                    <ToggleButton value={question4}>
                        {question4}
                    </ToggleButton>
                }
                {question5 !== "" &&
                    <ToggleButton value={question5}>
                        {question5}
                    </ToggleButton>
                }
            </ToggleButtonGroup>
            </div>

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