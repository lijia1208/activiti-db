package activiti.db.web.controller;

import activiti.db.web.model.vo.TaskVo;
import activiti.db.web.service.ActivitiConsumerService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

/**
 * @author maimaivv
 * @description
 * @date 2019-04-13 06:51
 */
@RestController
@RequestMapping(value = "/v1/activiti", produces = "application/json;charset=utf-8")
public class DbController {

    public static String processDefinitionKey = "leave";

    @Autowired
    private ActivitiConsumerService activitiConsumerService;


    @GetMapping("db")
    public String index(@RequestParam("dbName") String dbName) {

        return MessageFormat.format(" other develop init activiti db {0}", dbName);
    }

    @PostMapping("process")
    public void startProcessInstance() {
        activitiConsumerService.startActivitiProcess(DbController.processDefinitionKey);
    }

    @GetMapping("tasks")
    public List<Task> getTasks(@RequestParam String assignee) {
        List<Task> tasks = activitiConsumerService.getTasks(assignee);

        // lazy loading outside command context
        // 如果不指定要获取的字段，Json转换的时候会有懒加载问题
        return tasks;
    }


    @GetMapping("taskVos")
    public List<TaskVo> getTaskVos(@RequestParam String assignee) {

        return activitiConsumerService.getTaskVos(assignee);
    }

    @PostMapping("createComment")
    public void createComment(@RequestParam String taskId, @RequestParam String processInstId, @RequestParam String comment) {
        activitiConsumerService.addComment(taskId, processInstId, comment);
    }

    @DeleteMapping("deleteComments")
    public void deleteComments(@RequestParam String taskId, @RequestParam String processInstId){
        activitiConsumerService.deleteComments(taskId, processInstId);
    }

    @PostMapping("completeTask")
    public void completeTask(@RequestParam String taskId, @RequestParam String userId) {
        activitiConsumerService.completeTask(taskId, userId);
    }


}
