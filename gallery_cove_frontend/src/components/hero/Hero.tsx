import { useContext, useEffect, useState } from "react";
import { ArtistDTO } from "../../common/interfaces/artist";
import Artist from "../artist/Artist";
import { ArtworkDTO } from "../../common/interfaces/artwork";
import Artwork from "../artwork/Artwork";
import AppContext from "../../contexts/AppContext";
import Loader from "../loader/Loader";
import { BACKEND_URL } from "../../common/utils/backend-url";

const slogans = [
    "Handmade Wonders, Yours to Explore",
    "Where Every Piece Tells a Story",
    "Unique Creations, Made with Passion",
    "Crafted by Hand, Treasured by Heart",
    "Discover the Art in Everyday Objects",
    "Crafted with Care, Shared with Joy"
];

export default function Hero() {
    const [artists, setArtists] = useState<ArtistDTO[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [artworks, setArtworks] = useState<ArtworkDTO[]>([]);
    const [favoriteArtworks, setFavoriteArtworks] = useState<ArtworkDTO[]>([]);
    const { user } = useContext(AppContext);
    const favoriteIds = favoriteArtworks.map(artwork => artwork.idArtwork);
    const [currentSlogan, setCurrentSlogan] = useState(0);
    const isAdmin = user?.role === "ADMIN"

    useEffect(() => {
        async function fetchData() {
            await fetchTopArtists();
            if (user) {
                await fetchFavoriteArtworks();
            }
            await fetch3RandomArtworks();
        }

        fetchData();
    }, []);

    useEffect(() => {
        const interval = setInterval(() => {
            setCurrentSlogan((prevSlogan) => (prevSlogan + 1) % slogans.length);
        }, 4000);
        return () => clearInterval(interval);
    }, []);

    async function fetchTopArtists() {
        setIsLoading(true);
        try {
            const result = await fetch(`${BACKEND_URL}/user/getTop3Artists`, {
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

    async function fetch3RandomArtworks() {
        setIsLoading(true);
        try {
            const result = await fetch(`${BACKEND_URL}/artwork/get3Random`, {
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

    return (
        <>
            <section
                className="hero-component small"
                style={{ backgroundImage: 'url("/hero.jpg")' }}
            >
                <div className="headline-wrapper">
                    <img className="hero-logo" src="/logo.png" alt="Logo" />
                    {isAdmin ? (
                        <h1 className="headline">Hello, Admin!</h1>
                    ) : (
                        <h1 className="headline">{slogans[currentSlogan]}</h1>
                    )}
                </div>
            </section>
            {isLoading ? (
                <div className="loader-container">
                    <Loader dark={true} />
                </div>
            ) : (
                <>
                    <section className="container">
                        <h2 className="text-center">Top Artists</h2>
                        <div className="separator"></div>
                        <div className="card-list-component">
                            {artists.map(artist => {
                                return (
                                    <Artist
                                        key={artist.idUser}
                                        artist={artist}
                                        setArtists={setArtists}
                                    />
                                )
                            })}
                        </div>
                    </section>
                    {!isAdmin && (
                        <section className="container">
                            <h2 className="text-center">Explore some of the artworks</h2>
                            <div className="separator"></div>
                            <div className="card-list-component">
                                {artworks.map(artwork => {
                                    return (
                                        <Artwork
                                            key={artwork.idArtwork}
                                            setArtworks={setFavoriteArtworks}
                                            isFavorite={favoriteIds.includes(artwork.idArtwork)}
                                            artwork={artwork}
                                        />
                                    )
                                })}
                            </div>
                        </section>
                    )}
                </>
            )}
        </>
    )
}