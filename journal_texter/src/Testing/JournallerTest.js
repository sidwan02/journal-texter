import './JournallerTest.css';
import NavBar from "../dashboard/NavBar";
import React, {useState} from "react";

export default function () {
    const [userResponse, setUserResponse] = useState("");

    function test() {
        alert("It works");
    }

    return (
        <div className="journaller">
            <NavBar/>
            <div className="journaller-body">
                <div className="text-display grid-element"></div>
                <div className="question-display grid-element"></div>
                <div className="user-input grid-element">
                    <form className="user-input-form">
                        <input type="text" className="journal-response"/>
                        <input type="submit" onClick={test} className="journal-submit-button"/>
                    </form>
                </div>
                <div className="save-journal-element grid-element">
                    <button className="save-journal-entry" onClick={test}>Save</button>
                </div>
            </div>
        </div>
    );
}