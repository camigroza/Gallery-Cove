import { ChangeEvent, FormEvent, useContext, useState } from "react";
import AppContext from "../../contexts/AppContext";
import { useParams } from "react-router-dom";
import { ReviewDTO } from "../../common/interfaces/review";
import { BACKEND_URL } from "../../common/utils/backend-url";
import Loader from "../loader/Loader";

interface Props {
    setReviews: React.Dispatch<React.SetStateAction<ReviewDTO[]>>;
    setEditMode: React.Dispatch<React.SetStateAction<number>>;
    review?: ReviewDTO
}

interface FormData {
    description: string;
    numberOfStars: number;
}

const DEFAULT_FORM: FormData = {
    description: '',
    numberOfStars: 5
}

export default function ReviewForm({
    setReviews,
    review,
    setEditMode
}: Props) {
    const { id: idArtwork } = useParams();
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const { user } = useContext(AppContext);
    const defaultForm = review ? {
        description: review.description,
        numberOfStars: review.numberOfStars
    } : DEFAULT_FORM;
    const [form, setForm] = useState<FormData>(defaultForm);

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setIsLoading(true);

        if (!form.description) {
            form.description = '\n';
        }

        let endpointPath = 'add';
        let method = 'POST';

        if (review) {
            endpointPath = `update/${review.idReview}`
            method = 'PUT';
        }

        try {
            const result = await fetch(`${BACKEND_URL}/review/${endpointPath}`, {
                method,
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    description: form.description,
                    userId: user?.userId,
                    artworkId: idArtwork,
                    numberOfStars: form.numberOfStars
                })
            });
            const newReview: ReviewDTO = await result.json();

            if (review) {
                setReviews(prev => {
                    const tempPrev = [...prev];
                    const newIndex = tempPrev.findIndex(review => review.idReview === newReview.idReview)
                    tempPrev.splice(newIndex, 1, newReview)
                    return tempPrev;
                })
                setEditMode(0);
            } else {
                setReviews((prev) => {
                    return [newReview, ...prev];
                })
            }
        } catch (error) {
            console.log("error", error)
        } finally {
            setIsLoading(false);
        }
    }

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

    function handleAnimation(numberOfStars: number) {
        for (let i = 1; i <= numberOfStars; i++) {
            const button = document.querySelector(`#star-${i}`);
            button?.classList.add('active');
        }

        for (let i = 5; i > numberOfStars; i--) {
            const button = document.querySelector(`#star-${i}`);
            button?.classList.remove('active');
        }
    }

    return (
        <form
            onSubmit={handleSubmit}
            className="solid wider"
        >
            <h2>{review ? 'Edit' : 'Add'} Review</h2>
            <label>
                <span>
                    Description:
                </span>
                <textarea
                    onChange={(e) => handleChange(e)}
                    value={form.description}
                    name="description"
                    placeholder="Description"
                    autoComplete="off"
                />
            </label>
            <label className="stars-label">
                <span>
                    Stars:
                </span>
                <div className="stars-review-component">
                    {Array(5).fill(0).map((_, i) => {
                        return (
                            <button
                                type="button"
                                id={`star-${i + 1}`}
                                key={i}
                                className={[
                                    form.numberOfStars >= i + 1 ? "active" : ""
                                ].join(" ")}
                                onClick={(e) => {
                                    setForm(prev => {
                                        return {
                                            ...prev,
                                            numberOfStars: i + 1
                                        }
                                    })
                                }}
                                onMouseOver={() => handleAnimation(i + 1)}
                                onMouseLeave={() => handleAnimation(form.numberOfStars)}
                            >
                                <i className='bx bxs-star'></i>
                            </button>
                        )
                    })}
                </div>
            </label>
            <button type="submit" disabled={isLoading}>
                {isLoading ? (
                    <Loader dark={true} />
                ) : (
                    'Save'
                )}
            </button>
        </form>
    )
}