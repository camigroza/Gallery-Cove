import { ChangeEvent, FormEvent, useContext, useEffect, useState } from "react";
import AppContext from "../../contexts/AppContext";
import { ArtworkDTO } from "../../common/interfaces/artwork";
import { useNavigate, useParams } from "react-router-dom";
import InputImage from "../../components/input-image/InputImage";
import FileInput from "../../components/form-elements/FileInput";
import { HashLink } from "react-router-hash-link";
import useActiveLink from "../../hooks/useActiveLink";
import Loader from "../../components/loader/Loader";
import { BACKEND_URL } from "../../common/utils/backend-url";

interface FormData {
    title: string,
    description: string,
    price: string,
    categoryName: string,
    images: File[] | string[]
}

interface Category {
    id: number,
    name: string
}

const DEFAULT_FORM: FormData = {
    title: '',
    description: '',
    price: '',
    categoryName: '',
    images: []
}

export default function AddArtwork() {
    const { id } = useParams();
    const navigate = useNavigate();
    const { user } = useContext(AppContext);
    const [form, setForm] = useState<FormData>(DEFAULT_FORM);
    const [categories, setCategories] = useState<Category[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const { isActiveLink } = useActiveLink();
    const [errorMessage, setErrorMessage] = useState<string>('');


    useEffect(() => {
        async function fetchCategories() {
            try {
                const response = await fetch(`${BACKEND_URL}/category/getAll`);
                const data = await response.json();
                setCategories(data);
            } catch (error) {
                console.log("error", error);
            }
        }

        fetchCategories();
    }, []);

    useEffect(() => {
        if (!id) return;
        fetchArtwork();
    }, [])

    async function fetchArtwork() {
        try {
            const result = await fetch(`${BACKEND_URL}/artwork/get/${id}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            });
            const data: ArtworkDTO = await result.json();
            const formData: FormData = {
                categoryName: data.categoryName,
                description: data.description,
                price: String(data.price),
                images: data.photos,
                title: data.title
            }
            setForm(formData);
        } catch (error) {
            console.log("error")
        }
    }

    function handleImageChange(e: ChangeEvent<HTMLInputElement>) {
        const files = e.target.files ? Array.from(e.target.files) : [];
        setForm(prev => {
            return {
                ...prev,
                images: files
            }
        });
    }

    function handleChange(e: ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) {
        const value = e.target.value;
        const name = e.target.name;

        setForm(prev => {
            return {
                ...prev,
                [name]: value
            }
        })
    }

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setErrorMessage('');
        setIsLoading(true);

        if (!Number(form.price)) {
            setErrorMessage('The price must be a number!');
            setIsLoading(false);
            return;
        }

        if (form.images.length == 0) {
            setErrorMessage('You must upload at least one picture!');
            setIsLoading(false);
            return;
        }

        let endpointPath = 'add';
        let method = 'POST';

        if (id) {
            endpointPath = `update/${id}`
            method = 'PUT';
        }

        try {
            const result = await fetch(`${BACKEND_URL}/artwork/${endpointPath}`, {
                method,
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    userId: user?.userId,
                    description: form.description,
                    title: form.title,
                    price: Number(form.price),
                    categoryName: form.categoryName
                })
            });

            if (!result.ok) {
                setErrorMessage('Something went wrong, please try again.');
                return;
            }

            if (form.images[0] instanceof File) {
                const artwork: ArtworkDTO = await result.json();

                try {
                    await fetch(`${BACKEND_URL}/artwork_image/deleteAllImagesOfArtwork/${artwork.idArtwork}`, {
                        method: 'DELETE',
                        headers: {
                            "Content-Type": "application/json",
                        },
                    })
                } catch (error) {
                    setErrorMessage('Something went wrong, please try again.');
                    return;
                }

                for (const image of form.images) {
                    console.log("image", image)
                    const imageFormData = new FormData();
                    imageFormData.append('artworkId', artwork.idArtwork.toString());
                    imageFormData.append('photo', image);

                    const result = await fetch(`${BACKEND_URL}/artwork_image/add`, {
                        method: "POST",
                        body: imageFormData
                    });

                    if (!result.ok) {
                        setErrorMessage('Something went wrong, please try again.');
                        return;
                    }
                }
            }

            navigate("/profile/artworks#myArtworks");
        } catch (error) {
            setErrorMessage('Something went wrong, please try again.');
            console.log("error", error)
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <section className="container" id="addArtwork">
            <div className="profile-list">
                <HashLink
                    smooth
                    to='/profile/addArtwork#addArtwork'
                    className={[
                        "button",
                        "dark",
                        isActiveLink('/profile/addArtwork') ? 'active' : ''
                    ].join(" ")}
                >
                    Add Artwork
                </HashLink>
            </div>
            <br />
            <br />
            <form
                onSubmit={handleSubmit}
                className="solid"
            >
                <h2>{id ? "Edit" : "Add"} Artwork</h2>
                <label>
                    Title:
                    <input
                        onChange={handleChange}
                        value={form.title}
                        type="text"
                        name="title"
                        placeholder="Title"
                        autoComplete="off"
                        required
                    />
                </label>
                <label>
                    Description:
                    <textarea
                        onChange={handleChange}
                        value={form.description}
                        name="description"
                        placeholder="Description"
                        autoComplete="off"
                    />
                </label>
                <label>
                    Price:
                    <input
                        onChange={handleChange}
                        value={form.price}
                        type="text"
                        name="price"
                        placeholder="Price"
                        autoComplete="off"
                        required
                    />
                </label>
                <label>
                    Category Name:
                    <select
                        onChange={handleChange}
                        value={form.categoryName}
                        name="categoryName"
                        required
                        className="sort-artworks-select"
                    >
                        <option value="" disabled>Select Category</option>
                        {categories.map(category => (
                            <option key={category.id} value={category.name}>
                                {category.name}
                            </option>
                        ))}
                    </select>
                </label>
                <FileInput
                    labelTitle="Images: "
                    buttonTitle="Select files: "
                    name="images"
                    multiple={true}
                    onChange={handleImageChange}
                    images={form.images}
                />
                <button type="submit" disabled={isLoading}>
                    {isLoading ? (
                        <Loader dark={true} />
                    ) : (
                        'Save'
                    )}
                </button>
                <p className="status-message">{errorMessage}</p>
            </form>
        </section>
    )
}