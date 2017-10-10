package data_processing.filter;
import data_processing.container.Window;
import data_processing.filter.Term;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class FIR {
	private Window wnd;
	private List<Term> terms;
	private double output;
	private int nFeed;
	
	public FIR(String firDir, String firFile) {
		terms = new ArrayList<Term>();
		output = 0;
		nFeed = 0;
		
		int maxLag = 0;
		Path path = FileSystems.getDefault().getPath(firDir, firFile);
		try {
			List<String> lines = Files.readAllLines(path);
			for(int i = 0; i < lines.size(); i++) {
				String[] splited = lines.get(i).split(" ");
				
				int[] lags = new int[splited.length-1];
				for(int j = 0; j < splited.length-1; j++) {
					int lag = Integer.parseInt(splited[j]);
					if (lag > maxLag) maxLag = lag;
					lags[j] = lag;
				}
				
				double coef = Double.parseDouble(splited[splited.length-1]);
				
				terms.add(new Term(lags, coef));
			}
			
			wnd = new Window(maxLag + 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double feed(double val) {
		int necessary = wnd.length() - 1;
		
		wnd.push(val);
		if (nFeed < necessary) {
			output = val;
			nFeed++;
		} else {
			int nterms = terms.size();
			output = 0;
			for (int i = 0; i < nterms; i++) {
				output += terms.get(i).compute(wnd);
			}
		}
		
		return output;
	}
	
	public double getOutput() {
		return output;
	}
}