/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chungnt.formjetty;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cpu11165-local
 */
public class RegisterInformServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Enumeration<String> args = req.getParameterNames();
        
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);
        
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String repassword = req.getParameter("repassword");
        String fullname = req.getParameter("fullname");
        String dob = req.getParameter("dob");
        String sex = req.getParameter("sex");
        String address = req.getParameter("address");
        
        String err = "<p>";
        boolean isRequired = true;
        
        if(username.isEmpty()){
            isRequired = false;
            err += "</br> username is required!";
        }
        if(password.isEmpty()){
            err += "</br> password is required!";
        }
        if(fullname.isEmpty()){
            isRequired = false;
            err += "</br> fullname is required!";
        }
        err += "</p>";
        if(!isRequired){
            out.println(err);
            RequestDispatcher rd = req.getRequestDispatcher("index.html");
            rd.include(req, resp);
            return;
        }
        
        if(!password.equals(repassword)){
            out.println("<p>Two passwords are not match!</p>");
            
            return;
        }
        
        String html = "username : " + username;
        html += "</br>password: " + password;
        html += "</br>repassword: " + repassword;
        html += "</br>fullname: " + fullname;
        html += "</br>dob: " + dob;
        html += "</br>sex: " + sex;
        html += "</br>address: " + address;
                
        out.println("<h1>Welcome " + fullname + "</h1>");
        out.println("<p>" + html + "</p>");
    }
    
}
