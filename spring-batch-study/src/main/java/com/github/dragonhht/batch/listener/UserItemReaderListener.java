package com.github.dragonhht.batch.listener;

import com.github.dragonhht.batch.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;

import java.io.IOException;
import java.io.Writer;

import static java.lang.String.format;

/**
 * Reader监听.
 *
 * @author: huang
 * @Date: 2019-4-12
 */
@Slf4j
public class UserItemReaderListener implements ItemReadListener<User> {

    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(User user) {

    }

    @Override
    public void onReadError(Exception e) {
        log.error(e.getMessage());
    }
}
