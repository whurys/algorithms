package algorithms.ai.ml;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import cogito4j.Matrix;

public class RNNTest {
	
	private static final String EXPECTED = "Expected";
	private static final String PREDICTED = "Predicted";

	private static double[][] X;
	private static double[][] Y;

	private static Matrix w1;
	private static Matrix w2;

	private double[][] W1;
	private double[][] W2;

	private static double[][] XTest;
	/*
	 * -Xmx9000m -Xms6000m
	 */

	public static void main(String[] args) {

		int samples = 20;
		int numBytes = 100;
		
		System.out.println();
		System.out.println("---- Step 1 ------ Start Generating Data Training -------------");
		System.out.println("");

		Map<String,Double[][]> trainingData = generatorXY(samples, numBytes);
		trainingData.forEach((k,v) -> System.out.println(k+" : "+v));
		
		
		System.out.println();
		System.out.println("---------- End Generating Data Training ------- Step 1------");
		System.out.println("");

		System.out.println();
		System.out.println("----- Step 2----- Start Training -------------");
		System.out.println("");
		
		Map<String,Map<String, Double[][]>> trainedFunctions = new TreeMap<>();
		
		for (String key : trainingData.keySet()) {
			if(!key.contains("NO_")) {
				double[][] Y = {{1}};
				Map<String, Double[][]> weightMap = learnFunction(castDoubleToPrimitiveDouble(trainingData.get(key)), Y);
				trainedFunctions.put(key, weightMap);
			}
		}
//
//		Map<String, Double[][]> weightMap = learnFunction(X, Y);
//
		
		System.out.println("Size = "+trainedFunctions.hashCode());
		System.out.println();
		
		System.out.println("---------- End Training ------ Step 2-------");
		System.out.println("");
//
		System.out.println();
		System.out.println("---- Step 3------ Start Tests -------------");
		System.out.println("");
//
		
		trainedFunctions.forEach((k,v) -> {
			
			System.out.println(k);
			
			double[][] w1 = castDoubleToPrimitiveDouble(v.get("W1"));
			double[][] w2 = castDoubleToPrimitiveDouble(v.get("W2"));
			double[][] b1 = castDoubleToPrimitiveDouble(v.get("B1"));
			double[][] b2 = castDoubleToPrimitiveDouble(v.get("B2"));
			
			System.out.println("");
			
			double[][] XTest = generateTest(1, numBytes);
			testLearnFunction(XTest, w1, w2, b1, b2);
		});
		
		

//
//		generateTest(20, numBytes); // quatro cats criados
//
//		testLearnFunction(XTest, w1, w2, b1, b2);
//
		System.out.println();
		System.out.println("---------- End Tests -------------");
		System.out.println("");
//
		System.out.println("--------------------------------------------");
		System.out.println("#######------- FINISHED PROGRAM  -------####");
		System.out.println("--------------------------------------------");

	}

	public static Map<String, Double[][]> learnFunction(double[][] X, double[][] Y) {

		Map<String, Double[][]> weightMap = new HashMap<>();

		int cycles = 40000;

		int m = Y.length;
		int nodes = X.length; // hidden layer size

		double[][] W1 = np.random(nodes, X[0].length);
		double[][] b1 = new double[nodes][m];

		double[][] W2 = np.random(1, nodes);
		double[][] b2 = new double[1][m];

		X = np.T(X);
		Y = np.T(Y);
		
		int n = 0;

		for (int i = 0; i < cycles; i++) {

			// Implement Forward Propagation to calculate A2 (probabilities)
			// LAYER 1
			double[][] Z1 = np.add(np.dot(W1, X), b1);
			double[][] A1 = np.sigmoid(Z1);
			// double[][] A1 = np.relu(Z1);
			// LAYER 2
			double[][] Z2 = np.add(np.dot(W2, A1), b2);
			double[][] A2 = np.sigmoid(Z2);
			// double[][] A2 = np.relu(Z2);

			if (i == cycles - 1) {// store learned
				weightMap.put("W1", castPrimitiveDoubleToDouble(W1));
				weightMap.put("W2", castPrimitiveDoubleToDouble(W2));
				weightMap.put("B1", castPrimitiveDoubleToDouble(b1));
				weightMap.put("B2", castPrimitiveDoubleToDouble(b2));
			}

			double cost = np.cross_entropy(m, Y, A2);

			// Back Prop
			// LAYER 2
			double[][] dZ2 = np.subtract(A2, Y);
			double[][] dW2 = np.divide(np.dot(dZ2, np.T(A1)), m);
			double[][] db2 = np.divide(dZ2, m);
			// LAYER 1
			double[][] dZ1 = np.multiply(np.dot(np.T(W2), dZ2), np.subtract(1.0, np.power(A1, 2)));
			double[][] dW1 = np.divide(np.dot(dZ1, np.T(X)), m);
			double[][] db1 = np.divide(dZ1, m);

			// Gradient descent parameter update.
			// Learning Rate lr
			double lr = 0.01;
			W1 = np.subtract(W1, np.multiply(lr, dW1));
			b1 = np.subtract(b1, np.multiply(lr, db1));

			W2 = np.subtract(W2, np.multiply(lr, dW2));
			b2 = np.subtract(b2, np.multiply(lr, db2));

			int c = cycles / 10;
			if (i % c == 0) {
				np.print("============== Print after " + i + " Cycles.");
				np.print("Cost = " + cost);
				np.print("A2 = " + Arrays.deepToString(A2));
			}

		}

		return weightMap;
	}

