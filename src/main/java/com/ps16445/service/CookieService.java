package com.ps16445.service;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	HttpServletResponse response;
	
	// Get all cookie data
	public Cookie get(String name) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null)
			for(Cookie cookie : cookies)
				if(cookie.getName().equalsIgnoreCase(name))
					return cookie;
		return null;
	}
	
	// Get only value of cookie data
	public String getValue(String name) {
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies)
			if(name.equals(cookie.getName()))
				return cookie.getValue();
		return null;
	}
	
	// Create new cookie
	public Cookie add(String name, String value, int hours) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(hours * 60 * 60);
		cookie.setPath("/");
		response.addCookie(cookie);
		return cookie;
	}
	
	// Remove cookie
	public void remove(String name) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(name)) {
					cookie.setValue("");
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
	}

}
