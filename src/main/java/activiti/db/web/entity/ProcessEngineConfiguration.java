package activiti.db.web.entity;

import activiti.db.web.service.impl.ActivitiConsumerServiceImpl;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProcessEngineConfiguration implements ProcessEngineConfigurationConfigurer {

    private static Logger logger = LoggerFactory.getLogger(ProcessEngineConfiguration.class);

    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");

        logger.info("配置字体:" + processEngineConfiguration.getActivityFontName());
    }

}
