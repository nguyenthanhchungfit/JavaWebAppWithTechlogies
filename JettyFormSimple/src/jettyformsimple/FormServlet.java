/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyformsimple;

import hapax.Template;
import hapax.TemplateDictionary;
import hapax.TemplateException;
import hapax.TemplateLoader;
import hapax.TemplateResourceLoader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Nguyen Thanh Chung
 */
public class FormServlet extends HttpServlet{
   @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException{
        
        HttpSession session = req.getSession();
        
        System.out.println(session.getId());
        System.out.println(new Date(session.getLastAccessedTime()));
        
        
        
        res.setContentType("text/html");
        res.setStatus(HttpServletResponse.SC_OK);
        
        
        
        PrintWriter out = res.getWriter();
        TemplateLoader templateLoader = TemplateResourceLoader.create("chung/views");
        try{
            Template template = templateLoader.getTemplate("form.xtm");            
            TemplateDictionary templateDictionary = new TemplateDictionary();
            String dataSend = template.renderToString(templateDictionary);
            out.println(dataSend);
            return;
        }catch(Exception e){
            e.printStackTrace();
        }
        
        out.println("Error loading Page");       
    }
}
