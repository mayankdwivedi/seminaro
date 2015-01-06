package com.seminaro.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import com.seminaro.model.Seminar;
import com.seminaro.model.User;
import com.seminaro.utility.JedisFactory;
import com.seminaro.utility.Mail;

@Controller
public class HomeController {

	Mail newMail=new Mail();
/*	private static final Logger logger = Logger.getLogger(HomeController.class);
*/
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String openhomePage() {
/*		logger.debug("Inside Instruction page");
*/		return "home";
	}
	
	@RequestMapping(value = "/createseminar", method = RequestMethod.GET)
	public String createseminar() {
/*		logger.debug("Inside Instruction page");
*/		return "createseminar";
	}
	
	@RequestMapping(value = "/addseminar", method = RequestMethod.POST)
	public String addSeminar(@ModelAttribute("seminar") Seminar seminar,
			BindingResult result,HttpServletRequest request) {
/*		logger.debug("Inside Instruction page");
*/	
		
		
		HttpSession httpSession=request.getSession();
		String username=(String) httpSession.getAttribute("username");
		
		Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
		 Pipeline pipe =jedis.pipelined();
		
		 long seminarId = jedis.incr("SEMINAR_IDS");
		 
		 Map<String,String> seminarMap=new HashMap<String,String>();
		 seminarMap.put("topic",seminar.getTopic());
		 seminarMap.put("longDescription", seminar.getLongDescription());
		 seminarMap.put("organisedby",username);
		 seminarMap.put("venue",seminar.getVenue());
		 seminarMap.put("startDate",seminar.getStartDate());
		 seminarMap.put("endDate",seminar.getEndDate());
		 seminarMap.put("seats",seminar.getSeats());
		 seminarMap.put("price",seminar.getPrice());
		 seminarMap.put("createdDate",(seminar.getCreatedDate()).toString());
	    
		 newMail.sendmail("mayanksep19@gmail.com", "Seminar Details", "You have created a seminar with following details\nTopic "+seminar.getTopic()+"\nDescription "+seminar.getLongDescription()+"\nVenue"+seminar.getVenue()+"\nStart Date"+seminar.getStartDate()+"\nEnd Date"+seminar.getEndDate()+"\nSeats"+seminar.getSeats()+"\nPrice"+seminar.getPrice());
		 //get this by session
		 
		 
		 
		    jedis.hmset("SEMINAR_ALL:"+seminarId,seminarMap);
		    jedis.sadd("SEMINAR_ORGANISED:"+username, Long.toString(seminarId)); 

		   pipe.sync(); 
		return "home";
	}
	
