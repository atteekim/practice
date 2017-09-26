package springbook.user.domain;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements UserServiceImpl.MailSender {
    public void send(SimpleMailMessage simpleMailMessage) throws MailException {
    }
    public void send(SimpleMailMessage[] simpleMailMessages) throws MailException {
    }
}
