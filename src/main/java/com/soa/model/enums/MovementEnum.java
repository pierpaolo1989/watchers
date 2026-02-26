package com.soa.model.enums;

import lombok.Getter;

@Getter
public enum MovementEnum {
    MECHANICAL("Meccanico"),
    AUTOMATIC("Automatico"),
    QUARTZ("Quarzo"),
    SOLAR("Solare");

    MovementEnum(String value) {
        this.value = value;
    }

    private final String value;

}
