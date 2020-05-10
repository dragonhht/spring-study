package com.github.dragonhht.redis.model;

import lombok.*;

import java.io.Serializable;

/**
 * .
 *
 * @author: huang
 * @Date: 2019-6-13
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -2505102836548220485L;

    private String name;
    private int age;
}
