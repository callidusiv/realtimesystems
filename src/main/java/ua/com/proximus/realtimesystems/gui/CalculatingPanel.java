package ua.com.proximus.realtimesystems.gui;

import ua.com.proximus.realtimesystems.calculations.Calculations;
import ua.com.proximus.realtimesystems.measurements.TimeMeasure;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.EventObject;

public class CalculatingPanel extends JPanel {
  private PrioritiesTableModel model;
  private CounterPanel counterPanel = new CounterPanel(3);

  public CalculatingPanel(int countOfPriorities) {
    model = new PrioritiesTableModel(countOfPriorities);

    final JSpinner countOfPrioritiesSpinner = new JSpinner(new SpinnerNumberModel(countOfPriorities, 1, 99, 1));
    countOfPrioritiesSpinner.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        model.setCountOfPriorities((Integer) countOfPrioritiesSpinner.getValue());
      }
    });
    JButton calculateButton = new JButton("Calculate");
    calculateButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        double[] prevPo = Calculations.calcPrevPo(model.getCountOfTasks(), model.getLambda(), model.getNu());
        model.setPrevPo(prevPo);
        double[] realNu = Calculations.calcRealNu(model.getCountOfTasks(), model.getNu(), prevPo);
        model.setRealNu(realNu);
        double[] tp = new double[prevPo.length];
        for (int i = 0; i < tp.length; i++) {
          tp[i] = Calculations.calcTp(model.getCountOfTasks()[i], model.getLambda()[i], realNu[i]);
        }
        model.setTp(tp);

        for (int index = 1; index <= 3; index++) {
          counterPanel.setText(index - 1, String.format("steps = %d, avg time = %.2f", TimeMeasure.getInstance().getCount(index), TimeMeasure.getInstance().getAverage(index)));
          System.out.println("Times of " + index + " block: " + TimeMeasure.getInstance().getMeasurements(index));
        }
        TimeMeasure.getInstance().clear();
      }
    });

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
    topPanel.add(Box.createHorizontalGlue());
    topPanel.add(new JLabel("Count of priorities:"));
    topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    topPanel.add(countOfPrioritiesSpinner);
    topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    topPanel.add(calculateButton);
    topPanel.add(Box.createHorizontalGlue());

    JTable table = new JTable(model);

    setLayout(new BorderLayout());
    add(topPanel, BorderLayout.NORTH);
    add(new JScrollPane(table), BorderLayout.CENTER);
    add(counterPanel, BorderLayout.SOUTH);
  }
}
