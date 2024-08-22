import { useContext, useState } from "react";
import { ArtworkDTO } from "../../common/interfaces/artwork";
import { ReviewDTO } from "../../common/interfaces/review";
import Review from "../../components/review/Review";
import ReviewForm from "../../components/review/ReviewForm";
import AppContext from "../../contexts/AppContext";
import { HashLink } from "react-router-hash-link";

interface Props {
    artwork: ArtworkDTO;
    reviews: ReviewDTO[];
    setReviews: React.Dispatch<React.SetStateAction<ReviewDTO[]>>
}

export default function ArtworkReviews({ artwork, reviews, setReviews }: Props) {
    const { user } = useContext(AppContext);
    const [editMode, setEditMode] = useState(0);
    const isReviewed = Boolean(reviews.find(review => review.userId === user?.userId));

    return (
        <>
            <h2 className="text-center">Reviews:</h2>
            {user && user.userId !== artwork.idUser && user.role !== "ADMIN" && (
                !isReviewed || editMode ? (
                    <ReviewForm 
                        setReviews={setReviews} 
                        review={reviews.find(review => review.idReview === editMode)}
                        setEditMode={setEditMode}
                    />
                ) : (
                    <p className="text-center">
                        You've already reviewed this artwork. Go to <HashLink smooth className="link" to={`#review-${user.userId}`}>your review</HashLink>
                    </p>
                )
            )}
            <br />
            <br />
            <br />
            <br />
            {reviews.length > 0 ? (
                <div className="card-list-component">
                    {reviews.map((review) => {
                        return (
                            <Review
                                review={review}
                                key={review.idReview}
                                setEditMode={setEditMode}
                                setReviews={setReviews}
                            />
                        )
                    })}
                </div>
            ) : (
                <p>No reviews for this artwork yet.</p>
            )}
        </>
    )
}
