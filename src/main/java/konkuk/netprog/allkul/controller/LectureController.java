package konkuk.netprog.allkul.controller;

import konkuk.netprog.allkul.common.CommonResponse;
import konkuk.netprog.allkul.data.model.LectureModel;
import konkuk.netprog.allkul.service.LectureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping(path="/api/v1/lecture")
public class LectureController {
    @Autowired
    LectureService lectureService;

    @PostMapping(value = "/addLecture")
    public CommonResponse addLecture(@RequestParam String id, @RequestParam String name, @RequestParam String info, @RequestParam int maxStudents){
        log.info("[LectureController]-[saveLecture] Call LectureService saveLecture method");
        return lectureService.addLecture(new LectureModel(id, name, info, maxStudents, new Date()));
    }

    @GetMapping(value = "/getAllLecture")
    public CommonResponse getAllLecture(){
        log.info("[LectureController]-[saveLecture] Call LectureService getAllLecture method");
        return lectureService.getAllLecture();
    }

    @GetMapping(value = "/getLecture")
    public CommonResponse getAllLecture(@RequestParam String id){
        log.info("[LectureController]-[saveLecture] Call LectureService getLectureById method");
        return lectureService.getLectureById(id);
    }

    @DeleteMapping(value = "/deleteLecture")
    public CommonResponse deleteLecture(@RequestParam String id){
        log.info("[LectureController]-[saveLecture] Call LectureService deleteLectureByID method");
        return lectureService.deleteLectureByID(id);
    }
}