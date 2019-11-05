package com.wyy.test.rest.dto;

/**
 * @Date: 19-11-5
 * @Author: wyy
 */
public class UserDTO {
    private String username;
    private int age;

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", age=" + age +
                '}';
    }

    public int getAge() {
        return age;
    }

    public UserDTO setAge(int age) {
        this.age = age;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDTO setUsername(String username) {
        this.username = username;
        return this;
    }
}
