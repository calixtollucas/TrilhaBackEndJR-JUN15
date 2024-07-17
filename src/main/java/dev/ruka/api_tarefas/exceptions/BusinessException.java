package dev.ruka.api_tarefas.exceptions;

public class BusinessException extends RuntimeException {

    String exceptionThrownName;

    public BusinessException(String exceptionThrown, String message) {
        super(message);
        this.exceptionThrownName = exceptionThrown;
    }
}
