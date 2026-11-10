package hotelsearch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum RoomRate {
    @JsonAlias("one_person_rate")
    ONE_PERSON_RATE,

    @JsonAlias("two_person_rate")
    TWO_PERSON_RATE,

    @JsonAlias("four_person_rate")
    FOUR_PERSON_RATE;


    RoomRate() {}

}
