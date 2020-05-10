package spring.cloud.study2.service.service;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Description.
 *
 * @author: huang
 * Date: 18-2-12
 */

@RestController
public class Provider {

    private final Logger logger = Logger.getLogger(getClass());

    @RequestMapping(value = "/add" ,method = RequestMethod.GET)
    public Integer add(@RequestParam Integer a, @RequestParam Integer b, HttpServletRequest request) {
        logger.info("HOST: " + request.getRequestURI());
        return a + b;
    }

}
