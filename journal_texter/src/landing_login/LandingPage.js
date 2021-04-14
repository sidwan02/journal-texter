import {Link} from "react-router-dom";
import React from "react";
import "../css/LandingPage.css";

/**
 * The landing page is what will be displayed on the homepage of the JournalTexter website.  It links
 * to the sign up and login pages and describes what JournalTexter is and does.
 *
 * @returns {JSX.Element} Returns a div for the landing page of the website.
 * @constructor
 */
function LandingPage() {
    return (
        <div className="landing-page">
            <div className="header">
                <h1 className="website-title">Journal Texter</h1>
                <h3>Journaling made easy!</h3>
            </div>
            <div className="body">
                <div className="info">
                    <h2>Become a Journaller!</h2>
                    <p>Journaling is incredibly important!  It can help you achieve goals, track progress and growth,
                    gain self confidence, reduce stress, inspire yourself and others, and strengthen memory.  With all
                    of these benefits, why would you not want to journal?</p>
                </div>
                <div className="info">
                    <h2>Benefits of JournalTexter</h2>
                    <p>Unlike traditional journaling, JournalTexter helps you overcome writers block. A common
                    issue many journallers find is that they are unable to come up with ideas on what to write about.
                    JournalTexter addresses that issue by providing prompts to you as you write.  Additionally, past journal
                    entries are all really easy to find in one place!</p>
                </div>
                <div className="info">
                    <h2>How does it work?</h2>
                    <p>JournalTexter uses an algorithm that analyses what you type into your entries and gives you related
                    prompts.  With this amazing technology, trying to decide what to write is super easy!</p>
                </div>
            </div>
            <div className="action-bar">
                <div className="links">
                    <Link className="link-class" to="/login">Login!</Link>
                </div>
                <div id="circle"/>
                <div className="links">
                    <Link className="link-class" to="/signup">Start Today</Link>
                </div>
            </div>
        </div>
    );
}

export default LandingPage;