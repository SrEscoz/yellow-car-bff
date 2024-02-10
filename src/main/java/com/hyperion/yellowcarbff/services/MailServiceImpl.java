package com.hyperion.yellowcarbff.services;

import com.hyperion.yellowcarbff.entities.User;
import com.hyperion.yellowcarbff.model.responses.BasicResponse;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    private final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration configuration;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${mail.activation-url}")
    private String activationUrl;

    @Override
    public BasicResponse testMailService(String email) {
        logger.info("MailService    [sendSimpleTestMail] - INICIO");

        SimpleMailMessage message = new SimpleMailMessage();

        try {
            message.setFrom(from);
            message.setTo(email);
            message.setSubject("Hyperion Test");
            message.setText("Email de comprobación del servicio de correo de Hyperion");
            mailSender.send(message);

        } catch (MailException exception) {
            logger.error(exception.getMessage());
            throw exception;
        }

        logger.info("MailService    [sendSimpleTestMail] - FIN");
        return new BasicResponse(HttpStatus.OK.value(), "Servicio de correo activo");
    }

    @Override
    public void sendVerificationCode(User user, String token) {
        logger.info("MailService    [sendVerificationCode] - INICIO");

        MimeMessage message = mailSender.createMimeMessage();
        Map<String, Object> model = new HashMap<>();
        String url = activationUrl + token;

        logger.info("[sendVerificationCode] URL de activación generada -> " + url);
        model.put("url", url);

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
            Template template = configuration.getTemplate("verification-mail.ftl");
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            helper.setText(text, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Código de verificación");
            helper.setFrom(from);

            mailSender.send(message);

        } catch (Exception exception) {
            logger.error("[sendVerificationCode] No ha sido posible enviar el email -> ", exception);
        }

        logger.info("MailService    [sendVerificationCode] - FIN");
    }
}
