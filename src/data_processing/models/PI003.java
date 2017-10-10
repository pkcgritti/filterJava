package data_processing.models;
import data_processing.container.Window;
import data_processing.filter.FIR;
import data_processing.filter.PreFilter;

public class PI003 {
	private PreFilter pf;    // Pre filtro
	private FIR f1;          // Filtro estágio 1
	private FIR f2;          // Filtro estágio 2
	private Window f2Buffer; // Buffer para filtro estágio 2
	private double output;   // Valor de saída
	private double alpha;    // Coeficiente para filtro IIR de saída
	private double k1;
	private double k2;
	private double k3;
	
	public PI003(double l, double d, double e0, double Res, double DR, double G) {
		pf = new PreFilter(6, -0.5776, 0.3);
		f1 = new FIR("etc", "pi003_01.fc");
		f2 = new FIR("etc", "pi003_02.fc");
		f2Buffer = new Window(2);
		output = 0;
		alpha = 0.94;
		k1 = l * e0 * Res;
		k2 = l * Res + d * e0 * DR;
		k3 = d * e0;
		
		for(int i = 0; i < 200; i++) {
			this.feed(DR);
		}
	}
	
	public double feed(double val) {
		double pfOut = pf.feed(val);
		double f1Out = f1.feed(pfOut);
		
		double[] f2Buf;
		f2Buffer.push(k1 / (k2 - k3 * f1Out));
		f2Buf = f2Buffer.getData();
		
		double f2Out = f2.feed( ( f2Buf[1] - f2Buf[0] ) * 8760 );
		
		// TODO: Logging
		output = output * alpha + (1 - alpha) * f2Out;
		return output;
	}
	
	public double[] filter(double[] val) {
		int len = val.length;
		double[] retval = new double[len];
		
		for(int i = 0; i < len; i++) {
			retval[i] = this.feed(val[i]);
		}
		
		return retval;
	}
	
	public double getOutput() {
		return output;
	}
}