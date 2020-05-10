package com.github.dragonhht.batch.line.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.github.dragonhht.batch.model.User;
import org.springframework.batch.item.file.LineMapper;

import java.util.Map;

/**
 * 将每一行json数据转换为User对象.
 *
 * @author: huang
 * @Date: 2019-4-12
 */
public class UserLineMapper implements LineMapper<User> {

    private MappingJsonFactory factory = new MappingJsonFactory();

    @Override
    public User mapLine(String str, int lineNum) throws Exception {
        JsonParser jsonParser = factory.createParser(str);
        Map<String, Object> map = (Map<String, Object>) jsonParser.readValueAs(Map.class);
        User user = new User();
        user.setAddress((String) map.get("address"));
        user.setAge((Integer) map.get("age"));
        user.setId((String) map.get("id"));
        user.setName((String) map.get("name"));
        return user;
    }
}
