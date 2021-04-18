import React from "react";

export default function HiddenQuestionDisplay(props) {
    return (
        <div className="question-display grid-element hidden-question-display">
            <div className="hidden-question-display-text noselect">
                { props.text }
            </div>
        </div>
    );
}