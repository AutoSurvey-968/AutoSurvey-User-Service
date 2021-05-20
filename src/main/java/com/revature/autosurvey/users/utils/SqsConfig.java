package com.revature.autosurvey.users.utils;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class SqsConfig {

	@Bean
	public SqsAsyncClient sqsAsyncClient() {
		return SqsAsyncClient.builder()
				.endpointOverride(URI.create("https://sqs.us-east-2.amazonaws.com"))
				.region(Region.US_EAST_2)
				.credentialsProvider(StaticCredentialsProvider.create(new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return "${AWS_ACCESS_KEY_ID}";
                    }

                    @Override
                    public String secretAccessKey() {
                        return "${AWS_SECRET_ACCESS_KEY}";
                    }
                }))
                .build();
	}
}
