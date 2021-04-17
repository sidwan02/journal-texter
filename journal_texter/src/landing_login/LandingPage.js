import {Link, useHistory} from "react-router-dom";
import React from "react";
import "../css/LandingPage.css";
import logo from "../images/logo192.png"

/**
 * The landing page is what will be displayed on the homepage of the JournalTexter website.  It links
 * to the sign up and login pages and describes what JournalTexter is and does.
 *
 * @returns {JSX.Element} Returns a div for the landing page of the website.
 * @constructor
 */
function LandingPage() {
    const history = useHistory();

    return (
        <div className="landing-page">
            <div className="landing-page-nav-login-link-container">
                <button className="landing-page-nav-login-link" onClick={() => history.push('/login')}>Login
                </button>
            </div>
            <div className="landing-page-header">
                <div className="landing-page-title-container">
                    <h1 className="landing-page-title">Journal Texter</h1>
                    <h3 className="landing-page-subtitle">Journaling made easy!</h3>
                </div>
            </div>
            <div className="landing-page-body">
                <div className="landing-page-info-card-container">
                    <div className="landing-page-info-card">
                        <h2 className="landing-page-info-card-header">Become a Journaller!</h2>
                        <p className="landing-page-info-card-paragraph">Journaling is incredibly important! It can help
                            you achieve goals, track progress and growth,
                            gain self confidence, reduce stress, inspire yourself and others, and strengthen memory.
                            With
                            all
                            of these benefits, why would you not want to journal?</p>
                    </div>
                    <div className="landing-page-info-card">
                        <h2 className="landing-page-info-card-header">Benefits of JournalTexter</h2>
                        <p className="landing-page-info-card-paragraph">Unlike traditional journaling, JournalTexter
                            helps you overcome writers block. A common
                            issue many journallers find is that they are unable to come up with ideas on what to write
                            about.
                            JournalTexter addresses that issue by providing prompts to you as you write. Additionally,
                            past
                            journal
                            entries are all really easy to find in one place!</p>
                    </div>
                    <div className="landing-page-info-card">
                        <h2 className="landing-page-info-card-header">How does it work?</h2>
                        <p className="landing-page-info-card-paragraph">JournalTexter uses an algorithm that analyses
                            what you type into your entries and gives you
                            related
                            prompts. With this amazing technology, trying to decide what to write is super easy!</p>
                    </div>
                </div>
            </div>
            <div className="landing-page-action-bar">

                <div className="landing-page-link-container">
                    <Link className="landing-page-link" to="/signup">Sign Up!</Link>
                </div>
                <div className="landing-page-logo-container">
                    <img id="landing-page-logo" src={logo} alt="JournalTexter Logo"/>
                </div>
                <div className="landing-page-link-container">
                    <Link className="landing-page-link" to="/login">Login!</Link>
                </div>
            </div>
        </div>
    );
}

export default LandingPage;