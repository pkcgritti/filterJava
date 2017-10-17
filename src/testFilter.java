import data_processing.models.*;
import java.util.List;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.PrintWriter;
import java.io.IOException;

public class testFilter {
	static public double[] getData(String fName) {
		double[] data = null;
		Path path = FileSystems.getDefault().getPath("etc", fName);
		try {
			List<String> lines = Files.readAllLines(path);
			data = new double[lines.size()];
			int index = 0;
			
			for ( String line : lines ) {
				data[index] = Double.parseDouble(line);
				index++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	static public void main(String[] args) {
		double[] data = getData("data005.txt"); // DeltaRAbsOhm
		double[] filtered;                   // EF
		
		// PI003 pi = new PI003(150.37, 2.25, 0.3, 1e-4, 0.00005158645695, 16);
		// PI003 pi = new PI003(150.37, 2.25, 0.3, 1e-4, 0.00007177720702, 16);
		PI005ma pi = new PI005ma(150.37, 2.25, 0.3, 1e-4, 0.00007177720702, 16);
		
		filtered = pi.filter(data);

		try {
			PrintWriter writer = new PrintWriter("etc/output.txt", "UTF-8");
			for (double val : filtered) {
				writer.println(val);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < data.length; i++) {
			System.out.printf("Sample %4d: %8.4f\n", i + 1, filtered[i]);
		}
		
	}
}