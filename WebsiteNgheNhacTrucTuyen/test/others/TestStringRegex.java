/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package others;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author cpu11165-local
 */
public class TestStringRegex {
    public static void main(String[] args) {
        Pattern ptn = Pattern.compile("chẳng ph", Pattern.CASE_INSENSITIVE);
        Matcher mtch = ptn.matcher("Sống xa anh chẳng dễ dàng");
        if(mtch.find()){
            System.out.println("done");
        }else{
            System.out.println("not done");
        }
    }
}
