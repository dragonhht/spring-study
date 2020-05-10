package spring.cloud.study2.consumer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import spring.cloud.study2.consumer.service.ConsumerService;

import javax.annotation.Resource;

/**
 * 消费者.
 *
 * @author: huang
 * Date: 18-2-12
 */
@RestController
public class ConsumerController {

    @Resource
    private ConsumerService consumerService;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return consumerService.addService();
    }
}
