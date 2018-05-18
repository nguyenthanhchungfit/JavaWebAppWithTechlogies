/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chungnt.formjetty;

import hapax.Template;
import hapax.TemplateDictionary;
import hapax.TemplateException;
import hapax.TemplateLoader;
import hapax.TemplateResourceLoader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cpu11165-local
 */
public class FormServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException ,IOException{
        
        res.setContentType("text/html");
        res.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = res.getWriter();
        
        TemplateLoader templateLoader = TemplateResourceLoader.create("chung/views");
        try{
            Template template = templateLoader.getTemplate("book.xtm");
            TemplateDictionary templateDictionary = new TemplateDictionary();
            
            out.println(template.renderToString(templateDictionary));
            return;
            
        }catch(TemplateException e){
            e.printStackTrace();
        }        
        out.println("Error Loading Page!");
    }
}
