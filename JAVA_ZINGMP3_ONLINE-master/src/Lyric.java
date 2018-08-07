import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lyric {
	
	 public class lyrictime{
		  public String Text;
		  public int time;
		  public int next=0  ;
		  public String toString()
		  {
			  if (next<=time) return "Time: "+ time+":  "+ Text;
			  return "During : "+time+" - "+ next + " : " + Text;
		  }
	  }
	 
	 ArrayList <lyrictime>wTime = new ArrayList<lyrictime>();
	 private lyrictime lastlyrict = null;
	 public Lyric (String url)
	 {
		 new Thread(){
			public void run()
			{
		 
		 try {
			
			String data = gnGetLinkMp3.sendGet(url,"uid=0");
			parse(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
			}
		 }.start();
		 
	 }
	 private void parse(String data)
	 {
		 
		 String pattern ="(\\[|)(.*?)\\](.*?)(\\[|$)" ;
		 
		 Pattern r = Pattern.compile(pattern);
		 Matcher m = r.matcher(data);
		
		  while (m.find())
		  {
			 
			  add(m);
		  }
		 
	 }
	 private static boolean isInteger(String s) {
		    return isInteger(s,10);
		}
	  private static boolean isInteger(String s, int radix) {
		    if(s.isEmpty()) return false;
		    for(int i = 0; i < s.length(); i++) {
		        if(i == 0 && s.charAt(i) == '-') {
		            if(s.length() == 1) return false;
		            else continue;
		        }
		        if(Character.digit(s.charAt(i),radix) < 0) return false;
		    }
		    return true;
		}
	 private void add(Matcher m)
	 {
		 
	//	 view-source:http://mp3.zing.vn/bai-hat/Shape-Of-You-Ed-Sheeran/ZW78D8WC.html 
		 
		 String firstst = m.group(2);
		 if (!firstst.contains(":")) return ;
		 
		 String min = firstst.substring(0,firstst.indexOf(":"));
		 String sec = firstst.substring(min.length()+1);
		 String Text = m.group(3);
		 int iMin,iSec;
	
		 if (!isInteger(min))
		 {
			 
			 return;
		 }
		 try 
		 {
			  iMin = Integer.parseInt(min);
			  iSec=   (int)(Float.parseFloat(sec)*1000);
		 } catch (NumberFormatException e)
		 {
			 return;
		 }
		 int milisec = iMin*60*1000+ iSec ;
		 lyrictime time = new lyrictime();
		 time.time = milisec;
		 time.Text = Text;
	
		 if (lastlyrict!=null)
		 {
			 lastlyrict.next = milisec;
		 }
		 wTime.add(time);
		
		 lastlyrict = time;
		 
		 
	 }
	 public lyrictime getTextOn(int milisec)
	 {
		
		for ( lyrictime k : wTime )
		{
		 
			if (k.next<=k.time) continue;
			if (k.next> milisec &&milisec>=k.time ) return k;
		}
		 return null;
	 }
	 
}
