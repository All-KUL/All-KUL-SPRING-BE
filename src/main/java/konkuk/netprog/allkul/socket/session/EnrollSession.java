package konkuk.netprog.allkul.socket.session;

import konkuk.netprog.allkul.data.model.LectureModel;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Data
@Component
public class EnrollSession {
    private String sessionToken;
    private HashMap<String, LectureModel> selectedLectures;
    private Data enrollmentTime;
    private HashMap<String, String> users;

    public EnrollSession(){
        selectedLectures = new HashMap<>();
        users = new HashMap<>();
    }

    public void addUser(String userId){
        users.put(userId, userId);
    }

    public void deleteUser(String userId){
        users.remove(userId, userId);
    }
}
