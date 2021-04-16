import '../css/TOS.css';

export default function TOS() {
    return (
        <div className="terms-of-service">
            <div className="terms-of-service-container">
                <h1 className="tos-header">Terms Of Service</h1>
                <h3 className="tos-section-header">Description of Service</h3>
                <p className="tos-paragraph"><strong>JournalTexter</strong> allows users to sign into unique accounts
                    and enter text in response to prompts. <strong>JournalTexter</strong> may view journal entry
                    contents;
                    however, <strong>JournalTexter</strong> is not responsible for any of the contents. Contents may
                    change
                    or be deleted at any time, and <strong>JournalTexter</strong> may use user data however it chooses.
                </p>

                <h3 className="tos-section-header">User Data</h3>
                <p className="tos-paragraph"><strong>JournalTexter</strong> stores user data in order to better the
                    user experience. <strong>JournalTexter</strong> stores users' username, password, journal entries,
                    and identifiers such as ID tags. <strong>JournalTexter</strong> does not track its users in any way
                    besides the methods mentioned above. If you would like your data to be deleted, or would like to
                    request
                    to view the data that JournalTexter has stored on you, please
                    email <strong>journaltexter@gmail.com</strong>.
                    Your request will be processed as soon as it is possible for us.
                </p>

                <h3 className="tos-section-header">Copyright</h3>
                <p className="tos-paragraph"><strong>JournalTexter</strong> was designed and built in the United
                    States. Under United States copyright law, all images and text on this screen are copyright to
                    the owner's of <strong>JournalTexter</strong>.Users may not steal any content from
                    this website, and text that they enter may be used by <strong>JournalTexter</strong> however it chooses.
                </p>

                <h3 className="tos-section-header">Termination</h3>
                <p className="tos-paragraph"><strong>JournalTexter</strong> reserves the right to terminate the account
                    of any user or of the entire site at any time. If this happens, users may be given no warning and
                    all of
                    their data may be deleted.
                </p>
            </div>
        </div>
    )
}