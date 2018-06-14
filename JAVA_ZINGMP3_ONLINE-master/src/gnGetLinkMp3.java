import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class gnGetLinkMp3 {

	
	private final static String Host = "http://mp3.zing.vn";
	
	/*
	public gnGetLinkMp3()
	{
		
		ArrayList<Song> listsong =getAlbum("http://mp3.zing.vn/album/Nhung-Bai-Hat-Hay-Nhat-Cua-Le-Quyen-Le-Quyen/ZWZ9D090.html");
	 
	}
	*/
	
	public static ArrayList<Song> getAlbum(String url)
	{
		if (!isLink(url)) return null;
	
		ArrayList<Song> listsong = new ArrayList<Song>();
		try {
			String data = sendGet(url,"");
			String url_xml = getXMLFile(data);
			 
			if (url_xml==null) return null;
			String out = sendGet(url_xml,"tuser=1");
			String pattern = "(\\{\"id\":(.*?)\"is_vip\")";
			Pattern r = Pattern.compile(pattern);
			int curren =0;
			boolean found = false;
			Matcher m = r.matcher(out);
			while( m.find()){
				//if (curren>out.length() ) break;
			//	Matcher m = r.matcher(out.substring( curren));
				 
						String Data = m.group(1);
						curren= out.indexOf(Data)+Data.length();
						Song song = new Song();
						String id = json2GetValue("id",Data);;
						song.apiLink = getAPI(id);
						song.lyric = json2GetValue("lyric",Data);
						song.artist = json2GetValue("artists_names",Data);
						song.name = json2GetValue("name",Data);
						song.sourceList = "http:"+json2GetValue("128",Data);
						
						song.cover = json2GetValue("cover",Data);
						if (song.lyric!=null)
						if (song.lyric.length()>10)
						song.objLyric = new Lyric (song.lyric);
						listsong.add(song);
				
			}
		} catch (Exception e) {
			 
			e.printStackTrace();
		}
		return listsong;
	}
	

	public static String[] getAPI(String id)
	{
		String API_KEYCODE = "fafd463e2131914934b73310aa34a23f";
		if (id==null ) return null;
		try {
			String apiUrl = "http://api.mp3.zing.vn/api/mobile/song/getsonginfo?keycode="+API_KEYCODE+ "&requestdata={\"id\":\""+ id +"\"}";
			String data = sendGet(apiUrl,"");
			String pattern = "\"link_download\":\\{\"128\":\"(.*)\",\"320\":\"(.*)\"},\"thumbnail\"";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(data);
			if (m.find( )) {
				  String [] kbps= new String[]{m.group(1),m.group(2)};
				  kbps[0] = kbps[0].replace("\\/", "/");
				  kbps[1] = kbps[1].replace("\\/", "/");
				  return kbps;
			   }
			
		} catch (Exception e) {
		
			return null;
		}
		return null;
	}
	
	public static Song getSong(String url )
	{
		
		if (!isLink(url)) return null;
	     Song song = new Song();
		 try {
			String data = sendGet(url,"");
			String url_xml = getXMLFile(data);
			
			System.out.println(url_xml);
			if (url_xml==null) return null;
			String out = sendGet(url_xml,"tuser=1");
			String id =  urlToID(url);
			
			song.apiLink = getAPI(id);
			song.lyric = json2GetValue("lyric",out);
			song.artist = json2GetValue("artists_names",out);
			song.name = json2GetValue("name",out);
			song.sourceList = "http:"+json2GetValue("128",out);
			song.cover = json2GetValue("cover",out);
	 
			if (song.lyric!=null)
			if (song.lyric.length()>10)
			song.objLyric = new Lyric (song.lyric);
			return song;
			//http://mp3.zing.vn/bai-hat/Con-Mua-Ngang-Qua-Son-Tung-M-TP/ZWZCF80A.html
		} catch (Exception e) {
		}	
		return null;
	}
	private static  String json2GetValue(String key,String data)
	{
		String pattern = "\""+key+"\":(\\[|)\"(.*?)\"(,|\"|\\[||\\])";
		
		Pattern r = Pattern.compile(pattern);
	     	Matcher m = r.matcher(data);
	      	if (m.find( )) {
	    	  return m.group(2);
	       }
	      return null;
	}
	public static  String sendGet(String url,String cookie) throws Exception {
		   
		URL obj = new URL(url);
		HttpURLConnection  con = (HttpURLConnection ) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent",  "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) coc_coc_browser/63.4.160 Chrome/57.4.2987.160 Safari/537.36");
		con.setRequestProperty("Cookie", cookie);
		Reader reader = null;
	    if ("gzip".equals(con.getContentEncoding())) {
	         reader = new InputStreamReader(new GZIPInputStream(con.getInputStream()),"UTF8");
	    }
	    else {
	         reader = new InputStreamReader(con.getInputStream(),"UTF8");
	    }
		BufferedReader in = new BufferedReader( reader);
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}
	 
	private static String getXMLFile(String data)
	{
		String pattern="data-xml=\"(.*?)\"";
		Pattern r = Pattern.compile(pattern);
	     Matcher m = r.matcher(data);
	      if (m.find( )) {
	    	  String url =  m.group(1);
	    	  if (! url.startsWith("https://mp3.zing.vn")) url= "https://mp3.zing.vn/xhr"+url;
	    	  return url ;
	       }
	      
		return null;
	}
	private static String urlToID(String url)
	{
		String pattern = "mp3.zing.vn/(bai-hat)/.+/(.*)\\.html";
		Pattern r = Pattern.compile(pattern);
	     Matcher m = r.matcher(url);
	      if (m.find( )) {
	    	  return  m.group(2);
	       }
		return null;
	}
	public static boolean  isLink(String url)
	{
		String pattern = "(https|http)://(m\\.|)mp3.zing.vn/(album|bai-hat)/.*?\\.html";
		Pattern p = Pattern.compile (pattern);
		Matcher  m = p.matcher(url);
		return m.find();
	 
	}
	public static int checkLink(String url)
	{
		if (url.contains("/bai-hat")) return 1;
		if (url.contains("/album")) return 2;
		return 0;
	}
	
}
