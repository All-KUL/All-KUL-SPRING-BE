package konkuk.netprog.allkul.service.impl;

import konkuk.netprog.allkul.common.CommonResponse;
import konkuk.netprog.allkul.common.random.RandomIdGenerator;
import konkuk.netprog.allkul.data.model.LectureModel;
import konkuk.netprog.allkul.data.repository.LectureRepository;
import konkuk.netprog.allkul.service.LectureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class LectureServiceImpl implements LectureService {

    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    RandomIdGenerator randomIdGenerator;

    // LectureModel 정보를 통해 Lecture을 생성하는 로직
    @Override
    public CommonResponse addLecture(LectureModel lecture) {

        String newID = lecture.getId();
        // 만약 ID가 겹친다면 겹치지 않을때까지 새로운 4자리 랜덤 ID 생성
        while(lectureRepository.existsById(lecture.getId())){
            newID = randomIdGenerator.generateRandomFourDigitString();
            lecture.setId(newID);
            newID = "Duplicate ID Replaced to " + newID;
        }

        // 같은 강의 이름에 해당하는 강의가 없다면, 저장 가능
        if(!lectureRepository.existsByName(lecture.getName())){
            lectureRepository.save(lecture);
            log.info("[LectureServiceImpl]-[CreateLecture] Saved New Lecture");
            return new CommonResponse(true, HttpStatus.CREATED, "Saved New Lecture : " + newID);
        }
        log.info("[LectureServiceImpl]-[CreateLecture] Lecture Already Exists");
        return new CommonResponse(true, HttpStatus.CONFLICT, "Lecture Name Already Exists");
    }

    // 모든 강의를 Repository로부터 가져오기
    @Override
    public CommonResponse getAllLecture() {
        List<LectureModel> allLecture = lectureRepository.findAll();
        return new CommonResponse(true, HttpStatus.OK, "All Lectures", allLecture);
    }

    @Override
    public CommonResponse getLectureById(String id) {
        LectureModel lecture = lectureRepository.findFirstById(id);

        if(lecture != null) {
            // CommonResponse에 맞추기 위해 새로운 response List<LectureModel> 생성
            List<LectureModel> response = new LinkedList<>();
            response.add(lecture);

            return new CommonResponse(true, HttpStatus.OK, "Get Lecture by ID", response);
        }else {
            return new CommonResponse(false, HttpStatus.NO_CONTENT, "Invalid LectureID");
        }
    }

    @Override
    public CommonResponse deleteLectureByID(String id) {
        LectureModel lecture = lectureRepository.findFirstById(id);

        if(lecture != null) {
            lectureRepository.delete(lecture);
            return new CommonResponse(true, HttpStatus.OK, "Lecture [" + id + "] Deleted");
        }else {
            return new CommonResponse(false, HttpStatus.NO_CONTENT, "Invalid LectureID");
        }
    }
}
