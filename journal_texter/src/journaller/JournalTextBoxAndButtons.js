import React from "react";
import {AwesomeButton} from "react-awesome-button";
import "react-awesome-button/dist/styles.css";


function JournalTextBoxAndButtons(props) {

    const typeTextEvent = (event) => {
        {
            props.typeText(event.target.value)
        }
    }

    const sendPressEvent = (event) => {
        {
            props.sendPress(event)
        }
        let journalTextBox = document.getElementById("journalTextBox");
        journalTextBox.value = "";
    }

    const enterPressEvent = (event) => {
        if (event.key === 'Enter') {
            {
                props.sendPress(event)
            }
            let journalTextBox = document.getElementById("journalTextBox");
            journalTextBox.value = "";
        }
    }

    const savePressEvent = (event) => {
        {
            props.savePress(event)
        }
    }

    const overallStyle = {
        width: 800,
        padding: 10,
        marginLeft: 10,
        float: 'left',
    }

    const TextBoxStyle = {
        height: '40px',
        width: '800px',
        fontSize: 20,
    }

    return (
        <div style={overallStyle}>
            <input id="journalTextBox" type={props.type} placeholder={props.text} style={TextBoxStyle}
                   onChange={typeTextEvent} onKeyPress={enterPressEvent}/>
            <br/><br/>
            <AwesomeButton id="sendButton" type="primary" onPress={sendPressEvent}
                           style={{float: 'right'}}>Send</AwesomeButton>
            <AwesomeButton type="secondary" onPress={savePressEvent} style={{float: 'left'}}>
                Save Entry</AwesomeButton>
        </div>
    );
}

export default JournalTextBoxAndButtons;