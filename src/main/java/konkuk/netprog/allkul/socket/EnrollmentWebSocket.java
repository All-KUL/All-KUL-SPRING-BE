package konkuk.netprog.allkul.socket;

import konkuk.netprog.allkul.data.repository.LectureRepository;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Date;

@Slf4j
@Component
public class EnrollmentWebSocket extends WebSocketServer {
    private final SessionManager sessionManager;

    @Autowired
    public EnrollmentWebSocket(LectureRepository lectureRepository) {
        // WebSocketServer 부모 Class에게 Local InetSocketAddress 전송
        super(new InetSocketAddress(8081));
        // 각 세션들을 관리해줄 final SessionManager 선언
        this.sessionManager = new SessionManager(lectureRepository);

        // WebSocketServer start()
        super.start();
    }

    @Override
    // WebSocketServer가 정상적으로 실행되면 log 출력
    public void onStart() {
        log.info("[EnrollmentWebSocket] WebSocket is running at PORT 8081");
    }

    @Override
    // 만약 새로운 Connection이 Open되면 SessionManager로 Client JOIN
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    }

    @Override
    // 만약 새로운 Connection이 Close되면 SessionManager로 Client를 Session에서 Remove
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        sessionManager.removeFromSession(conn);
    }

    @Override
    // Client로부터 메세지를 받았을 때 여러 동작을 정의
    // "[명령어]데이터"와 같은 형식으로 메세지 구성
    public void onMessage(WebSocket conn, String message) {
        String[] parts = message.split("]", 2);
        if (parts.length == 2) {
            // 명령어의 [ 부분을 제거
            String command = parts[0].substring(1);
            String content = parts[1];
            content = content.trim();

            log.info("[EnrollmentWebSocket]-[onMessage] {} : {}", command, content);
            if(command.equals("joinClient")) {
                String[] tmp = content.split(",");
                String sessionId = tmp[0];
                String clientName = tmp[1];
                sessionManager.joinClient(conn, sessionId, clientName);
            } else if (command.equals("initEnrollment")) {
                sessionManager.initEnrollment(conn);
            } else if(command.equals("setEnrollTime")) {
                sessionManager.setEnrollTime(conn, content);
            } else if(command.equals("addLecture")) {
                sessionManager.addLecture(conn, content);
            } else if(command.equals("deleteLecture")) {
                sessionManager.deleteLecture(conn, content);
            } else if(command.equals("enroll")) {
                sessionManager.enroll(conn, content);
            } else if(command.equals("botEnroll")) {
                sessionManager.botEnroll(conn, content);
            } else if(command.equals("serverTime")){
                conn.send((new Date()).toString());
            } else if(command.equals("chat")){
                sessionManager.broadcastMessage(conn, "[chat] " + content);
            }
        } else {
           conn.send("명령어 형식이 올바르지 않습니다.");
        }
    }

    @Override
    // 만약 새로운 오류가 발생하면 에러 로그 출력
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }
}