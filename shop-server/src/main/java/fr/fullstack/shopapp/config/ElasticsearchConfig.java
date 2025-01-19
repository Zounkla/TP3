package fr.fullstack.shopapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "fr.fullstack.shopapp.repository.elasticsearch")
public class ElasticsearchConfig {
}
