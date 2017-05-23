package ua.com.proximus.realtimesystems.gui;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrioritiesTableModel implements TableModel {
  private int countOfPriorities;
  private int[] countOfTasks = new int[0];
  private double[] lambda = new double[0];
  private double[] nu = new double[0];
  private double[] prevPo;
  private double[] realNu;
  private double[] tp;
  private double[] tWait;

  private final List<TableModelListener> listeners = new ArrayList<TableModelListener>();

  PrioritiesTableModel(int countOfPriorities) {
    setCountOfPriorities(countOfPriorities);
  }

  void setCountOfPriorities(int countOfPriorities) {
    this.countOfPriorities = countOfPriorities;
    countOfTasks = Arrays.copyOf(countOfTasks, countOfPriorities);
    for (int i = 0; i < countOfTasks.length; i++) {
      if (countOfTasks[i] < 1) {
        countOfTasks[i] = 1;
      }
    }
    lambda = Arrays.copyOf(lambda, countOfPriorities);
    for (int i = 0; i < lambda.length; i++) {
      if (lambda[i] < 0.01) {
        lambda[i] = 0.01;
      }
    }
    nu = Arrays.copyOf(nu, countOfPriorities);
    for (int i = 0; i < nu.length; i++) {
      if (nu[i] > 50.0) {
        nu[i] = 50.0;
      } else {
        if (nu[i] <= 0) {
          nu[i] = 1.0;
        }
      }
    }
    prevPo = new double[countOfPriorities];
    realNu = new double[countOfPriorities];
    tp = new double[countOfPriorities];
    tWait = new double[countOfPriorities];
    fireTableChange();
  }

  int[] getCountOfTasks() {
    return countOfTasks;
  }

  double[] getLambda() {
    return lambda;
  }

  double[] getNu() {
    return nu;
  }

  void setPrevPo(double[] prevPo) {
    for (int i = 0; i < prevPo.length; i++) {
      this.prevPo[i] = (Math.rint(prevPo[i] * 100)) / 100.0;
    }
    fireTableChange();
  }

  void setRealNu(double[] realNu) {
    for (int i = 0; i < realNu.length; i++) {
      this.realNu[i] = (Math.rint(realNu[i] * 100)) / 100.0;
    }
    fireTableChange();
  }

  void setTp(double[] tp) {
    for (int i = 0; i < tp.length; i++) {
      this.tp[i] = (Math.rint(tp[i] * 100)) / 100.0;
      tWait[i] = (Math.rint((this.tp[i] - this.nu[i]) * 100)) / 100.0;
    }
    fireTableChange();
  }

  private void clearCalculations() {
    Arrays.fill(prevPo, 0.0);
    Arrays.fill(realNu, 0.0);
    Arrays.fill(tp, 0.0);
    Arrays.fill(tWait, 0.0);
    fireTableChange();
  }

  public int getRowCount() {
    return countOfPriorities;
  }

  public int getColumnCount() {
    return 8;
  }

  public String getColumnName(int columnIndex) {
    switch (columnIndex) {
      case 0:
        return "Number of priority";
      case 1:
        return "Count of tasks";
      case 2:
        return "Lambda";
      case 3:
        return "Nu";
      default:
        return "";
    }
  }

  public Class<?> getColumnClass(int columnIndex) {
    switch (columnIndex) {
      case 0:
      case 1:
        return Integer.class;
      case 2:
      case 3:
        return Double.class;
      default:
        return String.class;
    }
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    switch (columnIndex) {
      case 1:
        return true;
      case 2:
        return true;
      case 3:
        return true;
      default:
        return false;
    }
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    switch (columnIndex) {
      case 0:
        return rowIndex + 1;
      case 1:
        return countOfTasks[rowIndex];
      case 2:
        return lambda[rowIndex];
      case 3:
        return nu[rowIndex];
      case 4:
        if (rowIndex < getRowCount() - 1) {
          String prevPoTitle = "Po(";
          if (rowIndex <= 11) {
            for (int i = 0; i <= rowIndex; i++) {
              prevPoTitle += i + 1;
              if (i != rowIndex) {
                prevPoTitle += ",";
              }
            }
          } else {
            prevPoTitle += "1,..," + (rowIndex + 1);
          }
          return prevPoTitle + ") = " + prevPo[rowIndex];
        } else {
          return "";
        }
      case 5:
        return "Nu* = " + realNu[rowIndex];
      case 6:
        return "Tp = " + tp[rowIndex];
      case 7:
        return "Twait = " + tWait[rowIndex];
      default:
        return "";
    }
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    clearCalculations();
    switch (columnIndex) {
      case 1:
        if ((Integer) aValue >= 1) {
          countOfTasks[rowIndex] = (Integer) aValue;
        }
        break;
      case 2:
        if ((Double) aValue >= 0.01) {
          lambda[rowIndex] = (Double) aValue;
        }
        break;
      case 3:
        if ((Double) aValue >= 0.01 && (Double) aValue <= 50.0) {
          nu[rowIndex] = (Double) aValue;
        }
      default:
        break;
    }
  }

  public void addTableModelListener(TableModelListener l) {
    synchronized (listeners) {
      listeners.add(l);
    }
  }

  public void removeTableModelListener(TableModelListener l) {
    synchronized (listeners) {
      listeners.remove(l);
    }
  }

  private void fireTableChange() {
    synchronized (listeners) {
      for (TableModelListener l : listeners) {
        l.tableChanged(new TableModelEvent(this));
      }
    }
  }
}
