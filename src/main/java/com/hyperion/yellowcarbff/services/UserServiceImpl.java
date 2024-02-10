package com.hyperion.yellowcarbff.services;

import com.hyperion.yellowcarbff.entities.User;
import com.hyperion.yellowcarbff.entities.VerificationToken;
import com.hyperion.yellowcarbff.exceptions.DuplicateUserException;
import com.hyperion.yellowcarbff.exceptions.InvalidEmailException;
import com.hyperion.yellowcarbff.exceptions.InvalidTokenException;
import com.hyperion.yellowcarbff.model.requests.LoginUserRequest;
import com.hyperion.yellowcarbff.model.requests.RegisterUserRequest;
import com.hyperion.yellowcarbff.model.responses.BasicResponse;
import com.hyperion.yellowcarbff.repositories.UsersRepository;
import com.hyperion.yellowcarbff.repositories.VerificationTokenRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UsersService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<User> getUsers() {
        logger.info("UserService    [getUsers] - INICIO");

        List<User> users = usersRepository.findAll();

        logger.info("UserService    [getUsers] - FIN");
        return users;
    }

    @Override
    public BasicResponse registerUser(RegisterUserRequest request) {
        logger.info("UserService    [registerUser] - INICIO");

        /* Comprobamos que el usuario no este duplicado */
        if (usersRepository.existsById(request.getEmail())) {
            logger.info("[registerUser] Usuario duplicado -> " + request.getEmail());
            throw new DuplicateUserException("El usuario ya se encuentra registrado");
        }

        /* Creamos el usuario y guardamos el usuario */
        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setActive(false);

        usersRepository.save(user);

        /* Creamos el token */
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);

        tokenRepository.save(verificationToken);

        /* Enviamos el mail con la confirmación */
        mailService.sendVerificationCode(user, token);

        logger.info("UserService    [registerUser] - FIN");
        return new BasicResponse(HttpStatus.CREATED.value(), "Usuario creado");
    }

    @Override
    public User login(LoginUserRequest request) {
        logger.info("UserService    [login] - INICIO");

        /* Verificamos que exista el usuario y sea válido */
        User user = usersRepository.findByEmail(request.getEmail());

        if (user == null) {
            logger.info("[login] El usuario no existe -> " + request.getEmail());
            throw new InvalidEmailException("El usuario no existe");
        }
        if (!user.getActive()) {
            logger.info("[login] El usuario no esta activo -> " + request.getEmail());
            throw new InvalidEmailException("El usuario no esta activo");
        }
        if (!user.getPassword().equals(request.getPassword())) {
            logger.info("[login] Contraseña incorrecta -> " + request.getEmail());
            throw new InvalidEmailException("Contraseña invalida");
        }

        logger.info("UserService    [login] - FIN");
        return user;
    }

    @Override
    public BasicResponse generateNewToken(String email) {
        logger.info("UserService    [generateNewToken] - INICIO");

        /* Comprobamos la validez del correo */
        User user = usersRepository.findByEmail(email);

        if (user == null) {
            logger.info("[generateNewToken] El usuario no existe -> " + email);
            throw new InvalidEmailException("El usuario no existe en el sistema");
        }
        if (user.getActive()) {
            logger.info("[generateNewToken] El usuario ya esta activo -> " + email);
            throw new InvalidEmailException("El usuario ya esta activo");
        }

        /* Creamos el nuevo token */
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);

        tokenRepository.save(verificationToken);

        /* Enviamos el mail con la confirmación */
        mailService.sendVerificationCode(user, token);

        logger.info("UserService    [generateNewToken] - FIN");
        return new BasicResponse(HttpStatus.OK.value(), "Nuevo token generado");
    }

    @Override
    public BasicResponse verifyEmail(String token) {
        logger.info("UserService    [verifyEmail] - INICIO");

        /* Comprobamos la existencia del token y su validez */
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        Date date = new Date();

        if (verificationToken == null) {
            logger.info("[verifyEmail] El token no existe -> " + token);
            throw new InvalidTokenException("El token no es válido");
        }
        if (verificationToken.getExpirationDate().getTime() - date.getTime() <= 0) {
            logger.info("[verifyEmail] El token ha expirad -> " + token);
            throw new InvalidTokenException("El token ha expirado");
        }

        /* Actualizamos el usuario */
        User user = verificationToken.getUser();
        user.setActive(true);
        user.setToken(null);
        usersRepository.save(user);
        tokenRepository.delete(verificationToken);

        logger.info("UserService    [verifyEmail] - FIN");
        return new BasicResponse(HttpStatus.OK.value(), "Email verificado");
    }

}
