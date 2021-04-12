function LoginTextBox(props) {
    return (
        <div className="text-box">
            <input type={props.type} placeholder={props.text} onChange={event => props.change(event.target.value)} />
        </div>
    );
}

export default LoginTextBox;