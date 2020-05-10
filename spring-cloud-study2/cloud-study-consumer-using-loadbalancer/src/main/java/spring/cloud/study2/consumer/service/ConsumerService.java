package spring.cloud.study2.consumer.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 使用hystrix.
 *
 * @author: huang
 * Date: 18-2-12
 */
@Service
public class ConsumerService {

    @Resource
    private RestTemplate restTemplate;

    /**
     * 使用注解@HystrixCommand指定回调方法.
     * @return
     */
    @HystrixCommand(fallbackMethod = "addfallbackMethod")
    public String addService() {
        return restTemplate.getForEntity("http://PROVIDER-SERVICE/add?a=1&b=3", String.class).getBody();
    }

    public String addfallbackMethod() {
        return "ERROR";
    }

}
