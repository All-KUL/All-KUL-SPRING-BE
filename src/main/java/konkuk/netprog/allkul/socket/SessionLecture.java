package konkuk.netprog.allkul.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SessionLecture {
    private String lectureName;
    private int maxStudents;
    private int currentStudents;

    public void initStudent(){
        currentStudents = 0;
    }

    public boolean enroll() {
        if (currentStudents >= maxStudents)
            return false;
        currentStudents += 1;
        return true;
    }
}
