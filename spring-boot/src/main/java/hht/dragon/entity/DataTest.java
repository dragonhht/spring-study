package hht.dragon.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * 数据库表对应的实体
 * <p>
 * User : Dragon_hht
 * Date : 17-4-9
 * Time : 下午4:04
 */
@Entity
public class DataTest {

    //Id主键，GeneratedValue自增长
    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    //验证age最小为18
    @Min(value = 18, message = "不得小于18")
    private Integer age;

    public DataTest() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
