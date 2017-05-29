package ua.com.proximus.realtimesystems.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CounterPanel extends JPanel {
  private List<JLabel> rows = new ArrayList<JLabel>();

  public CounterPanel(int countOfRows) {
    setLayout(new GridLayout(countOfRows, 1));
    for (int i = 0; i < countOfRows; i++) {
      JLabel row = new JLabel("Statistic of " + (i + 1) + " block: ");
      rows.add(row);
      add(row);
    }
  }

  public void setText(int row, String text) {
    rows.get(row).setText("Statistic of " + (row + 1) + " block: " + text);
    repaint();
  }
}
