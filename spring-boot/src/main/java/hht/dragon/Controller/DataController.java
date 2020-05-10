package hht.dragon.Controller;

import hht.dragon.repository.DataRepository;
import hht.dragon.service.DataService;
import hht.dragon.entity.DataTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 数据库的基本操作
 * <p>
 * User : Dragon_hht
 * Date : 17-4-9
 * Time : 下午4:55
 */
@RestController
public class DataController {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private DataService service;

    //查找所有
    @RequestMapping("/get")
    public List<DataTest> getData() {
        return dataRepository.findAll();
    }

    //添加
    @RequestMapping("/add")
    public DataTest add() {
        String name = "hj";
        Integer age = 3;
        DataTest dataTest = new DataTest();
        dataTest.setAge(age);
        dataTest.setName(name);

        return dataRepository.save(dataTest);
    }

    //查询一个
    @GetMapping(value = "/getone/{id}")
    public DataTest getone(@PathVariable("id") Integer id) {
        return dataRepository.findOne(id);
    }

    //更新
    @RequestMapping("/update")
    public DataTest updateOne() {
        DataTest dataTest = new DataTest();
        dataTest.setId(3);
        dataTest.setName("张三");
        dataTest.setAge(99);

        return dataRepository.save(dataTest);
    }

    //删除
    @RequestMapping("/del")
    public void del() {
        dataRepository.delete(3);
    }

    //事务管理
    @RequestMapping("/addTwo")
    public void addTwo() {
        service.addTwo();
    }

    //表单验证,在要验证的对象前加Valid注解，表示要验证的对象,验证的结果信息放入BindingResult对象中
    @GetMapping(value = "/formCheck")
    public DataTest formCheck(@Valid DataTest data, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getFieldError().getDefaultMessage());
            return null;
        }

        return dataRepository.save(data);
    }

    //异常处理
    @RequestMapping("/doException")
    public String doException() throws Exception {
        throw new Exception("产生异常");
    }
}
