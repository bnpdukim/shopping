package com.example.user.domain;

import com.example.user.type.Sex;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by sajacaros on 2017-04-26.
 */
@Entity(name = "shopUser")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String principalId;
    private String name;
    private Sex sex;

    public User(String principalId, String name, Sex sex) {
        this.principalId = principalId;
        this.name = name;
        this.sex = sex;
    }
}
