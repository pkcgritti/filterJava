package data_processing.filter;
import data_processing.container.Window;

public class Term {
	private int[] lags;
	private double coef;
	
	public Term(int[] lags, double coef) {
		this.lags = lags;
		this.coef = coef;
	}
	
	public double compute(Window wnd) {
		double retval = coef;
		int lid = wnd.length() - 1;
		int len = lags.length;
		
		for(int i = 0; i < len; i++) {
			retval *= wnd.at(lid - lags[i]);
		}
		
		return retval;
	}
}