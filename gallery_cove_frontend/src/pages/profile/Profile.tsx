import { useContext } from "react"
import AppContext from "../../contexts/AppContext"
import { Route, Routes } from "react-router-dom"
import EditProfile from "./EditProfile"
import { HashLink } from 'react-router-hash-link';
import JoinedEvents from "./JoinedEvents";
import MyEvents from "./MyEvents";
import MyArtworks from "./MyArtworks";
import AddEvent from "./AddEvent";
import AddArtwork from "./AddArtwork";
import useActiveLink from "../../hooks/useActiveLink";

export default function Profile() {
    const { user } = useContext(AppContext);
    const { isActiveLink } = useActiveLink();
    const imgSrc = 'data:image/png;base64,' + user?.photo;

    return (
        <>
            <section
                className="hero-component medium"
                style={{ backgroundImage: 'url("/hero.jpg")' }}
            >
                <div className="placeholder">
                    <img className="avatar" src={imgSrc} alt={user?.name} style={{ objectFit: 'cover' }} />
                    <h1 className="large">{user?.name}</h1>
                    <div className="details">
                        <a target="_blank" href={`mailto:${user?.email}`}>
                            <i className='bx bx-envelope'></i>
                            {user?.email}
                        </a>
                        <a target="_blank" href={`tel:${user?.phoneNumber}`}>
                            <i className='bx bx-phone' ></i>
                            {user?.phoneNumber}
                        </a>
                    </div>
                    <div className="profile-list">
                        <HashLink
                            smooth
                            to='/profile/edit#editProfile'
                            className={[
                                "button",
                                isActiveLink('/profile/edit') ? "active2" : ""
                            ].join(" ")}
                        >
                            Edit profile
                        </HashLink>

                        <HashLink
                            smooth
                            to='/profile/events#myEvents'
                            className={[
                                "button",
                                isActiveLink('/profile/events') ? "active2" : ""
                            ].join(" ")}
                        >
                            My Events
                        </HashLink>

                        <HashLink
                            smooth
                            to='/profile/artworks#myArtworks'
                            className={[
                                "button",
                                isActiveLink('/profile/artworks') ? "active2" : ""
                            ].join(" ")}
                        >
                            My Artworks
                        </HashLink>
                    </div>
                </div>
            </section>
            <Routes>
                <Route path="edit" element={<EditProfile />} />
                <Route path="joinedEvents" element={<JoinedEvents />} />
                <Route path="addEvent" element={<AddEvent />} />
                <Route path="editEvent/:id?" element={<AddEvent />} />
                <Route path="events" element={<MyEvents />} />
                <Route path="history" element={<MyEvents history={true} />} />
                <Route path="artworks" element={<MyArtworks />} />
                <Route path="addArtwork" element={<AddArtwork />} />
                <Route path="editArtwork/:id?" element={<AddArtwork />} />
            </Routes>
        </>
    )
}