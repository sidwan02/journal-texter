import {Link} from "react-router-dom";
import React from "react";

function LandingPage() {
    return (
        <div>
            <h3>Journal Texter Main Page</h3>
            <Link to="/login">Login Here</Link>
            <br/>
            <Link to="/journaller">Journaller Here</Link>
        </div>
    );
}

export default LandingPage;