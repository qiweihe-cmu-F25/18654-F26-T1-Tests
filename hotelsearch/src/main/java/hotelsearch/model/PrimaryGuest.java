package hotelsearch.model;

import java.sql.Date;

public class PrimaryGuest {

    private String firstName;

    private String lastName;

    private String identityCardNumber;

    private Date dateOfBirth;

    public PrimaryGuest(String firstName, String lastName, String identityCardNumber, Date dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.identityCardNumber = identityCardNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }
}
