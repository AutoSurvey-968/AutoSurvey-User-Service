package com.revature.autosurvey.users.utils;
//

//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.sqs.AmazonSQSAsync;
//import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
//
//@Configuration
//public class SqsConfig {
//
//	private Regions region = Regions.US_EAST_2;
//
//	@Value("${SQS_USER}")
//	private String awsAccessKey;
//
//	@Value("${SQS_PASS}")
//	private String awsSecretKey;
//
//	@Bean
//	public QueueMessagingTemplate queueMessagingTemplate() {
//		return new QueueMessagingTemplate(amazonSQSAsync());
//	}
//
//	@Primary
//	@Bean
//	public AmazonSQSAsync amazonSQSAsync() {
//		return AmazonSQSAsyncClientBuilder.standard().withRegion(region)
//				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
//				.withE
//				.build();
//	}

//	@Bean
//	public SqsAsyncClient sqsAsyncClient() {
//
//		return SqsAsyncClient.builder().endpointOverride(URI.create()).region(Region.US_EAST_2)
//				.credentialsProvider(StaticCredentialsProvider.create(new AwsCredentials() {
//					@Override
//					public String accessKeyId() {
//						return "${AWS_ACCESS_KEY_ID}";
//					}
//
//					@Override
//					public String secretAccessKey() {
//						return "${AWS_SECRET_ACCESS_KEY}";
//					}
//				})).build();
//	}
// }

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

@Configuration
public class SqsConfig {
	@Value("${cloud.aws.region.static}")
	private String region;

	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String secretKey;

	@Bean
	public QueueMessagingTemplate queueMessagingTemplate() {
		return new QueueMessagingTemplate(amazonSQSAsync());
	}

	public AmazonSQSAsync amazonSQSAsync() {

		AmazonSQSAsyncClientBuilder amazonSQSAsyncClientBuilder = AmazonSQSAsyncClientBuilder.standard();
		AmazonSQSAsync amazonSQSAsync = null;
		amazonSQSAsyncClientBuilder.withRegion(Regions.US_EAST_2);
		BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
		amazonSQSAsyncClientBuilder.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials));
		amazonSQSAsync = amazonSQSAsyncClientBuilder.build();
		return amazonSQSAsync;

	}

}
