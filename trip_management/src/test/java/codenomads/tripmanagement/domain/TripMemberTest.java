package codenomads.tripmanagement.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TripMemberTest {

    private TripMember tripMember;
    private Trip testTrip;

    @BeforeEach
    void setUp() {
        tripMember = new TripMember();
        testTrip = new Trip();
        testTrip.setId(1L);
        testTrip.setTripName("Test Trip");
    }

    @Test
    void setAndGetUserIdShouldWorkCorrectly() {
        Long expectedUserId = 123L;
        tripMember.setUserId(expectedUserId);
        
        assertEquals(expectedUserId, tripMember.getUserId());
    }

    @Test
    void setAndGetTripShouldWorkCorrectly() {
        tripMember.setTrip(testTrip);
        
        assertEquals(testTrip, tripMember.getTrip());
    }

    @Test
    void getRoleDefaultValueShouldBeMember() {
        TripMember newMember = new TripMember();
        
        assertEquals(TripMember.Role.MEMBER, newMember.getRole());
    }

    @Test
    void roleEnumAllValuesShouldExist() {
        TripMember.Role[] roles = TripMember.Role.values();
        
        assertEquals(2, roles.length);
        assertTrue(java.util.Set.of(roles).contains(TripMember.Role.CREATOR));
        assertTrue(java.util.Set.of(roles).contains(TripMember.Role.MEMBER));
    }

    @Test
    void roleEnumNamesShouldBeCorrect() {
        assertEquals("CREATOR", TripMember.Role.CREATOR.name());
        assertEquals("MEMBER", TripMember.Role.MEMBER.name());
    }
}