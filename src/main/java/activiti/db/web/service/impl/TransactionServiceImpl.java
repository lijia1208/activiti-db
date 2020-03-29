package activiti.db.web.service.impl;

import activiti.db.web.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Transactional(rollbackFor = Exception.class)
    public void test2() {
//        Student studentForInsert = new Student();
//        studentForInsert.setId(new Long(19));
//        studentForInsert.setName("测试11");
//        studentForInsert.setScore(new BigDecimal(69));
//        studentMapper.updateByPrimaryKey(studentForInsert);
        System.out.println(1 / 0);
    }
}
