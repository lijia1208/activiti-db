package activiti.db.web.service.impl;

import activiti.db.web.entity.DummyTenantInfoHolder;
import activiti.db.web.model.vo.TaskVo;
import activiti.db.web.service.ActivitiConsumerService;
import com.mysql.jdbc.StringUtils;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.multitenant.TenantInfoHolder;
import org.activiti.engine.impl.persistence.entity.CommentEntityImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private IdentityService identityService;

    @Autowired
    private DummyTenantInfoHolder dummyTenantInfoHolder;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean startActivitiProcess(String processDefinitionKey, String userId) {

        userId = RandomUtils.nextInt(0, 20000) +"admin";

        dummyTenantInfoHolder.setCurrentUserId(userId);
        //TODO 无需每次部署最新的，在高并发场景下，版本时按数字递增的，会冲突
//        Deployment deployment = repositoryService.createDeployment()
//                .addClasspathResource("processes/leave-with-form.bpmn20.xml")
//                .deploy();

        logger.info("method startActivitiProcess start....");

//        logger.info("调用流程存储服务，查询部署数量："
//                + repositoryService.createDeploymentQuery().count());

        String tenantId = "";
        //String tenantId = "001";
        String businessKey = UUID.randomUUID().toString();

        Map<String, Object> startActivitiVariables = createStartActivitiVariables();

        //设置流程的发起用户，并在变量中放入initiator
        identityService.setAuthenticatedUserId(userId);
        //流程启动
        ExecutionEntity executionEntity = (ExecutionEntity) runtimeService
                .startProcessInstanceByKeyAndTenantId(processDefinitionKey, businessKey, startActivitiVariables, tenantId);
        identityService.setAuthenticatedUserId(null);

//        taskService.setAssignee("12513","libi");
        System.out.println("method startActivitiProcess end....");


        return false;
    }

    @Override
    public Stream<Task> getTasks(String assignee) {
        return getTasksByTaskQuery(getTaskQuery(assignee)).stream();
    }

    @Override
    public Stream<Task> getProcessTasks(String assignee, String processDefinitionKey) {
        return getTasksByTaskQuery(getTaskQuery(assignee, processDefinitionKey))
                .stream();
    }

    private List<Task> getTasksByTaskQuery(@NotNull TaskQuery taskQuery) {

        List<Task> tasks = Optional.ofNullable(
                taskQuery.list()).orElse(new ArrayList<>());

        recordTaskInfo(tasks);


        return tasks;
    }

    private void recordTaskInfo(List<Task> tasks) {
        tasks.forEach(task -> logTaskInfo(task));
    }


    private TaskQuery getTaskQuery(@NotNull String assignee) {
        return getTaskQuery(assignee, "");
    }

    private TaskQuery getTaskQuery(@NotNull String assignee, String processDefinitionKey) {
    return getTaskQuery(assignee, processDefinitionKey, "");
    }

    // TODO @NotNull注解的使用方式
    private TaskQuery getTaskQuery(@NotNull String assignee, String processDefinitionKey, String demoPrarm) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskAssignee(assignee);

        taskQuery = StringUtils.isNullOrEmpty(processDefinitionKey) ?
                taskQuery : taskQuery.processDefinitionKey(processDefinitionKey);

        return taskQuery;
    }




    @Override
    public List<TaskVo> getTaskVos(String assignee) {
        List<TaskVo> taskVos = this.getTasks(assignee)
                .map(task -> createTaskVo(task))
                .collect(Collectors.toList());


        return taskVos;
    }

    @Override
    public List<TaskVo> getProcessTaskVos(String processDefinitionKey, String assignee) {

        List<TaskVo> taskVos = this.getProcessTasks(processDefinitionKey, assignee)
                .map(task -> createTaskVo(task))
                .collect(Collectors.toList());


        return taskVos;
    }

    private TaskVo createTaskVo(Task task) {
        Comment comment = taskService.getTaskComments(task.getId())
                .stream()
                .findFirst()
                .orElse(new CommentEntityImpl());

        logger.info(taskService.getVariables(task.getId()).toString());

        return new TaskVo(task.getId(), task.getName(), comment.getFullMessage());
    }

    @Override
    public void addComment(String taskId, String processInstanceId, String message) {

        taskService.addComment(taskId, processInstanceId, message);

    }


    @Override
    public void deleteComments(String taskId, String processInstId) {
        // 会删除processInstId和同taskId下面的所有的Comment
        taskService.deleteComments(taskId, processInstId);
    }

    @Override
    public void completeTask(String taskId, String userId) {
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
        //发起流程时，activiti会从authenticatedUserId中读取用户，并放入initiator中
        //然后才会处理Variables.
        //所以Variables中的同名变量会再次更新initiator
        //startActivitiVariables.put("initiator","admin后放的");
        startActivitiVariables.put("apply", "zhangsan");
        startActivitiVariables.put("approve", "lisi");

        return startActivitiVariables;
    }
}
