package com.kklop.WizardOfJokes;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.context.config.annotation.EnableContextInstanceData;
import org.springframework.cloud.aws.context.config.annotation.EnableContextResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

import javax.security.auth.login.LoginException;

@SpringBootApplication
@EnableContextInstanceData
@EnableContextResourceLoader
public class WizardOfJokesApplication {

	public static void main(String[] args) {
		SpringApplication.run(WizardOfJokesApplication.class, args);

	}

	@Value("${TOKEN}")
    private String token;

	@Bean
    @Autowired
    public ReadyListener readyListener(ResourceLoader resourceLoader,
                                       @Value("${S3_URL}") String s3Url) {
	    return new ReadyListener(resourceLoader, s3Url);
    }

	@Bean
    public JDA jda(@Autowired ReadyListener readyListener) throws LoginException {
        return new JDABuilder(token)
                .addEventListener(readyListener)
                .build();
    }



}
