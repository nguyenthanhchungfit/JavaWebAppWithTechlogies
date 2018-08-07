/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checklog4japi2x;

/**
 *
 * @author cpu11165-local
 */
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class CheckLog4JAPI2x {

    /**
     * @param args the command line arguments
     */
    private static final Logger logger = LogManager.getLogger(CheckLog4JAPI2x.class);

    public static void main(String[] args) {
        // TODO code application logic here
        logger.trace("Entering application.");
        Bar bar = new Bar();
        if (!bar.doIt()) {
            logger.error("Didn't do it.");
        }
        logger.trace("Exiting application");
        logger.info("Chan");
    }

}
