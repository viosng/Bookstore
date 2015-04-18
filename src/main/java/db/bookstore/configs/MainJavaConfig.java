package db.bookstore.configs;

/**
 * Created by vio on 18.04.2015.
 */

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@PropertySource("classpath:db.properties")
@ComponentScan(basePackages = {"db.bookstore"})
@Configuration
@EnableAspectJAutoProxy
@ImportResource("classpath:library-conf.xml")
public class MainJavaConfig  {
    @Bean
    public static PropertySourcesPlaceholderConfigurer configurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}