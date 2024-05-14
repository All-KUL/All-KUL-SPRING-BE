package konkuk.netprog.allkul.common.random;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomIdGenerator {
    Random random = new Random();
    public String generateRandomFourDigitString() {
        random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        return String.format("%04d", randomNumber);
    }

    // 새로운 SessionID를 생성하는 매소드
    public String generateSessionId() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
