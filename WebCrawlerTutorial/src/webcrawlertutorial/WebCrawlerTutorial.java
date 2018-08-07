/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawlertutorial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author cpu11165-local
 */
public class WebCrawlerTutorial {
    
    public static Queue<String> queue = new LinkedList<String>();
    public static Set<String> marked = new HashSet<>();
    public static String regex = "http[s]*://(\\w+\\.)*(\\w+)";
    
    /**
     * @param args the command line arguments
     */
    
    public static void bfsAlgorithm(String root) throws IOException{
        queue.add(root);
        
        while(!queue.isEmpty()){
            String crawledUrl = queue.poll();
            System.out.println("\n=== Site crawled: " + crawledUrl + " ====");
            
            // we limit to 100 websites here
            if(marked.size() > 100){
                return;
            }
            
            boolean ok = false;
            URL url = null;
            BufferedReader br = null;
            
            while(!ok){
                try{
                   url = new URL(crawledUrl);
                   br = new BufferedReader(new InputStreamReader(url.openStream()));
                   ok = true;
                }catch(MalformedURLException e){
                    System.out.println("*** Maformed URL : " + crawledUrl);
                    crawledUrl = queue.poll();
                    ok = false;
                }catch(IOException ioe){
                    System.out.println("*** IOException for URL : " + crawledUrl);
                    crawledUrl = queue.poll();
                    ok = false;
                }
            }
            
            
            StringBuilder sb = new StringBuilder();
            while((crawledUrl = br.readLine())!= null){
                sb.append(crawledUrl);
            }
            
            crawledUrl = sb.toString();
            
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(crawledUrl);
            
            while(matcher.find()){
                String w = matcher.group();
                
                if(!marked.contains(w)){
                    marked.add(w);
                    System.out.println("Sited added for crawling : " + w);
                    queue.add(w);
                }
            }
            
            if(br!=null){
                br.close();
            }
            
        }
    }
    
    
    public static void showResults(){
        System.out.println("\nResults: ");
        System.out.println("Website crawled: " + marked.size() + "\n");
        for(String s : marked){
            System.out.println("* " + s);
        }
        
        
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            bfsAlgorithm("http://www.ssaurel.com/blog");
            showResults();
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
}
