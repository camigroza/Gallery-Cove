import { MouseEvent, useState } from "react";

interface Props {
    photos: string[],
    title: string
}

export default function Carousel({
    photos,
    title
}: Props) {
    const [currentIndex, setCurrentIndex] = useState(0);
    const imgSrc = photos.length > 0 ? 'data:image/png;base64,' + photos[currentIndex] : '';

    const handleChangeImage = (e: MouseEvent<Element>, index: number) => {
        e.preventDefault();
        e.stopPropagation();

        let tempIndex = index;

        if (index > photos.length - 1) {
            tempIndex = 0
        } 

        if (index < 0) {
            tempIndex = photos.length - 1;
        }

        setCurrentIndex(tempIndex)
    }

    return (
        <div className="carousel-component">
            <img className="current-img" src={imgSrc} alt={title} />
            {photos.length > 1 && (
                <>
                    <div
                        tabIndex={0}
                        role="button"
                        onClick={(e) => handleChangeImage(e, currentIndex - 1)}
                        className="button prev"
                    >
                        <i className='bx bx-chevron-left'></i>
                    </div>
                    <div
                        tabIndex={0}
                        role="button"
                        onClick={(e) => handleChangeImage(e, currentIndex + 1)}
                        className="button next"
                    >
                        <i className='bx bx-chevron-right' ></i>
                    </div>
                    <ul>
                        {photos.map((photo, index) => {
                            return (
                                <li 
                                    key={index}
                                    role="button"
                                    tabIndex={0}
                                    onClick={(e) => handleChangeImage(e, index)}
                                >
                                    <img 
                                        src={'data:image/png;base64,' + photo} 
                                        alt=""
                                        className={[
                                            currentIndex === index ? 'active' : ""
                                        ].join(" ")}
                                    />
                                </li>
                            )
                        })}
                    </ul>
                </>
            )}
        </div>
    )
}