package activiti.db;

import org.activiti.engine.impl.db.DbSchemaCreate;
import org.activiti.engine.impl.db.DbSchemaUpdate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author maimaivv
 * @description
 * @date 2019-04-13 06:20
 */
@SpringBootApplication(scanBasePackages = {"activiti.db"})
public class ActivitiDbApplication {
    public static void main(String[] args) {
        // 读取activiti.cfg.xml中的配置，手动创建表
        //DbSchemaCreate.main(args);
        //DbSchemaUpdate.main(args);
        SpringApplication.run(ActivitiDbApplication.class, args);
    }
}