	@RequestMapping(value = "/attendseminar", method = RequestMethod.GET)
	public ModelAndView attendseminar(HttpServletRequest request) {
/*		logger.debug("Inside Instruction page");
*/		
		Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();

		 Long seminarId = Long.parseLong(jedis.get("SEMINAR_IDS"));
		 HttpSession httpSession=request.getSession();
			String username=(String) httpSession.getAttribute("username");
			

		Map<Long,String> seminarDisplayMap=new HashMap<Long, String>();
		Map<Long,String> seminarAttendedMap=new HashMap<Long, String>();

		for(long i=1;i<=seminarId;i++){
		
		String seminarTopic=jedis.hget("SEMINAR_ALL:"+i,"topic");
		boolean hasAttended=jedis.sismember("SEMINAR_ATTENDEES:"+i, username);
		if(hasAttended){
			seminarAttendedMap.put(i, seminarTopic);
		}
		else{
		seminarDisplayMap.put(i, seminarTopic);
		}
		}
		
		ModelAndView modelAndView=new ModelAndView("attendseminar");
		modelAndView.addObject("seminarDisplayMap", seminarDisplayMap);
		modelAndView.addObject("seminarAttendedMap", seminarAttendedMap);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/seminarinfo", method = RequestMethod.GET)
	public ModelAndView seminarinfo(@RequestParam("seminarId")Long seminarId) {
		Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();

		Map<String,String> seminarInfo=jedis.hgetAll("SEMINAR_ALL:"+seminarId);
		ModelAndView modelAndView=new ModelAndView("seminarpage");
		modelAndView.addObject("seminarInfo", seminarInfo);
		modelAndView.addObject("seminarId", seminarId);
		return modelAndView;
	}
	
	//acceptseminar.html?seminarId=${seminarId}
	
	@RequestMapping(value = "/acceptseminar", method = RequestMethod.GET)
	public void acceptseminar(@ModelAttribute("seminarId") Long seminarId,
			BindingResult result,HttpServletRequest request,final RedirectAttributes redirectAttributes,HttpServletResponse response) {
		Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();

		HttpSession httpSession=request.getSession();
		String username=(String) httpSession.getAttribute("username");
		
		Long seatsLeft=Long.parseLong(jedis.hget("SEMINAR_ALL:"+seminarId,"seats"));
		if(seatsLeft>0){
		seatsLeft=seatsLeft-1;
		jedis.hset("SEMINAR_ALL:"+seminarId,"seats",seatsLeft.toString());
		 newMail.sendmail("mayanksep19@gmail.com", "Seminar Details","Thanks for joining the seminar on "+seminarId);
		 jedis.sadd("SEMINAR_ATTENDED:"+username,Long.toString(seminarId));
		 jedis.sadd("SEMINAR_ATTENDEES:"+seminarId, username);
		}
		else {
			redirectAttributes.addFlashAttribute("message",
					"Seats are full");
		}
		//reduce seats from total
		try {
			response.sendRedirect("attendseminar.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/unattendseminar", method = RequestMethod.GET)
	public void unattendseminar(@ModelAttribute("seminarId") Long seminarId,
			BindingResult result,HttpServletRequest request,HttpServletResponse response) {
		Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();

		HttpSession httpSession=request.getSession();
		String username=(String) httpSession.getAttribute("username");
		jedis.srem("SEMINAR_ATTENDED:"+username,Long.toString(seminarId));
		jedis.srem("SEMINAR_ATTENDEES:"+seminarId, username);
		Long seatsLeft=Long.parseLong(jedis.hget("SEMINAR_ALL:"+seminarId,"seats"));
		
		seatsLeft=seatsLeft+1;
		jedis.hset("SEMINAR_ALL:"+seminarId,"seats",seatsLeft.toString());
		newMail.sendmail("mayanksep19@gmail.com", "Seminar Details","You have removed yourself from joining the seminar on "+seminarId+"\nHope you visit our site soon for knowledgeable seminars.");

		
		//reduce seats from total
		try {
			response.sendRedirect("attendseminar.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public ModelAndView history(HttpServletRequest request){
		
		Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();

		 HttpSession httpSession=request.getSession();
			String username=(String) httpSession.getAttribute("username");
		
		    Set <String> seminarOrganised=jedis.smembers("SEMINAR_ORGANISED:"+username); 
			Set <String> seminarAttended=jedis.smembers("SEMINAR_ATTENDED:"+username);
			
            Map<Integer,String> seminarOrganisedTopic=new HashMap<Integer, String>();
            Map<Integer,String> seminarAttendedTopic=new HashMap<Integer, String>();

            String topic=null;
            Object seminarId=null;
			Iterator itSeminarOrganised=seminarOrganised.iterator();
			while(itSeminarOrganised.hasNext()){
				
				 seminarId=itSeminarOrganised.next();
				
				topic=jedis.hget("SEMINAR_ALL:"+itSeminarOrganised.next(), "topic");
				seminarOrganisedTopic.put(Integer.parseInt((String) seminarId), topic);
			}
			
			Iterator itseminarAttended=seminarAttended.iterator();
			while(itseminarAttended.hasNext()){
				seminarId=itseminarAttended.next();
				topic=jedis.hget("SEMINAR_ALL:"+itseminarAttended.next(), "topic");
				seminarOrganisedTopic.put(Integer.parseInt((String) seminarId), topic);
			}
		
		ModelAndView modelAndView=new ModelAndView("allhistory");
		modelAndView.addObject("seminarOrganisedTopic", seminarOrganisedTopic);
		modelAndView.addObject("seminarAttendedTopic", seminarAttendedTopic);
		
		return modelAndView;
	}
	
	
	
	
	
	@RequestMapping(value = "/facebookCall", method = RequestMethod.GET)
	  void facebookCall(HttpServletResponse response,HttpServletRequest request) throws IOException {
		String appName="mtest";
		String appID= "292305014282618";
		String appSecret= "a1f526e954c040d5a0ca9e7565602da9";
		
        
/*        String appId = "${grailsApplication.config.grails.facebookAppId}";
*/
		
		//http://localhost:8080/Seminaro/createseminar.html
		//http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/facebookRequest.html

		
		
		String callBack = "http://"+request.getServerName()+":"+request.getLocalPort()+request.getContextPath()+"/facebookRequest.html";
		
        String permission = "email,publish_stream";
        String facebookAuthUrl = "http://graph.facebook.com/oauth/authorize?client_id="+appID+"&redirect_uri="+callBack+"&scope="+permission;
       
        response.sendRedirect(facebookAuthUrl);


    }

	@RequestMapping(value = "/facebookRequest", method = RequestMethod.GET)
	  void facebookRequest(HttpServletResponse response,HttpServletRequest request) throws IOException {
		
		String appName="mtest";
		String appID= "292305014282618";
		String appSecret= "a1f526e954c040d5a0ca9e7565602da9";
        
		String callBack = "http://localhost:8080/Seminaro/facebookRequest.html";
        
        String facebookUrl = "https://graph.facebook.com/oauth/access_token?client_id="+appID+"&client_secret="+appSecret+"&code=${authCode}&redirect_uri="+callBack;
        URL url;
        String responsefrmUrl="";
		try {
			url = new URL(facebookUrl);
			responsefrmUrl = (String) url.getContent();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        String accessToken = responsefrmUrl.substring((responsefrmUrl.indexOf('=') + 1), responsefrmUrl.indexOf('&'));
        		
        HttpSession session=request.getSession();
        session.setAttribute("accessToken", accessToken);

        
        response.sendRedirect("facebookResponse.html");
    }


	@RequestMapping(value = "/facebookResponse", method = RequestMethod.GET)
	void facebookResponse(HttpServletRequest request) {
		
		String appName="mtest";
		String appID= "292305014282618";
		String appSecret= "a1f526e954c040d5a0ca9e7565602da9";
		HttpSession session=request.getSession();

        String accessToken = (String) session.getAttribute("accessToken");
        URL profileURL;
        String profileJson="";
		try {
			profileURL = new URL("https://graph.facebook.com/me?fields=first_name,last_name,email&access_token="+accessToken);
			 profileJson = (String) profileURL.getContent();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
           System.out.println("###########################################################"+profileJson);
	}

	
	
	
}
