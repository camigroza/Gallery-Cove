import { Route, Routes } from "react-router-dom";
import Header from "./components/header/Header";
import Hero from "./components/hero/Hero";
import Login from "./pages/login/Login";
import Register from "./pages/register/Register";
import ForgotPassword from "./pages/forgot-password/ForgotPassword";
import Artworks from "./pages/artworks/Artworks";
import Artists from "./pages/artists/Artists";
import { AppProvider } from "./contexts/AppContext";
import Events from "./pages/events/Events";
import Profile from "./pages/profile/Profile";
import Favorites from "./pages/favorites/Favorites";
import Contact from "./pages/contact/contact";
import Footer from "./components/footer/footer";
import Privacy from "./pages/privacy/privacy";
import Terms from "./pages/terms/terms";
import Visitors from "./pages/visitors/Visitors";

function App() {
  return (
    <>
      <AppProvider>
        <Header />
        <main>
          <Routes>
            <Route path="/" element={<Hero />} />
            <Route path="/artworks/:id?" element={<Artworks />} />
            <Route path="/artists/:id?" element={<Artists />} />
            <Route path="/visitors/:id?" element={<Visitors />} />
            <Route path="/events" element={<Events />} />
            <Route path="/favorites" element={<Favorites />} />
            <Route path="/profile/*" element={<Profile />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/forgot-password" element={<ForgotPassword />} />
            <Route path="/privacy" element={<Privacy />} />
            <Route path="/terms" element={<Terms />} />
            <Route path="/contact" element={<Contact />} />
            <Route path="*" element={<div>ERROR 404</div>} />
          </Routes>
        </main>
        <Footer />
      </AppProvider>
    </>
  );
}

export default App;
