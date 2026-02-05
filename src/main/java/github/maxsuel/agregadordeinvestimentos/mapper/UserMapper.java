package github.maxsuel.agregadordeinvestimentos.mapper;

import github.maxsuel.agregadordeinvestimentos.dto.request.auth.CreateUserDto;
import github.maxsuel.agregadordeinvestimentos.dto.response.auth.UserDto;
import github.maxsuel.agregadordeinvestimentos.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "creationTimestamp", ignore = true)
    @Mapping(target = "updateTimestamp", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "role", constant = "ADMIN")
    User toEntity(CreateUserDto createUserDto, String encodedPassword);

    UserDto toDto(User entity);

}
