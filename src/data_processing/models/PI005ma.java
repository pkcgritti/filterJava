package data_processing.models;

import data_processing.container.Window;

public class PI005ma {
	private Window ywnd;
	private Window dwnd;
	private Window Ybuff;
	private double output;
	private double alpha;
	private double k1;
	private double k2;
	private double k3;

	public PI005ma(double l, double d, double e0, double Res, double DR, double G) {
		// Criação de janelas para dados de entrada
		// y (DeltaOhmAbs) e d (Taxa de variação)
		ywnd = new Window(24);
		dwnd = new Window(8);
		Ybuff = new Window(2);
		k1 = l * e0 * Res;
		k2 = l * Res + d * e0 * DR;
		k3 = d * e0;
		alpha = 0.5;

		for (int i = 0; i < 200; i++) {
			this.feed(DR);
		}
	}

	public double feed(double value) {
		ywnd.push(value);
		double yout = computeWindowMean(ywnd);
		double Yout = (k1 / (k2 - k3 * yout));

		Ybuff.push(Yout);
		double[] Ydata = Ybuff.getData();

		dwnd.push((Ydata[1] - Ydata[0]) * 8760);
		double dYout = computeWindowMean(dwnd);
		output = output * alpha + (1 - alpha) * dYout;

		return output;
	}

	public double[] filter(double[] values) {
		int L = values.length;
		double[] retval = new double[L];

		for (int i = 0; i < L; i++)
			retval[i] = this.feed(values[i]);

		return retval;
	}

	// TODO: Mover método de cálculo de média
	// da janela para a propria definição da classe
	private double computeWindowMean(Window wnd) {
		double retval = 0;
		int wndSize = wnd.length();

		for (int i = 0; i < wndSize; i++)
			retval += wnd.at(i);
		retval /= wndSize;

		return retval;
	}
}
