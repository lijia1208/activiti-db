package activiti.db;

import org.activiti.engine.impl.db.DbSchemaCreate;
import org.activiti.engine.impl.db.DbSchemaUpdate;
import org.activiti.spring.boot.DataSourceProcessEngineAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author maimaivv
 * @description
 * @date 2019-04-13 06:20
 */

@SpringBootApplication(scanBasePackages = {"activiti.db"}, exclude = {DataSourceProcessEngineAutoConfiguration.class})
public class ActivitiDbApplication {
    public static void main(String[] args) {
        // 读取activiti.cfg.xml中的配置，手动创建表
        //DbSchemaCreate.main(args);
        //DbSchemaUpdate.main(args);
        SpringApplication.run(ActivitiDbApplication.class, args);
    }
}
