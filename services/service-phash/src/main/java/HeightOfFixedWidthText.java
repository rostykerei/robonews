import java.awt.*;
import javax.swing.*;

class HeightOfFixedWidthText {

    public static void show(String s) {
        JLabel l = new JLabel(s);
        l.setBackground(Color.CYAN);
        l.setSize(l.getPreferredSize());
        JOptionPane.showMessageDialog(null, l);
        System.out.println(l.getSize());
    }

    public static void main(String[] srgs) {
        String s = "Science Weekly podcast: Ben Goldacre on the need for drug trial transparency";

        String html = "<html><body " +
                "style='background: red; font-family: Arial; font-size: 16px; padding: 0; margin: 0px;"  +
                " width: 360px'>" + s + "</body></html>";

        System.out.println(System.currentTimeMillis());
        show(html);
        System.out.println(System.currentTimeMillis());
        //show(html1+"300"+html2+s+html3);
    }
}