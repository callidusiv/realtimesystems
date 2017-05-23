package ua.com.proximus.realtimesystems.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MainFrame {
  private JFrame frame;

  public MainFrame() {
    frame = new JFrame("Real Time Systems");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
  }

  public void init() {
    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(new CalculatingPanel(4));

    frame.setVisible(true);
  }
}
