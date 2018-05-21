/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyformsimple;

import hapax.Template;
import hapax.TemplateDictionary;
import hapax.TemplateLoader;
import hapax.TemplateResourceLoader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author Nguyen Thanh Chung
 */
public class RegisterFormServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
                throws ServletException, IOException {
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

        
        String err = "";
        boolean isRequired = true;
        boolean isPasswordMatched = true;
        
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
        if(isRequired && !password.equals(repassword)){
            err = ("<p>Two passwords are not match!</p>");
            isPasswordMatched = false;
        }
        
        TemplateLoader templateLoader = TemplateResourceLoader.create("chung/views");     
        if(!isRequired || !isPasswordMatched){  
            try{
                Template template = templateLoader.getTemplate("form.xtm");
                TemplateDictionary templateDictionary = new TemplateDictionary();
                templateDictionary.setVariable("err", "<h3>" + err + "</h3>");
                
                templateDictionary.setVariable("username", username);
                templateDictionary.setVariable("password", password);
                templateDictionary.setVariable("repassword", repassword);
                templateDictionary.setVariable("fullname", fullname);
                templateDictionary.setVariable("address", address);
                
                if(dob != ""){
                    templateDictionary.setVariable("dob", dob);
                }               
                if(sex.equals("female")){
                    templateDictionary.setVariable("femalesex", "selected");
                }else{
                    templateDictionary.setVariable("malesex", "selected");
                }
                
                out.println(template.renderToString(templateDictionary));
            }catch(Exception e){
                e.printStackTrace();
            }
            return;
        }       
        // Success
        try{
            Template template = templateLoader.getTemplate("information.xtm");
            TemplateDictionary templateDictionary = new TemplateDictionary();
            templateDictionary.setVariable("username", username);
            templateDictionary.setVariable("password", password);
            templateDictionary.setVariable("fullname", fullname);
            templateDictionary.setVariable("dob", dob);
            templateDictionary.setVariable("sex", sex);
            templateDictionary.setVariable("address", address);
            out.println(template.renderToString(templateDictionary));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);
        TemplateLoader templateLoader = TemplateResourceLoader.create("chung/views");
        try{
            Template template = templateLoader.getTemplate("form.xtm");
            TemplateDictionary templateDictionary = new TemplateDictionary();
            out.println(template.renderToString(templateDictionary));
        }catch(Exception e){
                e.printStackTrace();
        } 
    }
}
