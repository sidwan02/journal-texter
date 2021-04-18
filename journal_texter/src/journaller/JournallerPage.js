import '../css/JournallerPage.css';
import NavBar from "../dashboard/NavBar";
import React, {useEffect, useRef, useState} from "react";
import QuestionDisplay from "./SubComponents/QuestionDisplay";
import HiddenQuestionDisplay from "./SubComponents/HiddenQuestionDisplay";
import axios from "axios";
import {useHistory} from "react-router-dom";

export default function (props) {
    const [userResponse, setUserResponse] = useState("");
    const [texts, setTexts] = useState([]);
    const [recentUserResponse, setRecentUserResponse] = useState([]);
    const [showQuestionDisplay, setShowQuestionDisplay] = useState(false);
    const [questions, setQuestions] = useState(["", "", "", "", ""]);
    const [numUserInput, setNumUserInput] = useState(0);

    const user = JSON.parse(localStorage.getItem('token'))['token'];
    const entryID = props.location.state.entryID;
    const history = useHistory()

    let entryName = '';

    /**
     * Loads in a random question at the start
     */
    const firstQuestionLoad = async () => {
        const toSend = {
            entryID: entryID,
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

        axios.post(
            "http://localhost:4567/handleRequestQuestion",
            toSend,
            config
        ).then(response => {
            let questionsList = response.data["questions"]
            let firstQuestion = questionsList[0];
            setTexts(texts.concat(
                <div className="journal-entry-text-container align-left">
                    <div className="journal-entry-text">{firstQuestion}</div>
                </div>));

        })
    }

    /**
     * The useEffect that allows the firstQuestion to load
     */
    useEffect(() => {
        firstQuestionLoad()
    }, [])

    /**
     * Updates the journal with user input
     */
    const addResponse = () => {
        // Filters out certain characters from the userResponse
        let filteredResponse = userResponse.replaceAll('<', '');
        filteredResponse = filteredResponse.replaceAll('>', '');
        filteredResponse = filteredResponse.replaceAll(';', '');
        filteredResponse = filteredResponse.replaceAll('{', '');
        filteredResponse = filteredResponse.replaceAll('}', '');
        filteredResponse = filteredResponse.replaceAll('@', '');

        if (filteredResponse !== '') {
            setShowQuestionDisplay(true);

            let newText = (<div className="journal-entry-text-container align-right">
                <div className="journal-entry-text">{filteredResponse}</div>
            </div>);

            setTexts(texts.concat(newText));

            setRecentUserResponse(recentUserResponse.concat(filteredResponse));

            setNumUserInput(numUserInput + 1);
        }
    }

    /**
     * Lets the user request a set of new questions
     */
    const loadNewQuestions = () => {
        if (numUserInput !== 0) {
            const toSend = {
                entryID: entryID,
                userID: user,
                text: recentUserResponse,
                state: "requestQuestion"
            }

            let config = {
                headers: {
                    "Content-Type": "application/json",
                    'Access-Control-Allow-Origin': '*',
                }
            }

            axios.post(
                "http://localhost:4567/handleRequestQuestion",
                toSend,
                config
            ).then(response => {
                let questionsList = response.data["questions"]
                setQuestions(questionsList);
            })
        }
    }

    /**
     * The useEffect that allows a new set of questions to be generated every time the user inputs
     */
    useEffect(() => {
        loadNewQuestions()
    }, [numUserInput])

    /**
     * Sends a POST request to the backend to save the entry.
     */
    function saveEntry() {
        const toSend = {
            entryID: entryID,
            question: "",
            userID: user,
            entryTitle: entryName,
            text: recentUserResponse,
            state: "saveEntry",
            entryName: entryName,
        }

        console.log(toSend);

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/handleSaveUserInputs",
            toSend,
            config
        ).then(() => {
            history.push('/');
        })
    }

    /**
     * Function for the onClickEvent for the save button. Also allows the user to name their entry
     */
    function saveButtonClick() {
        entryName = prompt("Please enter journal entry name:", "");
        if (entryName != null && entryName !== "") {
            console.log(entryName)
            saveEntry();
        }
    }

    /**
     * Allows the user to send their response by just pressing ENTER
     * @param event - the event you want to associate with the ENTER press
     */
    function enterPressed(event) {
        let code = event.keyCode || event.which;
        if (code === 13) {
            submitUserResponse();
        }
    }

    /**
     * Function that deals with the user response and clears the userResponse textbox
     */
    function submitUserResponse() {
        addResponse();
        setUserResponse("");
        document.getElementById("journaling-text-box").value = '';
    }

    const messagesEndRef = useRef(null);

    /**
     * Lets the journalHistory div autoscroll to the bottom
     */
    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" })
    }

    useEffect(() => {
        scrollToBottom()
    }, [texts]);

    return (
        <div className="journaller">
            <NavBar/>
            <div className="journaller-body">
                <div className="text-display grid-element">
                    {texts}
                    <div ref={messagesEndRef} />
                </div>
                {showQuestionDisplay ?
                    <QuestionDisplay questions={questions} texts={texts} setTexts={setTexts}
                                     recentUserResponse={recentUserResponse}
                                     setRecentUserResponse={setRecentUserResponse}
                                     loadNewQuestions={loadNewQuestions}
                                     user={user} entryID={entryID}
                                     setShowQuestionDisplay={setShowQuestionDisplay}/>
                    : <HiddenQuestionDisplay text={"Respond to the prompt!"}/>}
                <input type="text" onKeyPress={enterPressed}
                       autoComplete="off"
                       onChange={event => setUserResponse(event.target.value)}
                       autoFocus
                       placeholder="Respond Here (Longer Responses Are Better!)"
                       id="journaling-text-box"
                       className="grid-element journal-type-box"/>
                <button onClick={submitUserResponse} className="grid-element text-submit-button journal-button">Respond</button>
                <button onClick={saveButtonClick} className="save-journal-entry grid-element journal-button">Save Entry</button>
            </div>
        </div>
    );
}