package com.learning.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InfoMessages {

    SAVING_DATA_IN_MONGO("saving data in mongo..."),
    SAVING_DATA_IN_JPA("saving data in jpa..."),
    UPDATING_DATA_IN_MONGO("updating data in mongo..."),
    UPDATING_DATA_IN_JPA("updating data in jpa..."),
    DELETING_DATA_IN_JPA("deleting data in jpa..."),
    DELETING_DATA_IN_MONGO("deleting data in mongo..."),
    DELETED_SUCCESSFULLY("successfully deleted data for id : %s");


    private final String infoMessage;
}
