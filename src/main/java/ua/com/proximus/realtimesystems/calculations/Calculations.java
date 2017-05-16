package ua.com.proximus.realtimesystems.calculations;

public class Calculations {
  /**
   * Calculate Po for n, lambda and nu parameters
   * @param n Number of task
   * @param lambda Average intensity of task flow
   * @param nu Average time for task execution
   * @return Po for n, lambda and nu parameters
   */
  public static double calcPo(int n, double lambda, double nu) {
    // Calculate Po fo n, lambda and nu parameters
    double ro = lambda * nu;
    int nCurrent = n;
    long nMultiply = 1;
    double denominator = 1.0;
    for (int k = 0; k < n; k++) {
      nMultiply *= nCurrent;
      denominator += nMultiply * Math.pow(ro, k + 1);
      nCurrent -= 1;
    }
    return 1.0 / denominator;
  }

  /**
   * Calculate Po(1, 2, .. i) for i = 1 .. m - 1, where m - number of priorities (n.length)
   * @param n Number of task in i priority
   * @param lambda Average intensity of task flow in i priority
   * @param nu Average time for task execution in i priority
   * @return Array of Po(1, 2, .. k) for i = 1 .. i - 1, where m - number of priorities (n.length)
   */
  public static double[] calcPrevPo(int[] n, double[] lambda, double[] nu) {
    double[] prevPo = new double[n.length];
    for (int i = 0; i < n.length - 1; i++) {
      // Calculate n, lambda, nu for Po(1, 2, ... i)
      int ni = 0;
      double lambdai = 0.0;
      double nui = 0.0;
      for (int k = 0; k <= i; k++) {
        ni += n[k];
        lambdai += n[k] * lambda[k];
        nui += n[k] * nu[k];
      }
      lambdai /= ni;
      nui /= ni;
      prevPo[i] = calcPo(ni, lambdai, nui);
    }
    return prevPo;
  }

  /**
   * Calculate real nu (nu*) taking into account the number of priorities
   * @param n Number of task in i priority
   * @param nu Average time for task execution in i priority
   * @param prevPo Array of Po(1, 2, .. k) for i = 1 .. i - 1, where m - number of priorities
   * @return Array of real nu (nu*) taking into account the number of priorities
   */
  public static double[] calcRealNu(int[] n, double[] nu, double[] prevPo) {
    // Calculate real nu (nu*)
    double[] nuReal = new double[n.length];
    nuReal[0] = nu[0];
    for (int i = 1; i < nuReal.length; i++) {
      nuReal[i] = nu[i] / prevPo[i - 1];
    }
    return nuReal;
  }

  /**
   * Calculate Tp - total average execution time of one task in i priority
   * @param n Number of task in i priority
   * @param lambda Average intensity of task flow in i priority
   * @param nuReal Real average time for task execution in i priority
   * @return Tp - total average execution time of one task in i priority
   */
  public static double calcTp(int n, double lambda, double nuReal) {
    double po = calcPo(n, lambda, nuReal);
    return nuReal * (n - ((1 - po) / (lambda * nuReal))) + nuReal * po;
  }
}
