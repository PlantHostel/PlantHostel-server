package com.planthostelserver.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResidenceType {
    ONE("1인 가구"), TWO("2인 가구"), THREE("3인 가구"), FOUR("4인 가구");

    private final String description;
}
