package com.cami.gallerycove.controller;

import com.cami.gallerycove.domain.exception.EmailNotFoundException;
import com.cami.gallerycove.domain.exception.EntityNotFoundException;
import com.cami.gallerycove.domain.exception.ResetPasswordException;
import com.cami.gallerycove.domain.model.MailStructure;
import com.cami.gallerycove.domain.params.VerifyCodeParams;
import com.cami.gallerycove.service.MailService.MailService;
import com.cami.gallerycove.service.ResetPasswordCodeService.ResetPasswordCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins="https://gallery-cove.onrender.com", allowedHeaders = "*")
@RequestMapping("/forgot_password")
public class ForgotPasswordController {

    private final MailService mailService;
    private final ResetPasswordCodeService resetPasswordCodeService;

    @Autowired
    public ForgotPasswordController(MailService mailService, ResetPasswordCodeService resetPasswordCodeService) {
        this.mailService = mailService;
        this.resetPasswordCodeService = resetPasswordCodeService;
    }

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<String> sendVerificationCode(@RequestBody String userEmail) {
        try {
            int code = resetPasswordCodeService.generateCode(userEmail);

            String title = "Cod de verificare pentru resetarea parolei";
            String description = "Draga iubitorule de arta,\n\n" +
                    "Ai solicitat recent resetarea parolei pentru contul tau.\nPentru a continua procesul, " +
                    "te rugam sa introduci codul de verificare de mai jos in aplicatia noastra:\n\n" +
                    "Cod de verificare: " + String.valueOf(code) +
                    "\n\nAcest cod este valabil pentru o perioada limitata de timp si va expira automat " +
                    "in cateva minute.\nTe rugam sa nu il partajezi cu nimeni.\n" +
                    "Daca nu ai solicitat acest reset de parola, te rugam sa ignori acest mesaj." +
                    "\n\nCu stima,\nEchipa Gallery Cove";

            MailStructure mailStructure = new MailStructure(title, description);
            mailService.sendEmail(userEmail, mailStructure);

            return ResponseEntity.ok().body("Verification code sent successfully!");
        } catch (EmailNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PostMapping("/verifyCode")
    public ResponseEntity<String> verifyCode(@RequestBody VerifyCodeParams params) {
        try {
            if (resetPasswordCodeService.verifyCode(params.getEmail(), params.getCode())) {
                resetPasswordCodeService.deleteCode(params.getEmail());
                return ResponseEntity.ok().body("Verification code is valid!");
            }
        } catch (EntityNotFoundException | ResetPasswordException e) {
            return ResponseEntity.badRequest().body("ERROR:\n" + e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return null;
    }
}
