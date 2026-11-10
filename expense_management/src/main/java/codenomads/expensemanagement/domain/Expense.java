package codenomads.expensemanagement.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import lombok.Data;

@Entity
@Table(name = "expenses")
@Data
public class Expense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private ExpenseSource source;

    @ElementCollection
    private Set<Long> responsibleUserIds = new HashSet<>();

    @NotNull
    @Column(nullable = false)
    @ColumnDefault("0")
    private Long tripId;

    @NotNull
    @Column(nullable = false)
    private Long createdByUserId;

    @Column(nullable = false)
    private LocalDate occurredOn;

    public enum ExpenseSource {
        HOTEL,
        FLIGHT,
        CARHIRE,
        EXPERIENCE
    }
}