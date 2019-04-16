package activiti.db.web.service.impl;

import activiti.db.web.model.vo.TaskVo;
import activiti.db.web.service.ActivitiConsumerService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.CommentEntityImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author maimaivv
 * @description
 * @date 2019-04-14 06:37
 */
@Service
public class ActivitiConsumerServiceImpl implements ActivitiConsumerService {


    private static Logger logger = LoggerFactory.getLogger(ActivitiConsumerServiceImpl.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean startActivitiProcess(String processDefinitionKey) {

        logger.info("method startActivitiProcess start....");

        logger.info("调用流程存储服务，查询部署数量："
                + repositoryService.createDeploymentQuery().count());

        Map<String, Object> startActivitiVariables = createStartActivitiVariables();

        //流程启动
        ExecutionEntity executionEntity = (ExecutionEntity) runtimeService
                .startProcessInstanceByKey(processDefinitionKey, startActivitiVariables);

        System.out.println("method startActivitiProcess end....");
        return false;
    }

    @Override
    public List<Task> getTasks(String assignee) {
        Optional<List<Task>> tasks = Optional.ofNullable(
                taskService.createTaskQuery()
                        .taskAssignee(assignee)
                        .list());

        tasks.orElse(new ArrayList<>()).stream()
                .forEach(task -> logTaskInfo(task));

        return tasks.orElse(new ArrayList<>());
    }

    @Override
    public List<TaskVo> getTaskVos(String assignee) {
        List<TaskVo> taskVos = this.getTasks(assignee)
                .stream()
                .map(task -> {
                    Comment comment = taskService.getTaskComments(task.getId())
                            .stream()
                            .findFirst()
                            .orElse(new CommentEntityImpl());

                    logger.info(taskService.getVariables(task.getId()).toString());

                    return new TaskVo(task.getId(), task.getName(), comment.getFullMessage());
                })
                .collect(Collectors.toList());



        return taskVos;
    }

    @Override
    public void addComment(String taskId, String processInstanceId, String message){

        taskService.addComment(taskId, processInstanceId, message);

    }


    @Override
    public void deleteComments(String taskId, String processInstId) {
        // 会删除processInstId和同taskId下面的所有的Comment
        taskService.deleteComments(taskId, processInstId);
    }

    @Override
    public void completeTask(String taskId, String userId){
        taskService.claim(taskId, userId);

        taskService.complete(taskId);

    }

    private void logTaskInfo(Task task) {
        logger.info("任务ID:" + task.getId());
        logger.info("任务的办理人:" + task.getAssignee());
        logger.info("任务名称:" + task.getName());
        logger.info("任务的创建时间:" + task.getCreateTime());

        logger.info("流程实例ID:" + task.getProcessInstanceId());
        logger.info("#####################################");
    }

    private Map<String, Object> createStartActivitiVariables() {
        Map<String, Object> startActivitiVariables = new HashMap<>();
        startActivitiVariables.put("apply", "zhangsan");
        startActivitiVariables.put("approve", "lisi");
        return startActivitiVariables;
    }
}
