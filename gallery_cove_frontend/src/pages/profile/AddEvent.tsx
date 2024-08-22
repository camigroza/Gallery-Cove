import { ChangeEvent, FormEvent, useContext, useEffect, useState } from "react";
import AppContext from "../../contexts/AppContext";
import { useNavigate, useParams } from "react-router-dom";
import { EventDTO } from "../../common/interfaces/event";
import EventLinks from "./EventLinks";
import Loader from "../../components/loader/Loader";
import { BACKEND_URL } from "../../common/utils/backend-url";

interface FormData {
    description_mail: string;
    date: string;
    city: string;
    street: string;
    number: string;
    title: string;
    time: string;
}


const DEFAULT_FORM: FormData = {
    description_mail: '',
    date: '',
    city: '',
    street: '',
    number: '',
    title: '',
    time: ''
}

export default function AddEvent() {
    const { id } = useParams();
    const navigate = useNavigate();
    const { user } = useContext(AppContext);
    const [form, setForm] = useState<FormData>(DEFAULT_FORM);
    const [isLoading, setIsLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState<string>('');

    useEffect(() => {
        fetchEvent();
    }, [])

    function handleChange(e: ChangeEvent<HTMLInputElement> | ChangeEvent<HTMLTextAreaElement>) {
        const value = e.target.value;
        const name = e.target.name;

        setForm(prev => {
            return {
                ...prev,
                [name]: value
            }
        })
    }

    async function fetchEvent() {
        try {
            const result = await fetch(`${BACKEND_URL}/event/get/${id}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            });
            const data: EventDTO = await result.json();
            const [city, street, number] = data.location.split(",");
            const formData: FormData = {
                description_mail: data.descriptionMail,
                date: data.date,
                city: city,
                street: street,
                number: number,
                title: data.title,
                time: data.time
            }
            setForm(formData);
        } catch (error) {
            console.log("error")
        }
    }

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setErrorMessage('');
        setIsLoading(true);

        let endpointPath = 'add';
        let method = 'POST';

        if (id) {
            endpointPath = `update/${id}`
            method = 'PUT';
        }

        try {
            const result = await fetch(`${BACKEND_URL}/event/${endpointPath}`, {
                method,
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    userId: user?.userId,
                    descriptionMail: form.description_mail,
                    date: form.date,
                    location: form.city + ", " + form.street + ", " + form.number,
                    title: form.title,
                    time: form.time
                })
            });

            if (!result.ok) {
                setErrorMessage('Something went wrong, please try again.');
                return;
            }

            navigate('/profile/events#myEvents');
        } catch (error) {
            setErrorMessage('Something went wrong, please try again.');
            console.log("error", error)
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <section className="container" id="addEvent">
            <EventLinks />
            <form
                onSubmit={handleSubmit}
                className="solid"
            >
                <h2>{id ? "Edit" : "Add"} Event</h2>
                <label>
                    Title:
                    <input
                        onChange={(e) => handleChange(e)}
                        value={form.title}
                        type="text"
                        name="title"
                        placeholder="Title"
                        autoComplete="off"
                        required
                    />
                </label>
                <label>
                    Description:
                    <textarea
                        onChange={(e) => handleChange(e)}
                        value={form.description_mail}
                        name="description_mail"
                        placeholder="Description"
                        autoComplete="off"
                        required
                    />
                </label>
                <div className="label-container">
                    <label>
                        City:
                        <input
                            onChange={(e) => handleChange(e)}
                            value={form.city}
                            type="text"
                            name="city"
                            placeholder="City"
                            autoComplete="off"
                            required
                        />
                    </label>
                    <label>
                        Street:
                        <input
                            onChange={(e) => handleChange(e)}
                            value={form.street}
                            type="text"
                            name="street"
                            placeholder="Street"
                            autoComplete="off"
                            required
                        />
                    </label>
                    <label>
                        Number:
                        <input
                            onChange={(e) => handleChange(e)}
                            value={form.number}
                            type="text"
                            name="number"
                            placeholder="Number"
                            autoComplete="off"
                            required
                        />
                    </label>
                </div>
                <div className="label-container">
                    <label>
                        Date:
                        <input
                            onChange={(e) => handleChange(e)}
                            value={form.date}
                            type="date"
                            name="date"
                            required
                        />
                        <i className='bx bx-calendar'></i>
                    </label>
                    <label>
                        Time:
                        <input
                            onChange={(e) => handleChange(e)}
                            value={form.time}
                            type="time"
                            name="time"
                            required
                        />
                        <i className='bx bx-time-five'></i>
                    </label>
                </div>
                <button type="submit" disabled={isLoading}>
                    {isLoading ? (
                        <Loader dark={true} />
                    ) : (
                        'Save'
                    )}
                </button>
                <p className="status-message">{errorMessage}</p>
            </form>
        </section>
    )
}