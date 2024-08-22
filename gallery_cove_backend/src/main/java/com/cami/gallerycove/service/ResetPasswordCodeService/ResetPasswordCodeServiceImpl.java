package com.cami.gallerycove.service.ResetPasswordCodeService;

import com.cami.gallerycove.domain.exception.EmailNotFoundException;
import com.cami.gallerycove.domain.exception.EntityNotFoundException;
import com.cami.gallerycove.domain.exception.ResetPasswordException;
import com.cami.gallerycove.domain.model.ResetPasswordCode;
import com.cami.gallerycove.repository.ResetPasswordCodeRepository;
import com.cami.gallerycove.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class ResetPasswordCodeServiceImpl implements ResetPasswordCodeService {

    private final ResetPasswordCodeRepository resetPasswordCodeRepository;
    private final UserRepository userRepository;

    @Autowired
    public ResetPasswordCodeServiceImpl(ResetPasswordCodeRepository resetPasswordCodeRepository, UserRepository userRepository) {
        this.resetPasswordCodeRepository = resetPasswordCodeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResetPasswordCode getResetPasswordCode(String email) {
        Optional<ResetPasswordCode> resetPasswordCodeOptional = resetPasswordCodeRepository.findById(email);
        if (resetPasswordCodeOptional.isPresent()) {
            return resetPasswordCodeOptional.get();
        } else {
            throw new EntityNotFoundException("Reset password code not found for email: " + email);
        }
    }

    @Override
    public int generateCode(String email) {
        if (userRepository.findByEmail(email) == null) {
            throw new EmailNotFoundException("User with email " + email + " doesn't exist!");
        }

        Optional<ResetPasswordCode> resetPasswordCodeOptional = resetPasswordCodeRepository.findById(email);
        if (resetPasswordCodeOptional.isPresent()) {
            resetPasswordCodeRepository.deleteById(email);
        }

        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(5);
        resetPasswordCodeRepository.save(new ResetPasswordCode(email, code, expireTime));
        return code;
    }

    @Override
    public void deleteCode(String email) {
        resetPasswordCodeRepository.deleteById(email);
    }

    @Override
    public boolean verifyCode(String email, int code) {
        try {
            ResetPasswordCode resetPasswordCode = this.getResetPasswordCode(email);

            if (resetPasswordCode.getVerificationCode() != code) {
                throw new ResetPasswordException("Invalid code!");
            }

            if (! resetPasswordCode.getExpireTime().isAfter(LocalDateTime.now())) {
                throw new ResetPasswordException("Expired code!");
            }

            resetPasswordCodeRepository.deleteById(email);
            return true;
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
