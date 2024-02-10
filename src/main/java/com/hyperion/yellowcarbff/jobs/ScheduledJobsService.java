package com.hyperion.yellowcarbff.jobs;

import com.hyperion.yellowcarbff.entities.User;
import com.hyperion.yellowcarbff.entities.VerificationToken;
import com.hyperion.yellowcarbff.repositories.UsersRepository;
import com.hyperion.yellowcarbff.repositories.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ScheduledJobsService {

    private final Logger logger = LoggerFactory.getLogger(ScheduledJobsService.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UsersRepository usersRepository;

    private final String deleteTokensCron = "0 */1 * ? * *";

    @Scheduled(cron = deleteTokensCron) // Se ejecuta el job todos los días a las 12:00
    public void deleteTokens() {
        List<VerificationToken> tokens = tokenRepository.findAll();
        AtomicInteger nEliminated = new AtomicInteger();
        Calendar calendar = Calendar.getInstance();

        tokens.forEach(token -> {
            User user = token.getUser();
            user.setToken(null);
            usersRepository.save(user);
            tokenRepository.delete(token);
            nEliminated.getAndIncrement();
        });

        logger.info("[ScheduledJobsService] Eliminados " + nEliminated + " tokens (" + dateFormat.format(calendar.getTime()) + ")");
    }
}
