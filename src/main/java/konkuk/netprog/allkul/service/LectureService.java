package konkuk.netprog.allkul.service;

import konkuk.netprog.allkul.common.CommonResponse;
import konkuk.netprog.allkul.data.model.LectureModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LectureService {
    CommonResponse addLecture(LectureModel lecture);
    CommonResponse getAllLecture();
    CommonResponse getLectureById(String id);
    CommonResponse deleteLectureByID(String id);
}
