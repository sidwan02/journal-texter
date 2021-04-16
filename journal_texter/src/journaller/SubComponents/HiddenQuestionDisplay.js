import React from "react";

export default function HiddenQuestionDisplay(props) {
    return (
        <div className="question-display grid-element hidden-question-display">
            <div>
                { props.text }
            </div>
        </div>
    );
}