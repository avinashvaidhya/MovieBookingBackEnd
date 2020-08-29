package com.moviebooking.backend;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@SpringBootApplication
public class MovieBookingBackEndApplication {

	@Autowired
	private Environment env;
	
	public static void main(String[] args) {
		SpringApplication.run(MovieBookingBackEndApplication.class, args);
	}

	   @Bean(value = "dataSource")
	    @ConfigurationProperties(prefix =  "spring.datasource")
	    public DataSource dataSource() {
		   DriverManagerDataSource dataSource = new DriverManagerDataSource();
		    dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
		    dataSource.setUrl(env.getProperty("spring.datasource.url"));
		    dataSource.setUsername(env.getProperty("spring.datasource.username"));
		    dataSource.setPassword(env.getProperty("spring.datasource.password"));
		   
		    System.out.println(env.getProperty("spring.datasource.driver-class-name") +" "
		    		+ env.getProperty("spring.datasource.url") + " "
		    		+ env.getProperty("spring.datasource.username") + " "
		    		+ env.getProperty("spring.datasource.password"));
		    
	        return dataSource;
	    }
	
	
}
