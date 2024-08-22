import { jwtDecode } from "jwt-decode";
import { ChangeEvent, FormEvent, useContext, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import AppContext, { User } from "../../contexts/AppContext";
import { BACKEND_URL } from "../../common/utils/backend-url";
import Loader from "../../components/loader/Loader";

interface FormData {
    email: string,
    password: string,
}

const DEFAULT_FORM = {
    email: "",
    password: "",
}

export default function Login() {
    const navigate = useNavigate();
    const { setUser } = useContext(AppContext);
    const [form, setForm] = useState<FormData>(DEFAULT_FORM);
    const [remember, setRemember] = useState<boolean>(false);
    const [errorMessage, setErrorMessage] = useState<string>('');
    const [isLoading, setIsLoading] = useState<boolean>(false);

    function handleChange(e: ChangeEvent<HTMLInputElement>) {
        const value = e.target.value;
        const name = e.target.name;

        setForm(prev => {
            return {
                ...prev,
                [name]: value
            }
        })
    }

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setErrorMessage('');
        setIsLoading(true);

        try {
            const result = await fetch(`${BACKEND_URL}/api/auth/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: form.email,
                    password: form.password,
                })
            });

            if (!result.ok) {
                switch (result.status) {
                    case 401:
                        setErrorMessage('The password is incorrect!');
                        break;
                    case 404:
                        setErrorMessage('There is no user with this email!');
                        break;
                    default:
                        setErrorMessage('Something went wrong, please try again.');
                        break;
                }
                return;
            }

            const token = await result.text();

            if (remember) {
                window.localStorage.setItem('token', token);
            }

            const user: User = jwtDecode(token);
            setUser(user)
            navigate("/");
        } catch (error) {
            setErrorMessage('Something went wrong. Please try again.');
            console.log("error", error)
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <section
            className="hero-component"
            style={{ backgroundImage: 'url("/hero.jpg")' }}
        >
            <form onSubmit={handleSubmit}>
                <h1>Login</h1>
                <label>
                    <input
                        onChange={(e) => handleChange(e)}
                        value={form.email}
                        type="email"
                        name="email"
                        placeholder="Email"
                        required
                    />
                    <i className='bx bx-envelope'></i>
                </label>
                <label>
                    <input
                        onChange={(e) => handleChange(e)}
                        value={form.password}
                        type="password"
                        name="password"
                        placeholder="Password"
                        required
                    />
                    <i className='bx bx-lock-alt' ></i>
                </label>
                <div className="form-row">
                    <label>
                        <input
                            type="checkbox"
                            name="remember"
                            onChange={() => setRemember(prev => !prev)}
                        />
                        <span>Remember me</span>
                    </label>
                    <Link to='/forgot-password'>Forgot password</Link>
                </div>
                <button type="submit" disabled={isLoading}>
                    {isLoading ? (
                        <Loader dark={true} />
                    ) : (
                        'Login'
                    )}
                </button>
                <p className="status-message">{errorMessage}</p>
            </form>
        </section>
    )
}