package activiti.db.web.entity;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.asyncexecutor.multitenant.ExecutorPerTenantAsyncExecutor;
import org.activiti.engine.impl.cfg.multitenant.MultiSchemaMultiTenantProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.multitenant.TenantInfoHolder;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MultiSchemaMultiTenantProcessEnginenConfig {

    private static Logger logger = LoggerFactory.getLogger(MultiSchemaMultiTenantProcessEngineConfiguration.class);

    @Primary
    @Bean
    public ProcessEngine processEngine(TenantInfoHolder dummyTenantInfoHolder) {
        MultiSchemaMultiTenantProcessEngineConfiguration engineConfig =
                new MultiSchemaMultiTenantProcessEngineConfiguration(dummyTenantInfoHolder);
        engineConfig.setAsyncExecutorActivate(true);
        engineConfig.setAsyncExecutor(new ExecutorPerTenantAsyncExecutor(dummyTenantInfoHolder));
        engineConfig.setDatabaseType(MultiSchemaMultiTenantProcessEngineConfiguration.DATABASE_TYPE_MYSQL);
        engineConfig.setDatabaseSchemaUpdate(MultiSchemaMultiTenantProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

//        return engineConfig;

        engineConfig.registerTenant("", createDataSource("jdbc:mysql://127.0.0.1:3306/activiti?characterEncoding=UTF-8", "root", "123456", "com.mysql.jdbc.Driver"));
        engineConfig.registerTenant("租户2018", createDataSource("jdbc:mysql://127.0.0.1:3306/activiti_2018?characterEncoding=UTF-8", "root", "123456", "com.mysql.jdbc.Driver"));
        engineConfig.registerTenant("租户2019", createDataSource("jdbc:mysql://127.0.0.1:3306/activiti_2019?characterEncoding=UTF-8", "root", "123456", "com.mysql.jdbc.Driver"));
        engineConfig.registerTenant("租户2020", createDataSource("jdbc:mysql://127.0.0.1:3306/activiti_2020?characterEncoding=UTF-8", "root", "123456", "com.mysql.jdbc.Driver"));


        engineConfig.setActivityFontName("宋体");
        engineConfig.setLabelFontName("宋体");
        engineConfig.setAnnotationFontName("宋体");

        return engineConfig.buildProcessEngine();
    }


    @Bean
    public DummyTenantInfoHolder dummyTenantInfoHolder()
    {
        DummyTenantInfoHolder tenantInfoHolder = new DummyTenantInfoHolder();

        List<String> stubUsers = new ArrayList<>();
        for(int i=0; i<20000; i++)
        {
            stubUsers.add(i + "admin");
        }

        tenantInfoHolder.addTenant("");
        tenantInfoHolder.addUser("", "admin");
        stubUsers.forEach(user-> tenantInfoHolder.addUser("", user));

        tenantInfoHolder.addUser("", "lijia");
        tenantInfoHolder.addUser("", "lililili");
        tenantInfoHolder.addUser("", "libi");

        tenantInfoHolder.addTenant("租户2018");
        tenantInfoHolder.addUser("租户2018", "admin2018");
        tenantInfoHolder.addUser("租户2018", "lijia2018");


        tenantInfoHolder.addTenant("租户2019");
        tenantInfoHolder.addUser("租户2019", "admin2019");
        tenantInfoHolder.addUser("租户2019", "lijia2019");

        tenantInfoHolder.addTenant("租户2020");
        tenantInfoHolder.addUser("租户2020", "admin2020");
        tenantInfoHolder.addUser("租户2020", "lijia2020");

        return tenantInfoHolder;
    }


    public DataSource createDataSource(String jdbcUrl, String jdbcUsername, String jdbcPassword, String driver) {
        Map<String, String> map = new HashMap<>(16);
        map.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, driver);
        map.put(DruidDataSourceFactory.PROP_URL, jdbcUrl);
        map.put(DruidDataSourceFactory.PROP_USERNAME, jdbcUsername);
        map.put(DruidDataSourceFactory.PROP_PASSWORD, jdbcPassword);
        try {
            return DruidDataSourceFactory.createDataSource(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
