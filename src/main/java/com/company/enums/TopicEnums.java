package com.company.enums;

public enum TopicEnums {
    LIBRARY_EVENTS("library-events");
    private final String message;

    TopicEnums(String message) {
        this.message = message;
    }

    public String getInfo() {
        return message;
    }
}
