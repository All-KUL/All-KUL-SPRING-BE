package konkuk.netprog.allkul.service;

import konkuk.netprog.allkul.data.model.LectureModel;

import java.util.List;

public interface LectureService {
    boolean CreateLecture(LectureModel lecture);
    List<LectureModel> getAllLecture();
    LectureModel getLectureById(String id);
    boolean deleteLectureByID(String id);
}
