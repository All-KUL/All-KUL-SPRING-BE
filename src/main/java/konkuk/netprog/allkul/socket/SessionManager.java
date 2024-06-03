package konkuk.netprog.allkul.socket;

import konkuk.netprog.allkul.common.random.RandomIdGenerator;
import konkuk.netprog.allkul.data.repository.LectureRepository;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class SessionManager {

    private final LectureRepository lectureRepository;
    private final RandomIdGenerator randomIdGenerator;
    private final Map<String, Map<WebSocket, String>> sessionMap;
    private final Map<String, EnrollmentManager> enrollManagerMap;

    public SessionManager(LectureRepository lectureRepository){
        this.lectureRepository = lectureRepository;
        this.randomIdGenerator = new RandomIdGenerator();
        this.sessionMap = new HashMap<>();
        this.enrollManagerMap = new HashMap<>();
    }

    public void joinClient(WebSocket conn, String sessionId, String clientName){
        if(clientName == null)
            clientName = "User" + randomIdGenerator.generateRandomFourDigitString();

        // 만약 현재 sessionMap에 sessionId가 존재하지 않는다면
        if (sessionId == null || !sessionMap.containsKey(sessionId)) {
            // 새로운 SessionID 생성
            sessionId = randomIdGenerator.generateSessionId();
            // SessionMap에 새로운 Session 생성
            sessionMap.put(sessionId, new HashMap<>());
            // 새로 생성된 Session에 WebSocket의 현재 Connection 추가
            sessionMap.get(sessionId).put(conn, clientName);

            // 해당 세션에 대한 새로운 enrollmentMap 생성
            enrollManagerMap.put(sessionId, new EnrollmentManager(lectureRepository));

            // Client에게 새로 생성된 SessionID 전송
            // conn.send(sessionId);
            broadcastMessage(conn,"[create]"+ sessionId);
            log.info("[SessionManager]-[joinClient] [{}] Created New Session [{}]", clientName, sessionId);
        }else {
            // 기존 Session에 WebSocket의 현재 Connection 추가
            sessionMap.get(sessionId).put(conn, clientName);
            broadcastMessage(conn,"["+ clientName + "] Joined Session [" + sessionId + "]");
            log.info("[SessionManager]-[joinClient] Client [{}] Joined Session [{}]", clientName, sessionId);
        }
    }

    public void initEnrollment(WebSocket conn){
        String sessionId = getSessionId(conn);
        if (sessionId != null)
            enrollManagerMap.get(sessionId).initEnrollment();
    }

    // YYYY.MM.DD-HH:MM
    public void setEnrollTime(WebSocket conn, String dateString){
        String sessionId = getSessionId(conn);
        try {
            if (sessionId != null) {
                // 정규식에서 .을 표현하기 위하여 \\ 처럼 백슬래시 2개를 사용하여 \\.으로 표현
                String[] parts = dateString.split("[\\.:-]");
                int year = Integer.parseInt(parts[0]);
                // 월은 0부터 시작하므로 1을 빼줌
                int month = Integer.parseInt(parts[1]) - 1;
                int day = Integer.parseInt(parts[2]);
                int hour = Integer.parseInt(parts[3]);
                int minute = Integer.parseInt(parts[4]);

                Date newDate = new Date(year - 1900, month, day, hour, minute);
                enrollManagerMap.get(sessionId).setEnrollmentTime(newDate);
                broadcastMessage(conn, "[setEnrollTime]-[success] 수강신청 시간을 <" + newDate + "> 으로 변경하였습니다.");
            }
        } catch(Exception e){
            e.printStackTrace();
            conn.send("[setEnrollTime]-[fail] Invalid Date Format!");
        }
    }

    public void addLecture(WebSocket conn, String lectureID){
        String sessionId = getSessionId(conn);
        String msg = enrollManagerMap.get(sessionId).addLecture(lectureID);
        if (sessionId != null)
            conn.send(msg);
            //broadcastMessage(conn, enrollManagerMap.get(sessionId).addLecture(lectureID));
    }

    public void deleteLecture(WebSocket conn, String lectureID){
        String sessionId = getSessionId(conn);
        String msg = enrollManagerMap.get(sessionId).deleteLecture(lectureID);
        if (sessionId != null)
            conn.send(msg);
            //broadcastMessage(conn, enrollManagerMap.get(sessionId).deleteLecture(lectureID));
    }

    public void enroll(WebSocket conn, String lectureID){
        String sessionId = getSessionId(conn);
        String msg = enrollManagerMap.get(sessionId).enroll(lectureID);
        if(msg.contains("fail"))
            conn.send(msg);
        else if(sessionId != null)
            conn.send(msg);
            //broadcastMessage(conn, enrollManagerMap.get(sessionId).enroll(lectureID));
    }

    public void broadcastMessage(WebSocket sender, String message) {
        String sessionId = getSessionId(sender);
        String senderName = getSenderName(sender);
        if (sessionId != null) {
            sessionMap.get(sessionId).forEach((conn, name) -> {
                conn.send("[" + senderName +"]-" + message);
            });
        }
    }

    public void removeFromSession(WebSocket conn) {
        // 현재 Conn이 속해있는 SessionID 추출
        String sessionId = getSessionId(conn);
        if (sessionId != null) {
            broadcastMessage(conn,"["+ sessionMap.get(sessionId).get(conn) + "] Left Session [" + sessionId + "]");
            // SessionID에 해당하는 Session에서 WebSocket Conn 제거
            sessionMap.get(sessionId).remove(conn);
            log.info("[SessionManager]-[removeFromSession] Client Removed from Session [{}]", sessionId);
            // 만약 현재 세션이 비었을 경우
            if (sessionMap.get(sessionId).isEmpty()) {
                // 해당 세션과 Enrollement를 SessionMap에서 제거
                sessionMap.remove(sessionId);
                enrollManagerMap.remove(sessionId);
                log.info("[SessionManager]-[removeFromSession] Session [{}] Removed", sessionId);
            }
        }
    }

    // SessionMap의 Conn이 속해있는 SessionID를 반환하는 메소드
    private String getSessionId(WebSocket conn) {
        return sessionMap.entrySet().stream()
                .filter(entry -> entry.getValue().containsKey(conn))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private String getSenderName(WebSocket conn) {
        String sessionID =  getSessionId(conn);
        return sessionMap.get(sessionID).get(conn);
    }
}
