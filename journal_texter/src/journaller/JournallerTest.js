import '../css/JournallerTest.css';
import NavBar from "../dashboard/NavBar";
import React, {useState} from "react";
import QuestionDisplay from "./SubComponents/QuestionDisplay";
import HiddenQuestionDisplay from "./SubComponents/HiddenQuestionDisplay";

export default function () {
    const [userResponse, setUserResponse] = useState("");
    const [texts, setTexts] = useState([]);
    const [showQuestionDisplay, setShowQuestionDisplay] = useState(true);
    // const [questions, setQuestions] = useState(["", "", "", "", ""]);
    // for testing
    const [questions, setQuestions] = useState(["what happens if the question is longer and therefore takes up more space?", "prompt 2", "prompt 3", "prompt 4", "prompt 5"]);

    const addResponse = () => {
        setTexts(texts.concat(
            <div className="journal-entry-text-container align-right">
                <div className="journal-entry-text">{userResponse}</div>
            </div>));
    }

    function saveEntry() {
        // TODO Connect to save functionality
        alert("NEED TO ADD SAVE ENTRY");
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