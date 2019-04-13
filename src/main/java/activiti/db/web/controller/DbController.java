package activiti.db.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

/**
 * @author maimaivv
 * @description
 * @date 2019-04-13 06:51
 */
@RestController
@RequestMapping(value = "/v1/activiti", produces = "application/json;charset=utf-8")
public class DbController {

    @GetMapping("/db")
    public String index(@RequestParam("dbName") String dbName) {
        return MessageFormat.format("init activiti db {0}", dbName);
    }


}
