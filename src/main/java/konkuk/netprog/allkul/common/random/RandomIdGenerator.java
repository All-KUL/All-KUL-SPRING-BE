package konkuk.netprog.allkul.common.random;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomIdGenerator {
    public String generateRandomFourDigitString() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        return String.format("%04d", randomNumber);
    }
}
