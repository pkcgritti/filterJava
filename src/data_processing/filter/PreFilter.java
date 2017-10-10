package data_processing.filter;
import data_processing.container.Window;

public class PreFilter {
	private Window pWnd;
	private Window dWnd;
	private Window fWnd;
	private double output;
	private double coef;
	private double threshold;
	private int npFeed;
	private int ndFeed;
	private int nfFeed;
	
	public PreFilter(int wSize, double coef, double threshold) {
		this.pWnd = new Window(2);
		this.dWnd = new Window(2);
		this.fWnd = new Window(wSize);
		this.output = 0;
		this.coef = coef;
		this.threshold = threshold;
		this.npFeed = 0;
		this.nfFeed = 0;
	}
	
	public double feed(double val) {
		double currDiff = feedPWnd(val);
		double pred = feedDWnd(currDiff);
		output = feedFWnd(val, currDiff, pred);
		return output;
	}
	
	/**
	 * Real time differentiation
	 * @param val True input
	 * @return Current differentiation
	 */
	private double feedPWnd(double val) {
		int necessary = pWnd.length() - 1;
		pWnd.push(val);
		
		if (npFeed < necessary) {
			npFeed++;
			return 0;
		}
		
		// TODO: Change 8760 to real time stamp
		return ( pWnd.at(1) - pWnd.at(0) ) * 8760;
	}
	
	/**
	 * ARIMA Prediction
	 * @param val Current differentiation
	 * @return Derivative prediction
	 */
	private double feedDWnd(double val) {
		int necessary = dWnd.length() - 1;
		dWnd.push(val);
		
		if (ndFeed < necessary) {
			ndFeed++;
			return 0;
		}
		
		return coef * dWnd.at(0);
	}
	
	private double feedFWnd(double rval, double rdiff, double prediction) {
		int necessary = fWnd.length() - 1;
		
		if (nfFeed < necessary) {
			nfFeed++;
			fWnd.push(rval);
			return rval;
		}
		
		double error = rdiff - prediction;
		if ( error * error < threshold ) {
			fWnd.push(rval);
			return rval;
		}
		
		double retval = 0;
		for(int i = 0; i < necessary; i++) {
			retval += fWnd.at(i);
		}
		retval /= necessary;
		fWnd.push(retval);
		return retval;
		
		
	}
}