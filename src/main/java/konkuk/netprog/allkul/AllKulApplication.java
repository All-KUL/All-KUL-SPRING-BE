package konkuk.netprog.allkul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// DataSource Exclude 옵션 추가
// SecurityAutoConfiguration Exclude 옵션 추가
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})
public class AllKulApplication {

	public static void main(String[] args) {
		SpringApplication.run(AllKulApplication.class, args);
	}

}
