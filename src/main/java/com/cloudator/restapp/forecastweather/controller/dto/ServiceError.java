package com.cloudator.restapp.forecastweather.controller.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


/**
 * Generic DTO used to provide error details to the client
 */
public final class ServiceError implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique ID used to identify the current instance
     */
    private final String uuid;

    /**
     * String with the code that identify the type of error (the exception class or Error)
     */
    private String type;

    /**
     * String with any message to show to the user regarding the error
     */
    private String message;

    /**
     * String with the details of the root cause
     */
    private String rootCause;

    /**
     * Array with the stack for the exception (if any exception causes the error)
     */
    private String[] stack;

    /**
     * Creates a service error with a specific message and a exception detail as the root cause
     *
     * @param message   String with the error message
     * @param exception Instance of the exception that causes the error
     */
    public ServiceError(String message, Exception exception) {
        this.uuid = String.valueOf(UUID.randomUUID());
        if (message != null) {
            this.message = message;
            if (exception == null) {
                this.type = "Error";
            } else {
                this.type = exception.getClass().getName();
                this.rootCause = exception.getMessage();
            }
        } else if (exception != null) {
            this.message = exception.getMessage();
            if (exception.getCause() != null) {
                this.rootCause = exception.getCause().getMessage();
            }
            this.type = exception.getClass().getName();
        }
        if (exception != null) {
            StackTraceElement[] exStackArray = exception.getStackTrace();
            if (exStackArray != null && exStackArray.length > 0) {
                this.stack = new String[exStackArray.length];
                for (int i = 0; i < exStackArray.length; i++) {
                    this.stack[i] = exStackArray[i].toString();
                }
            }
        }
    }

    /**
     * Creates an instance with a specific error message
     *
     * @param message String with the error message
     */
    public ServiceError(String message) {
        this(message, null);
    }

    /**
     * Creates a service error from a Exception
     *
     * @param exception Instance of the exception that causes the error
     */
    public ServiceError(Exception exception) {
        this(null, exception);
    }

    public String getMessage() {
        return this.message;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceError that = (ServiceError) o;
        return (!Objects.equals(uuid, that.uuid));
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return "ServiceError{" + "uuid='" + uuid + "'" +
                ", type='" + type + "'" +
                ", message='" + message + "'" +
                ", rootCause=\'" + rootCause + "'" +
                '}';
    }

}
