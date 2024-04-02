package konkuk.netprog.allkul.data.repository;

import konkuk.netprog.allkul.data.model.LectureModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LectureRepository extends MongoRepository<LectureModel, String> {

    // READ
    List<LectureModel> findAll();
    LectureModel findFirstById(String id);
    boolean existsById(String id);

    // DELETE
    void deleteById(String id);
}