	public static void testLearnFunction(double[][] X, double[][] W1, double[][] W2, double[][] b1, double[][] b2) {
		X = np.T(X);
		double[][] Z1 = np.add(np.dot(W1, X), b1);
		double[][] A1 = np.sigmoid(Z1);
		double[][] Z2 = np.add(np.dot(W2, A1), b2);
		double[][] A2 = np.sigmoid(Z2);
		np.print("A2 = " + Arrays.deepToString(A2));
	}

	public static double testLearnedFunction(double[][] X, double[][] W1, double[][] W2, double[][] b1, double[][] b2) {
		double[][] Z1 = np.add(np.dot(W1, X), b1);
		double[][] A1 = np.sigmoid(Z1);
		double[][] Z2 = np.add(np.dot(W2, A1), b2);
		double[][] A2 = np.sigmoid(Z2);
		return A2[0][0];
	}

	public static void testLearnFunction(double[] X, double[][] W1, double[][] W2, double[][] b1, double[][] b2) {
		// Criamos aqui uma matrix que para passar por todos os treinos realisados
		double[][] o = new double[W1.length][X.length];
		// Preenche a matrix com o mesmo objeto para passar por cada treino
		for (int i = 0; i < W1.length; i++) {
			o[i] = X;
		}
		o = np.T(o);
		double[][] Z1 = np.add(np.dot(W1, o), b1);
		double[][] A1 = np.sigmoid(Z1);
		double[][] Z2 = np.add(np.dot(W2, A1), b2);
		double[][] A2 = np.sigmoid(Z2);
		np.print("A2 = " + Arrays.deepToString(A2));
	}

	// criando dados simulados para testes
	private static Map<String, Double[][]> generatorXY(int samples, int numBytes) {

		Map<String, Double[][]> modelMap = new HashMap<>();

		Random r = new Random();

		X = new double[samples][numBytes];
		Y = new double[samples][1];
		

		for (int m = 0; m < samples; m++) {

			String valueY = "";
			
			if (m % 2 == 0 || m % 5 == 0 || m % 7 == 0) { // todos as posicoes no arrays que sao gatos sao pares ou
															// divididos por 7 ...
				// acao arbitraria.
				// Gerando dados in bytes aleatorios
				for (int n = 0; n < numBytes; n++) {
					if (n >= numBytes / 2) {
						X[m][n] = 0.99;
					} else {
						X[m][n] = 0.000001;
					}
				}
				Y[m][0] = 1; // igual ao Cat (valor 1)
				valueY=EXPECTED+"_"+m;

			} else {
				// Gerando dados in bytes aleatorios
				for (int n = 0; n < numBytes; n++) {
					X[m][n] = r.nextDouble() + 0.0001;
				}
				Y[m][0] = 0; // diff Cat (valor 0)
				valueY="NO_"+EXPECTED+"_"+m;
			}
			modelMap.put(valueY, castPrimitiveDoubleToDouble(X));
		}

		return modelMap;
	}

	public static Double[][] castPrimitiveDoubleToDouble(double[][] matrix) {
		Double[][] matrixDouble = new Double[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrixDouble[i][j] = matrix[i][j];
			}
		}
		return matrixDouble;
	}

	public static double[][] castDoubleToPrimitiveDouble(Double[][] matrix) {
		double[][] matrixDouble = new double[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrixDouble[i][j] = matrix[i][j];
			}
		}
		return matrixDouble;
	}

	public static double[][] generateTest(int samples, int numBytes) {

		double[][] XTest = new double[samples][numBytes];

		for (int m = 0; m < samples; m++) {
			// Gerando dados in bytes aleatorios
			for (int n = 0; n < numBytes; n++) {
				if (n >= numBytes / 2) {
					XTest[m][n] = 0.99;
				} else {
					XTest[m][n] = 0.000001;
				}
			}
		}
		return XTest;
	}



}
