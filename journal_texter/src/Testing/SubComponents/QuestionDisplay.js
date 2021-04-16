import React from "react";

export default function QuestionDisplay(props) {
    let questions = props.questions;
    let questionElements = questions.map((question) =>
        <div onClick={() => handleQuestion(question)} className="prompt-selector question-display-element">
            <div>{question}</div>
        </div>
    )

    const handleQuestion = (question) => {
        props.setTexts(props.texts.concat(
            <div className="journal-entry-text-container align-left">
                <div className="journal-entry-text">{question}</div>
            </div>));
        props.setShowQuestionDisplay(false);
    }

    return (
        <div className="question-display grid-element">
            <div className="question-display-header question-display-element">
                <div className="question-display-header-text">
                    Select a prompt to respond to!
                </div>
            </div>
            { questionElements }
        </div>
    );
}