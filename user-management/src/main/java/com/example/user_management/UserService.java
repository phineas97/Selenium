package com.example.user_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service // 标记为一个服务组件
public class UserService {

    private final UserRepository userRepository;

    @Autowired // 构造器注入 UserRepository
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 查询所有用户
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 根据ID查询用户
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // 添加或更新用户
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // 根据ID删除用户
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 根据名称查询用户 (自定义方法)
    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }
}