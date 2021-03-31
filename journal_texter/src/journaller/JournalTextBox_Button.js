import React from "react";
import {AwesomeButton} from "react-awesome-button";
import "react-awesome-button/dist/styles.css";


function JournalTextBox_Button(props) {

    const onChangeEvent = (event) => {
        {
            props.change(event.target.value)
        }
    }

    const onClickEvent = (event) => {
        {
            props.click(event)
        }
        let journalTextBox = document.getElementById("journalTextBox");
        journalTextBox.value = "";
    }

    const TextBoxStyle = {
        height: '40px',
        width: '1000px',
        fontSize: 20,
    }

    return (
        <p>
            <input id="journalTextBox" type={props.type} placeholder={props.text} style={TextBoxStyle}
                   onChange={onChangeEvent}/>
            <br/><br/>
            <AwesomeButton type="primary" onPress={onClickEvent}>Send</AwesomeButton>
        </p>
    );
}

export default JournalTextBox_Button;