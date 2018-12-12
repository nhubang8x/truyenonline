package online.hthang.truyenonline;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEncryptableProperties
@EnableScheduling
public class TruyenOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(TruyenOnlineApplication.class, args);
    }
}
