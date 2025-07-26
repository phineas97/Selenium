package com.example.user_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // 标记为 REST 控制器
@RequestMapping("/api/users") // 所有接口都以 /api/users 开头
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    // 获取所有用户 (GET /api/users)
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 根据ID获取用户 (GET /api/users/{id})
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok) // 如果找到用户，返回 200 OK 和用户数据
                   .orElseGet(() -> ResponseEntity.notFound().build()); // 否则返回 404 Not Found
    }

    // 创建新用户 (POST /api/users)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 返回 201 Created 状态码
    public User createUser(@RequestBody User user) { // @RequestBody 将请求体中的 JSON 映射到 User 对象
        return userService.saveUser(user);
    }

    // 更新用户 (PUT /api/users/{id})
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.getUserById(id)
                .map(user -> {
                    user.setName(userDetails.getName());
                    user.setEmail(userDetails.getEmail());
                    User updatedUser = userService.saveUser(user);
                    return ResponseEntity.ok(updatedUser); // 返回 200 OK 和更新后的用户数据
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // 如果用户不存在，返回 404 Not Found
    }

    // 删除用户 (DELETE /api/users/{id})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 返回 204 No Content 状态码
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}