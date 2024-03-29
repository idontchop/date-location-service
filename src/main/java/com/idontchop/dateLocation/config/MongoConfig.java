package com.idontchop.dateLocation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


@Configuration
@EnableReactiveMongoRepositories
public class MongoConfig {
	
	/*
    @Value("${spring.data.mongodb.host}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port}")
    private String mongoPort;

    @Value("${spring.data.mongodb.database}")
    private String mongoDB;*/
  
    
    @Bean
    public MongoClient mongo() {
        return MongoClients.create();
    }
 
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), "test-loc");
    }

	protected String getDatabaseName() {
		return "test-loc";
	}


}
