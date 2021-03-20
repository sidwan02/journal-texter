/*landing.jsx*/
import React from "react";
import {Link} from "react-router-dom";

const MainPage = () => {
    return (
        <div>
            <h3>Journal Texter Main Page</h3>
            <Link to="/login">Login Here</Link>
            <br/>
            <Link to="/journaller">Journaller Here</Link>
        </div>
    );
};

export default MainPage;