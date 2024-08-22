import { useState } from "react"
import ForgotPasswordForm from "./ForgotPasswordForm";
import VerifyCodeForm from "./VerifyCodeForm";
import ChangePasswordForm from "./ChangePasswordForm";

export type Action = 'sendEmail' | 'verifyCode' | 'changePassword'; 

type ActionComponent = {
    [key in Action]: React.ReactNode
}

export default function ForgotPassword() {
    const [email, setEmail] = useState('');
    const [action, setAction] = useState<Action>('sendEmail')

    const actionComponents: ActionComponent = {
        sendEmail: (
            <ForgotPasswordForm 
                email={email}
                setEmail={setEmail}
                setAction={setAction}
            />
        ),
        verifyCode: (
            <VerifyCodeForm 
                email={email}
                setAction={setAction}
            />
        ),
        changePassword: (
            <ChangePasswordForm 
                email={email}
                setAction={setAction}
            />
        ),
    }

    return (
        <section
            className="hero-component"
            style={{ backgroundImage: 'url("/hero.jpg")' }}
        >
            {actionComponents[action]}
        </section>
    )
}