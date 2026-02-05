package github.maxsuel.agregadordeinvestimentos.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.maxsuel.agregadordeinvestimentos.dto.response.account.AccountResponseDto;
import github.maxsuel.agregadordeinvestimentos.dto.request.account.CreateAccountDto;
import github.maxsuel.agregadordeinvestimentos.dto.request.user.UpdateUserDto;
import github.maxsuel.agregadordeinvestimentos.entity.User;
import github.maxsuel.agregadordeinvestimentos.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(
    name = "Users", 
    description = "Endpoints for users management"
)
public class UserController {

    private final UserService userService;

    @Operation(
        summary = "Search user by ID.", 
        description="Retrieves user details based on the provided user ID."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User found.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = User.class)
            )   
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User does not exist.", 
            content = @Content
        )
    })
    @GetMapping(path = "/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId) {
        var user = userService.getUserById(userId);

        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Lists all registered users.",
        description = "Retrieves a list of all users registered in the system."
    )
    @ApiResponse(
        responseCode = "200",
        description = "List of users retrieved successfully.",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(
                schema = @Schema(implementation = User.class)
            )
        )
    )
    @GetMapping(path = "/all")
    public ResponseEntity<List<User>> listAllUsers() {
        return ResponseEntity.ok(userService.listAllUsers());
    }

    @Operation(
        summary = "Update a user's data.", 
        description = "Updates the information of an existing user based on the provided user ID and new data."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204", 
            description = "User updated successfully.", 
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found.", 
            content = @Content
        )
    })
    @PutMapping(path = "/{userId}")
    public ResponseEntity<Void> updateUserById(
        @PathVariable("userId") String userId,
        @Valid @RequestBody UpdateUserDto updateUserDto
    ) {
        userService.updateUserById(userId, updateUserDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Remove a user from the system.", 
        description = "Deletes a user based on the provided user ID."
    )
    @ApiResponse(
        responseCode = "204", 
        description = "User successfully deleted.", 
        content = @Content
    )
    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Create an account for the user.", 
        description = "Opens a new investment portfolio associated with the user and sets the billing address."
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Account successfully created.", 
        content = @Content
    )
    @PostMapping(path = "/{userId}/accounts")
    public ResponseEntity<Void> createAccount(
        @PathVariable("userId") String userId,
        @Valid @RequestBody CreateAccountDto createAccountDto
    ) {
        userService.createAccount(userId, createAccountDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "List all accounts associated with a user.", 
        description = "Retrieves all investment portfolios linked to the user."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Accounts retrieved successfully.", 
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                array = @ArraySchema(
                    schema = @Schema(implementation = AccountResponseDto.class)
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found.", 
            content = @Content
        )
    })
    @GetMapping(path = "/{userId}/accounts")
    public ResponseEntity<List<AccountResponseDto>> listAllAccounts(
        @PathVariable("userId") String userId
    ) {
        var accounts = userService.listAllAccounts(userId);

        return ResponseEntity.ok(accounts);
    }
}
