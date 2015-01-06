package com.seminaro.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import com.seminaro.model.User;
import com.seminaro.utility.JedisFactory;
import com.seminaro.utility.Mail;

@Controller
public class LoginController {

	private static final Logger logger = Logger
			.getLogger(LoginController.class);
	
	
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(ModelMap model) {

		return "login";

	}
	

	@RequestMapping(value = "/logout.html", method = RequestMethod.GET)
	public String moveToLoginPage(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		return "login";
	}
	
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	public String forgotPassword(@ModelAttribute("user") User user,
			BindingResult result, HttpServletRequest request,
			final RedirectAttributes redirectAttributes) {
		
		return "forgotPasswordPage";
	}
	
	
	@RequestMapping(value = "/forgotPasswordPage", method = RequestMethod.POST)
	public String forgotPasswordPage(@ModelAttribute("user") User user,
			BindingResult result, HttpServletRequest request,
			final RedirectAttributes redirectAttributes) {
		System.out.println("Username is ********************"+user.getUsername());
		
		if(user.getUsername()==null){
			redirectAttributes.addFlashAttribute("message",
					"Enter username and then click forgot password");
			return "redirect:login.html";
		}

		Mail newMail=new Mail();
		String passwordResetLink="http://localhost:8080/Seminaro/passwordReset.html?username="+user.getUsername();
		newMail.sendmail("mayanksep19@gmail.com", "Password Reset Seminaro","You can reset your password using the below link. \n \n"+passwordResetLink);

		
		redirectAttributes.addFlashAttribute("message",
				"Reset password link send to your mail");
		return "redirect:login.html";
		
	}
	
	
	@RequestMapping(value = "/passwordReset", method = RequestMethod.GET)
	public ModelAndView passwordReset(@ModelAttribute("user") User user,
			BindingResult result, HttpServletRequest request,
			final RedirectAttributes redirectAttributes) {
		
		ModelAndView modelAndView=new ModelAndView("passwordReset");
		modelAndView.addObject("username", user.getUsername());
		return modelAndView;
		
	}
	
	@RequestMapping(value = "/passwordResetPage", method = RequestMethod.POST)
	public String passwordResetPage(@ModelAttribute("user") User user,
			BindingResult result, HttpServletRequest request,
			final RedirectAttributes redirectAttributes) {
		Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
		Pipeline pipeline = jedis.pipelined();

		if(!(user.getPassword()).equals(user.getMailId())){
			redirectAttributes.addFlashAttribute("message",
					"Passord and Confirm Password do not match");
			return "redirect:passwordReset.html";
		}

		System.out.println("Password got is ################"+user.getPassword());
		
		jedis.set("USERNAME_PASSWORD:" + user.getUsername(), user.getPassword());
		
		jedis.hset("USERNAME_ALL:" + user.getUsername(), "password",user.getPassword());

		pipeline.sync();
		redirectAttributes.addFlashAttribute("message",
				"Password changed , try logging in");
		return "redirect:login.html";
	}

	@RequestMapping(value = "/loginValidate", method = RequestMethod.POST)
	public String loginValidate(@ModelAttribute("user") User user,
			BindingResult result, HttpServletRequest request,
			final RedirectAttributes redirectAttributes) {
		Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
        
		String username = user.getUsername();
		String password = user.getPassword();
		
			String passwordDB = jedis.get("USERNAME_PASSWORD:"+ username);
			if(passwordDB==null){
				redirectAttributes.addFlashAttribute("message",
						"User does not exist");
				return "redirect:login.html";
			}
			else{
				if (password.equals(passwordDB)) {
					logger.info("User logged in");
					HttpSession session = request.getSession();
					session.setAttribute("username", username);
					return "redirect:home.html";
				} else {
					redirectAttributes.addFlashAttribute("user", user);
					redirectAttributes.addFlashAttribute("message",
							"Incorrect username  and password");
					return "redirect:login.html";
				}
			}
		
		
	}
	
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public String createAccount(@ModelAttribute("user") User user,
			BindingResult result, HttpServletRequest request,final RedirectAttributes redirectAttributes) {
	
		
		Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
		Pipeline pipeline = jedis.pipelined();
		
		boolean isUsernameExists=jedis.sismember("USERNAME_ALL", user.getUsername());
		
		if(isUsernameExists){
			redirectAttributes.addFlashAttribute("message",
					"Username already exists");
			return "redirect:login.html";
		}

		if(!(user.getMailId()).contains("@")){
			redirectAttributes.addFlashAttribute("message",
					"Provide correct mailId");
			return "redirect:login.html";
			
		}
		
		jedis.set("USERNAME_PASSWORD:" + user.getUsername(), user.getPassword());

		
		Map<String, String> userProperties = new HashMap<String, String>();

		userProperties.put("firstName", user.getFirstName());
		userProperties.put("lastName", user.getLastName());
		userProperties.put("username", user.getUsername());
		userProperties.put("password", user.getPassword());
		userProperties.put("mailid", user.getMailId());
		userProperties.put("currentCity",user.getCurrentAddress());
		

		/*
		 * WE HAVE TO DO THIS WAY FINALLY
		 */
		/*
		 * pipeline.hmset(Keys.USER_DATA.formated(String.valueOf(userId)),
		 * BeanUtils.toMap(user));
		 */
		jedis.hmset("USER_ALL:" + user.getUsername(), userProperties);
		pipeline.sync();
		
		//logger.debug("user with user id = "+jedis.hmget(key, fields));
		JedisFactory.getInstance().returnJedisResource(jedis);
		logger.debug("user successfully registered");
		
		
		HttpSession session = request.getSession();
		session.setAttribute("username", user.getUsername());
		

		return "redirect:home.html";
	}

	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {

		model.addAttribute("error", "true");
		return "login";

	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {

		return "login";

	}
}
