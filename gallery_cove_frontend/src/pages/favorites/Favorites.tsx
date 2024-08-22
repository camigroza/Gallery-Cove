import { ChangeEvent, useContext, useEffect, useState } from "react";
import { ArtworkDTO } from "../../common/interfaces/artwork";
import Artwork from "../../components/artwork/Artwork";
import Loader from "../../components/loader/Loader";
import { getFilteredItems } from "../../common/filter";
import AppContext from "../../contexts/AppContext";
import { BACKEND_URL } from "../../common/utils/backend-url";

export default function Favorites() {
    const { user } = useContext(AppContext);
    const [artworks, setArtworks] = useState<ArtworkDTO[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [filter, setFilter] = useState<string>('');
    const filteredArtworks = getFilteredItems(artworks, filter, 'title');

    useEffect(() => {
        fetchArtworks();
    }, []);

    async function fetchArtworks() {
        try {
            const result = await fetch(`${BACKEND_URL}/favorite/getAllByUser/${user?.userId}`, {
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
            setIsLoading(false);
        }
    }

    function handleFilter(e: ChangeEvent<HTMLInputElement>) {
        const filter = e.target.value;
        setFilter(filter.toLowerCase());
    }

    return (
        <>
            <section
                className="hero-component small"
                style={{ backgroundImage: 'url("/hero.jpg")' }}
            >
                <div className="placeholder">
                    <h1 className="large">Favorite artworks</h1>
                    <div className="fixed">
                        {isLoading ? (
                            <Loader />
                        ) : (
                            <input
                                type="text"
                                name="search"
                                placeholder="Search by title"
                                autoComplete="off"
                                onChange={handleFilter}
                            />
                        )}
                    </div>
                </div>
            </section>
            <section className="container">
                <div className="card-list-component">
                    {filteredArtworks.length > 0 ? (
                        filteredArtworks.map(artwork => {
                            return (
                                <Artwork
                                    key={artwork.idArtwork}
                                    isFavorite={true}
                                    setArtworks={setArtworks}
                                    artwork={artwork}
                                />
                            )
                        })
                    ) : (
                        !isLoading && (
                            artworks.length > 0 ? (
                                <p>There are no artworks with this name.</p>
                            ) : (
                                <p>You don't have any favorite artworks yet.</p>
                            )
                        )
                    )}
                </div>
            </section>
        </>
    )
}