package dev.dokan.dokan_java;

public final class DokanyException extends RuntimeException {

    private final int errorCode;

    public DokanyException(Exception e) {
        super(e);
        this.errorCode = Integer.MIN_VALUE;
    }

    public DokanyException(String message) {
        super(message);
        this.errorCode = Integer.MIN_VALUE;
    }
    public DokanyException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DokanyException(Throwable cause, int errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public DokanyException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = Integer.MIN_VALUE;
    }

    public DokanyException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString(){
        return String.format("%s, cause: %s, error code: %s",getMessage(),getCause(),errorCode);
    }

}