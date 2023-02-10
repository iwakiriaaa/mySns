package com.mySns.controller;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mySns.model.LoginIdSession;

@Controller
public class NewVueController {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	LoginIdSession loginIdSession;
	
	/**
	 * vue.js用
	 * @throws UnsupportedEncodingException 
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(path = "/vueLogin/{email}/{password}", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	public Map<String,String> vuepractice(@PathVariable String email,@PathVariable String password) throws UnsupportedEncodingException {
		

		String emailDeco = URLDecoder.decode(email, "UTF-8");

		//SQL発行（ログイン）
		List<Map<String, Object>> user = jdbcTemplate.queryForList("SELECT * FROM user WHERE email=? && password=?",emailDeco ,password);

		//vueに返すデータ
		Map<String,String> responseData = new HashMap<String,String>();
		
		//件数チェックして表示するコンポーネント名をreturnする。
		if(user.size() > 0) {
			loginIdSession.setUserName(user.get(0).get("user_name").toString());
			loginIdSession.setUserId((Integer)user.get(0).get("user_id"));
			
			List<Map<String, Object>> todoList = jdbcTemplate.queryForList("SELECT * FROM user u LEFT JOIN todo t ON u.user_id = ? && t.user_id = ? where t.user_id = ?", 
					 loginIdSession.getUserId(), loginIdSession.getUserId(), loginIdSession.getUserId());
			
			System.out.println(responseData.get(todoList));
			
			String userId = "";
			String title = "";
			String honbun = "";
			for (int i = 0; i < todoList.size(); i++) {
				userId += i != (todoList.size() - 1) ? todoList.get(i).get("user_id").toString() + "," : todoList.get(i).get("user_id").toString();
				title += i != (todoList.size() - 1) ? todoList.get(i).get("title").toString() + "," : todoList.get(i).get("title").toString();;
				honbun += i != (todoList.size() - 1) ? todoList.get(i).get("honbun").toString() + "," : todoList.get(i).get("honbun").toString();;
				responseData.put("todoListUserId", userId);
				responseData.put("todoListTitle", title);
				responseData.put("todoListHonbun", honbun);
			}
			
			System.out.println(responseData.get("todoListUserId"));
			
			responseData.put("userName", user.get(0).get("user_name").toString());
			responseData.put("compName", "MyTodo");
		} else {
			responseData.put("compName", "MyLogin");
		}
		return responseData;
	}
	
	@ResponseBody
	@CrossOrigin
	@RequestMapping(path = "/vueSubmit", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	public Map<String,String> vueToukou(@RequestParam("title") String title, @RequestParam("content") String content) {

		jdbcTemplate.update("INSERT INTO todo VALUES(?, ?, ?, ?)", title, content, null, loginIdSession.getUserId());
		List<Map<String, Object>> todoList = jdbcTemplate.queryForList("SELECT * FROM user u LEFT JOIN todo t ON u.user_id = ? && t.user_id = ? where t.user_id = ?", 
											 loginIdSession.getUserId(), loginIdSession.getUserId(), loginIdSession.getUserId());
		
		//vueに返すデータ
		Map<String,String> responseData = new HashMap<String,String>();
		
		//件数チェックして表示するコンポーネント名をreturnする。
		if(todoList.size() > 0) {
			System.out.println(todoList);
			responseData.put("data", todoList.toString());
			return responseData;
		} else {
			responseData.put("compName", "MyLogin");
			return responseData;
		}
	}
}
