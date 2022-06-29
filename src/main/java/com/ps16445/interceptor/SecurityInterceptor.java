package com.ps16445.interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

@Service
public class SecurityInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler)	throws Exception {
		HttpSession session = request.getSession();
		if(session.getAttribute("users") == null){
			response.sendRedirect(request.getContextPath() + "/sties/login");
			return false;
		}
		return true;
	}
}
