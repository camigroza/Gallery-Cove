import { FormEvent, useState } from "react";
import { Action } from "./ForgotPassword";
import OtpInput from "../../components/OTPInput";
import { BACKEND_URL } from "../../common/utils/backend-url";

interface Props {
    email: string,
    setAction: React.Dispatch<React.SetStateAction<Action>>
}

export default function VerifyCodeForm({
    email,
    setAction
}: Props) {
    async function handleSubmit(code: string) {
        try {
            const response = await fetch(`${BACKEND_URL}/forgot_password/verifyCode`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email,
                    code: Number(code)
                })
            });

            if (!response.ok) {
                throw new Error('HTTP error, status = ' + response.status);
            }

            setAction('changePassword');
        } catch (error) {
            console.log("error", error)
        }
    }

    return (
        <form onSubmit={(e) => (e.preventDefault)}>
            <h1>Verify Code</h1>
            <label>
                <OtpInput 
                    length={6} 
                    onOtpSubmit={handleSubmit}
                />
            </label>
        </form>
    )
}