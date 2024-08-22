import { FormEvent, useState } from "react";
import Loader from "../../components/loader/Loader";
import { Status } from "../../common/types/status";
import { BACKEND_URL } from "../../common/utils/backend-url";

const statusMessage = {
    idle: '',
    success: 'Mail sent successfully!',
    error: 'Something went wrong, please try again.'
}

export default function Contact() {
    const [message, setMessage] = useState<string>('');
    const [loading, setLoading] = useState<boolean>(false);
    const [status, setStatus] = useState<Status>('idle');

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();

        setLoading(true);

        try {
            const response = await fetch(`${BACKEND_URL}/user/sendEmailToAdmin`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: message
            });

            if (!response.ok) {
                setStatus('error');
                throw new Error('HTTP error, status = ' + response.status);
            }

            setMessage('');
            setStatus('success')
        } catch (error) {
            setStatus('error');
            console.log("error", error)
        } finally {
            setLoading(false);
        }
    }

    return (

        <section
            className="hero-component"
            style={{ backgroundImage: 'url("/hero.jpg")' }}
        >
            <form
                onSubmit={handleSubmit}
                className="large"
            >
                <h1>Send us a message</h1>
                <textarea
                    value={message}
                    placeholder="Tell us your feedback"
                    onChange={(e) => setMessage(e.target.value)}
                />
                <small>This message will be sent anonymously.</small>
                <small>If you wish to contact us directly, please do so via email, at <a target="_blank" href={`mailto:gallerycove@gmail.com`}>gallerycove@gmail.com</a></small>
                <button disabled={loading}>
                    {loading ? (
                        <Loader dark={true} />
                    ) : (
                        'Send'
                    )}
                </button>
                <p className="status-message">{statusMessage[status]}</p>
            </form>
        </section>
    )
}