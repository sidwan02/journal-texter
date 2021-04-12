import React, {useState} from 'react';
import PropTypes from 'prop-types';
import axios from "axios";
import {Link, useHistory} from "react-router-dom";
import LoginTextBox from "./LoginTextBox";
import '../css/LoginPage.css';

export default function SignUpPage({setToken}) {
    const [username, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState("");
    const history = useHistory();

    async function signUpUser() {
        const toSend = {
            username: username,
            password: password,
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        return axios.post(
            "http://localhost:4567/signup",
            toSend,
            config
        )
            .then(response => response.data)
            .catch(error => {
                console.log(error.response);
                setError(error.response.data);
                return null;
            });
    }

    const handleSubmit = async e => {
        if (password === confirmPassword) {
            const token = await signUpUser();

            if (token !== null) {
                assignToken(token);
            }
        } else {
            setError("Passwords do not match");
        }
    }

    function assignToken(token) {
        setToken(token);
        history.push('/');
    }

    return (
        <div className="container">
            <div className="login-box">
                <div className="title">
                    <Link className="home-link link" to="/">JournalTexter</Link>
                </div>
                <div className="login-form">
                    <LoginTextBox text="Username" change={setUserName} type="text"/>
                    <LoginTextBox text="Password" change={setPassword} type="password"/>
                    <LoginTextBox text="Confirm Password" change={setConfirmPassword} type="password"/>
                    <p className="error-text">{error}</p>
                    <button type="submit" onClick={handleSubmit}>Submit</button>
                </div>
            </div>
        </div>
    )
}

SignUpPage.propTypes = {
    setToken: PropTypes.func.isRequired
};