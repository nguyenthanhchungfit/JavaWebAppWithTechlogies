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
import user_data.Communicator;
import user_data.User;
import user_data.UserResult;

/**
 *
 * @author cpu11165-local
 */
public class LoginServlet extends HttpServlet{

    private final int port = 8001;
    private final String host = "localhost";

    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);
        
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        if(username.equals("") || password.equals("")){
            TemplateLoader templateLoader = TemplateResourceLoader.create("chung/views");     
            try{
                Template template = templateLoader.getTemplate("login.xtm");
                TemplateDictionary templateDictionary = new TemplateDictionary();
                templateDictionary.setVariable("err", "<h3>Please fill in all fields!</h3>");
                
                templateDictionary.setVariable("username", username);
                templateDictionary.setVariable("password", password);
                out.println(template.renderToString(templateDictionary));
            }catch(Exception e){
                e.printStackTrace();
            }
            return;
        }
        
        // Da dien day du thong tin
        // Check du lieu ben DataCenter
        User user = checkValidAccount(username, password);
        
        if(user == null){
            TemplateLoader templateLoader = TemplateResourceLoader.create("chung/views");     
            try{
                Template template = templateLoader.getTemplate("login.xtm");
                TemplateDictionary templateDictionary = new TemplateDictionary();
                templateDictionary.setVariable("err", "<h3>Username or password is wrong!</h3>");           
                templateDictionary.setVariable("username", username);
                templateDictionary.setVariable("password", password);
                out.println(template.renderToString(templateDictionary));
            }catch(Exception e){
                e.printStackTrace();
            }
            return;
        }
        
        TemplateLoader templateLoader = TemplateResourceLoader.create("chung/views");     
            try{
                Template template = templateLoader.getTemplate("information.xtm");
                TemplateDictionary templateDictionary = new TemplateDictionary();
                
                templateDictionary.setVariable("username", user.username);
                templateDictionary.setVariable("password", user.password);
                templateDictionary.setVariable("fullname", user.fullname);
                templateDictionary.setVariable("address", user.address);
                templateDictionary.setVariable("phone", user.phone);
                templateDictionary.setVariable("dob", user.dob);
                templateDictionary.setVariable("email", user.email);       
                templateDictionary.setVariable("sex", user.sex);                
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
            Template template = templateLoader.getTemplate("login.xtm");
            TemplateDictionary templateDictionary = new TemplateDictionary();
            out.println(template.renderToString(templateDictionary));
        }catch(Exception e){
                e.printStackTrace();
        } 
    }
    
    
    private User checkValidAccount(String username, String password){
        User user = null;
        try{
            TTransport transport = new TSocket(host, port);
            transport.open();
            
            TProtocol protocol = new TBinaryProtocol(transport);               
            CommunicatedDataCenter.Client client = new CommunicatedDataCenter.Client(protocol);        
            UserResult res = client.getInformationUser(username, password);
              if(res.error == 0){
                  user =  res.user;
              }
            transport.close();      
        }catch(TException ex){
            
            ex.printStackTrace();
        }
  
        return user;
    }
    
}
