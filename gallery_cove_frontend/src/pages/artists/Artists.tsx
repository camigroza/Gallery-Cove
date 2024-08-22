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

export default function Artists() {
    const { user } = useContext(AppContext);
    const [artists, setArtists] = useState<ArtistDTO[]>([]);
    const [artist, setArtist] = useState<ArtistDTO>();
    const [artworks, setArtworks] = useState<ArtworkDTO[]>([]);
    const [favoriteArtworks, setFavoriteArtworks] = useState<ArtworkDTO[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [filter, setFilter] = useState<string>('');
    const filteredArtists = getFilteredItems(artists, filter, 'name');
    const filteredArtworks = getFilteredItems(artworks, filter, 'title');
    const { id } = useParams();
    const favoriteIds = favoriteArtworks.map(artwork => artwork.idArtwork);
    const imgSrc = 'data:image/png;base64,' + artist?.photo;

    useEffect(() => {
        async function fetchData() {
            if (id) {
                await fetchArtist();
                if (user) {
                    await fetchFavoriteArtworks();
                }
                await fetchArtworks();
            } else {
                await fetchArtists();
            }
        }
        fetchData()
    }, [id]);

    async function fetchArtists() {
        setIsLoading(true);
        try {
            const result = await fetch(`${BACKEND_URL}/user/getAllArtists`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            });
            const data: ArtistDTO[] = await result.json();
            setArtists(data);
        } catch (error) {
            console.log("error")
        } finally {
            setIsLoading(false);
        }
    }

    async function fetchArtist() {
        setIsLoading(true);
        try {
            const result = await fetch(`${BACKEND_URL}/user/get/${id}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            });
            const data: ArtistDTO = await result.json();
            setArtist(data);
        } catch (error) {
            console.log("error")
        } finally {
            setIsLoading(false);
        }
    }

    async function fetchArtworks() {
        setIsLoading(true);
        try {
            const result = await fetch(`${BACKEND_URL}/artwork/getAllByUser/${id}`, {
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

    async function fetchFavoriteArtworks() {
        setIsLoading(true);
        try {
            const result = await fetch(`${BACKEND_URL}/favorite/getAllByUser/${user?.userId}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            });
            const data: ArtworkDTO[] = await result.json();
            setFavoriteArtworks(data);
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
                    {id && <img className="avatar" src={imgSrc} alt={artist?.name} style={{ objectFit: 'cover' }} />}
                    <h1 className="large">{id ? artist?.name : 'Artists'}</h1>
                    {id && (
                        <div className="details">
                            <a target="_blank" href={`mailto:${artist?.email}`}>
                                <i className='bx bx-envelope'></i>
                                {artist?.email}
                            </a>
                            <a target="_blank" href={`tel:${artist?.phoneNumber}`}>
                                <i className='bx bx-phone' ></i>
                                {artist?.phoneNumber}
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
                                        setArtists={setArtists}
                                    />
                                )
                            })
                        ) : (
                            !isLoading && (
                                <p>There are no artists with this name.</p>
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