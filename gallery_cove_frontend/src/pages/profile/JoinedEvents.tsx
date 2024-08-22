import { useContext, useEffect, useState } from "react";
import { EventDTO } from "../../common/interfaces/event";
import AppContext from "../../contexts/AppContext";
import Event from "../../components/event/Event";
import EventLinks from "./EventLinks";
import Loader from "../../components/loader/Loader";
import { BACKEND_URL } from "../../common/utils/backend-url";

export default function JoinedEvents() {
    const { user } = useContext(AppContext);
    const [joinedEvents, setJoinedEvents] = useState<EventDTO[]>([]);
    const joinedEventsIds = joinedEvents.map(event => event.idEvent);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        fetchJoinedEvents()
    }, []);

    async function fetchJoinedEvents() {
        setIsLoading(true);

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
            setIsLoading(false)
        }
    }

    return (
        <section
            id="joinedEvents"
            className="container"
        >
            <EventLinks />
            {isLoading && (
                <div className="loader-container">
                    <Loader dark />
                </div>
            )}
            <div className="card-list-component full-row">
                {joinedEvents.length > 0 ? (
                    joinedEvents.map((event, index) => {
                        const isJoined = joinedEventsIds.includes(event.idEvent);
                        return (
                            <Event
                                key={event.idEvent}
                                event={event}
                                index={index}
                                isJoined={isJoined}
                                setJoinedEvents={setJoinedEvents}
                                past=""
                            />
                        )
                    })
                ) : (
                    !isLoading && (
                        <p>You have no upcoming joined events.</p>
                    )
                )}
            </div>
        </section>
    )
}