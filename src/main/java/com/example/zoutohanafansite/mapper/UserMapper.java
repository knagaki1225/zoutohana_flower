package com.example.zoutohanafansite.mapper;

import com.example.zoutohanafansite.entity.auth.User;
import com.example.zoutohanafansite.entity.form.UserSearchForm;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE login_id = #{loginId} AND deleted = false")
    User getUserByLoginId(String loginId);

    @Select("SELECT * FROM users WHERE id = #{id} AND deleted = false")
    User getUserById(long id);

    // src/main/resources/mapper/UserMapper.xml
    List<User> getAllUsers(UserSearchForm form);

    @Insert("""
            INSERT INTO users 
                (login_id, nickname, password, self_introduction,
                 icon, address, birth_year, gender, security_key, status)
            VALUES (#{loginId}, #{nickname}, #{password}, #{selfIntroduction}, #{icon}, #{address}, #{birthYear}, #{gender}, #{securityKey}, #{status})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);

    @Update("UPDATE users SET password = #{password}, security_key = #{securityKey} WHERE login_id = #{loginId}")
    void updatePassword(String password, String securityKey, String loginId);

    @Update("UPDATE users SET status = #{status} WHERE id = #{id}")
    void updateStatus(@Param("status") String status, @Param("id") long id);

    @Update("UPDATE users SET deleted = true WHERE id = #{id}")
    void deleteUser(long id);

    @Update("""
        UPDATE users
        SET nickname = #{nickname}, self_introduction = #{selfIntroduction}, address = #{address}, birth_year = #{birthYear}, icon = #{icon}, updated_at = CURRENT_TIMESTAMP
        WHERE login_id = #{loginId}
    """)
    void updateUser(User user);
}
