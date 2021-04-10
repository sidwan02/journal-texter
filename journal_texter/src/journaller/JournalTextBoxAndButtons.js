import React from "react";
import {AwesomeButton} from "react-awesome-button";
import "react-awesome-button/dist/styles.css";


function JournalTextBoxAndButtons(props) {

    const typeTextEvent = (event) => {
        {
            props.typeText(event.target.value)
        }
    }

    const sendClickEvent = (event) => {
        {
            props.sendClick(event)
        }
        let journalTextBox = document.getElementById("journalTextBox");
        journalTextBox.value = "";
    }

    const enterPressEvent = (event) => {
        if (event.key === 'Enter') {
            {
                props.sendClick(event)
            }
            let journalTextBox = document.getElementById("journalTextBox");
            journalTextBox.value = "";
        }
    }

    const promptQuestionEvent = (event) => {
        {
            props.promptClick(event)
        }
    }

    const confirmQuestionEvent = (event) => {
        {
            props.confirmClick(event)
        }
    }

    const overallStyle = {
        width: 1000,
        padding: 10,
        margin: 'auto',
    }

    const TextBoxStyle = {
        height: '40px',
        width: '1000px',
        fontSize: 20,
    }

    return (
        <div style={overallStyle}>
            <input id="journalTextBox" type={props.type} placeholder={props.text} style={TextBoxStyle}
                   onChange={typeTextEvent} onKeyPress={enterPressEvent}/>
            <br/><br/>
            <AwesomeButton id="sendButton" type="primary" onPress={sendClickEvent}
                           style={{float: 'right'}}>Send</AwesomeButton>
            <AwesomeButton type="secondary" onPress={promptQuestionEvent} style={{float: 'left'}}>
                Request Questions</AwesomeButton>
            <AwesomeButton type="secondary" onPress={confirmQuestionEvent} style={{float: 'left'}}>
                Choose Question
            </AwesomeButton>
        </div>
    );
}

export default JournalTextBoxAndButtons;