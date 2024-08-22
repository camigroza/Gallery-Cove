import { createContext, useEffect, useState } from 'react'
import { JwtPayload, jwtDecode } from 'jwt-decode';
import { useNavigate } from 'react-router-dom';
import { ArtistDTO } from '../common/interfaces/artist';

interface Props {
    children: React.ReactNode
}

export interface User extends JwtPayload, ArtistDTO {
    userId: number
}

export type AppContext = {
    user: User | null,
    setUser: (theme: User) => void,
    setToken: (token: string) => void,
    logout: () => void
};

const DEFAULT_USER = null;

const AppContext = createContext<AppContext>({
    user: DEFAULT_USER,
    setUser: () => {},
    setToken: () => {},
    logout: () => {}
});

export const AppProvider = ({ children }: Props) => {
    const [token, setToken] = useState(localStorage.getItem("token") || '');
    const navigate = useNavigate();
    const [user, setUser] = useState<User | null>(getDecodedUser);

    useEffect(() => {
        if (!token) return;
        const decodedUser: User | null = getDecodedUser();
        localStorage.setItem("token", token);
        setUser(decodedUser);
    }, [token]);

    function getDecodedUser() {
        let decodedUser = DEFAULT_USER;

        try {
            decodedUser = jwtDecode(token);
        } catch (error) {
            console.log("Invalid token")
        } finally {
            return decodedUser;
        }
    }

    function logout() {
        localStorage.removeItem('token');
        setUser(null);
        navigate('/');
    }

    return (
        <AppContext.Provider value={{
            user,
            setUser,
            setToken,
            logout,
        }}>
            {children}
        </AppContext.Provider>
    )
}

export default AppContext;