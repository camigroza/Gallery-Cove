import { ChangeEvent, useContext, useEffect, useState } from "react";
import { ArtworkDTO } from "../../common/interfaces/artwork";
import Artwork from "../../components/artwork/Artwork";
import Loader from "../../components/loader/Loader";
import { getFilteredItems } from "../../common/filter";
import AppContext from "../../contexts/AppContext";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import { ReviewDTO } from "../../common/interfaces/review";
import ArtworkDetails from "./ArtworkDetails";
import ArtworkReviews from "./ArtworkReviews";
import { Sort } from "../../common/types/sort";
import { BACKEND_URL } from "../../common/utils/backend-url";

export default function Artworks() {
    const [params] = useSearchParams();
    const navigate = useNavigate();
    const { user } = useContext(AppContext);
    const [artworks, setArtworks] = useState<ArtworkDTO[]>([]);
    const [artwork, setArtwork] = useState<ArtworkDTO | null>(null);
    const [favoriteArtworks, setFavoriteArtworks] = useState<ArtworkDTO[]>([]);
    const [reviews, setReviews] = useState<ReviewDTO[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [filter, setFilter] = useState<string>('');
    const filteredArtworks = getFilteredItems(artworks, filter, 'title');
    const [sort, setSort] = useState<Sort>((params.get('sort') as Sort) || '');
    const favoriteIds = favoriteArtworks.map(artwork => artwork.idArtwork);
    const { id } = useParams();

    useEffect(() => {
        async function fetchData() {
            setArtworks([])
            if (user) {
                await fetchFavoriteArtworks();
            }
            if (id) {
                await fetchArtwork();
                await fetchReviews();
            } else {
                await fetchArtworks();
            }
        }
        setArtwork(null);
        fetchData();
    }, [id, params]);

    useEffect(() => {
        if (id || params.get('category')) return;
        handleRedirect(`/artworks?sort=${sort}`)
    }, [sort])

    function handleRedirect(url: string) {
        if (url !== "/artworks?sort=") {
            navigate(url)
        } else {
            navigate("/artworks");
        }
    }

    async function fetchArtworks() {
        let endpointPath = 'DTOforFE';
        const category = params.get('category');
        const sort = params.get('sort') as Sort;

        const mapSort = {
            descendingDate: '',
            ascendingDate: 'OldToNew',
            ascendingPrice: 'ByPriceAsc',
            descendingPrice: 'ByPriceDesc',
            random: 'Random'
        }

        if (Boolean(category)) {
            endpointPath = `getAllByCategory/${category}`
        } else if (Boolean(sort)) {
            endpointPath = "getAll" + mapSort[sort];
        }

        setIsLoading(true);
        try {
            const result = await fetch(`${BACKEND_URL}/artwork/${endpointPath}`, {
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

    async function fetchArtwork() {
        setIsLoading(true);
        try {
            const result = await fetch(`${BACKEND_URL}/artwork/get/${id}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            });
            const data: ArtworkDTO = await result.json();
            setArtwork(data);
        } catch (error) {
            console.log("error")
        } finally {
            setIsLoading(false);
        }
    }

    async function fetchReviews() {
        setIsLoading(true);
        try {
            const result = await fetch(`${BACKEND_URL}/review/getAllByArtwork/${id}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            });
            const data: ReviewDTO[] = await result.json();
            setReviews(data);
        } catch (error) {
            console.log("error");
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
                    id ? "extra-small" : "small"
                ].join(" ")}
                style={{ backgroundImage: 'url("/hero.jpg")' }}
            >
                {!id && (
                    <div className="placeholder">
                        <h1 className="large">Artworks</h1>
                        <div className="fixed">
                            {isLoading ? (
                                <Loader />
                            ) : (
                                !id && (
                                    <>
                                        <input
                                            type="text"
                                            name="search"
                                            placeholder="Search by title"
                                            autoComplete="off"
                                            onChange={handleFilter}
                                        />
                                        <br />
                                        <select
                                            onChange={e => setSort(e.target.value as Sort)}
                                            name="sort"
                                            className="sort-artworks-select"
                                            value={sort}
                                        >
                                            <option value="">Sort Implicit (newest to oldest)</option>
                                            <option value="ascendingDate">Sort Oldest to newest</option>
                                            <option value="ascendingPrice">Sort By Price Ascending</option>
                                            <option value="descendingPrice">Sort By Price Descending</option>
                                            <option value="random">Sort Random</option>
                                        </select>
                                    </>
                                )
                            )}
                        </div>
                    </div>
                )}
            </section>
            <section className="container">
                {!id ? (
                    <div className="card-list-component">
                        {filteredArtworks.length > 0 ? (
                            filteredArtworks.map(artwork => {
                                return (
                                    <Artwork
                                        key={artwork.idArtwork}
                                        setArtworks={user?.role === 'ADMIN' ? setArtworks : setFavoriteArtworks}
                                        isFavorite={favoriteIds.includes(artwork.idArtwork)}
                                        artwork={artwork}
                                        userRole={user?.role}
                                    />
                                )
                            })
                        ) : (
                            !isLoading && (
                                <p>There are no artworks with this name.</p>
                            )
                        )}
                    </div>
                ) : (
                    isLoading ? (
                        <div className="loader-container">
                            <Loader dark />
                        </div>
                    ) : (
                        artwork && (
                            <div className="artwork-details">
                                <ArtworkDetails artwork={artwork} />
                                <ArtworkReviews
                                    artwork={artwork}
                                    reviews={reviews}
                                    setReviews={setReviews}
                                />
                            </div>
                        )
                    )
                )}
            </section>
        </>
    )
}