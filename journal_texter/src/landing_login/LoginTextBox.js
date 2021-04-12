function LoginTextBox(props) {
    return (
        <div className="login-text-box">
            <input className="login-text-box-input" type={props.type} placeholder={props.text} onChange={event => props.change(event.target.value)} />
        </div>
    );
}

export default LoginTextBox;