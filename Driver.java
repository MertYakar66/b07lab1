
import java.io.File;
import java.io.FileNotFoundException;

public class Driver {

	public static void main(String[] args) {
		
		System.out.println("## 1. Creating Polynomials ##");
		
		// Our first polynomial is p1(x) = 5x^3 + 6.
		// We need one array for the coefficients and another for the exponents.
		double[] p1_coeffs = {6, 5};
		int[] p1_exps = {0, 3};
		Polynomial p1 = new Polynomial(p1_coeffs, p1_exps);
		System.out.println("p1(x) = " + p1);

		// And here's our second polynomial, p2(x) = -9x^4 - 2x.
		double[] p2_coeffs = {-2, -9};
		int[] p2_exps = {1, 4};
		Polynomial p2 = new Polynomial(p2_coeffs, p2_exps);
		System.out.println("p2(x) = " + p2);
		System.out.println("-------------------------------------\n");

		System.out.println("## 2. Testing Addition and Evaluation ##");
		Polynomial s = p1.add(p2);
		System.out.println("s(x) = p1(x) + p2(x) = " + s);
		System.out.println("s(0.1) = " + s.evaluate(0.1));
		
		// Now let's see if 1 is a root of the resulting polynomial.
		if(s.hasRoot(1)) {
			System.out.println("1 is a root of s(x)");
		} else {
			System.out.println("1 is not a root of s(x)");
		}
		System.out.println("-------------------------------------\n");
		
		System.out.println("## 3. Testing Multiplication ##");
		Polynomial p = p1.multiply(p2);
		System.out.println("p(x) = p1(x) * p2(x) = " + p);
		System.out.println("p(2) = " + p.evaluate(2));
		System.out.println("-------------------------------------\n");
		
		System.out.println("## 4. Testing File Saving and Loading ##");
		try {
			String filename = "polynomial.txt";
			// Let's save the result of our multiplication to a file.
			p.saveToFile(filename);
			System.out.println("Saved p(x) to the file '" + filename + "'");
			
			// Now, we'll try to create a new polynomial by reading that same file.
			File inputFile = new File(filename);
			Polynomial pFromFile = new Polynomial(inputFile);
			System.out.println("Loaded from file: " + pFromFile);
			
			// Just to be sure, let's check if the one we loaded is identical to the one we saved.
			System.out.println("Does loaded polynomial match original? " + p.toString().equals(pFromFile.toString()));

		} catch (FileNotFoundException e) {
			System.err.println("ERROR: A file operation failed.");
			e.printStackTrace();
		}
		System.out.println("-------------------------------------\n");
	}
}