package codenomads.tripmanagement.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "trips")
@Data
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String tripName;

    @NotNull
    @Column(nullable = false)
    private Date startDate;

    @NotNull
    @Column(nullable = false)
    private Date endDate;

    @NotNull
    @Column(nullable = false)
    private Long creatorId;

    @ElementCollection
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TripMember> members = new HashSet<>();

    @Embeddable
    @Data
    public static class Booking {
        private Long bookingId;
        @Enumerated(EnumType.STRING)
        private BookingSource source;
    }

    public void addMember(TripMember member) {
        member.setTrip(this);
        members.add(member);
    }

    public void removeMember(Long userId) {
        members.removeIf(member -> member.getUserId().equals(userId));
    }
}