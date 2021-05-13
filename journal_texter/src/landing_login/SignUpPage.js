import React, {useState} from 'react';
import PropTypes from 'prop-types';
import axios from "axios";
import {Link, useHistory} from "react-router-dom";
import LoginTextBox from "./LoginTextBox";
import '../css/LoginPage.css';

/**
 * This is the component where the user is able to sign up for JournalTexter.
 *
 * @param setToken A method to set the token to the user's unique login id.
 * @returns {JSX.Element} The user sign up element.
 * @constructor
 */
export default function SignUpPage({setToken}) {
    const [username, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [agreeToTOS, setAgreeToTOS] = useState(false);
    const [error, setError] = useState("");
    const history = useHistory();

    /**
     * Signs up the user in the database, or throws an error.
     *
     * @returns {Promise<AxiosResponse<any>>} The promise returns if the server returns data.
     */
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


        const API_URL = "https://journaltexter-api.herokuapp.com/"

        return axios.post(
            API_URL + "signup",
            // "http://localhost:4567/signup",
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
     * Method to handle the user submitting the form.
     *
     * @returns {Promise<void>} Returns when the token is correctly assigned.
     */
    const handleSubmit = async () => {
        if (password !== confirmPassword) {
            setError("Passwords do not match");
        } else if (username === "") {
            setError("Username cannot be blank.");
        } else if (!document.getElementById("tos-checkbox").checked) {
            setError("You must agree to the Terms of Service");
        } else {
            const token = await signUpUser();

            if (token !== null) {
                assignToken(token);
            }
        }
    }

    /**
     * Sets the token and goes to the dashboard.
     *
     * @param token The user's unique token id.
     */
    function assignToken(token) {
        setToken(token);
        history.push('/');
    }

    return (
        <div className="login-page">
            <div className="login-box">
                <div className="login-box-title">
                    <Link className="login-box-home-link login-box-link noselect" to="/">JournalTexter</Link>
                </div>
                <div className="login-form">
                    <LoginTextBox text="Username" change={setUserName} type="text"/>
                    <LoginTextBox text="Password" change={setPassword} type="password"/>
                    <LoginTextBox text="Confirm Password" change={setConfirmPassword} type="password"/>
                    <div className="tos-agreement-container">
                        <input type="checkbox" id="tos-checkbox"/>
                        <label htmlFor="tos-checkbox" className="tos-checkbox-label">
                            Agree to the <a href="/tos" target='_blank' className="link-to-tos">Terms of Service</a>
                        </label>
                    </div>
                    <p className="login-form-error-text">{error}</p>
                    <button className="login-page-submit" type="submit" onClick={handleSubmit}>Submit</button>
                </div>
            </div>
        </div>
    )
}

/**
 * Checks the types of the props.
 *
 * @type {{setToken: Validator<NonNullable<(...args: any[]) => any>>}} Requires that the type of setToken
 * is a function.
 */
SignUpPage.propTypes = {
    setToken: PropTypes.func.isRequired
};