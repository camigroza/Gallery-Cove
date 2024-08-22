import { ChangeEvent, useContext, useEffect, useState } from "react";
import { EventDTO } from "../../common/interfaces/event";
import Event from "../../components/event/Event";
import Loader from "../../components/loader/Loader";
import { getFilteredItems } from "../../common/filter";
import AppContext from "../../contexts/AppContext";
import { useNavigate, useSearchParams } from "react-router-dom";
import { BACKEND_URL } from "../../common/utils/backend-url";

export default function Events() {
    const [params] = useSearchParams();
    const navigate = useNavigate();
    const { user } = useContext(AppContext);
    const [events, setEvents] = useState<EventDTO[]>([]);
    const [joinedEvents, setJoinedEvents] = useState<EventDTO[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [filter, setFilter] = useState<string>('');
    const filteredEvents = getFilteredItems(events, filter, 'title');
    const joinedEventsIds = joinedEvents.map(event => event.idEvent);
    const [sort, setSort] = useState<string>((params.get('show')) || '');

    useEffect(() => {
        async function fetchData() {
            if (user) {
                await fetchJoinedEvents()
            }
            await fetchEvents();
        }

        if (params.get('show') !== null) {
            setSort(params.get('show') as string);
        }
        fetchData()
    }, [params]);

    useEffect(() => {
        handleRedirect(`/events?show=${sort}`)
    }, [sort])

    function handleRedirect(url: string) {
        if (url !== "/events?show=") {
            navigate(url)
        } else {
            navigate("/events");
        }
    }

    async function fetchEvents() {
        setEvents([]);
        let endpointPath = 'DTOforFE';
        const show = params.get('show');

        if (Boolean(show)) {
            endpointPath += "Past";
        }

        setIsLoading(true);
        try {
            const result = await fetch(`${BACKEND_URL}/event/${endpointPath}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            });
            const data: EventDTO[] = await result.json();
            setEvents(data);
        } catch (error) {
            console.log("error")
        } finally {
            setIsLoading(false);
        }
    }

    async function fetchJoinedEvents() {
        try {
            const result = await fetch(`${BACKEND_URL}/event/whereJoined/${user?.userId}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            });
            const data: EventDTO[] = await result.json();
            setJoinedEvents(data);
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
                    <h1 className="large">Events</h1>
                    <div className="fixed">
                        {isLoading ? (
                            <Loader />
                        ) : (
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
                                    onChange={e => {
                                        setSort(e.target.value);
                                        handleRedirect("/events?show=" + e.target.value);
                                    }
                                    }
                                    name="sort"
                                    className="sort-artworks-select"
                                    value={sort}
                                >
                                    <option value="">Showing Upcoming Events</option>
                                    <option value="past">Showing Past Events</option>
                                </select>
                            </>
                        )}
                    </div>
                </div>
            </section>
            <section className="container">
                <div className="card-list-component full-row">
                    {filteredEvents.length > 0 ? (
                        filteredEvents.map((event, index) => {
                            const isJoined = joinedEventsIds.includes(event.idEvent);
                            const isMine = event.userId === user?.userId;
                            return (
                                <Event
                                    key={event.idEvent}
                                    event={event}
                                    index={index}
                                    isJoined={isJoined}
                                    isMine={isMine}
                                    setJoinedEvents={user?.role === "ADMIN" ? setEvents : setJoinedEvents}
                                    past={sort}
                                />
                            )
                        })
                    ) : (
                        !isLoading && (
                            <p>There are no events with this name.</p>
                        )
                    )}
                </div>
            </section>
        </>
    )
}