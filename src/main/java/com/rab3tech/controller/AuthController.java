package com.rab3tech.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.rab3tech.dao.ProfileDTO;
import com.rab3tech.dao.ProfileDao;

@Controller //@Repository , @Service ,@Component
public class AuthController {
	
	@Autowired
	private ProfileDao profileDao;
	
		
	@GetMapping("/logout")
	public String logout(HttpServletRequest req) {
		// This code invalidate the session
		HttpSession session = req.getSession(false);
		if (session != null)
			session.invalidate();

		req.setAttribute("hmmmm", "You have logged out successfully!!");
		return "login";
	}
	
	
	@PostMapping("/auth")
	public String validateUser(HttpServletRequest req){
		String pusername=req.getParameter("username");
		String ppassword=req.getParameter("password");
		ProfileDTO profileDTO=profileDao.authUser(pusername, ppassword);
		if(profileDTO!=null) {
		   //page->request-session-application	
		   HttpSession session=req.getSession(true); 	
		   session.setAttribute("userData", profileDTO);
		   //adding profileDTO object inside request scope with namemagic
		   //req.setAttribute("magic", profileDTO);
		  return "dashboard";
	  }else {  //user is not there
		  req.setAttribute("hmmmm", "Sorry , username and password are not correct");
		  return "login";
	  }
	}

}
