package hht.dragon.service;

import hht.dragon.repository.DataRepository;
import hht.dragon.entity.DataTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务管理示例
 * <p>
 * User : Dragon_hht
 * Date : 17-4-10
 * Time : 下午3:59
 */
@Service
public class DataService {

    @Autowired
    private DataRepository dataRepository;

    @Transactional
    public void addTwo() {
        DataTest dataTest = new DataTest();
        dataTest.setAge(19);
        dataTest.setName("90");
        dataRepository.save(dataTest);

        DataTest dataTest_1 = new DataTest();
        dataTest_1.setAge(34);
        dataTest_1.setName("op90909090");
        dataRepository.save(dataTest_1);

    }

}
