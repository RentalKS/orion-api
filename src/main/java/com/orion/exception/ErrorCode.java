package com.orion.exception;

public enum ErrorCode {

    /* DB ERRORS */
    UNIQUE_FIELD_CONSTRAINT(1000, "unique.field.constraint"),
    REQUESTED_DATA_NOT_FOUND(1001, "requested.data.not.found"),
    EXISTED_EMAIL(1002, "existed.email"),
    EXISTED_NAME(1003, "existed.name"),

    /* REQUESTS ERRORS */
    BAD_REQUEST(2000, "bad.request"),
    DOMAIN_NAME_NOT_EXIST(2001, "domain.name.not.exist"),
    USER_IS_NOT_ACTIVATED(2002, "user.is.not.activated"),
    DOMAIN_EXIST(2003, "domain.exist"),
    PROJECT_CATEGORY_NAME_EXIST(2004, "project.category.name.exist"),
    INVALID_TOKEN(2006, "invalid.token"),
    FROM_TO_NOT_NULL(2007, "from.to.not.null"),
    CANCELLATION_TYPE(2008, "cancellation.type"),
    CURRENT_PASSWORD(2009, "current.password"),
    TARIFF_COMMISSION_VALID(2010, "tariff.commission.valid"),
    WRONG_STATUS(2011, "order.status.type"),
    ONLY_PUBLISHED_OR_DRAFT(2012, "template.status");

    private final int code;
    private final String reasonPhrase;
    private final String errorPrefix = "errorCode";
    private final String titlePostfix = "title";

    ErrorCode(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    public int getCode() {
        return this.code;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public String getMessageTitleKey() {
        return this.errorPrefix + "." + this.name() + "." + this.titlePostfix;
    }

    @Override
    public String toString() {
        return "Code: " + this.code + ". Title: " + this.getMessageTitleKey() + ".";
    }
}
