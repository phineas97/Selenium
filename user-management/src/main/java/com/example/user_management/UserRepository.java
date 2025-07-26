package com.example.user_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 标记为一个仓库组件
// JpaRepository 提供了常用的 CRUD 操作，第一个泛型是实体类，第二个是主键类型
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA 会根据方法名自动生成查询
    User findByName(String name);
}