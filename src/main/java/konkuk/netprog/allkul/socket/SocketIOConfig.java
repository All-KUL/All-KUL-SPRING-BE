package konkuk.netprog.allkul.socket;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {

    @Value("${cors.allowed-origins}")
    private String[] corsAllowedOrigins;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        // TODO : 현재 서버 호스트를 자동으로 감지하여 HostName 설정
        // Set Socket Host, Port
        config.setHostname("127.0.0.1");
        config.setPort(9092);
        // Set CORS
        for(String origin : corsAllowedOrigins)
            config.setOrigin(origin);
        return new SocketIOServer(config);
    }
}
