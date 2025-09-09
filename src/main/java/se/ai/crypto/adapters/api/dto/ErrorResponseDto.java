package se.ai.crypto.adapters.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {
    String message;
    String type;
}
