
public class Polynomial {

    private double[] coefficients;

    public Polynomial() {
        this.coefficients = new double[]{0};
    }

    public Polynomial(double[] coefficients) {
        this.coefficients = coefficients;
    }

    public Polynomial add(Polynomial other) {
        // Determine the length of the longer and shorter coefficient arrays
        int minLength = Math.min(this.coefficients.length, other.coefficients.length);
        int maxLength = Math.max(this.coefficients.length, other.coefficients.length);

        // The result polynomial will have a degree of the longer one
        double[] resultCoefficients = new double[maxLength];

        // Add the coefficients that overlap
        for (int i = 0; i < minLength; i++) {
            resultCoefficients[i] = this.coefficients[i] + other.coefficients[i];
        }

        // Copy the remaining coefficients from the longer polynomial
        if (this.coefficients.length > other.coefficients.length) {
            for (int i = minLength; i < maxLength; i++) {
                resultCoefficients[i] = this.coefficients[i];
            }
        } else {
            for (int i = minLength; i < maxLength; i++) {
                resultCoefficients[i] = other.coefficients[i];
            }
        }

        return new Polynomial(resultCoefficients);
    }

    public double evaluate(double x) {
        double result = 0.0;
        // The polynomial is of the form a_0*x^0 + a_1*x^1 + a_2*x^2 + ...
        for (int i = 0; i < this.coefficients.length; i++) {
            result += this.coefficients[i] * Math.pow(x, i);
        }
        return result;
    }
    public boolean hasRoot(double value) {
        return evaluate(value) == 0.0;
    }
}