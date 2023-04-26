package com.mySns.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
//aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
//aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
//aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
//aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
	@Autowired
	JdbcTemplate jdbcTemplate;
	@GetMapping(path = "/profile")
	public String[] profileGetUserProfile(@RequestParam("id") Integer id) {
		List<Map<String, Object>> loginUser = jdbcTemplate.queryForList("SELECT * FROM user WHERE user_id=?", id);
		List<Map<String, Object>> toukouList = jdbcTemplate.queryForList("SELECT * FROM toukou WHERE user_id=?", id);
		
		String[] responseData = new String[2];
		responseData[0] = String.valueOf(loginUser.get(0).get("user_name"));
		responseData[1] = String.valueOf(toukouList.size());
		
		return responseData;
	}
}
