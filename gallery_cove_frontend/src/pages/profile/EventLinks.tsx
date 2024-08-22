import { HashLink } from "react-router-hash-link";
import useActiveLink from "../../hooks/useActiveLink";
import { useContext, useEffect } from "react";
import AppContext from "../../contexts/AppContext";
import { useLocation } from "react-router-dom";
import { BACKEND_URL } from "../../common/utils/backend-url";

interface Data {
    token: string;
}

export default function EventLinks() {
    const { isActiveLink } = useActiveLink();
    const { user, setToken } = useContext(AppContext);
    const location = useLocation();

    useEffect(() => {
        fetchUser();
    }, [location]);

    async function fetchUser() {
        try {
            const response = await fetch(`${BACKEND_URL}/user/getToken/${user?.userId}`, {
                method: "GET"
            });

            const data: Data = await response.json();
            const token = data.token;
            setToken(token);
        } catch (error) {
            console.log("error", error)
        }
    }

    return (
        <>
            <div className="profile-list">
                <HashLink
                    smooth
                    to='/profile/joinedEvents#joinedEvents'
                    className={[
                        "button",
                        "dark",
                        isActiveLink("/profile/joinedEvents") ? 'active' : ""
                    ].join(" ")}
                >
                    Upcoming Joined Events
                </HashLink>
                {user?.role === "artist" && (
                    <HashLink
                        smooth
                        to='/profile/addEvent#addEvent'
                        className={[
                            "button",
                            "dark",
                            isActiveLink("/profile/addEvent") ? 'active' : ""
                        ].join(" ")}
                    >
                        Add Event
                    </HashLink>
                )}
                <HashLink
                    smooth
                    to='/profile/history'
                    className={[
                        "button",
                        "dark",
                        isActiveLink("/profile/history") ? 'active' : ""
                    ].join(" ")}

                >
                    Events History
                </HashLink>
            </div>
            <br />
            <br />
        </>
    )
}