package com.github.dragonhht.shell.converter;

import com.github.dragonhht.shell.model.Model;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * 自定义参数转换器.
 *
 * @author: huang
 * @Date: 2019-4-13
 */
@Component
public class ModelConverter implements Converter<String, Model> {


    /**
     * 假设接收一个字符串，各属性间以@分开
     * @param s
     * @return
     */
    @Override
    public Model convert(String s) {
        String[] arrays = s.split("@");
        return new Model(arrays[0], arrays[1]);
    }
}
