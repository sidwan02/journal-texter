import '../css/JournallerTest.css';
import NavBar from "../dashboard/NavBar";
import React, {useEffect, useState} from "react";
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
    const user = JSON.parse(localStorage.getItem('token'))['token'];
    const entryID = props.location.state.entryID;
    const history = useHistory()

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

    useEffect(() => {
        firstQuestionLoad()
    }, [])

    /**
     * Updates the journal with user input. Sends a POST request to the backend to generate new questions
     */
    const addResponse = () => {
        setTexts(texts.concat(
            <div className="journal-entry-text-container align-right">
                <div className="journal-entry-text">{userResponse}</div>
            </div>));

        setRecentUserResponse(recentUserResponse.concat(userResponse));

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

    /**
     * Manually saves the entry
     */
    function saveEntry() {
        const toSend = {
            entryID: entryID,
            question: "",
            userID: user,
            text: recentUserResponse,
            state: "saveEntry"
        }

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
        ).then(response => {

        })
        history.push('/');
    }

    function enterPressed(event) {
        let code = event.keyCode || event.which;
        if (code === 13) {
            submitUserResponse();
        }
    }

    function submitUserResponse() {
        setShowQuestionDisplay(true);

        addResponse();
        setUserResponse("");
        document.getElementById("journaling-text-box").value = '';
    }

    return (
        <div className="journaller">
            <NavBar/>
            <div className="journaller-body">
                <div className="text-display grid-element">
                    {texts}
                </div>
                {showQuestionDisplay ?
                    <QuestionDisplay questions={questions} texts={texts} setTexts={setTexts}
                                     recentUserResponse={recentUserResponse}
                                     setRecentUserResponse={setRecentUserResponse}
                                     user={user} entryID={entryID}
                                                        setShowQuestionDisplay={setShowQuestionDisplay}/>
                                                        : <HiddenQuestionDisplay />}
                <input type="text" onKeyPress={enterPressed}
                       autoComplete="off"
                       onChange={event => setUserResponse(event.target.value)}
                       id="journaling-text-box"
                       className="grid-element journal-type-box"/>
                <button onClick={submitUserResponse} className="grid-element text-submit-button journal-button">Send</button>
                <button onClick={saveEntry} className="save-journal-entry grid-element journal-button">Save</button>
            </div>
        </div>
    );
}