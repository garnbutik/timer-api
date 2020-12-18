package com.garnbutik.mapper;

import com.garnbutik.model.*;
import com.garnbutik.model.dto.CreateTimeRegDTO;
import com.garnbutik.model.dto.CreateUserDTO;
import com.garnbutik.model.dto.UserResponseBody;
import com.garnbutik.model.responseBodies.TimeRegistrationResponseBody;
import com.garnbutik.security.JwtTokenIssuer;
import com.garnbutik.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.inject.Inject;
import java.util.List;

@Mapper(componentModel = "cdi", uses = {UserService.class, JwtTokenIssuer.class})
public interface CustomObjectMapper {

    User createUserDtoToUser(CreateUserDTO createUserDTO);

    @Mapping(target = "token", source = "user", qualifiedByName = "issueToken")
    UserResponseBody userToResponseUserDto(User user);

    @Mapping(source = "user", target = "user", qualifiedByName = {"getUserByUsernameOrEmail"})
    @Mapping(source = "project", target = "project")
    TimeRegistration createTimeRegDtoToTimeRegEntity(CreateTimeRegDTO timeRegDTO);

    @Mapping(expression = "java( timeRegistration.getProject().getId() )", target = "project")
    TimeRegistrationResponseBody timeRegistrationToResponseBody(TimeRegistration timeRegistration);

    List<TimeRegistrationResponseBody> listOfTimeRegsToListOfTimeRegResponseBody(List<TimeRegistration> list);
}
