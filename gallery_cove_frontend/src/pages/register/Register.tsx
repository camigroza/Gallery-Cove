import { ChangeEvent, FormEvent, FormEventHandler, useState } from "react"
import { useLocation, useNavigate } from "react-router-dom";
import FileInput from "../../components/form-elements/FileInput";
import Loader from "../../components/loader/Loader";
import { BACKEND_URL } from "../../common/utils/backend-url";

interface FormData {
    name: string,
    phoneNumber: string,
    email: string,
    password: string,
    confirmPassword: string,
    photo: File | null
}

const DEFAULT_FORM = {
    name: "",
    phoneNumber: "",
    email: "",
    password: "",
    confirmPassword: "",
    photo: null
}

export default function Register() {
    const navigate = useNavigate();
    const [form, setForm] = useState<FormData>(DEFAULT_FORM);
    const [errorMessage, setErrorMessage] = useState<string>('');
    const [isLoading, setIsLoading] = useState<boolean>(false);

    function handleChange(e: ChangeEvent<HTMLInputElement>) {
        const value = e.target.type === 'file' && e.target.files ? e.target.files[0] : e.target.value;
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

        if (form.phoneNumber.length != 10) {
            setErrorMessage("The phone number must be 10 digits long!");
            setIsLoading(false);
            return;
        }

        const uppercaseRegex = /[A-Z]/;
        if (!uppercaseRegex.test(form.password)) {
            setErrorMessage("The password must contain at least one uppercase character!");
            setIsLoading(false);
            return;
        }

        const digitRegex = /\d/;
        if (!digitRegex.test(form.password)) {
            setErrorMessage("The password must contain at least one digit!");
            setIsLoading(false);
            return;
        }

        const specialCharRegex = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/;
        if (!specialCharRegex.test(form.password)) {
            setErrorMessage("The password must contain at least one special character!");
            setIsLoading(false);
            return;
        }

        if (!form.photo) {
            setErrorMessage("Please choose a profile photo!");
            setIsLoading(false);
            return;
        }

        const formData = new FormData();
        formData.append('name', form.name);
        formData.append('phoneNumber', form.phoneNumber);
        formData.append('email', form.email);
        formData.append('password', form.password);
        formData.append('confirmPassword', form.confirmPassword);
        if (form.photo) {
            formData.append('photo', form.photo);
        }

        try {
            const result = await fetch(`${BACKEND_URL}/api/auth/register`, {
                method: "POST",
                body: formData
            });

            if (!result.ok) {
                const errorText = await result.text();
                switch (result.status) {
                    case 400:
                        setErrorMessage(errorText);
                        break;
                    default:
                        setErrorMessage('Something went wrong, please try again.');
                        break;
                }
                return;
            }

            navigate("/login");
        } catch (error) {
            setErrorMessage('Something went wrong. Please try again.');
            console.log("error")
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
                <h1>Register</h1>
                <label>
                    <input
                        onChange={(e) => handleChange(e)}
                        value={form.name}
                        type="text"
                        name="name"
                        placeholder="Name"
                        autoComplete="off"
                        required
                    />
                    <i className='bx bx-user' ></i>
                </label>
                <label>
                    <input
                        onChange={(e) => handleChange(e)}
                        value={form.phoneNumber}
                        type="text"
                        name="phoneNumber"
                        placeholder="Phone Number"
                        autoComplete="off"
                        required
                    />
                    <i className='bx bx-phone' ></i>
                </label>
                <hr />
                <label>
                    <input
                        onChange={(e) => handleChange(e)}
                        value={form.email}
                        type="email"
                        name="email"
                        placeholder="Email"
                        autoComplete="off"
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
                        autoComplete="off"
                        required
                    />
                    <i className='bx bx-lock-open-alt' ></i>
                </label>
                <label>
                    <input
                        onChange={(e) => handleChange(e)}
                        value={form.confirmPassword}
                        type="password"
                        name="confirmPassword"
                        placeholder="Confirm password"
                        autoComplete="off"
                        required
                    />
                    <i className='bx bx-lock-alt' ></i>
                </label>
                <hr />
                <FileInput
                    buttonTitle="Profile photo: "
                    name="photo"
                    multiple={false}
                    onChange={handleChange}
                    images={[form.photo] as File[]}
                />
                <button type="submit" disabled={isLoading}>
                    {isLoading ? (
                        <Loader dark={true} />
                    ) : (
                        'Register'
                    )}
                </button>
                <p className="status-message">{errorMessage}</p>
            </form>
        </section>
    )
}