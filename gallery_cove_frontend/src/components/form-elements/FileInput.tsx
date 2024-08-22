import { ChangeEvent } from "react"
import InputImage from "../input-image/InputImage"

interface Props {
    labelTitle?: string,
    buttonTitle: string,
    name: string,
    multiple: boolean,
    onChange: (e: ChangeEvent<HTMLInputElement>) => void,
    images: File[] | string[]
}

export default function FileInput({
    labelTitle,
    buttonTitle,
    name,
    multiple,
    onChange,
    images,
}: Props) {
    return (
        <label className="file-label">
            {labelTitle}
            <input
                onChange={onChange}
                type="file"
                name={name}
                multiple={multiple}
                hidden
            />
            <div className="file-button">
                <div>{buttonTitle}</div>
                <div className="file-list">{images.map((image, index) => {
                    return (
                        <InputImage key={index} file={image} />
                    )
                })}
                </div>
            </div>
        </label>
    )
}