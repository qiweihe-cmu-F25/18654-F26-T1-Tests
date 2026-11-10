package codenomads.experience_search.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class ExperienceSearchCriteria {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    private Double maxPrice;
    private Double minPrice;
    private String organizer;
    private String name;
    private Double latitude;
    private Double longitude;
    private Integer numberOfParticipants;
}