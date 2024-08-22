import { ChangeEvent, useContext, useEffect, useState } from "react";
import { ArtistDTO } from "../../common/interfaces/artist";
import Artist from "../../components/artist/Artist";
import Loader from "../../components/loader/Loader";
import { getFilteredItems } from "../../common/filter";
import { useParams } from "react-router-dom";
import { ArtworkDTO } from "../../common/interfaces/artwork";
import Artwork from "../../components/artwork/Artwork";
import AppContext from "../../contexts/AppContext";
import { BACKEND_URL } from "../../common/utils/backend-url";

export default function Visitors() {
    const { user } = useContext(AppContext);
    const [visitors, setVisitors] = useState<ArtistDTO[]>([]);
    const [visitor, setVisitor] = useState<ArtistDTO>();
    const [artworks, setArtworks] = useState<ArtworkDTO[]>([]);
    const [favoriteArtworks, setFavoriteArtworks] = useState<ArtworkDTO[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [filter, setFilter] = useState<string>('');
    const filteredArtists = getFilteredItems(visitors, filter, 'name');
    const filteredArtworks = getFilteredItems(artworks, filter, 'title');
    const { id } = useParams();
    const favoriteIds = favoriteArtworks.map(artwork => artwork.idArtwork);
    const imgSrc = 'data:image/png;base64,' + visitor?.photo;

    useEffect(() => {
        async function fetchData() {
            if (id) {
                await fetchVisitor();
            } else {
                await fetchVisitors();
            }
        }
        fetchData()
    }, [id]);

    async function fetchVisitors() {
        setIsLoading(true);
        try {
            const result = await fetch(`${BACKEND_URL}/user/getAllVisitors`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            });
            const data: ArtistDTO[] = await result.json();
            setVisitors(data);
        } catch (error) {
            console.log("error")
        } finally {
            setIsLoading(false);
        }
    }

    async function fetchVisitor() {
        setIsLoading(true);
        try {
            const result = await fetch(`${BACKEND_URL}/user/get/${id}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            });
            const data: ArtistDTO = await result.json();
            setVisitor(data);
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
                className={[
                    "hero-component",
                    id ? "medium" : "small"
                ].join(" ")}
                style={{ backgroundImage: 'url("/hero.jpg")' }}
            >
                <div className="placeholder">
                    {id && <img className="avatar" src={imgSrc} alt={visitor?.name} style={{ objectFit: 'cover' }} />}
                    <h1 className="large">{id ? visitor?.name : 'Visitors'}</h1>
                    {id && (
                        <div className="details">
                            <a target="_blank" href={`mailto:${visitor?.email}`}>
                                <i className='bx bx-envelope'></i>
                                {visitor?.email}
                            </a>
                            <a target="_blank" href={`tel:${visitor?.phoneNumber}`}>
                                <i className='bx bx-phone' ></i>
                                {visitor?.phoneNumber}
                            </a>
                        </div>
                    )}
                    <div className="fixed">
                        {isLoading ? (
                            <Loader />
                        ) : (
                            <input
                                type="text"
                                name="search"
                                placeholder={id ? "Search artworks" : "Search by name"}
                                autoComplete="off"
                                onChange={handleFilter}
                            />
                        )}
                    </div>
                </div>
            </section>
            <section className="container">
                {!id ? (
                    <div className="card-list-component">
                        {filteredArtists.length > 0 ? (
                            filteredArtists.map(artist => {
                                return (
                                    <Artist
                                        key={artist.idUser}
                                        artist={artist}
                                        userRole={user?.role}
                                        setArtists={setVisitors}
                                    />
                                )
                            })
                        ) : (
                            !isLoading && (
                                <p>There are no visitors with this name.</p>
                            )
                        )}
                    </div>
                ) : (
                    <div className="card-list-component">
                        {filteredArtworks.length > 0 ? (
                            filteredArtworks.map(artwork => {
                                return (
                                    <Artwork
                                        key={artwork.idArtwork}
                                        setArtworks={setFavoriteArtworks}
                                        isFavorite={favoriteIds.includes(artwork.idArtwork)}
                                        artwork={artwork}
                                        userRole={user?.role}
                                    />
                                )
                            })
                        ) : (
                            !isLoading && (
                                artworks.length > 0 ? (
                                    <p>There are no artworks with this name.</p>
                                ) : (
                                    <p>This user has no artworks yet.</p>
                                )
                            )
                        )}
                    </div>
                )}
            </section>
        </>
    )
}