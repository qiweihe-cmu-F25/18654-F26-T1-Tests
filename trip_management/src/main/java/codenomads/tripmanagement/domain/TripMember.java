package codenomads.tripmanagement.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "trip_members")
@Data
public class TripMember {
    @Id
    @NotNull
    @Column(nullable = false)
    private Long userId;

    @Id
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    private Trip trip;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private Role role = Role.MEMBER;

    public enum Role {
        CREATOR,
        MEMBER
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripMember that = (TripMember) o;
        return userId.equals(that.userId) && trip.equals(that.trip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, trip.getId());
    }
}
