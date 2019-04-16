package activiti.db.web.service;

import activiti.db.web.model.vo.TaskVo;
import org.activiti.engine.task.Task;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author maimaivv
 * @description
 * @date 2019-04-17 06:18
 */
public interface ActivitiConsumerService {
    @Transactional(rollbackFor = Exception.class)
    boolean startActivitiProcess(String processDefinitionKey);

    List<Task> getTasks(String assignee);

    List<TaskVo> getTaskVos(String assignee);

    void addComment(String taskId, String processInstanceId, String message);

    void deleteComments(String taskId, String processInstId);

    void completeTask(String taskId, String userId);
}
