package com.github.dragonhht.batch.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * .
 *
 * @author: huang
 * @Date: 2019-4-12
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "batch_user")
public class User {
    @Id
    @Column(length = 16)
    private String id;
    private String name;
    private String address;
    private int age;
}
