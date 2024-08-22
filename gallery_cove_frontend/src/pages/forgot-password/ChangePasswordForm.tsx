import { FormEvent, useState } from "react";
import { Action } from "./ForgotPassword";
import { useNavigate } from "react-router-dom";
import { BACKEND_URL } from "../../common/utils/backend-url";
import Loader from "../../components/loader/Loader";

interface Props {
    email: string,
    setAction: React.Dispatch<React.SetStateAction<Action>>
}

export default function ChangePasswordForm({
    email,
    setAction
}: Props) {
    const navigate = useNavigate();
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState<string>('');
    const [isLoading, setIsLoading] = useState<boolean>(false);

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setErrorMessage('');
        setIsLoading(true);

        try {
            const response = await fetch(`${BACKEND_URL}/user/updatePassword`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email,
                    password,
                    confirmPassword
                })
            });

            if (!response.ok) {
                const errorText = await response.text();
                switch (response.status) {
                    case 400:
                        setErrorMessage(errorText);
                        break;
                    default:
                        setErrorMessage('Something went wrong, please try again.');
                        break;
                }
                return;
            }

            navigate('/login')
        } catch (error) {
            setErrorMessage('Something went wrong. Please try again.');
            console.log("error", error)
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <form onSubmit={handleSubmit}>
            <h1>Change Password</h1>
            <label>
                <input
                    autoFocus={true}
                    type="password"
                    name="password"
                    placeholder="New password"
                    autoComplete="off"
                    onChange={(e) => setPassword(e.target.value)}
                    value={password}
                    required
                />
                <i className='bx bx-lock-open-alt' ></i>
            </label>
            <label>
                <input
                    type="password"
                    name="confirmPassword"
                    placeholder="Confirm new password"
                    autoComplete="off"
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    value={confirmPassword}
                    required
                />
                <i className='bx bx-lock-alt' ></i>
            </label>
            <button type="submit" disabled={isLoading}>
            {isLoading ? (
                        <Loader dark={true} />
                    ) : (
                        'Update password'
                    )}
                </button>
            <p className="status-message">{errorMessage}</p>
        </form>
    )
}