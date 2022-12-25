package boardTest.myBoard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	/**
	 * OpenJDK 64-Bit Server VM warning: Options -Xverify:none and -noverify were deprecated in JDK 13 and will likely be removed in a future release.
	 * 해당 경고는 JDK13에서 -Xverify:none, -noverify 옵션이 제거되면서 발생하는 오류
	 * 해결 방법 : Edit Configurations => "Enable Launch Optimization" 체크 해제
	 */
}
