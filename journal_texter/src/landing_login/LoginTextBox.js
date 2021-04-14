/**
 * A textbox for the login and signup screens.
 *
 * @param props The type of the textbox, the placeholder text, and what to change on the statechange of the textbox.
 * @returns {JSX.Element} The textbox that the user can type in.
 * @constructor
 */
function LoginTextBox(props) {
    return (
        <div className="login-text-box">
            <input className="login-text-box-input" type={props.type} placeholder={props.text} onChange={event => props.change(event.target.value)} />
        </div>
    );
}

export default LoginTextBox;