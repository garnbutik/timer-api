package com.garnbutik.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garnbutik.configuration.JsonPatchConverter;
import com.garnbutik.db.Repository;
import com.garnbutik.exceptions.LoginException;
import com.garnbutik.mapper.CustomObjectMapper;
import com.garnbutik.model.Project;
import com.garnbutik.model.dto.CreateUserDTO;
import com.garnbutik.model.TimeRegistration;
import com.garnbutik.model.User;
import com.garnbutik.security.PasswordUtils;
import com.garnbutik.security.TokenIssuer;
import org.mapstruct.Named;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
public class UserService {

    @Inject
    private Repository repository;
    @Inject
    private CustomObjectMapper customObjectMapper; //Mapstruct object mapper
    @Inject
    private TokenIssuer tokenIssuer;
    @Inject
    private Logger logger;
    @Inject
    private ObjectMapper objectMapper;

    public User createUser(CreateUserDTO userDTO) {
        User user = customObjectMapper.createUserDtoToUser(userDTO);
        user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        return repository.saveUser(user);
    }

    @Named("getUserById")
    public User getUserById(Long id) {
        return repository.findUserById(id);
    }

    @Named("getUserByUsernameOrEmail")
    public User getUserByUsernameOrEmail(String searchParam) {
        return repository.findUserByUsernameOrEmail(searchParam);
    }

    public User updateUser(String userPatchString, UriInfo uriInfo) {
        String username = uriInfo.getPathParameters().getFirst("username");
        User userToBePatched = repository.findUserByUsernameOrEmail(username);
        if (userToBePatched == null) {
            throw new NotFoundException("Could not find user with username: " + username);
        }

        JsonPatchConverter patchConverter = new JsonPatchConverter();
        JsonPatch jsonPatch = patchConverter.convertToJsonPatch(userPatchString);

        JsonStructure target = objectMapper.convertValue(userToBePatched, JsonStructure.class);
        JsonValue patchedUser = jsonPatch.apply(target);
        User updatedUser = objectMapper.convertValue(patchedUser, User.class);
        repository.saveUser(updatedUser);
        return null;
    }

    public Project getProjectById(Long id) {
        return repository.findProjectById(id);
    }

    public String authenticate(String username, String candidatePassword) {
        User user = repository.findUserByUsernameOrEmail(username);
        if (user == null || !PasswordUtils.checkPassword(candidatePassword, user.getPassword())) {
            logger.info("Failed login attempt by: " + username);
            throw new LoginException("not authorized");
        }
        return tokenIssuer.issueToken(user);
    }

    public TimeRegistration createTimeRegistration(TimeRegistration timeRegistration) {
        timeRegistration.setRegDate(LocalDateTime.now());
        return repository.saveTimeRegistration(timeRegistration);
    }

    public List<TimeRegistration> getTimeRegistrationsByUsername(String username) {
        return repository.findTimeRegistrationsByUsername(username);
    }
    public List<TimeRegistration> getTimeRegistrationsByUsernameWithDateFilter(
            String username, LocalDate dateFrom, LocalDate dateTo) {
        return repository.findTimeRegistrationsByUsernameWithDateFilter(username, dateFrom, dateTo);
    }

    public void deleteTimeRegistration(Long id) {
        repository.deleteTimeRegistration(id);
    }
}
