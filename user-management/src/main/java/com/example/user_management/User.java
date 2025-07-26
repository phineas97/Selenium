package com.example.user_management;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data; // Lombok 注解，自动生成 getter/setter/构造器等

@Entity // 标记这是一个 JPA 实体，对应数据库中的一张表
@Data   // Lombok: 自动生成 getter, setter, toString, equals, hashCode
public class User {

    @Id // 标记为主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 设置主键生成策略为自增长
    private Long id;

    private String name;
    private String email;

    // 如果不使用 Lombok，你需要手动添加构造器、getter 和 setter 方法
    // public User() {}
    // public User(Long id, String name, String email) { ... }
    // public Long getId() { ... }
    // public void setId(Long id) { ... }
    // ...
}