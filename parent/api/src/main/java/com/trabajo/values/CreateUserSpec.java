package com.trabajo.values;

import java.util.Locale;

import com.trabajo.ValidationException;
import com.trabajo.process.TitleVV;


public class CreateUserSpec {

	private TitleVV 	title; 
	private FullName 	fullName; 
	private UserName 	userName;
	private Password 	password; 
	private EmailAddr 	email; 
	private Locale 		locale; 
	private LangCode  langPref;
	
	public CreateUserSpec(
			String title, 
			String fullName, 
			String userName,
			String password, 
			String email, 
			String languageTag, 
			String langPref) throws ValidationException {
		
		this.title=new TitleVV(title);
		this.fullName=new FullName(fullName);
		this.userName=new UserName(userName);
		this.locale=Locale.forLanguageTag(languageTag);
		this.langPref=new LangCode(langPref);
		this.email=new EmailAddr(email);
		this.password=new Password(password);
	}


	public TitleVV getTitle() {
		return title;
	}

	public FullName getFullName() {
		return fullName;
	}

	public UserName getUserName() {
		return userName;
	}

	public Password getPassword() {
		return password;
	}

	public EmailAddr getEmail() {
		return email;
	}

	public Locale getLocale() {
		return locale;
	}

	public LangCode getLangPref() {
		return langPref;
	}
}
