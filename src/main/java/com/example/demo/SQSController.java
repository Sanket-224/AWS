package com.example.demo;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import com.amazonaws.auth.AWSCredentials;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import com.example.model.InputCreateQueueRequest;
import com.example.model.InputMessageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SQSController {

    @RequestMapping(method = RequestMethod.POST, value = "/createQueueInSqs")
    public @ResponseBody String createQueue(@RequestBody InputCreateQueueRequest queueName) {

        AWSCredentials credentials = new BasicAWSCredentials(
                "AKIAQWLB5HJC7WYZGG46",
                "vXTuOGlx3rDWAq7ie+Xv0Y42co4MFImKl/q9t2/C"
        );

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_WEST_1)
                .build();

        try {
            //Create a queue.
            System.out.println("Creating a new SQS queue called " + queueName.getQueueName());
            final CreateQueueRequest createQueueRequest =
                    new CreateQueueRequest("MyQueue2");
            final String myQueueUrl = sqs.createQueue(createQueueRequest)
                    .getQueueUrl();

            return myQueueUrl;

        } catch (final AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means " +
                    "your request made it to Amazon SQS, but was " +
                    "rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
            return "error";
        } catch (final AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means " +
                    "the client encountered a serious internal problem while " +
                    "trying to communicate with Amazon SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            return "error";
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAllQueuesFromSqs")
    public @ResponseBody String listAllQueues() throws SQLException {

        AWSCredentials credentials = new BasicAWSCredentials(
                "AKIAQWLB5HJC7WYZGG46",
                "vXTuOGlx3rDWAq7ie+Xv0Y42co4MFImKl/q9t2/C"
        );

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_WEST_1)
                .build();

        //List all queues.
        System.out.println("Listing all queues in your account.\n");
        String queueURL = "";
        for (final String queueUrl : sqs.listQueues().getQueueUrls()) {
            System.out.println("  QueueUrl: " + queueUrl);
            queueURL += queueUrl;
        }
        return queueURL;

    }

    @RequestMapping(method = RequestMethod.POST, value = "/sendMessageToSqs")
    public @ResponseBody String sendMessage(@RequestBody InputMessageRequest msg) {

        AWSCredentials credentials = new BasicAWSCredentials(
                "AKIAQWLB5HJC7WYZGG46",
                "vXTuOGlx3rDWAq7ie+Xv0Y42co4MFImKl/q9t2/C"
        );

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_WEST_1)
                .build();

        // Send a message.
        String myQueueUrl = sqs.getQueueUrl(msg.getQueueName()).getQueueUrl();
        System.out.println("Sending a message to MyQueue.\n");
        final String msgId = sqs.sendMessage(new SendMessageRequest(myQueueUrl, msg.getMessage())).getMessageId();

        return "Message ID: " + msgId;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/receiveMessageFromSqs")
    public @ResponseBody String receiveMessage(@RequestBody InputCreateQueueRequest queueName) {

        AWSCredentials credentials = new BasicAWSCredentials(
                "AKIAQWLB5HJC7WYZGG46",
                "vXTuOGlx3rDWAq7ie+Xv0Y42co4MFImKl/q9t2/C"
        );

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_WEST_1)
                .build();

        // Receive messages
        System.out.println("Receiving messages from MyQueue.\n");
        String myQueueUrl = sqs.getQueueUrl(queueName.getQueueName()).getQueueUrl();
        final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl).withWaitTimeSeconds(20);
        final List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        for (final Message message : messages) {
            System.out.println("Message");
            System.out.println("  MessageId:     " + message.getMessageId());
            System.out.println("  ReceiptHandle: " + message.getReceiptHandle());
            System.out.println("  MD5OfBody:     " + message.getMD5OfBody());
            System.out.println("  Body:          " + message.getBody());
            for (final Entry<String, String> entry : message.getAttributes().entrySet()) {
                System.out.println("Attribute");
                System.out.println("  Name:  " + entry.getKey());
                System.out.println("  Value: " + entry.getValue());
            }
        }
        return Arrays.deepToString(messages.toArray());
    }

}
