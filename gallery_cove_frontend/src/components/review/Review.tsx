import { useContext, useState } from "react";
import { ReviewDTO } from "../../common/interfaces/review"
import AppContext from "../../contexts/AppContext";
import DeletePopup from "../popup/Popup";
import { BACKEND_URL } from "../../common/utils/backend-url";

interface Props {
    review: ReviewDTO,
    setEditMode: React.Dispatch<React.SetStateAction<number>>,
    setReviews: React.Dispatch<React.SetStateAction<ReviewDTO[]>>;
}

export default function Review({
    review,
    setEditMode,
    setReviews
}: Props) {
    const { user } = useContext(AppContext);
    const [showDeletePopup, setShowDeletePopup] = useState(false);
    const imgSrc = 'data:image/png;base64,' + review.userPhoto;

    const renderStars = (numberOfStars: number) => {
        const stars = [];
        for (let i = 0; i < numberOfStars; i++) {
            stars.push(<i key={i} className='bx bxs-star'></i>);
        }
        return stars;
    };

    async function handleDelete() {
        try {
            await fetch(`${BACKEND_URL}/review/delete/${review.idReview}`, {
                method: 'DELETE',
                headers: {
                    "Content-Type": "application/json",
                },
            });
            setReviews(prev => prev.filter(r => r.idReview !== review.idReview));
        } catch (error) {
            console.log('error', error)
        }
    }

    return (
        <div
            id={`review-${review.userId}`}
            className="review-component"
        >
            <div className="user-container">
                <img className="avatar" src={imgSrc} alt='' style={{ width: '50px', height: '50px', objectFit: 'cover' }} />
                <div>
                    <p className="user">{review.userName}</p>
                    <p className="date">{review.date} at {review.time.substring(0, 5)}</p>
                </div>
            </div>
            <p className="stars">{renderStars(review.numberOfStars)}</p>
            <hr />
            <p className="description" style={{
                whiteSpace: 'preserve'
            }}>{review.description}</p>
            {review.userId === user?.userId && (
                <div className="controls">
                    <button
                        className="button"
                        onClick={() => setEditMode(review.idReview)}
                    >
                        Edit
                    </button>
                    <DeletePopup
                        show={showDeletePopup}
                        setShow={setShowDeletePopup}
                        onDelete={handleDelete}
                    >
                        <button
                            className="button"
                            onClick={() => setShowDeletePopup(true)}
                        >
                            Delete
                        </button>
                    </DeletePopup>
                </div>
            )}
            {user?.role === "ADMIN" && (
                <div className="controls">
                    <DeletePopup
                        show={showDeletePopup}
                        setShow={setShowDeletePopup}
                        onDelete={handleDelete}
                    >
                        <button
                            className="button"
                            onClick={() => setShowDeletePopup(true)}
                        >
                            Delete
                        </button>
                    </DeletePopup>
                </div>
            )}
        </div>
    )
}