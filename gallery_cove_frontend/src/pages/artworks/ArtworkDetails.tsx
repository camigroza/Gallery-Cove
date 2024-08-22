import { ArtworkDTO } from "../../common/interfaces/artwork";
import Carousel from "../../components/carousel/Carousel";

interface Props {
    artwork: ArtworkDTO;
}

export default function ArtworkDetails({ artwork }: Props) {
    return (
        <article className="card-component artwork">
            <header>
                <div className="artwork-photos">
                    <Carousel
                        photos={artwork.photos}
                        title={artwork.title}
                    />
                </div>
            </header>
            <footer>
                <h1 className="description">{artwork.title}</h1>
                <p style={{
                    whiteSpace: 'preserve'
                }}>{artwork.description}</p>
                <hr />
                <div>Category: {artwork.categoryName}</div>
                <div>Price: {artwork.price} RON</div>
                <hr />
                <p>Date: {artwork.date}</p>
                <div>Made by: {artwork.userName}</div>
            </footer>
        </article>
    )
}