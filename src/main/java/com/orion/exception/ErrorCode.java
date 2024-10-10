package com.orion.exception;

import lombok.Getter;

public enum ErrorCode {

    /* DB ERRORS */
    UNIQUE_FIELD_CONSTRAINT(1000, "The field value must be unique."),
    REQUESTED_DATA_NOT_FOUND(1001, "The requested data could not be found."),
    EXISTED_EMAIL(1002, "This email already exists."),
    EXISTED_NAME(1003, "This name already exists."),

    /* REQUESTS ERRORS */
    BAD_REQUEST(2000, "The request is invalid."),
    DOMAIN_NAME_NOT_EXIST(2001, "The domain name does not exist."),
    USER_IS_NOT_ACTIVATED(2002, "The user is not activated."),
    DOMAIN_EXIST(2003, "The domain already exists."),
    INVALID_TOKEN(2006, "The token provided is invalid."),
    FROM_TO_NOT_NULL(2007, "The from and to values cannot be null."),
    CANCELLATION_TYPE(2008, "The cancellation type is not valid."),
    CURRENT_PASSWORD(2009, "The current password is incorrect."),
    WRONG_STATUS(2011, "The order status type is incorrect."),
    ONLY_PUBLISHED_OR_DRAFT(2012, "The template status must be either published or draft."),
    VEHICLE_NOT_AVAILABLE(2013, "The vehicle is not available."),
    PAYMENT_STATUS_ERROR(2013, "The vehicle status is incorrect.");

    @Getter
    private final int code;
    @Getter
    private final String reasonPhrase;

    ErrorCode(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    public String getMessageTitleKey() {
        String errorPrefix = "errorCode";
        String titlePostfix = "title";
        return errorPrefix + "." + this.name() + "." + titlePostfix;
    }

    @Override
    public String toString() {
        return "Code: " + this.code + ". Title: " + this.getMessageTitleKey() + ".";
    }
}
