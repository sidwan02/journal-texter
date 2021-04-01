import React, {useState} from 'react';
import PropTypes from 'prop-types';
import axios from "axios";
import {Link, useHistory} from "react-router-dom";
import LoginTextBox from "./LoginTextBox";
import '../css/LoginPage.css';

export default function LoginPage({setToken}) {
    const [username, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const history = useHistory();

    async function loginUser() {
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
            "http://localhost:4567/login",
            toSend,
            config
        )
            .then(response => response.data)
            .catch(function (error) {
                console.log(error);
            });
    }

    const handleSubmit = async e => {
        const token = await loginUser({
            username,
            password
        });

        setToken(token);
        history.push('/dashboard');
    }

    return (
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