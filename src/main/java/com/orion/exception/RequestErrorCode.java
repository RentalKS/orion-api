package com.orion.exception;

public enum RequestErrorCode {

    BAD_REQUEST(2000, "The request is invalid."),
    DOMAIN_NAME_NOT_EXIST(2001, "The domain name does not exist."),
    USER_IS_NOT_ACTIVATED(2002, "The user is not activated."),
    DOMAIN_EXIST(2003, "The domain already exists."),
    PROJECT_CATEGORY_NAME_EXIST(2004, "The project category name already exists."),
    INVALID_TOKEN(2006, "The token provided is invalid."),
    FROM_TO_NOT_NULL(2007, "The from and to values cannot be null."),
    CANCELLATION_TYPE(2008, "The cancellation type is not valid."),
    CURRENT_PASSWORD(2009, "The current password is incorrect."),
    TARIFF_COMMISSION_VALID(2010, "The tariff commission value is not valid."),
    WRONG_STATUS(2011, "The order status type is incorrect."),
    ONLY_PUBLISHED_OR_DRAFT(2012, "The template status must be either published or draft.");

    private final int code;
    private final String message;

    RequestErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return "Code: " + this.code + ", Message: " + this.message;
    }
}
