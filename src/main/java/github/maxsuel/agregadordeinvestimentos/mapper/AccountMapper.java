package github.maxsuel.agregadordeinvestimentos.mapper;

import github.maxsuel.agregadordeinvestimentos.dto.response.account.AccountResponseDto;
import github.maxsuel.agregadordeinvestimentos.dto.request.account.CreateAccountDto;
import github.maxsuel.agregadordeinvestimentos.entity.Account;
import github.maxsuel.agregadordeinvestimentos.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "description", source = "createAccountDto.description")
    @Mapping(target = "accountStocks", expression = "java(new java.util.ArrayList<>())")
    Account toEntity(CreateAccountDto createAccountDto, User user) ;

    AccountResponseDto toDto(Account account);

}
