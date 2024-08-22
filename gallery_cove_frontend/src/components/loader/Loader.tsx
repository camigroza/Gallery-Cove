interface Props {
    dark?: boolean
}

export default function Loader({ dark }: Props) {
    return (
        <div className={[
            "dot-spinner",
            dark ? "dark" : ""
        ].join(" ")}>
            <div className="dot-spinner__dot"></div>
            <div className="dot-spinner__dot"></div>
            <div className="dot-spinner__dot"></div>
            <div className="dot-spinner__dot"></div>
            <div className="dot-spinner__dot"></div>
            <div className="dot-spinner__dot"></div>
            <div className="dot-spinner__dot"></div>
            <div className="dot-spinner__dot"></div>
        </div>
    )
}