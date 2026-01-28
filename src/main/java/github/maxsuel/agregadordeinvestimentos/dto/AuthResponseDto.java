package github.maxsuel.agregadordeinvestimentos.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response after a successful login")
public record AuthResponseDto(

    @Schema(
        description = "JWT token used for authenticated requests",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    String accessToken,

    @Schema(
        description = "The authenticated user details",
        implementation = UserDto.class
    )
    UserDto user

) {
}