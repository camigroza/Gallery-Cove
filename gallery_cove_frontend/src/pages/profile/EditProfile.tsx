import { ChangeEvent, FormEvent, useContext, useState } from "react";
import AppContext from "../../contexts/AppContext";
import DeletePopup from "../../components/popup/Popup";
import FileInput from "../../components/form-elements/FileInput";
import { useNavigate } from "react-router-dom";
import Loader from "../../components/loader/Loader";
import { BACKEND_URL } from "../../common/utils/backend-url";

interface FormData {
    name: string,
    phoneNumber: string,
    photo: string | File | null
}

interface Data {
    token: string;
}

const DEFAULT_FORM: FormData = {
    name: '',
    phoneNumber: '',
    photo: ''
}

export default function EditProfile() {
    const [errorMessage, setErrorMessage] = useState<string>('');
    const navigate = useNavigate();
    const { user, setToken, logout } = useContext(AppContext);
    const formData: FormData = user ? {
        name: user.name,
        phoneNumber: user.phoneNumber,
        photo: user.photo
    } : DEFAULT_FORM;
    const [form, setForm] = useState<FormData>(formData);
    const [showDeletePopup, setShowDeletePopup] = useState(false);
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

    async function handleDeleteAccount() {
        setIsLoading(true);

        try {
            await fetch(`${BACKEND_URL}/user/delete/${user?.userId}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            logout();
        } catch (error) {
            console.log("error", error)
        } finally {
            setIsLoading(false);
        }
    }

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setErrorMessage('');
        setIsLoading(true);

        const formData = new FormData();
        formData.append('name', form.name);
        formData.append('phoneNumber', form.phoneNumber);

        if (form.photo instanceof File) {
            formData.append('photo', form.photo);
        }

        try {
            const response = await fetch(`${BACKEND_URL}/user/updateProfile/${user?.userId}`, {
                method: "PATCH",
                body: formData
            });

            if (!response.ok) {
                setErrorMessage('Something went wrong, please try again.');
                return;
            }

            const data: Data = await response.json();
            const token = data.token;
            setToken(token);
            navigate('/profile');
        } catch (error) {
            setErrorMessage('Something went wrong, please try again.');
            console.log("error", error)
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <section className="container" id="editProfile">
            <form
                onSubmit={handleSubmit}
                className="solid"
            >
                <h2>Edit Profile</h2>
                <label>
                    Name:
                    <input
                        onChange={(e) => handleChange(e)}
                        value={form.name}
                        type="text"
                        name="name"
                        placeholder="Name"
                        autoComplete="off"
                    />
                </label>
                <label>
                    Phone Number:
                    <input
                        onChange={(e) => handleChange(e)}
                        value={form.phoneNumber}
                        type="text"
                        name="phoneNumber"
                        placeholder="Phone Number"
                        autoComplete="off"
                    />
                </label>
                <FileInput
                    labelTitle="Profile photo:"
                    buttonTitle="Select photo: "
                    name="photo"
                    multiple={false}
                    onChange={handleChange}
                    images={[form.photo] as string[]}
                />
                <button type="submit" disabled={isLoading}>
                {isLoading ? (
                        <Loader dark={true} />
                    ) : (
                        'Save'
                    )}
                    </button>
                <DeletePopup
                    show={showDeletePopup}
                    setShow={setShowDeletePopup}
                    onDelete={handleDeleteAccount}
                >
                    <button
                        type="button"
                        className="danger"
                        onClick={() => setShowDeletePopup(true)}
                    >
                        Delete account
                    </button>
                </DeletePopup>
                <p className="status-message">{errorMessage}</p>
            </form>
        </section>
    )
}