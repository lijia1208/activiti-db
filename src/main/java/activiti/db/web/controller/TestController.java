package activiti.db.web.controller;

import activiti.db.web.service.RollbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author maimaivv
 * @description
 * @date 2019-04-14 09:12
 */
@RestController
@RequestMapping(value = "/v1/test", produces = "application/json;charset=utf-8")
public class TestController {

    @Autowired
    RollbackService testRollbackService;

    @RequestMapping("/testRollback")
    public void test1() {
        try {
            testRollbackService.test1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

