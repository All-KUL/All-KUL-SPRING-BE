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
    public CommonResponse createLecture(LectureModel lecture) {

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

    // TODO : Create Below Fucntions
    @Override
    public List<LectureModel> getAllLecture() {
        return null;
    }

    @Override
    public LectureModel getLectureById(String id) {
        return null;
    }

    @Override
    public boolean deleteLectureByID(String id) {
        return false;
    }
}
