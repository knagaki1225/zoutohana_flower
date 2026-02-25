package com.example.zoutohanafansite.repository;

import com.example.zoutohanafansite.entity.auth.User;
import com.example.zoutohanafansite.entity.form.UserSearchForm;
import com.example.zoutohanafansite.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final UserMapper userMapper;

    public UserRepository(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserByLoginId(String loginId){
        return userMapper.getUserByLoginId(loginId);
    }

    public User getUserById(long id){
        return userMapper.getUserById(id);
    }

    public List<User> getAllUsers(UserSearchForm form) {
        return userMapper.getAllUsers(form);
    }

    public void insertUser(User user){
        userMapper.insertUser(user);
    }

    public void updatePassword(String password, String securityKey, String loginId){
        userMapper.updatePassword(password,securityKey,loginId);
    }

    public void updateStatus(String status, long id){
        userMapper.updateStatus(status,id);
    }

    public void deleteUser(long id){
        userMapper.deleteUser(id);
    }

    public void updateUser(User user){
        userMapper.updateUser(user);
    }
}
