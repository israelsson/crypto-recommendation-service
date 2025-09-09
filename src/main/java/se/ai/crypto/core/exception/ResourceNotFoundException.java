package se.ai.crypto.core.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message, Exception e) {
        super(message, e);
    }
}
