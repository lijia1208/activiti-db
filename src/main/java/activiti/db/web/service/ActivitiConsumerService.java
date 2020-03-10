package activiti.db.web.service;

import activiti.db.web.model.vo.TaskVo;
import org.activiti.engine.task.Task;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author maimaivv
 * @description
 * @date 2019-04-17 06:18
 */
public interface ActivitiConsumerService {
    @Transactional(rollbackFor = Exception.class)
    boolean startActivitiProcess(String processDefinitionKey, String userId);

    Stream<Task> getTasks(String assignee);

    Stream getProcessTasks(String processDefinitionKey, String assignee);

    List<TaskVo> getTaskVos(String assignee);

    List<TaskVo> getProcessTaskVos(String processDefinitionKey, String assignee);

    void addComment(String taskId, String processInstanceId, String message);

    void deleteComments(String taskId, String processInstId);

    void completeTask(String taskId, String userId);
}
