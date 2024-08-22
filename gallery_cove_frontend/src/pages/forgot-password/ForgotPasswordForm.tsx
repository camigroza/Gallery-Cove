import { FormEvent, useState } from "react";
import { Action } from "./ForgotPassword";
import { Status } from "../../common/types/status";
import Loader from "../../components/loader/Loader";
import { BACKEND_URL } from "../../common/utils/backend-url";

interface Props {
    email: string,
    setEmail: React.Dispatch<React.SetStateAction<string>>,
    setAction: React.Dispatch<React.SetStateAction<Action>>
}

const statusMessage = {
    idle: '',
    success: 'Mail sent successfully!',
    error: 'Something went wrong, please try again.'
}

export default function ForgotPasswordForm({
    email,
    setEmail,
    setAction
}: Props) {
    const [loading, setLoading] = useState<boolean>(false);
    const [status, setStatus] = useState<Status>('idle');
    const [errorMessage, setErrorMessage] = useState<string>('');

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setLoading(true);
        setErrorMessage('');

        if (email === '') {
            setErrorMessage('Something went wrong, please try again.');
            setLoading(false);
            return;
        }

        try {
            const response = await fetch(`${BACKEND_URL}/forgot_password/sendVerificationCode`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: email
            });

            if (!response.ok) {
                switch (response.status) {
                    case 400:
                        setErrorMessage('There is no user with this email!');
                        break;
                    default:
                        setErrorMessage('Something went wrong, please try again.');
                        break;
                }
                return;
            }

            setAction('verifyCode');
        } catch (error) {
            setStatus('error');
            setErrorMessage('Something went wrong. Please try again.');
            console.log("error", error)
        } finally {
            setLoading(false);
        }
    }

    return (
        <form onSubmit={handleSubmit}>
            <h1>Forgot Password</h1>
            <label>
                <input
                    autoFocus={true}
                    type="email"
                    name="email"
                    placeholder="Email"
                    onChange={(e) => setEmail(e.target.value)}
                    value={email}
                />
                <i className='bx bx-envelope'></i>
            </label>
            <button type="submit" disabled={loading}>
                {loading ? (
                    <Loader dark={true} />
                ) : (
                    'Send Email'
                )}
            </button>
            <p className="status-message">{errorMessage}</p>
        </form>
    )
}