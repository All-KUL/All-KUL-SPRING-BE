package konkuk.netprog.allkul.socket.session;

import konkuk.netprog.allkul.common.random.RandomIdGenerator;
import konkuk.netprog.allkul.socket.common.CommonSocketResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Data
@Component
public class SessionManager {
    RandomIdGenerator randomIdGenerator;
    private HashMap<String,EnrollSession> sessions;

    public SessionManager(){
        randomIdGenerator = new RandomIdGenerator();
        sessions = new HashMap<>();
    }

    // 새로운 세션 추가
    public CommonSocketResponse createSession(String userID){
        // 4 Digit으로 Duplicated되지 않은 Session 생성
        String sessionToken = randomIdGenerator.generateRandomFourDigitString();
        while(!sessions.containsKey(sessionToken))
            sessionToken = randomIdGenerator.generateRandomFourDigitString();

        sessions.put(sessionToken, new EnrollSession());

        //Hashmap의 Session에 새로운 User 추가
        sessions.get(sessionToken).addUser(userID);

        System.out.println("[SessionManager]-[createSession] New Session Created");
        // 새롭게 만들어진 Session Token을 CommonSocketResponse으로 Return
        return new CommonSocketResponse("Success", sessionToken);
    }

    // Session 제거
    public CommonSocketResponse deleteSession(String sessionToken){
        sessions.remove(sessionToken);
        System.out.println("[SessionManager]-[deleteSession] Session Deleted");
        return new CommonSocketResponse("Success", sessionToken);
    }
}
