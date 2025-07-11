package com.example;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    private HelloBean helloBean;

    // @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            Context ctx = new InitialContext();
            helloBean = (HelloBean) ctx.lookup("java:global/ejb-demo-1.0-SNAPSHOT/HelloBean");

            resp.setContentType("text/plain");
            String name = req.getParameter("name");
            if (name == null) name = "world";
            
            resp.getWriter().println(helloBean.sayHello(name));
            
        } catch (NamingException e) {
            throw new ServletException("Failed to lookup HelloBean", e);
        }


    }
}