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

    @PostMapping(value = "/create")
    public CommonResponse createLecture(@RequestParam String id, @RequestParam String name, @RequestParam String info, @RequestParam int maxStudents){
        log.info("[LectureController]-[saveLecture] Call LectureService saveLecture method");
        return lectureService.createLecture(new LectureModel(id, name, info, maxStudents, new Date()));
    }
}