package com.rab3tech.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.rab3tech.dao.ProfileDTO;
import com.rab3tech.dao.ProfileDao;
import com.rab3tech.utils.Utils;

//CTR=SHIFT+O
@Controller
public class CustomerController {

	@Autowired
	private ProfileDao profileDao;

	@GetMapping("/profiles")
	public String profiles(HttpServletRequest req) {
		// I need to fetch whole profiles data from database
		List<ProfileDTO> profileDTOs = profileDao.findAll();
		// adding profileDTO object inside request scope with namemagic
		req.setAttribute("profileDTOs", profileDTOs);
		req.setAttribute("listoptions", profileDao.findAllQualification());
		return "profiles";
	}

	@GetMapping("/loggedUser")
	public String loggedUser(HttpServletRequest req) {
		Set<ProfileDTO> loggedUsers = ProfileDTO.loggedInUser();
		req.setAttribute("profileDTOs", loggedUsers);
		return "loggedUsers";
	}

	@GetMapping("/filterProfile")
	public String filterProfile(HttpServletRequest req) {
		String filterText = req.getParameter("filterText");
		List<ProfileDTO> profileDTOs = null;
		if (!"Select".equalsIgnoreCase(filterText)) {
			profileDTOs = profileDao.filterProfiles(filterText);
		} else {
			profileDTOs = profileDao.findAll();
		}
		// adding profileDTO object inside request scope with namemagic
		req.setAttribute("listoptions", profileDao.findAllQualification());
		req.setAttribute("profileDTOs", profileDTOs);
		return "profiles";
	}

	@GetMapping("/deleteProfile")
	public String deleteProfile(HttpServletRequest req) {
		String pusername = req.getParameter("username");
		profileDao.deleteByUsername(pusername);
		return "redirect:/profiles"; // Here we are by passing jsp and it will
										// go another action=/profiles
	}

	@GetMapping("/editProfile")
	public String editProfileAction(HttpServletRequest req) {
		String pusername = req.getParameter("username"); // <a
		ProfileDTO profileDTO = profileDao.findByUsername(pusername);
		req.setAttribute("profileDTO", profileDTO);
		return "esignup";
	}

	@GetMapping("/searchProfile")
	public String searchProfile(HttpServletRequest req) {
		String search = req.getParameter("search");
		List<ProfileDTO> profileDTOs = profileDao.searchProfiles(search);
		// adding profileDTO object inside request scope with namemagic
		req.setAttribute("profileDTOs", profileDTOs);
		req.setAttribute("listoptions", profileDao.findAllQualification());
		return "profiles";
	}
	
	
		@PostMapping("/usignup")
		protected String usignup(HttpServletRequest req) {
			String username=req.getParameter("username");
			String name=req.getParameter("name");
			String email=req.getParameter("email");
			String qualification=req.getParameter("qualification");
			String mobile=req.getParameter("mobile");
			String gender=req.getParameter("gender");
			String photo=req.getParameter("photo");
			ProfileDTO profileDTO=new  ProfileDTO(username, "", name, email, mobile, gender, photo, qualification);
			profileDao.updateSignup(profileDTO);
			return "redirect:/profiles"; 
		}

	@PostMapping("/signup")
	protected String signup(HttpServletRequest req) {
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String qualification = req.getParameter("qualification");
		String mobile = req.getParameter("mobile");
		String gender = req.getParameter("gender");
		String photo = req.getParameter("photo");
		String password = Utils.generateRandomPassword(5);
		String username = email;
		ProfileDTO profileDTO = new ProfileDTO(username, password, name, email, mobile, gender, photo, qualification);
		profileDao.createSignup(profileDTO);
		req.setAttribute("hmmmm", "Hi , " + name + " , you have done signup successfully!!!!!!!!!!!");
		return "login";
	}

	@GetMapping("/sortProfile")
	protected String sortProfile(HttpServletRequest req) {
		// I need to fetch whole profiles data from database
		String sort = req.getParameter("sort");
		List<ProfileDTO> profileDTOs = profileDao.sortProfiles(sort);
		// adding profileDTO object inside request scope with namemagic
		req.setAttribute("profileDTOs", profileDTOs);
		req.setAttribute("listoptions", profileDao.findAllQualification());
		return "profiles";
	}

}
