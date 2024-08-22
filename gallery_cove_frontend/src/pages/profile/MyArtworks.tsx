import { useContext, useEffect, useState } from "react";
import AppContext from "../../contexts/AppContext";
import { ArtworkDTO } from "../../common/interfaces/artwork";
import Artwork from "../../components/artwork/Artwork";
import { HashLink } from "react-router-hash-link";
import Loader from "../../components/loader/Loader";
import { BACKEND_URL } from "../../common/utils/backend-url";

export default function MyArtworks() {
    const { user } = useContext(AppContext);
    const [artworks, setArtworks] = useState<ArtworkDTO[]>([]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        fetchArtworks()
    }, []);

    async function fetchArtworks() {
        setIsLoading(true);

        try {
            const result = await fetch(`${BACKEND_URL}/artwork/getAllByUser/${user?.userId}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            });
            const data: ArtworkDTO[] = await result.json();
            setArtworks(data);
        } catch (error) {
            console.log("error")
        } finally {
            setIsLoading(false)
        }
    }

    return (
        <section
            id="myArtworks"
            className="container"
        >
            <div className="profile-list">
                <HashLink
                    smooth
                    to='/profile/addArtwork#addArtwork'
                    className="button dark"
                >
                    Add Artwork
                </HashLink>
            </div>
            <br />
            <br />
            {isLoading && (
                <div className="loader-container">
                    <Loader dark />
                </div>
            )}
            <div className="card-list-component">
                {artworks.length > 0 ? (
                    artworks.map(artwork => {
                        return (
                            <Artwork
                                key={artwork.idArtwork}
                                artwork={artwork}
                                setArtworks={setArtworks}
                            />
                        )
                    })
                ) : (
                    !isLoading && (
                        <p>You have not added an artwork yet.</p>
                    )
                )}
            </div>
        </section>
    )
}