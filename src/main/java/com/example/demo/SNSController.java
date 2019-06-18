package com.example.demo;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SNSController {

    @RequestMapping(method = RequestMethod.GET, value = "/createTopicInSns")
    public @ResponseBody String createTopic() {

        AWSCredentials credentials = new BasicAWSCredentials(
                "AKIAQWLB5HJC7WYZGG46",
                "vXTuOGlx3rDWAq7ie+Xv0Y42co4MFImKl/q9t2/C"
        );


        return "";
    }
}
