package konkuk.netprog.allkul.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "lecture")
public class LectureModel {

    @Id
    @Column(unique = true)
    private String id;

    @Column(unique = true)
    private String name;

    private String info;
    private int maxStudents;

    @CreatedDate
    private Date createdAt;
}
