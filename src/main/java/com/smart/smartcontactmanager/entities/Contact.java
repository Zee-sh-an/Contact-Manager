package com.smart.smartcontactmanager.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "CONTACT")
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid;
	@NotBlank(message = "Name can note be null!!")
	private String name;
	private String secondName;
	private String work;
	@Column(unique = true)
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email !!")
	private String email;
	@NotBlank(message = "Phone Number can not be empty!!")
	@Size(min = 10,max = 13,message = "Enter a valid number")
	private String phone;
	private String image;
	private String description;

	@ManyToOne
	private User user;

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Contact(int cid, String name, String secondName, String work, String email, String phone,
			String image, String description) {
		super();
		this.cid = cid;
		this.name = name;
		this.secondName = secondName;
		this.work = work;
		this.email = email;
		this.phone = phone;
		this.image = image;
		this.description = description;
	}

	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * @Override public String toString() { return "Contact [cid=" + cid + ", name="
	 * + name + ", secondName=" + secondName + ", work=" + work + ", email=" + email
	 * + ", phone=" + phone + ", image=" + image + ", description=" + description +
	 * ", user=" + user + "]"; }
	 */
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.cid==((Contact)obj).getCid();
	}
	
}
