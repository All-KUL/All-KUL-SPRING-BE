package konkuk.netprog.allkul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// DataSource Exclude 옵션 추가
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class AllKulApplication {

	public static void main(String[] args) {
		SpringApplication.run(AllKulApplication.class, args);
	}

}
