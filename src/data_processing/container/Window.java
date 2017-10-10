package data_processing.container;
import java.lang.IndexOutOfBoundsException;

public class Window {
	private double[] data;
	
	public Window(int len) {
		this.data = new double[len];
	}
	
	public double at(int idx) {
		if (idx >= this.length()) {
			throw new IndexOutOfBoundsException();
		}
		return this.data[idx];
	}
	
	public double[] getData() {
		return this.data;
	}
	
	public int length() {
		return this.data.length;
	}
	
	public void push(double val) {
		int len = this.length();
		for(int i = 0; i < len-1; i++) {
			data[i] = data[i+1];
		}
		data[len-1] = val;
	}
	
	public void push(double[] val) {
		int len = val.length;
		for(int i = 0; i < len; i++) {
			this.push(val[i]);
		}
	}
	
	public void print() {
		int len = this.length();
		for(int i = 0; i < len; i++) {
			System.out.printf("%8.4f", data[i]);
		}
		System.out.print('\n');
	}
}