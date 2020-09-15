package com.oneso.web.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthorizationFilter implements Filter {

  private ServletContext context;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    context = filterConfig.getServletContext();
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    String uri = request.getRequestURI();
    this.context.log("Requested Resource:" + uri);

    HttpSession session = request.getSession(false);

    if (session == null) {
      response.sendRedirect("/login");
    } else {
      chain.doFilter(servletRequest, servletResponse);
    }
  }
}
