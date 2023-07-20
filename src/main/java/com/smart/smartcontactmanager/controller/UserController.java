package com.smart.smartcontactmanager.controller;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.smart.smartcontactmanager.dao.ContactRepository;
import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.Contact;
import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println("USERNAME " + userName);

		User user = userRepository.getUserByUserName(userName);

		model.addAttribute("user", user);
	}

	@RequestMapping("/index")
	private String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {

		model.addAttribute("title", "Add contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_contact_form";
	}

	@PostMapping("/process-contact")
	public String processContact(@Valid @ModelAttribute Contact contact, BindingResult result,
			@RequestParam("image") MultipartFile file, Model model, Principal principal, HttpSession session) {

		try {

//			if (result.hasErrors()) {
//				System.out.println("binding result");
//				model.addAttribute("contact", contact);
//				return "normal/add_contact_form";
//			}

			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);

//			processing and uploading file...

			if (file.isEmpty()) {
				// give message if u want if the file is empty
				System.out.println("file is empty");
				contact.setImage("default.png");
			} else {
				contact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			contact.setUser(user);

			user.getContacts().add(contact);

			this.userRepository.save(user);

			System.out.println("DATA " + contact);

			System.out.println("Added to database");

			// success message
			session.setAttribute("message", new Message("Your contact is added !! Add more..", "success"));

			return "normal/add_contact_form";

		} catch (Exception e) {
			System.out.println("ERROR " + e.getMessage());
			e.printStackTrace();
			// error message
			session.setAttribute("message", new Message("Something went wrong!!", "danger"));
		}
		return "normal/add_contact_form";
	}

	// show contacts handler
	// per Page = 5[n]
	// current page = 0
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "Show User Contacts");

		String userName = principal.getName();
		User U = this.userRepository.getUserByUserName(userName);

		Pageable pageable = PageRequest.of(page, 4);

		Page<Contact> contacts = this.contactRepository.findContactsByuser(U.getId(), pageable);

		model.addAttribute("contact", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());

		return "normal/show_contacts";

	}

	@GetMapping("/{cid}/contact")
	public String showContactDetail(@PathVariable("cid") Integer cid, Model model, Principal principal) {
		System.out.println("cid" + cid);

		Optional<Contact> contactOptional = this.contactRepository.findById(cid);
		Contact contact = contactOptional.get();

		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		// security for not get another id contact by url
		if (user.getId() == contact.getUser().getId()) {
			// passing the contact detail
			model.addAttribute("contact", contact);
		}
		return "normal/contact_details";
	}

	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Model model, Principal principal,
			HttpSession session) {

		Optional<Contact> userOptional = this.contactRepository.findById(cid);
		Contact contact = userOptional.get();

		User user = this.userRepository.getUserByUserName(principal.getName());

		if (user.getId() == contact.getUser().getId()) {

			user.getContacts().remove(contact);

			this.userRepository.save(user);

//			this.contactRepository.delete(contact);

			System.out.println("deleting");
			session.setAttribute("message", new Message("Contact deletd sucessfully..!!", " alert-success"));
		}

		return "redirect:/user/show-contacts/0";
	}

	@PostMapping("/update/{cid}")
	public String updateForm(@PathVariable("cid") int cid, Principal principal, Model model) {

		model.addAttribute("title", "Update-contact");

		Contact contact = this.contactRepository.findById(cid).get();
		model.addAttribute("contact", contact);

		return "normal/updateForm";
	}

	@PostMapping("/update-process")
	public String processUpdateContact(@Valid @ModelAttribute Contact contact, BindingResult result,
			@RequestParam("image") MultipartFile file, Model model, Principal principal, HttpSession session) {

		try {

			// old contact details
			Contact oldcontact = this.contactRepository.findById(contact.getCid()).get();
			// deleting old file
			File deletetFile = new ClassPathResource("static/img").getFile();

			File file2 = new File(deletetFile, oldcontact.getImage());
			file2.delete();

			if (file.isEmpty()) {
				System.out.println("file is empty");
				contact.setImage("default.png");
			} else {
				contact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			User user = this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);

			this.contactRepository.save(contact);

			session.setAttribute("message", new Message("Your contact updated sucessfully!!", "success"));

		} catch (Exception e) {
			e.printStackTrace();
			return "/normal/updateForm";
		}
		return "redirect:/user/" + contact.getCid() + "/contact";
	}
	
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		model.addAttribute("title", "Profile page");
		return"normal/profile";
	}
}
