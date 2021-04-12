import {Link} from "react-router-dom";
import React from "react";
import "../css/LandingPage.css";

function LandingPage() {
    return (
        <div className="landing-page">
            <div className="header">
                <h1 className="website-title">Journal Texter</h1>
                <h3>Journaling made easy!</h3>
            </div>
            <div className="body">
                <div className="info">
                    <h2>Channeling your journaling</h2>
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aspernatur dolore enim mollitia nobis
                        quae qui. Aspernatur blanditiis cum ea eligendi, error iure minus nesciunt optio praesentium
                        quos similique tempora ut!</p>
                </div>
                <div className="info">
                    <h2>Other cool things</h2>
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aspernatur dolore enim mollitia nobis
                        quae qui. Aspernatur blanditiis cum ea eligendi, error iure minus nesciunt optio praesentium
                        quos similique tempora ut!</p>
                </div>
                <div className="info">
                    <h2>Other other cool things</h2>
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aspernatur dolore enim mollitia nobis
                        quae qui. Aspernatur blanditiis cum ea eligendi, error iure minus nesciunt optio praesentium
                        quos similique tempora ut!</p>
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