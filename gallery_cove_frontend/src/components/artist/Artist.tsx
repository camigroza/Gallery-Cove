import { MouseEvent, useState } from "react";
import { Link } from "react-router-dom";
import { ArtistDTO } from "../../common/interfaces/artist";
import DeletePopup from "../popup/Popup";
import { preventDefaultClick } from "../../common/utils/preventDefaultClick";
import { BACKEND_URL } from "../../common/utils/backend-url";

interface Props {
    artist: ArtistDTO;
    userRole?: string;
    setArtists: React.Dispatch<React.SetStateAction<ArtistDTO[]>>
}

export default function Artist({ artist, userRole, setArtists }: Props) {
    const {
        idUser,
        name,
        email,
        photo,
        role
    } = artist;
    const imgSrc = 'data:image/png;base64,' + photo;

    const isAdmin = userRole === "ADMIN";
    const [showDeletePopup, setShowDeletePopup] = useState(false);

    async function handleDelete(e: MouseEvent<Element>) {
        e.preventDefault();
        e.stopPropagation();

        try {
            await fetch(`${BACKEND_URL}/user/delete/${idUser}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            setArtists(prev => prev.filter(artist => {
                return artist.idUser !== idUser
            }));
        } catch (error) {
            console.log("error", error)
        }
    }

    function handleShowDeletePopup(e: MouseEvent) {
        preventDefaultClick(e);
        setShowDeletePopup(true);
    }

    return (
        <Link to={`/${role}s/${idUser}`}>
            <article className="card-component">
                <header>
                    <img
                        src={imgSrc}
                        alt={idUser.toString()}
                    />
                </header>
                <footer>
                    <div className="title">
                        <div className="description">{name}</div>
                        <div className="chip-component">{email}</div>
                    </div>
                    <div className="controls">
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
                        ) : null
                        }
                    </div>
                </footer>
            </article>
        </Link>
    )
}