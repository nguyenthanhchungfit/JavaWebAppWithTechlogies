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
        int a = 10;
        if (a == 10) {
            try {
                System.out.println("try");
                return;
            } catch (Exception ex) {
                System.out.println("catch");
                ex.printStackTrace();
            } finally {
                System.out.println("finally");
            }
        }

        System.out.println("a = " + a);

        try {
            System.out.println("try 2");
            return;
        } catch (Exception ex) {
            System.out.println("catch 2");
            ex.printStackTrace();
        } finally {
            System.out.println("finally 2");
        }
    }
}
