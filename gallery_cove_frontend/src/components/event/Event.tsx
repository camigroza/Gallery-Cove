import { MouseEvent, useContext, useState } from "react";
import { EventDTO } from "../../common/interfaces/event";
import MapLeaflet from "../map/Map";
import AppContext from "../../contexts/AppContext";
import Loader from "../loader/Loader";
import { useLocation, useNavigate } from "react-router-dom";
import DeletePopup from "../popup/Popup";
import { preventDefaultClick } from "../../common/utils/preventDefaultClick";
import { BACKEND_URL } from "../../common/utils/backend-url";

interface Props {
    event: EventDTO,
    index: number,
    isJoined: boolean,
    setJoinedEvents: React.Dispatch<React.SetStateAction<EventDTO[]>>,
    isMine?: boolean
    past: string
}

export default function Event({
    event,
    index,
    isJoined,
    setJoinedEvents,
    isMine,
    past
}: Props) {
    const navigate = useNavigate();
    const location = useLocation();
    const { user } = useContext(AppContext);
    const [showDeletePopup, setShowDeletePopup] = useState(false);
    const [loading, setLoading] = useState(false);
    const { userName, title, date, location: eventLocation, descriptionMail, time, idEvent, numberOfUsersJoined } = event;
    const [city, street, number] = eventLocation.split(",");
    let buttonLabel = 'Delete';
    const isMyEventsPage = location.pathname.includes("/profile/events");
    const isAdmin = user?.role === "ADMIN";
    const isPast = past === "past";
    const [interested, setInterested] = useState(numberOfUsersJoined);

    if (!isMyEventsPage) {
        buttonLabel = isJoined ? 'Leave' : 'Join'
    }

    async function handleAction() {
        setLoading(true);
        let action = 'join';
        let method = 'POST';
        let body: string | null = JSON.stringify({
            eventId: idEvent,
            userId: user?.userId
        })

        if (isJoined) {
            action = 'leave';
            method = 'DELETE';
        }

        if (isMyEventsPage) {
            action = `delete/${idEvent}`;
            method = 'DELETE';
            body = null;
        }

        try {
            await fetch(`${BACKEND_URL}/event/${action}`, {
                method,
                headers: {
                    "Content-Type": "application/json",
                },
                body
            });

            if (isJoined) {
                setJoinedEvents(prev => prev.filter(event => event.idEvent !== idEvent));
                setInterested(interested - 1);
            } else {
                setInterested(interested + 1);
                setJoinedEvents(prev => {
                    const tempEvents = [...prev];
                    tempEvents.push(event)

                    return tempEvents;
                })
            }

            setLoading(false);
        } catch (error) {
            console.log("error", error)
        }
    }

    async function handleDelete(e: MouseEvent<Element>) {
        e.preventDefault();
        e.stopPropagation();

        try {
            await fetch(`${BACKEND_URL}/event/delete/${event.idEvent}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            setJoinedEvents(prev => prev.filter(event => event.idEvent !== idEvent));
        } catch (error) {
            console.log("error", error)
        }
    }

    function handleShowDeletePopup(e: MouseEvent) {
        preventDefaultClick(e);
        setShowDeletePopup(true);
    }

    return (
        <article className="card-component split">
            <header>
                <MapLeaflet locationData={{ city, street, number }} index={index} />
            </header>
            <footer>
                <div className="title">
                    <h2 className="description">{title}</h2>
                    <div className="chip-component">
                        {String(new Date(date).toDateString())} at {time}
                    </div>
                    <hr />
                    <div className="location">{city}, Str. {street}, Nr. {number}</div>
                    <br />
                    <p style={{
                        whiteSpace: 'preserve'
                    }}>{descriptionMail}</p>
                    <p style={{ fontStyle: 'italic', fontSize: '16px', color: '#688565' }}>{interested} people {isPast ? "participated" : "interested"}</p>
                </div>
                <div className="artist event">Created by: {userName}</div>
                <div className="controls">
                    {user && (
                        <>
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
                            ) : (
                                !isPast && (
                                    <>
                                        {isMyEventsPage ? (
                                            <>
                                                <button
                                                    className="button"
                                                    onClick={() => navigate(`/profile/editEvent/${idEvent}`)}
                                                >
                                                    Edit
                                                </button>
                                                <DeletePopup
                                                    show={showDeletePopup}
                                                    setShow={setShowDeletePopup}
                                                    onDelete={handleAction}
                                                >
                                                    <button
                                                        className="button"
                                                        onClick={() => setShowDeletePopup(true)}
                                                    >
                                                        {loading ? <Loader dark={true} /> : buttonLabel}
                                                    </button>
                                                </DeletePopup>
                                            </>
                                        ) : isMine ? (
                                            null
                                        ) : (
                                            <button
                                                className="button"
                                                onClick={handleAction}
                                            >
                                                {loading ? <Loader dark={true} /> : buttonLabel}
                                            </button>
                                        )}
                                    </>
                                )
                            )}
                        </>
                    )}
                </div>
            </footer>
        </article>
    )
}