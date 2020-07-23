package com.rab3tech.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rab3tech.dao.ProfileDTO;
import com.rab3tech.dao.ProfileDao;
import com.rab3tech.utils.Utils;

//CTR=SHIFT+O
@Controller
public class CustomerController {

	@Autowired
	private ProfileDao profileDao;

	@GetMapping("/profiles")
	public String profiles(Model model) {
		// I need to fetch whole profiles data from database
		List<ProfileDTO> profileDTOs = profileDao.findAll();
		// adding profileDTO object inside request scope with namemagic
		model.addAttribute("profileDTOs", profileDTOs);
		model.addAttribute("listoptions", profileDao.findAllQualification());
		return "profiles";
	}

	@GetMapping("/loggedUser")
	public String loggedUser(Model model) {
		Set<ProfileDTO> loggedUsers = ProfileDTO.loggedInUser();
		model.addAttribute("profileDTOs", loggedUsers);
		return "loggedUsers";
	}

	@GetMapping("/filterProfile")
	public String filterProfile(@RequestParam("filterText") String  filterText,Model model) {
		List<ProfileDTO> profileDTOs = null;
		if (!"Select".equalsIgnoreCase(filterText)) {
			profileDTOs = profileDao.filterProfiles(filterText);
		} else {
			profileDTOs = profileDao.findAll();
		}
		// adding profileDTO object inside request scope with namemagic
		model.addAttribute("listoptions", profileDao.findAllQualification());
		model.addAttribute("profileDTOs", profileDTOs);
		return "profiles";
	}

	@GetMapping("/deleteProfile")
	public String deleteProfile(@RequestParam("username") String  username) {
		profileDao.deleteByUsername(username);
		return "redirect:/profiles"; // Here we are by passing jsp and it will
										// go another action=/profiles
	}

	@GetMapping("/editProfile")
	public String editProfileAction(@RequestParam("username") String  username,Model model) {
		ProfileDTO profileDTO = profileDao.findByUsername(username);
		model.addAttribute("profileDTO", profileDTO);
		return "esignup";
	}

	@GetMapping("/searchProfile")
	public String searchProfile(@RequestParam("search") String  search,Model model) {
		List<ProfileDTO> profileDTOs = profileDao.searchProfiles(search);
		// adding profileDTO object inside request scope with namemagic
		model.addAttribute("profileDTOs", profileDTOs);
		model.addAttribute("listoptions", profileDao.findAllQualification());
		return "profiles";
	}
	
	//Reflection
	@PostMapping("/usignup")
	protected String usignup(@ModelAttribute ProfileDTO profileDTO) {
//		/@ModelAttribute .. it is created an object of ProfileDTO
		   //reading all the parameters from request object as per attributes of ProfileDTO 
		   //and setting the same in the profileDTO instance
			/*String username=req.getParameter("username");
			String name=req.getParameter("name");
			String email=req.getParameter("email");
			String qualification=req.getParameter("qualification");
			String mobile=req.getParameter("mobile");
			String gender=req.getParameter("gender");
			String photo=req.getParameter("photo");
			ProfileDTO profileDTO=new  ProfileDTO(username, "", name, email, mobile, gender, photo, qualification);*/
			profileDao.updateSignup(profileDTO);
			return "redirect:/profiles"; 
	}

	@PostMapping("/signup")
	protected String signup(@ModelAttribute ProfileDTO profileDTO,Model model) {
		/*String name = req.getParameter("name");
		String email = req.getParameter("email");
		String qualification = req.getParameter("qualification");
		String mobile = req.getParameter("mobile");
		String gender = req.getParameter("gender");
		String photo = req.getParameter("photo");*/
		String password = Utils.generateRandomPassword(5);
		//String username = email;
		profileDTO.setPassword(password);
		profileDTO.setUsername(profileDTO.getEmail());
		//ProfileDTO profileDTO = new ProfileDTO(username, password, name, email, mobile, gender, photo, qualification);
		profileDao.createSignup(profileDTO);
		model.addAttribute("hmmmm", "Hi , " + profileDTO.getName() + " , you have done signup successfully!!!!!!!!!!!");
		return "login";
	}

	@GetMapping("/sortProfile")
	protected String sortProfile(@RequestParam("sort") String  sort,Model model) {
		// I need to fetch whole profiles data from database
		List<ProfileDTO> profileDTOs = profileDao.sortProfiles(sort);
		// adding profileDTO object inside request scope with namemagic
		model.addAttribute("profileDTOs", profileDTOs);
		model.addAttribute("listoptions", profileDao.findAllQualification());
		return "profiles";
	}

}
