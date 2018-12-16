package com.dnd.dao;

import com.dnd.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class UserDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    private static final String ADD_USER = "INSERT INTO users(username, first_name, last_name) VALUES(:username, :first_name, :last_name);";
    private static final String REMOVE_USER = "DELETE FROM users WHERE username=:username LIMIT ONE ROW;";
    private static final String GET_USERS = "SELECT username, admin, first_name, last_name FROM users;";
    private static final String GET_USER = "SELECT username, admin, first_name, last_name FROM users WHERE username = :username;";

    public List<User> getUsers() {
        return jdbcTemplate.query(GET_USERS, new BeanPropertyRowMapper<>(User.class));
    }

    public User getUser(String username) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        return jdbcTemplate.queryForObject(GET_USER, params, new BeanPropertyRowMapper<>(User.class));
    }

}
