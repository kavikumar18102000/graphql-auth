package com.auth.graphqlauth;

import com.auth.graphqlauth.record.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyRecord.class)
public class GraphqlAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlAuthApplication.class, args);
    }

}
