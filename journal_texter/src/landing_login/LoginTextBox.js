function LoginTextBox(props) {
    return (
        <p>
            <input type={props.type} placeholder={props.text} onChange={event => props.change(event.target.value)} />
        </p>
    );
}

export default LoginTextBox;