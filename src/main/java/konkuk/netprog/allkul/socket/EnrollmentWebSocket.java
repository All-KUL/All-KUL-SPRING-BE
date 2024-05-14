package konkuk.netprog.allkul.socket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
public class EnrollmentWebSocket extends WebSocketServer {
    private final SessionManager sessionManager;

    public EnrollmentWebSocket() {
        // WebSocketServer 부모 Class에게 Local InetSocketAddress 전송
        super(new InetSocketAddress(8081));
        // 각 세션들을 관리해줄 final SessionManager 선언
        this.sessionManager = new SessionManager();

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
        sessionManager.joinClient(conn, handshake);
    }

    @Override
    // 만약 새로운 Connection이 Close되면 SessionManager로 Client를 Session에서 Remove
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        sessionManager.removeFromSession(conn);
    }

    @Override
    // Client로부터 메세지를 받았을 때 여러 동작을 정의
    public void onMessage(WebSocket conn, String message) {
        sessionManager.broadcastMessage(conn, message);
    }

    @Override
    // 만약 새로운 오류가 발생하면 SessionManager로 Client를 Session에서 Remove
    public void onError(WebSocket conn, Exception ex) {
        sessionManager.removeFromSession(conn);
        ex.printStackTrace();
    }
}