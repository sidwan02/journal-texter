
function JournalTextBox(props) {

    const onChangeEvent = (event) => {
        {
        props.change(event.target.value)
        }
    }

    const TextBoxStyle = {
        height: '40px',
        width: '1000px',
        fontSize: 20,
    }

    return (
        <p>
            <input type={props.type} placeholder={props.text} style={TextBoxStyle}
                   onChange={onChangeEvent}/>
        </p>
    );
}

export default JournalTextBox;