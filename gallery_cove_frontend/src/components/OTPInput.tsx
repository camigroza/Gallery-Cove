import { ChangeEvent, KeyboardEvent, useEffect, useRef, useState } from "react";

interface Props {
    length?: number,
    onOtpSubmit: (code: string) => void
}

export default function OtpInput({ 
    length = 4, 
    onOtpSubmit 
}: Props) {
    const [otp, setOtp] = useState(
        new Array(length).fill(""));
    const inputRefs = useRef<HTMLInputElement[]>([]);

    useEffect(() => {
        if (inputRefs.current[0]) {
            inputRefs.current[0].focus();
        }
    }, []);

    const handleChange = (index: number, e: ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        if (isNaN(Number(value))) return;

        const newOtp = [...otp];
        // allow only one input
        newOtp[index] =
            value.substring(value.length - 1);
        setOtp(newOtp);

        // submit trigger
        const combinedOtp = newOtp.join("");
        if (combinedOtp.length === length)
            onOtpSubmit(combinedOtp);

        // Move to next input if current field is filled
        if (value && index < length - 1 &&
            inputRefs.current[index + 1]) {
            inputRefs.current[index + 1].focus();
        }
    };

    const handleClick = (index: number) => {
        inputRefs.current[index].setSelectionRange(1, 1);

        // optional
        if (index > 0 && !otp[index - 1]) {
            inputRefs.current[otp.indexOf("")].focus();
        }
    };

    const handleKeyDown = (index: number, e: KeyboardEvent<HTMLInputElement>) => {
        if (
            e.key === "Backspace" &&
            !otp[index] &&
            index > 0 &&
            inputRefs.current[index - 1]
        ) {
            // Move focus to the previous input field on backspace
            inputRefs.current[index - 1].focus();
        }
    };

    return (
        <div className="otp-input">
            {otp.map((value, index) => {
                return (
                    <input
                        key={index}
                        type="text"
                        ref={(input) => {
                            if (input) {
                                inputRefs.current[index] = input
                            }
                        }}
                        value={value}
                        onChange={(e) => handleChange(index, e)}
                        onClick={() => handleClick(index)}
                        onKeyDown={(e) => handleKeyDown(index, e)}
                        className="otpInput"
                    />
                );
            })}
        </div>
    );
};