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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import user_data.CommunicatedDataCenter;
import user_data.User;
import user_data.UserResult;


/**
 *
 * @author Nguyen Thanh Chung
 */
public class RegisterFormServlet extends HttpServlet {
    
    
    private final int port = 8001;
    private final String host = "localhost";
    
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
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String email = req.getParameter("email");

        
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
        if(email.isEmpty()){
            isRequired = false;
            err += "</br> email is required!";
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
                templateDictionary.setVariable("phone", phone);
                templateDictionary.setVariable("email", email);
                
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
        
        User user = new User(username, password, fullname, dob, sex, phone, email, address);
        
        err = "";
        
        // Check username is existed?
        boolean isExistedUsername = this.checkUsernameIsExisted(user.username, host, port);
        if(isExistedUsername){
            err += "Username is existed!!</br>";
        }
        // Check email is existed?
        boolean isExistedEmail = this.checkEmailIsExisted(user.email, host, port);
        if(isExistedEmail){
            err += "Email is existed!!</br>";
        }
        // Check phone is existed?
        boolean isExistedPhone = this.checkPhoneIsExisted(user.phone, host, port);
        if(isExistedPhone){
            err += "Phone is existed!!</br>";
        }
        
        
        if(isExistedEmail || isExistedPhone || isExistedUsername){
            this.sendError(err, user, out, "form.xtm");
            return;
        }
        
        boolean addUserSuccess = this.addNewUser(user, host, port);
        if(!addUserSuccess){
            err = "Signup Failed!!";
            this.sendError(err, user, out, "form.xtm");
            return;
        }
        
        try{
            Template template = templateLoader.getTemplate("information.xtm");
            TemplateDictionary templateDictionary = new TemplateDictionary();
            templateDictionary.setVariable("username", username);
            templateDictionary.setVariable("password", password);
            templateDictionary.setVariable("fullname", fullname);
            templateDictionary.setVariable("dob", dob);
            templateDictionary.setVariable("sex", sex);
            templateDictionary.setVariable("address", address);
            templateDictionary.setVariable("phone", phone);
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
    
    
    private void sendError(String error, User user, PrintWriter out, String templateFile){
        TemplateLoader templateLoader = TemplateResourceLoader.create("chung/views");
        try{
            Template template = templateLoader.getTemplate(templateFile);
            TemplateDictionary templateDictionary = new TemplateDictionary();
            
            if(error != null){
                templateDictionary.setVariable("err", "<h3>" + error + "</h3>");
            }
            
            templateDictionary.setVariable("username", user.username);
            templateDictionary.setVariable("password", user.password);
            templateDictionary.setVariable("fullname", user.fullname);
            templateDictionary.setVariable("dob", user.dob);
            templateDictionary.setVariable("address", user.address);
            templateDictionary.setVariable("phone", user.phone);
            templateDictionary.setVariable("email", user.email);
             
            if(user.sex.equals("female")){
                templateDictionary.setVariable("femalesex", "selected");
            }else{
                templateDictionary.setVariable("malesex", "selected");
            }
            
            out.println(template.renderToString(templateDictionary));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    private boolean checkUsernameIsExisted(String username, String host, int port){
            boolean flag = false;
        try{
            TTransport transport = new TSocket(host, port);
            transport.open();           
            TProtocol protocol = new TBinaryProtocol(transport);               
            CommunicatedDataCenter.Client client = new CommunicatedDataCenter.Client(protocol);        
            flag = client.isExistedUser(username);            
            transport.close();      
        }catch(TException ex){
            
            ex.printStackTrace();
        }

        return flag;
    }
    
    
    private boolean checkEmailIsExisted(String email, String host, int port){
            boolean flag = false;
        try{
            TTransport transport = new TSocket(host, port);
            transport.open();           
            TProtocol protocol = new TBinaryProtocol(transport);               
            CommunicatedDataCenter.Client client = new CommunicatedDataCenter.Client(protocol);        
            flag = client.isExistedEmail(email);            
            transport.close();      
        }catch(TException ex){
            
            ex.printStackTrace();
        }

        return flag;
    }
    
    
    private boolean checkPhoneIsExisted(String phone, String host, int port){
        boolean flag = false;
        try{
            TTransport transport = new TSocket(host, port);
            transport.open();           
            TProtocol protocol = new TBinaryProtocol(transport);               
            CommunicatedDataCenter.Client client = new CommunicatedDataCenter.Client(protocol);        
            flag = client.isExistedPhone(phone);            
            transport.close();      
        }catch(TException ex){
            
            ex.printStackTrace();
        }

        return flag;
    }
    
    private boolean addNewUser(User user, String host, int port){
        boolean flag = false;
        try{
            TTransport transport = new TSocket(host, port);
            transport.open();           
            TProtocol protocol = new TBinaryProtocol(transport);               
            CommunicatedDataCenter.Client client = new CommunicatedDataCenter.Client(protocol);        
            flag = client.addNewUser(user);
            transport.close();      
        }catch(TException ex){
            
            ex.printStackTrace();
        }

        return flag;
    }
}
