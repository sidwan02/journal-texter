import React, {useState} from 'react';
import PropTypes from 'prop-types';
import axios from "axios";
import {Link, useHistory} from "react-router-dom";
import LoginTextBox from "./LoginTextBox";
import '../css/LoginPage.css';

/**
 * This is the component where the user is able to login JournalTexter.
 *
 * @param setToken A method to set the token to the user's unique login id.
 * @returns {JSX.Element} The user login element.
 * @constructor
 */
export default function LoginPage({setToken}) {
    const [username, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const history = useHistory();

    /**
     * Sends a request to the backend to try and login the user.
     *
     * @returns {Promise<AxiosResponse<any>>} When the method resolves.
     */
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
            .catch(error => {
                console.log(error.response);
                setError(error.response.data);
                return null;
            });
    }

    /**
     * Handles the user submitting the form.
     *
     * @returns {Promise<void>} Assigns when the token is assigned to the user.
     */
    const handleSubmit = async () => {
        if (username !== "") {
            const token = await loginUser();

            if (token !== null) {
                assignToken(token);
            }
        } else {
            setError("Please enter username and password");
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
                    <p className="error-text">{error}</p>
                    <button className="submit" type="submit" onClick={handleSubmit}>Submit</button>
                </div>
                <div className="sign-up">
                    <Link className="sign-up-link link" to="/signup">Sign Up!</Link>
                </div>
            </div>
        </div>
    )
}

LoginPage.propTypes = {
    setToken: PropTypes.func.isRequired
};
