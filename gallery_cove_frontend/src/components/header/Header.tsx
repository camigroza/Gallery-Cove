import { useContext, useEffect, useRef } from "react";
import { Link } from "react-router-dom";
import AppContext from "../../contexts/AppContext";

export default function Header() {
    const { user, logout } = useContext(AppContext);
    const headerRef = useRef<HTMLElement>(null);
    const imgSrc = 'data:image/png;base64,' + user?.photo;

    function handleOpaque() {
        const hero = document.querySelector(".hero-component")

        if (!hero || !headerRef.current) return;
        const scrollY = window.scrollY;

        if (scrollY + headerRef.current.clientHeight >= hero.clientHeight) {
            headerRef.current?.classList.add('opaque')
        } else {
            headerRef.current?.classList.remove('opaque')
        }
    }

    useEffect(() => {
        handleOpaque()
        window.addEventListener('scroll', handleOpaque)

        return () => {
            window.removeEventListener('scroll', handleOpaque)
        }
    }, [])

    return (
        <header className="site-header" ref={headerRef}>
            <nav>
                <div className="site-links">
                    <div className="logo">
                        <Link to="/">
                            <img src="/logo.png" alt="Logo" />
                        </Link>
                    </div>
                    <ul>
                        <li>
                            <Link
                                to="/artworks"
                                className="nav-link"
                            >
                                Artworks
                            </Link>
                        </li>
                        <li>
                            <Link
                                to="/artists"
                                className="nav-link"
                            >
                                Artists
                            </Link>
                        </li>
                        {user?.role === "ADMIN" && (
                            <li>
                                <Link
                                    to="/visitors"
                                    className="nav-link"
                                >
                                    Visitors
                                </Link>
                            </li>
                        )}
                        <li>
                            <Link
                                to="/events"
                                className="nav-link"
                            >
                                Events
                            </Link>
                        </li>
                    </ul>
                </div>
                <ul>
                    {user ? (
                        <>
                            {user.role !== "ADMIN" ? (
                                <>
                                    <li>
                                        <Link
                                            to="/favorites"
                                            className="nav-link"
                                        >
                                            Favorites
                                        </Link>
                                    </li>
                                    <li>
                                        <Link
                                            to="/profile"
                                            className="nav-link"
                                        >
                                            <span style={{paddingRight: '10px'}}>My Profile</span>

                                            <img className="avatar" src={imgSrc} alt=""
                                                style={{
                                                    width: '50px',
                                                    height: '50px',
                                                    objectFit: 'cover',
                                                    outline: '2px solid rgb(243, 239, 239)',
                                                    outlineOffset: '3px'
                                                }} />

                                        </Link>
                                    </li>
                                </>
                            ) : null}
                            <li className="logout">
                                <button
                                    onClick={logout}
                                    type="button"
                                    className="nav-link"
                                >
                                    Logout
                                </button>
                            </li>
                        </>
                    ) : (
                        <>
                            <li>
                                <Link
                                    to="/login"
                                    className="nav-link"
                                >
                                    Login
                                </Link>
                            </li>
                            <li>
                                <Link
                                    to="/register"
                                    className="nav-link"
                                >
                                    Register
                                </Link>
                            </li>
                        </>
                    )}

                </ul>
            </nav>
        </header>
    )
}