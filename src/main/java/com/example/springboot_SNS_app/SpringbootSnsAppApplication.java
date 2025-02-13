package com.example.springboot_SNS_app;

import com.example.springboot_SNS_app.config.AWSNSConfig;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

@SpringBootApplication
@RestController
public class SpringbootSnsAppApplication {

	@Autowired
	private AWSNSConfig amazonSNS;

	@Value(("${cloud.aws.topic-endPoint.uri}"))
	private String topicName;

	@GetMapping("/addSubscription/{email}")
	public String addSubscrption(@PathVariable String email){

		SubscribeRequest request = SubscribeRequest.builder()
				.protocol("email")
				.topicArn(topicName)
				.endpoint(email)
				.build();

		amazonSNS.getSnsClient().subscribe(request);
		return "Subscription request is pending. To confirm the subscription, check your email : " + email;
	}

	@GetMapping("/sendNotification")
	public String publishMessageToTopic(){
		PublishRequest publishRequest = PublishRequest.builder()
				.topicArn(topicName)
				.message(buildEmailBody())
				.subject("Notification: Network connectivity issue")
				.build();

		amazonSNS.getSnsClient().publish(publishRequest);
		return "Notification send successfully !!";
	}



	private String buildEmailBody(){
		return "Dear Employee ,\n" +
				"\n" +
				"\n" +
				"Connection down Bangalore."+"\n"+
				"All the servers in Bangalore Data center are not accessible. We are working on it ! \n" +
				"Notification will be sent out as soon as the issue is resolved. For any questions regarding this message please feel free to contact IT Service Support team";
	}

	public static void main(String[] args) {

		// Load .env variables
		Dotenv dotenv = Dotenv.load();
		System.setProperty("AWS_ACCESS_KEY", dotenv.get("AWS_ACCESS_KEY"));
		System.setProperty("AWS_SECRET_KEY", dotenv.get("AWS_SECRET_KEY"));
		System.setProperty("AWS_SNS_TOPIC_ARN", dotenv.get("AWS_SNS_TOPIC_ARN"));


		SpringApplication.run(SpringbootSnsAppApplication.class, args);
	}

}
