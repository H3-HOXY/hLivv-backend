package hoxy.hLivv.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

/**
 * @author 이상원
 */
@Configuration
public class TemplateConfig {
    @Bean
    public TemplateEngine htmlTemplateEngine(SpringResourceTemplateResolver springResourceTemplateResolver) {
        TemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(springResourceTemplateResolver);

        return templateEngine;
    }
}
