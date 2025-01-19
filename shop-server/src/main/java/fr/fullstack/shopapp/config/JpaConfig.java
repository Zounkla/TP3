package fr.fullstack.shopapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "fr.fullstack.shopapp.repository.jpa")
public class JpaConfig {
}
