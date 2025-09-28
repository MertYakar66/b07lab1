import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Polynomial {

    private double[] coeffs;
    private int[] exps;

    /**
     * Default constructor for a zero polynomial.
     */
    public Polynomial() {
        this.coeffs = new double[0];
        this.exps = new int[0];
    }

    /**
     * Constructor to create a polynomial from coefficient and exponent arrays.
     */
    public Polynomial(double[] coeffs, int[] exps) {
        this.coeffs = new double[coeffs.length];
        this.exps = new int[exps.length];
        // Use a basic loop to copy array elements
        for (int i = 0; i < coeffs.length; i++) {
            this.coeffs[i] = coeffs[i];
            this.exps[i] = exps[i];
        }
    }
    
    /**
     * Constructor that initializes the polynomial from a file.
     */
    public Polynomial(File file) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(file)) {
            if (!scanner.hasNextLine()) {
                this.coeffs = new double[0];
                this.exps = new int[0];
                return;
            }
            String line = scanner.nextLine().trim();
            
            // Temporary lists to hold parsed terms
            ArrayList<Double> coeffsList = new ArrayList<>();
            ArrayList<Integer> expsList = new ArrayList<>();
            
            parseLineAndFillLists(line, coeffsList, expsList);
            
            // Simplify the collected terms (sort and combine duplicates)
            simplify(coeffsList, expsList);
        }
    }
    
    /**
     * Helper method to parse a string and populate temporary lists of terms.
     */
    private void parseLineAndFillLists(String polyString, ArrayList<Double> coeffsList, ArrayList<Integer> expsList) {
        if (polyString.isEmpty()) {
            return;
        }

        String processedString = polyString.replace("-", "+-");
        if (processedString.startsWith("+-")) {
            processedString = processedString.substring(2);
            processedString = "-" + processedString;
        } else if (processedString.startsWith("+")) {
             processedString = processedString.substring(1);
        }

        String[] termStrings = processedString.split("\\+");

        for (String term : termStrings) {
            if (term.isEmpty()) continue;

            double coeff;
            int exp;

            if (!term.contains("x")) {
                coeff = Double.parseDouble(term);
                exp = 0;
            } else {
                String[] parts = term.split("x", -1);
                
                if (parts[0].isEmpty()) {
                    coeff = 1.0;
                } else if (parts[0].equals("-")) {
                    coeff = -1.0;
                } else {
                    coeff = Double.parseDouble(parts[0]);
                }
                
                if (parts.length < 2 || parts[1].isEmpty()) {
                    exp = 1;
                } else {
                    exp = Integer.parseInt(parts[1]);
                }
            }
            coeffsList.add(coeff);
            expsList.add(exp);
        }
    }

    /**
     * Private helper method to sort terms by exponent and combine duplicates.
     * This method does the job that the TreeMap did in the advanced version.
     */
    private void simplify(ArrayList<Double> coeffsList, ArrayList<Integer> expsList) {
        // Step 1: Sort the terms by exponent using a simple Selection Sort
        int n = expsList.size();
        for (int i = 0; i < n - 1; i++) {
            int min_idx = i;
            for (int j = i + 1; j < n; j++) {
                if (expsList.get(j) < expsList.get(min_idx)) {
                    min_idx = j;
                }
            }
            // Swap exponent
            int tempExp = expsList.get(min_idx);
            expsList.set(min_idx, expsList.get(i));
            expsList.set(i, tempExp);
            // Swap corresponding coefficient
            double tempCoeff = coeffsList.get(min_idx);
            coeffsList.set(min_idx, coeffsList.get(i));
            coeffsList.set(i, tempCoeff);
        }

        // Step 2: Combine sorted terms with the same exponent
        if (n == 0) {
            this.coeffs = new double[0];
            this.exps = new int[0];
            return;
        }

        ArrayList<Double> finalCoeffs = new ArrayList<>();
        ArrayList<Integer> finalExps = new ArrayList<>();

        double currentCoeff = coeffsList.get(0);
        int currentExp = expsList.get(0);

        for (int i = 1; i < n; i++) {
            if (expsList.get(i) == currentExp) {
                currentCoeff += coeffsList.get(i);
            } else {
                if (currentCoeff != 0) { // Don't add terms that cancel out
                    finalCoeffs.add(currentCoeff);
                    finalExps.add(currentExp);
                }
                currentCoeff = coeffsList.get(i);
                currentExp = expsList.get(i);
            }
        }
        // Add the very last term
        if (currentCoeff != 0) {
            finalCoeffs.add(currentCoeff);
            finalExps.add(currentExp);
        }
        
        // Step 3: Convert the final, clean lists into our instance arrays
        this.coeffs = new double[finalCoeffs.size()];
        this.exps = new int[finalCoeffs.size()];
        for (int i = 0; i < finalCoeffs.size(); i++) {
            this.coeffs[i] = finalCoeffs.get(i);
            this.exps[i] = finalExps.get(i);
        }
    }

    /**
     * Adds this polynomial to another polynomial.
     */
    public Polynomial add(Polynomial other) {
        ArrayList<Double> resultCoeffs = new ArrayList<>();
        ArrayList<Integer> resultExps = new ArrayList<>();
        
        int i = 0; // pointer for this polynomial's terms
        int j = 0; // pointer for other polynomial's terms

        while (i < this.coeffs.length && j < other.coeffs.length) {
            if (this.exps[i] == other.exps[j]) {
                double sum = this.coeffs[i] + other.coeffs[j];
                if (sum != 0) {
                    resultCoeffs.add(sum);
                    resultExps.add(this.exps[i]);
                }
                i++;
                j++;
            } else if (this.exps[i] < other.exps[j]) {
                resultCoeffs.add(this.coeffs[i]);
                resultExps.add(this.exps[i]);
                i++;
            } else {
                resultCoeffs.add(other.coeffs[j]);
                resultExps.add(other.exps[j]);
                j++;
            }
        }
        
        // Add any remaining terms from this polynomial
        while (i < this.coeffs.length) {
            resultCoeffs.add(this.coeffs[i]);
            resultExps.add(this.exps[i]);
            i++;
        }

        // Add any remaining terms from the other polynomial
        while (j < other.coeffs.length) {
            resultCoeffs.add(other.coeffs[j]);
            resultExps.add(other.exps[j]);
            j++;
        }

        // Convert ArrayLists to arrays using a simple for loop
        double[] finalCoeffs = new double[resultCoeffs.size()];
        int[] finalExps = new int[resultExps.size()];
        for (int k = 0; k < resultCoeffs.size(); k++) {
            finalCoeffs[k] = resultCoeffs.get(k);
            finalExps[k] = resultExps.get(k);
        }

        return new Polynomial(finalCoeffs, finalExps);
    }

    /**
     * Evaluates the polynomial for a given value of x.
     */
    public double evaluate(double x) {
        double result = 0.0;
        for (int i = 0; i < coeffs.length; i++) {
            result += this.coeffs[i] * Math.pow(x, this.exps[i]);
        }
        return result;
    }

    /**
     * Checks if a given value is a root of the polynomial.
     */
    public boolean hasRoot(double value) {
        return Math.abs(evaluate(value)) < 1e-9;
    }

    /**
     * Multiplies this polynomial by another polynomial.
     */
    public Polynomial multiply(Polynomial other) {
        ArrayList<Double> rawCoeffs = new ArrayList<>();
        ArrayList<Integer> rawExps = new ArrayList<>();

        // Create all the new terms, without combining them yet
        for (int i = 0; i < this.coeffs.length; i++) {
            for (int j = 0; j < other.coeffs.length; j++) {
                int newExp = this.exps[i] + other.exps[j];
                double newCoeff = this.coeffs[i] * other.coeffs[j];
                rawCoeffs.add(newCoeff);
                rawExps.add(newExp);
            }
        }
        
        // Create a new polynomial and use our simplify helper to process the raw terms
        Polynomial result = new Polynomial();
        result.simplify(rawCoeffs, rawExps);
        return result;
    }
    
    /**
     * Saves the polynomial to a text file.
     */
    public void saveToFile(String filename) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.print(this.toString());
        }
    }
    
    public String toString() {
        if (coeffs.length == 0) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();

        for (int i = coeffs.length - 1; i >= 0; i--) {
            double coeff = coeffs[i];
            int exp = exps[i];

            if (coeff > 0 && i != coeffs.length - 1) {
                sb.append("+");
            }

            // Coefficient formatting without DecimalFormat
            // Check if the coefficient is a whole number
            if (exp > 0 && Math.abs(coeff) == 1) {
                if (coeff == -1) {
                    sb.append("-");
                }
            } else {
                if (coeff == (long) coeff) {
                    sb.append((long) coeff); // Print as integer if whole
                } else {
                    sb.append(coeff); // Print as double otherwise
                }
            }

            if (exp > 0) {
                sb.append("x");
                if (exp > 1) {
                    sb.append(exp);
                }
            }
        }
        return sb.toString();
    }
}
