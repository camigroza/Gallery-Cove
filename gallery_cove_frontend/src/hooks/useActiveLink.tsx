import { useLocation } from "react-router-dom";

export default function useActiveLink() {
    const location = useLocation();
    
    function isActiveLink(to: string) {
        return to === location.pathname;
    }

    return {
        isActiveLink: isActiveLink
    }
}