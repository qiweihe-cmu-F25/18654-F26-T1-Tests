package hotelsearch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum RoomType {
    @JsonAlias("onePersonRooms")
    @JsonProperty("one_person_rooms")
    ONE_PERSON_ROOMS,

    @JsonAlias("twoPersonRooms")
    @JsonProperty("two_person_rooms")
    TWO_PERSON_ROOMS,

    @JsonAlias("fourPersonRooms")
    @JsonProperty("four_person_rooms")
    FOUR_PERSON_ROOMS;

    RoomType() { }


}
