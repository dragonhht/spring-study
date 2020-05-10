package com.github.dragonhht.batch.listener;

import com.github.dragonhht.batch.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static java.lang.String.format;

/**
 * Writer监听.
 *
 * @author: huang
 * @Date: 2019-4-12
 */
@Slf4j
public class UserWriterListener implements ItemWriteListener<User> {


    @Override
    public void beforeWrite(List<? extends User> items) {
    }

    @Override
    public void afterWrite(List<? extends User> items) {
    }

    @Override
    public void onWriteError(Exception e, List<? extends User> items) {
        log.error(e.getMessage());
        for (User user : items) {
            log.error("Failed writing user id: {}", user.getId());
        }
    }
}
