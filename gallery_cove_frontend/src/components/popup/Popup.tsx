import { MouseEvent } from "react";
import { preventDefaultClick } from "../../common/utils/preventDefaultClick";

interface Props {
    onDelete: (e: any) => void
    show: boolean,
    setShow: React.Dispatch<React.SetStateAction<boolean>>,
    children: React.ReactNode

}

export default function DeletePopup({
    onDelete,
    show,
    setShow,
    children
}: Props) {
    function handleDelete(e: MouseEvent<Element>) {
        preventDefaultClick(e);
        onDelete(e);
        setShow(false)
    }

    function handleCancel(e: MouseEvent<Element>) {
        preventDefaultClick(e);
        setShow(false);
    }

    return (
        <>
            <div className="popup-component">
                {children}
                {show && (
                    <div className="popup-container">
                        <p>Are you sure?</p>
                        <div className="button-list">
                            <div
                                className="button"
                                role="button"
                                tabIndex={0}
                                onClick={handleCancel}
                            >
                                Cancel
                            </div>
                            <div
                                className="button danger"
                                role="button"
                                tabIndex={0}
                                onClick={(e) => handleDelete(e)}
                            >
                                Delete
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </>
    )
}