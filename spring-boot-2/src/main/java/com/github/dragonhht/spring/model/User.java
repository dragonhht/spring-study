package com.github.dragonhht.spring.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

/**
 * .
 *
 * @author: huang
 * @Date: 2019-4-12
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class User {
    @Id
    private String id;
    private String name;
    private int age;
}
