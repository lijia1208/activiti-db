package activiti.db.web.service.impl;

import activiti.db.web.service.RollbackService;
import activiti.db.web.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RollbackServiceImpl implements RollbackService {
    @Autowired
    TransactionService transactionService;

    @Transactional(rollbackFor = Exception.class)
    public void test1() {
        try {
            transactionService.test2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
