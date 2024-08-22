import { MouseEvent, useContext, useState } from "react";
import { ArtworkDTO } from "../../common/interfaces/artwork"
import AppContext from "../../contexts/AppContext";
import { useLocation, useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import Carousel from "../carousel/Carousel";
import Loader from "../loader/Loader";
import { preventDefaultClick } from "../../common/utils/preventDefaultClick";
import DeletePopup from "../popup/Popup";
import { categoryColors } from "../../common/utils/categoryColors";
import { BACKEND_URL } from "../../common/utils/backend-url";

interface Props {
    artwork: ArtworkDTO,
    isFavorite?: boolean,
    setArtworks: React.Dispatch<React.SetStateAction<ArtworkDTO[]>>
    userRole?: string
}

export const FAVORITE_ICON = {
    full: 'bxs-heart',
    empty: 'bx-heart'
}

export default function Artwork({ artwork, isFavorite, setArtworks, userRole }: Props) {
    const location = useLocation();
    const navigate = useNavigate();
    const { categoryName, description, price, userName, idArtwork, date, idUser, photos, title } = artwork;
    const { user } = useContext(AppContext);
    const [favoriteIcon, setFavoriteIcon] = useState(isFavorite ? FAVORITE_ICON.full : FAVORITE_ICON.empty);
    const [isLoadingFavorite, setIsLoadingFavorite] = useState<boolean>(false);
    const [showDeletePopup, setShowDeletePopup] = useState(false);
    const isMine = artwork.idUser === user?.userId;
    const isMyArtworksPage = location.pathname.includes("profile/artworks");
    const isAdmin = userRole === "ADMIN";

    async function handleFavorite(e: MouseEvent<Element>) {
        e.preventDefault();
        e.stopPropagation();

        setIsLoadingFavorite(true);

        let action = 'add';
        let method = 'POST';

        if (isFavorite) {
            action = 'delete';
            method = 'DELETE';
        }

        try {
            await fetch(`${BACKEND_URL}/favorite/${action}`, {
                method: method,
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    userId: user?.userId,
                    artworkId: idArtwork
                })
            });

            if (isFavorite) {
                setArtworks(prev => prev.filter(artwork => artwork.idArtwork !== idArtwork));
            } else {
                setArtworks(prev => {
                    const tempArtworks = [...prev];
                    tempArtworks.push({
                        categoryName,
                        description,
                        title,
                        price,
                        userName,
                        idArtwork,
                        date,
                        idUser,
                        photos
                    })

                    return tempArtworks;
                })
            }
        } catch (error) {
            console.log("error", error)
        } finally {
            setIsLoadingFavorite(false);
        }
    }

    async function handleDelete(e: MouseEvent<Element>) {
        e.preventDefault();
        e.stopPropagation();

        try {
            await fetch(`${BACKEND_URL}/artwork/delete/${idArtwork}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                },
            });

            setArtworks(prev => prev.filter(artwork => artwork.idArtwork !== idArtwork));
        } catch (error) {
            console.log("error", error)
        }
    }

    function handleHover(isFavorite: boolean) {
        if (isLoadingFavorite) return;
        if (isFavorite) {
            setFavoriteIcon(FAVORITE_ICON.empty);
        } else {
            setFavoriteIcon(FAVORITE_ICON.full);
        }
    }

    function handleRedirect(e: MouseEvent<Element>, url: string) {
        preventDefaultClick(e);
        navigate(url);
    }

    function handleShowDeletePopup(e: MouseEvent) {
        preventDefaultClick(e);
        setShowDeletePopup(true);
    }

    return (
        <Link to={`/artworks/${idArtwork}`}>
            <article className="card-component">
                <header>
                    <Carousel
                        photos={photos}
                        title={title}
                    />
                </header>
                <footer>
                    <div className="title">
                        <div className="description">{title}</div>
                        <div
                            className="chip-component spaced"
                            style={{
                                background: categoryColors[categoryName.split(" ").join("") as keyof typeof categoryColors]
                            }}
                            role="button"
                            tabIndex={0}
                            onClick={(e) => handleRedirect(e, `/artworks?category=${categoryName}`)}
                        >
                            {categoryName.toUpperCase()}
                        </div>
                        <div>
                            Made by:&nbsp;
                            <span
                                className="artist"
                                role="button"
                                tabIndex={0}
                                onClick={(e) => handleRedirect(e, `/artists/${idUser}`)}
                            >
                                {userName}
                            </span>
                        </div>
                    </div>
                    <div className="price">{price} <sup>RON</sup> </div>
                    <div className="controls">
                        {user && (
                            <>
                                {isAdmin ? (
                                    <DeletePopup
                                        onDelete={(e) => handleDelete(e)}
                                        show={showDeletePopup}
                                        setShow={setShowDeletePopup}
                                    >
                                        <div
                                            className="button"
                                            role="button"
                                            tabIndex={0}
                                            onClick={handleShowDeletePopup}
                                        >
                                            Delete
                                        </div>
                                    </DeletePopup>
                                ) : (
                                    <>
                                        {isMyArtworksPage ? (
                                            <>
                                                <div
                                                    className="button"
                                                    role="button"
                                                    tabIndex={0}
                                                    onClick={(e) => handleRedirect(e, `/profile/editArtwork/${idArtwork}`)}
                                                >
                                                    Edit
                                                </div>
                                                <DeletePopup
                                                    onDelete={(e) => handleDelete(e)}
                                                    show={showDeletePopup}
                                                    setShow={setShowDeletePopup}
                                                >
                                                    <div
                                                        className="button"
                                                        role="button"
                                                        tabIndex={0}
                                                        onClick={handleShowDeletePopup}
                                                    >
                                                        Delete
                                                    </div>
                                                </DeletePopup>

                                            </>
                                        ) : (
                                            isMine ? (
                                                null
                                            ) : (
                                                <div
                                                    className="button"
                                                    role="button"
                                                    tabIndex={0}
                                                    onClick={handleFavorite}
                                                    onMouseOver={() => handleHover(Boolean(isFavorite))}
                                                    onMouseOut={() => handleHover(!isFavorite)}
                                                >
                                                    {isLoadingFavorite ? (
                                                        <Loader dark />
                                                    ) : (
                                                        isFavorite ? (
                                                            <i className={[
                                                                'bx',
                                                                favoriteIcon,
                                                            ].join(' ')}></i>

                                                        ) : (
                                                            <i className={[
                                                                'bx',
                                                                favoriteIcon,
                                                            ].join(' ')}></i>
                                                        )
                                                    )}
                                                </div>
                                            )
                                        )}
                                    </>
                                )}
                            </>
                        )}
                    </div>
                </footer>
            </article>
        </Link>
    )
}