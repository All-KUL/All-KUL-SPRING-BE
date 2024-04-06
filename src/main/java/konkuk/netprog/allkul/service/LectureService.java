package konkuk.netprog.allkul.service;

import konkuk.netprog.allkul.common.CommonResponse;
import konkuk.netprog.allkul.data.model.LectureModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LectureService {
    CommonResponse createLecture(LectureModel lecture);
    List<LectureModel> getAllLecture();
    LectureModel getLectureById(String id);
    boolean deleteLectureByID(String id);
}
