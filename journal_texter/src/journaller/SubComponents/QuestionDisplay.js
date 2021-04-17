import React from "react";
import axios from "axios";

export default function QuestionDisplay(props) {
    let questions = props.questions;
    let recentUserResponse = props.recentUserResponse;
    let user = props.user;
    let entryID = props.entryID;
    let questionElements = questions.map((question) =>
        <div onClick={() => handleQuestion(question)} className="prompt-selector question-display-element">
            <div>{question}</div>
        </div>
    )

    const handleQuestion = (question) => {
        if (question !== "") {
            const toSend = {
                entryID: entryID,
                question: question,
                userID: user,
                text: recentUserResponse,
                state: "saveQuestion"
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
            ).then(() => {
                props.setTexts(props.texts.concat(
                    <div className="journal-entry-text-container align-left">
                        <div className="journal-entry-text">{question}</div>
                    </div>));

                props.setShowQuestionDisplay(false);

                props.setRecentUserResponse([]);
            })
        }
    }

    function requestNewQuestions() {
        //alert("Requesting new questions");
        props.loadNewQuestions();
    }

    return (
        <div className="question-display grid-element">
            <div className="question-display-header question-display-element">
                <div className="question-display-header-text">
                    Select a prompt to respond to!
                </div>
            </div>
            {questionElements}
            <div className="question-display-footer question-display-element">
                <div onClick={requestNewQuestions} className="question-display-footer-text">
                    Click to Refresh Prompts!
                </div>
            </div>
        </div>
    );
}