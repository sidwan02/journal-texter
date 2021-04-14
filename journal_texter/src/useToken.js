import { useState } from 'react';

/**
 * A hook that allows the user to save a token to the local storage.
 *
 * @returns {{setToken: saveToken, token: unknown}}
 */
export default function useToken() {
    /**
     * Gets the current token.
     *
     * @returns {string|CancelToken|*} Returns the value of the current token.
     */
    const getToken = () => {
        const tokenString = localStorage.getItem('token');
        const userToken = JSON.parse(tokenString);
        return userToken?.token
    };

    const [token, setToken] = useState(getToken());

    /**
     * Sets the current token to a user token.
     *
     * @param userToken The token taken from the given JSON.
     */
    const saveToken = userToken => {
        localStorage.setItem('token', JSON.stringify(userToken));
        setToken(userToken.token);
    };

    return {
        setToken: saveToken,
        token
    }
}