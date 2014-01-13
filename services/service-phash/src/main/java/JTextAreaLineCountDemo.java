import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import javax.swing.*;

public class JTextAreaLineCountDemo extends JPanel {
    JTextArea textArea;

    public JTextAreaLineCountDemo() {
        super(new GridBagLayout());

        String inputStr = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmo";
        textArea = new JTextArea(inputStr);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(textArea, c);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent ce) {
                System.out.println("Line count: " + countLines(textArea));
            }
        });
    }

    private static int countLines(JTextArea textArea) {
        AttributedString text = new AttributedString(textArea.getText());
        FontRenderContext frc = textArea.getFontMetrics(textArea.getFont())
                .getFontRenderContext();
        AttributedCharacterIterator charIt = text.getIterator();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(charIt, frc);
        float formatWidth = (float) textArea.getSize().width;
        lineMeasurer.setPosition(charIt.getBeginIndex());

        int noLines = 0;
        while (lineMeasurer.getPosition() < charIt.getEndIndex()) {
            lineMeasurer.nextLayout(formatWidth);
            noLines++;
        }

        return noLines;
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("JTextAreaLineCountDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new JTextAreaLineCountDemo());

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}           