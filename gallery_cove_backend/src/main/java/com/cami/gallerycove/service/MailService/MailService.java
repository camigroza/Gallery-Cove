package com.cami.gallerycove.service.MailService;

import com.cami.gallerycove.domain.model.MailStructure;

public interface MailService {

    void sendEmail(String email, MailStructure mailStructure);
}
