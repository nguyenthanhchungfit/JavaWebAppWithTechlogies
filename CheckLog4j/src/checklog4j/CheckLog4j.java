/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checklog4j;

import org.apache.log4j.Logger;

/**
 *
 * @author cpu11165-local
 */
public class CheckLog4j {

    /**
     * @param args the command line arguments
     */
    private static final Logger logger = Logger.getLogger(CheckLog4j.class);

    public static void main(String[] args) {
        // TODO code application logic here

        for (int i = 0; i < 1000; i++) {
            logger.debug("debug log");
            logger.error("error log");
            logger.info("info log");
        }
    }

}
