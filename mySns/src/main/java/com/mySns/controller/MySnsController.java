package com.mySns.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mySns.model.LoginIdSession;

import jakarta.servlet.http.HttpSession;

@Controller
public class MySnsController {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	LoginIdSession loginIdSession;

	@GetMapping(path = "/login")
	public String loginGet() {
		return "login";
	}
	
	@GetMapping(path = "/regist")
	public String registGet() {
		return "regist";
	}
	
	@GetMapping(path = "/home")
	public String homeViewGet(Model model) {
		List<Map<String, Object>> toukouList = jdbcTemplate.queryForList("SELECT u.user_id, u.user_name, t.content FROM user u RIGHT JOIN toukou t ON u.user_id = t.user_id;");
		
		model.addAttribute("user_name", loginIdSession.getUserName());
		model.addAttribute("loginId", loginIdSession.getUserId());
		model.addAttribute("toukouList", toukouList);
		return "home";
	}
	
	@GetMapping(path = "/edit")
	public String editViewGet(Model model) {
		List<Map<String, Object>> user;
		
		if (loginIdSession.getUserId() != null) {
			user = jdbcTemplate.queryForList("SELECT email, user_name FROM user WHERE user_id=?",loginIdSession.getUserId());
			model.addAttribute("emailValue", user.get(0).get("email"));
			model.addAttribute("userNameValue", user.get(0).get("user_name"));
			return "userEdit";
		} else {
			return "redirect:/login";
		}
		
	}
	
	@PostMapping(path = "/login")
	public String loginPost(Model model, String email, String password) {
		List<Map<String, Object>> loginUser;
		loginUser = jdbcTemplate.queryForList("SELECT * FROM user WHERE email=? && password=?",email ,password);
		
		if (loginUser.size() == 1) {
			loginIdSession.setUserName(loginUser.get(0).get("user_name").toString());
			loginIdSession.setUserId((Integer)loginUser.get(0).get("user_id"));
			return "redirect:/home";
		} else {
			model.addAttribute("errorMsg", "emailまたはパスワードが違います");
			return "login";
		}
	}
	
	@PostMapping(path = "/regist")
	public String registPost(HttpSession session, String email, String password, String userName) {
		
		if (email != null && password != null && userName != null) {
			jdbcTemplate.update("INSERT INTO user VALUES(?, ?, ?, ?)",null, email, userName, password);
			return "redirect:/login";
		} else {
			return "redirect:/regist";
		}
	}
	
	@PostMapping(path = "/submit")
	public String toukouPost(String bms_message) {
		
		if (bms_message != null) {
			jdbcTemplate.update("INSERT INTO toukou VALUES(?, ?, ?)", null, bms_message, loginIdSession.getUserId());
		}
		return "redirect:/home";
	}
	
	@PostMapping(path = "/edit")
	public String editPost(String userName, String password) {
		if (loginIdSession.getUserId() != null && userName != null && password != null) {
			jdbcTemplate.update("UPDATE user SET user_name=?, password=? WHERE user_id=?", userName, password, loginIdSession.getUserId());
		} 
		return "redirect:/login";
	}
	
	
	/**
	 * vue.js用
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(path = "/vue", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	public Map<String,String> vuepractice() {
		Map<String,String> responseData = new HashMap<String,String>();
		
		//String returnText = vueData + "という文字を受信できたので練習問題クリアです";
		
		int rand = (int)(Math.random()*10) + 1;
		
		responseData.put("data1", String.valueOf(rand));
		return responseData;
	}
	
	@ResponseBody
	@CrossOrigin
	@RequestMapping(path = "/", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	public Map<String,String> vuepractice2(@RequestParam("message") String message) {
		Map<String,String> responseData = new HashMap<String,String>();
		
		responseData.put("data1", message);
		return responseData;
	}
}
