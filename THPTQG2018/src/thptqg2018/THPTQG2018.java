/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thptqg2018;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Nguyen Thanh Chung
 */
public class THPTQG2018 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ProtocolException, MalformedURLException, IOException, ParseException {

        //String source = "{\"success\":true,\"data\":{\"candidate_number\":\"02013129\",\"mark_info\":\"To\\u00e1n:   3.60   Ng\\u1eef v\\u0103n:   4.25   L\\u1ecbch s\\u1eed:   4.25   \\u0110\\u1ecba l\\u00ed:   3.50   GDCD:   6.75   KHXH: 4.83   Ti\\u1ebfng Anh:   2.40   \"}}";
        //System.out.println(source);
        //String decode = decodeUnicode(source);
        //System.out.println(decode);
//       crawlCandidate();
        //System.out.println(getSBD(12,301));
        String data = "Toán:   4.40   Ngữ văn:   7.00   Lịch sử:   4.25   Địa lí:   7.00   GDCD:   7.00   KHXH: 6.08   Tiếng Anh:   6.20   ";
        HashMap<String, String> map = parseStringToMap(data);
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        //crawler();
    }

    private static void crawler() throws IOException, ParseException {
        String sbd = getSBD(1, 67);
        String stringCrawl = crawlCandidate(sbd);
        String decodeStringCrawl = decodeUnicode(stringCrawl);
        JSONObject jsonObj = parseJSON(decodeStringCrawl);
        boolean success = (boolean) jsonObj.get("success");
        if (success) {
            System.out.println(jsonObj.toJSONString());
            JSONObject dataObj = (JSONObject) jsonObj.get("data");
            String point = (String) dataObj.get("mark_info");

        } else {
            System.out.println("fail!");
        }
    }

    private static String getSBD(int code_province, int sbd) {
        StringBuilder sb = new StringBuilder("00000000");
        if (code_province < 10) {
            sb.setCharAt(1, (char) (code_province + 48));
        } else {
            sb.setCharAt(1, (char) (code_province % 10 + 48));
            sb.setCharAt(0, (char) ((code_province / 10) % 10 + 48));
        }

        // add sbd
        int index = 7;
        while (sbd != 0) {
            int sodu = sbd % 10;
            sb.setCharAt(index, (char) (sodu + 48));
            index--;
            sbd /= 10;
        }

        return sb.toString();
    }

    private static String crawlCandidate(String sbd) throws MalformedURLException, IOException {
        // TODO code application logic here
        String url = "https://diemthi.tuyensinh247.com/tsHighSchool/ajaxDiemthi2018";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,vi;q=0.5");

        String urlParameters = "sbd=" + sbd;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return response.toString();
    }

    private static JSONObject parseJSON(String jsonString) throws ParseException {
        System.out.println(jsonString);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
        return jsonObject;
    }

    private static String decodeUnicode(String encode) {
        StringBuilder sb = new StringBuilder();
        int length = encode.length();
        int prevIndex = 0;
        int lastIndex = 0;
        for (int i = 0; i < length; i++) {
            if (encode.charAt(i) == '\\') {
                String unicodeString = encode.substring(i, i + 6);
                System.out.println(unicodeString);
                String decodeString = convertUnicodeToString(unicodeString);
                lastIndex = i;
                if (prevIndex <= lastIndex) {
                    sb.append(encode.substring(prevIndex, lastIndex));
                }
                sb.append(decodeString);
                i += 5;
                prevIndex = i + 1;
            }
        }
        sb.append(encode.substring(prevIndex, length));

        return sb.toString();

    }

    //\u00e1
    private static String convertUnicodeToString(String unicode) {
        System.out.println(unicode);
        unicode = unicode.toUpperCase();
        int a = 0;
        for (int i = 2; i < 6; i++) {
            int b = unicode.charAt(i);
            if (b >= 65) {
                b -= 55;
            } else {
                b -= 48;
            }
            a += b * Math.pow(16, 6 - i - 1);
        }
        char c = (char) a;
        return c + "";
    }

    private static HashMap<String, String> parseStringToMap(String data) {
        HashMap<String, String> map = new HashMap<>();
        String[] arr = data.split("   ");
        int length = arr.length;
        for (int i = 0; i < length; i += 2) {
            map.put(arr[i].substring(0, arr[i].length() - 1), arr[i + 1]);
        }
        return map;
    }

}
