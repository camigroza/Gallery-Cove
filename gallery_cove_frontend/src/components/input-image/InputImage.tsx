import { useEffect, useState } from "react";

interface Props {
    file: File | string;
}

export default function InputImage({ file }: Props) {
    const [image, setImage] = useState<File | string>(file);

    useEffect(() => {
        async function getImage() {
            if (file instanceof File) {
                try {
                    const tempImage = await toBase64(file);
                    setImage(tempImage as string);
                } catch (error) {
                    console.error("Error converting file to base64", error);
                }
            } else {
                setImage(file);
            }
        }

        getImage();
    }, [file]);

    const toBase64 = (file: File) => new Promise<string>((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result as string);
        reader.onerror = reject;
    });

    if (typeof image === 'string') {
        return <img src={image.startsWith('data:image/') ? image : 'data:image/png;base64,' + image} alt="Uploaded" />;
    }

    return null;
}
