package com.github.dragonhht.study.model;

import lombok.*;

import java.io.Serializable;

/**
 * .
 *
 * @author: huang
 * @Date: 2019-4-15
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
    private int id;
    private String name;
}
