import { useContext, useEffect, useState } from "react";
import { EventDTO } from "../../common/interfaces/event";
import AppContext from "../../contexts/AppContext";
import Event from "../../components/event/Event";
import EventLinks from "./EventLinks";
import Loader from "../../components/loader/Loader";
import { BACKEND_URL } from "../../common/utils/backend-url";

interface Props {
    history?: boolean
}

export default function MyEvents({
    history = false
}: Props) {
    const { user } = useContext(AppContext);
    const [events, setEvents] = useState<EventDTO[]>([]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        fetchEvents()
    }, [history]);

    async function fetchEvents() {
        setIsLoading(true);
        const endpointPath = history ? 'getAllThatPassedByUser' : 'getAllThatFollowByUser';

        try {
            const result = await fetch(`${BACKEND_URL}/event/${endpointPath}/${user?.userId}`, {
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

    return (
        <section
            id="myEvents"
            className="container"
        >
            <EventLinks />
            {isLoading && (
                <div className="loader-container">
                    <Loader dark/>
                </div>
            )}
            <div className="card-list-component full-row">
                {events.length > 0 ? (
                    events.map((event, index) => {
                        return (
                            <Event
                                key={event.idEvent}
                                event={event}
                                index={index}
                                isJoined={true}
                                isMine={true}
                                setJoinedEvents={setEvents}
                                past={history ? "past" : ""}
                            />
                        )
                    })
                ) : (
                    !isLoading && (
                        <p>You have not created an event yet.</p>
                    )
                )}
            </div>
        </section>
    )
}