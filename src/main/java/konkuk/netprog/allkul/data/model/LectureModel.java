package konkuk.netprog.allkul.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
