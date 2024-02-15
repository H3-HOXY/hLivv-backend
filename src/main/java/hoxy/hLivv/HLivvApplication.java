package hoxy.hLivv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HLivvApplication {

    public static void main(String[] args) {
        SpringApplication.run(HLivvApplication.class, args);
    }

}
