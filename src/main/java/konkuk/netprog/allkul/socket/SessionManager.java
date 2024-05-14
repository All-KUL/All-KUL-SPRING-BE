package konkuk.netprog.allkul.socket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
public class SessionManager {
    private final Map<String, Map<WebSocket, Boolean>> sessionMap;
    private final Random random;

    public SessionManager(){
        this.sessionMap = new HashMap<>();
        this.random = new Random();
    }

    public void joinClient(WebSocket conn, ClientHandshake handshake){
        // WebSocket Connection으로부터 SessionID Header 받아오기
        String sessionId = handshake.getFieldValue("SessionID");
        // 만약 현재 sessionMap에 sessionId가 존재하지 않는다면
        if (sessionId == null || !sessionMap.containsKey(sessionId)) {
            // 새로운 SessionID 생성
            sessionId = generateSessionId();
            // SessionMap에 새로운 Session 생성
            sessionMap.put(sessionId, new HashMap<>());
            // 새로 생성된 Session에 WebSocket의 현재 Connection 추가
            sessionMap.get(sessionId).put(conn, true);
            // Client에게 새로 생성된 SessionID 전송
            conn.send(sessionId);
            log.info("[SessionManager]-[joinClient] New Session [{}] Created", sessionId);
        }else {
            // 기존 Session에 WebSocket의 현재 Connection 추가
            sessionMap.get(sessionId).put(conn, true);
            conn.send("Successfully Joined Session [" + sessionId + "]");
            log.info("[SessionManager]-[joinClient] New Client Joined Session [{}]", sessionId);
        }
    }

    public void broadcastMessage(WebSocket sender, String message) {
        String sessionId = getSessionId(sender);
        if (sessionId != null) {
            sessionMap.get(sessionId).forEach((conn, isActive) -> {
                if (conn != sender && isActive) {
                    conn.send(message);
                }
            });
        }
    }

    public void removeFromSession(WebSocket conn) {
        // 현재 Conn이 속해있는 SessionID 추출
        String sessionId = getSessionId(conn);
        if (sessionId != null) {
            // SessionID에 해당하는 Session에서 WebSocket Conn 제거
            sessionMap.get(sessionId).remove(conn);
            log.info("[SessionManager]-[removeFromSession] Client Removed from Session [{}]", sessionId);
            // 만약 현재 세션이 비었을 경우
            if (sessionMap.get(sessionId).isEmpty()) {
                // 해당 세션 자체를 SessionMap에서 제거
                sessionMap.remove(sessionId);
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

    // 새로운 SessionID를 생성하는 매소드
    private String generateSessionId() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
