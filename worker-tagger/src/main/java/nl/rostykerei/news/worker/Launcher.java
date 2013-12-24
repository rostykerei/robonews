package nl.rostykerei.news.worker;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 12/23/13
 * Time: 10:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class Launcher {

    public static void main(String... args) {
        AbstractApplicationContext ctx =
                new ClassPathXmlApplicationContext("workerTaggerContext.xml");
    }
}
