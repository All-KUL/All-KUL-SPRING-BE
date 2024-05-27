package konkuk.netprog.allkul.socket;

import konkuk.netprog.allkul.data.model.LectureModel;
import konkuk.netprog.allkul.data.repository.LectureRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Setter
@Component
public class EnrollmentManager {

    private final LectureRepository lectureRepository;

    private Date enrollmentTime;
    private Map<String, SessionLecture> lectureMap;

    public EnrollmentManager(LectureRepository lectureRepository){
        this.lectureRepository = lectureRepository;
        this.enrollmentTime = new Date();
        this.lectureMap = new HashMap<>();
    }

    public synchronized void initEnrollment() {
        for (Map.Entry<String, SessionLecture> entry : lectureMap.entrySet()) {
            String sessionId = entry.getKey();
            lectureMap.get(sessionId).initStudent();
        }
    }

    public synchronized String addLecture(String id){
        try{
            LectureModel lecture = lectureRepository.findFirstById(id);
            if(lecture == null)
                return "[addLecture]-[fail] 강의가 개설과목 목록에 존재하지 않습니다!";

            String lectureID = lecture.getId();
            if(lectureMap.containsKey(lectureID))
                return "[addLecture]-[fail] 해당 세션이 이미 해당 강의가 존재합니다!";

            lectureMap.put(lectureID, new SessionLecture( lecture.getName(),lecture.getMaxStudents(), 0));
            return "[addLecture]-[success] 성공적으로 강의 <" + lecture.getName() + ">를 세션에 추가하였습니다.";
        }catch (Exception e){
            e.printStackTrace();
            return "[addLecture]-[fail] 강의 추가시 에러가 발생했습니다!";
        }
    }

    public synchronized String deleteLecture(String id){
        if(lectureMap.containsKey(id)) {
            String lectureName = lectureMap.get(id).getLectureName();
            lectureMap.remove(id);
            return "[deleteLecture]-[success] 성공적으로 강의 <" + lectureName + ">를 세션에서 제거하였습니다.";
        }
        return "[deleteLecture]-[fail] 강의가 세션에 존재하지 않습니다!";
    }

    public synchronized String enroll(String id) {
        if (enrollmentTime.before(new Date())) {
            String lectureName = lectureMap.get(id).getLectureName();
            if(lectureMap.get(id).enroll())
                return "[enroll]-[success] <" + lectureName + "> 수강 신청에 성공하였습니다";
            return "[enroll]-[fail] <" + lectureName + "> 수강 인원이 초과되었습니다!";
        }
        return "[enroll]-[fail] 수강신청 기간이 아닙니다!";
    }
}
