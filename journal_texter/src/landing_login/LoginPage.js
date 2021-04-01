import React, { useState } from 'react';
import PropTypes from 'prop-types';
import {Link, useHistory} from "react-router-dom";
import LoginTextBox from "./LoginTextBox";
import '../css/LoginPage.css';

async function loginUser(credentials) {
    return fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(credentials)
    })
        .then(data => data.json())
}

export default function LoginPage({ setToken }) {
    const [username, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const history = useHistory();

    const handleSubmit = async e => {
        const token = await loginUser({
            username,
            password
        });
        setToken(token);
        history.push('/dashboard');
    }

    return(
        <div className="loginBox">
            <h1>JournalTexter</h1>
            <LoginTextBox text="Username" change={setUserName} type="text"/>
            <LoginTextBox text="Password" change={setPassword} type="password"/>
            <p>
                <button onClick={handleSubmit}>Submit</button>
            </p>
            <p>
                <Link to="/signup">Sign Up!</Link>
            </p>
        </div>
    )
}

LoginPage.propTypes = {
    setToken: PropTypes.func.isRequired
};