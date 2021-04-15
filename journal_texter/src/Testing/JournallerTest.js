import './JournallerTest.css';
import NavBar from "../dashboard/NavBar";
import React, {useState} from "react";
import ToggleButton from "@material-ui/lab/ToggleButton";
import ToggleButtonGroup from "@material-ui/lab/ToggleButtonGroup";

export default function () {
    const [userResponse, setUserResponse] = useState("");

    function test() {
        submitUserForm();
    }

    function enterPressed(event) {
        let code = event.keyCode || event.which;
        if(code === 13) {
            submitUserForm();
        }
    }

    function submitUserForm() {
        setQuestion1("test1");
        setQuestion2("test2");
        setQuestion3("test3");
        setQuestion4("test4");
        setQuestion5("test5");
    }

    const [selectedQuestion, setSelectedQuestion] = useState("");
    const [question1, setQuestion1] = useState("test1");
    const [question2, setQuestion2] = useState("test2");
    const [question3, setQuestion3] = useState("test3");
    const [question4, setQuestion4] = useState("test4");
    const [question5, setQuestion5] = useState("test5");

    const handleQuestions = (event, newQuestion) => {
        setSelectedQuestion(newQuestion);
        setQuestion1("");
        setQuestion2("");
        setQuestion3("");
        setQuestion4("");
        setQuestion5("");
    }

    return (
        <div className="journaller">
            <NavBar/>
            <div className="journaller-body">
                <div className="text-display grid-element"></div>
                <div className="question-display grid-element">
                    <div className="question-display-header question-display-element">
                        <div className="question-display-header-text">
                            Select a prompt to respond to!
                        </div>
                    </div>
                    <div className="prompt-selector question-display-element">
                        <div>{question1}</div>
                    </div>
                    <div className="prompt-selector question-display-element">
                        <div>{question2}</div>
                    </div>
                    <div className="prompt-selector question-display-element">
                        <div>{question3}</div>
                    </div>
                    <div className="prompt-selector question-display-element">
                        <div>{question4}</div>
                    </div>
                    <div className="prompt-selector question-display-element">
                        <div>{question5}</div>
                    </div>
                </div>
                <input type="text" onKeyPress={enterPressed}
                       onChange={event => setUserResponse(event.target.value)}
                       className="grid-element journal-type-box"/>
                <button onClick={test} className="grid-element text-submit-button journal-button">Send</button>
                <button onClick={test} className="save-journal-entry grid-element journal-button">Save</button>
            </div>
        </div>
    );
}