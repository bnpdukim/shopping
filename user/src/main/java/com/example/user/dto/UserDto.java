package com.example.user.dto;

import com.example.user.type.Sex;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by sajacaros on 2017-04-26.
 */
public class UserDto {
    @Getter
    @NoArgsConstructor
    @ToString
    public static class New {
        private String principalId;
        private String password;
        private String name;
        private Sex sex;

        public New(String principalId, String password, String name, Sex sex) {
            this.principalId = principalId;
            this.password = password;
            this.name = name;
            this.sex = sex;
        }
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class Response {
        private Long id;
        private String principalId;
        private String name;
        private Sex sex;

        public Response( Long id, String principalId, String name, Sex sex ) {
            this.id = id;
            this.principalId = principalId;
            this.name = name;
            this.sex = sex;
        }
    }
}
