// ECS629/759 Assignment 2 - ID3 Skeleton Code
// Author: Simon Dixon

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;

class ID3 {

	/** Each node of the tree contains either the attribute number (for non-leaf
	 *  nodes) or class number (for leaf nodes) in <b>value</b>, and an array of
	 *  tree nodes in <b>children</b> containing each of the children of the
	 *  node (for non-leaf nodes).
	 *  The attribute number corresponds to the column number in the training
	 *  and test files. The children are ordered in the same order as the
	 *  Strings in strings[][]. E.g., if value == 3, then the array of
	 *  children correspond to the branches for attribute 3 (named data[0][3]):
	 *      children[0] is the branch for attribute 3 == strings[3][0]
	 *      children[1] is the branch for attribute 3 == strings[3][1]
	 *      children[2] is the branch for attribute 3 == strings[3][2]
	 *      etc.
	 *  The class number (leaf nodes) also corresponds to the order of classes
	 *  in strings[][]. For example, a leaf with value == 3 corresponds
	 *  to the class label strings[attributes-1][3].
	 **/
	class TreeNode {

		TreeNode[] children;
		int value;

		public TreeNode(TreeNode[] ch, int val) {
			value = val;
			children = ch;
		} // constructor

		public String toString() {
			return toString("");
		} // toString()
		
		String toString(String indent) {
			if (children != null) {
				String s = "";
				for (int i = 0; i < children.length; i++)
					s += indent + data[0][value] + "=" +
							strings[value][i] + "\n" +
							children[i].toString(indent + '\t');
				return s;
			} else
				return indent + "Class: " + strings[attributes-1][value] + "\n";
		} // toString(String)

	} // inner class TreeNode

	private int attributes; 	// Number of attributes (including the class)
	private int examples;		// Number of training examples
	private TreeNode decisionTree;	// Tree learnt in training, used for classifying
	private String[][] data;	// Training data indexed by example, attribute
	private String[][] strings; // Unique strings for each attribute
	private int[] stringCount;  // Number of unique strings for each attribute

	public ID3() {
		attributes = 0;
		examples = 0;
		decisionTree = null;
		data = null;
		strings = null;
		stringCount = null;
	} // constructor
	
	public void printTree() {
		if (decisionTree == null)
			error("Attempted to print null Tree");
		else
			System.out.println(decisionTree);
	} // printTree()

	/** Print error message and exit. **/
	static void error(String msg) {
		System.err.println("Error: " + msg);
		System.exit(1);
	} // error()

	static final double LOG2 = Math.log(2.0);
	
	static double xlogx(double x) {
		return x == 0? 0: x * Math.log(x) / LOG2;
	} // xlogx()

	/** Execute the decision tree on the given examples in testData, and print
	 *  the resulting class names, one to a line, for each example in testData.
	 **/
	public void classify(String[][] testData) {
		if (decisionTree == null)
			error("Please run training phase before classification");
		//split data based on the attribute that corresponds to the VALUE
		//of decisionTree
		//next split data based on the value of each child of decisionTree
		//in turn?
		//once you reach a leaf node (value == attributes), assign that value
		//as the class (final position of each row array)
	} // classify()

	public void train(String[][] trainingData) {
		System.out.println("called train");
		indexStrings(trainingData);//henceforth I should refer to the data array
		System.out.println("result of indexStrings:");
		printStrings();
		//calculate entropy of entire data set at current time
		double totalEntropy = calcEntropy(data);	
		if (totalEntropy == 0) { 
			//this is a leaf so do something?
		}
		System.out.println("totalEntropy of this dataset is: "+totalEntropy);	
		//stores the entropy of a sub-dataset split on a given attribute
		double[] potentialGain = new double[attributes-1];
		int bestAttribute = null; 
		//NOW, for each attribute not yet split on, do the following: 
		for (int i = 0; i < data[0].length-1; i++) {//currently < length-1 to avoid splitting on the class col
			//for each attribute!
			potentialGain[i] = totalEntropy;
			for (int j = 0; j < strings[i].length; j++) {
				//for each example
				potentialGain[i] -= calcEntropy(createSubset(data, i, j));
			}
			bestAttribute = (bestAttribute == null || potentialGain[i] < bestAttribute) ? i : bestAttribute;
			//now the potentialGain array is populated with the information gain from each subset
			//and bestAttribute contains the index of the attribute that gives the most info gain
		}
		//so I think here is where I should do the tree stuff:
		//the value for this node should be bestAttribute
		//the children[] for this node should be the number of unique strings
		if (decisionTree == null) {
			decisionTree = new TreeNode(new TreeNode[attributes[bestAttribute].length], bestAttribute);
		} else {
			//iterate through the existing tree until you find a null child spot?? 
		}
		//I should then create subset arrays for each of the possible values of bestAttribute
		//and call train() on them, this should complete this whole recursive thing??
	} // train()

	public String[][] createSubset(String[][] dataset, int attr, int value) {
		//takes an input dataset and returns a dataset trimmed based on an attribute
		return dataset;
	}// createSubset()

	public double calcEntropy(String[][] subset) {
		//pass the array you want testing, will take arrays with fewer rows but atm columns must be intact?
		double rows = subset.length-1;
		double[] classInstances = new double[stringCount[attributes-1]]; //should always be int[2] in test
		for (int i = 0; i < stringCount[attributes-1]; i++) {
		//initial loop loops through each class, inner loop checks for matches against that class
			String checkClass = strings[attributes-1][i];
			for (int j = 1; j <= rows; j++) {
				String currentRow = subset[j][attributes-1];
				if (currentRow.equals(checkClass)) {
					//System.out.println("a match!");	
					classInstances[i]++;
				}
				else {
					//System.out.println("not a match");
				}
			}
		}// loops creating class arrays
		//this block does the entropy calculation
		//hardcoded version for testing, this will only work on sets with 2 classes
		double testEntropy;
		testEntropy = (-xlogx(classInstances[0]/rows) -xlogx(classInstances[1]/rows));
		//this version assumed there is minimum 1 class, essentially appends 1 more 
		//chunk of math for each additional class
		double entropy = -xlogx(classInstances[0]/rows);
		for (int k = 1; k < classInstances.length; k++) {
			entropy -= (xlogx(classInstances[k]/rows));	
		}
		return entropy;	
	}// calcEntropy

	/** Given a 2-dimensional array containing the training data, numbers each
	 *  unique value that each attribute has, and stores these Strings in
	 *  instance variables; for example, for attribute 2, its first value
	 *  would be stored in strings[2][0], its second value in strings[2][1],
	 *  and so on; and the number of different values in stringCount[2].
	 **/
	void indexStrings(String[][] inputData) {
		data = inputData;
		examples = data.length;
		attributes = data[0].length;
		stringCount = new int[attributes];
		strings = new String[attributes][examples];// might not need all columns
		int index = 0;
		for (int attr = 0; attr < attributes; attr++) {
			stringCount[attr] = 0;
			for (int ex = 1; ex < examples; ex++) {
				for (index = 0; index < stringCount[attr]; index++)
					if (data[ex][attr].equals(strings[attr][index]))
						break;	// we've seen this String before
				if (index == stringCount[attr])		// if new String found
					strings[attr][stringCount[attr]++] = data[ex][attr];
			} // for each example
		} // for each attribute
	} // indexStrings()

	/** For debugging: prints the list of attribute values for each attribute
	 *  and their index values.
	 **/
	void printStrings() {
		for (int attr = 0; attr < attributes; attr++)
			for (int index = 0; index < stringCount[attr]; index++)
				System.out.println(data[0][attr] + " value " + index +
									" = " + strings[attr][index]);
	} // printStrings()
		
	/** Reads a text file containing a fixed number of comma-separated values
	 *  on each line, and returns a two dimensional array of these values,
	 *  indexed by line number and position in line.
	 **/
	static String[][] parseCSV(String fileName)
								throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String s = br.readLine();
		int fields = 1;
		int index = 0;
		while ((index = s.indexOf(',', index) + 1) > 0)
			fields++;
		int lines = 1;
		while (br.readLine() != null)
			lines++;
		br.close();
		String[][] data = new String[lines][fields];
		Scanner sc = new Scanner(new File(fileName));
		sc.useDelimiter("[,\n]");
		for (int l = 0; l < lines; l++)
			for (int f = 0; f < fields; f++)
				if (sc.hasNext())
					data[l][f] = sc.next();
				else
					error("Scan error in " + fileName + " at " + l + ":" + f);
		sc.close();
		return data;
	} // parseCSV()

	public static void main(String[] args) throws FileNotFoundException,
												  IOException {
													  
		if (args.length != 2) {
			error("Expected 2 arguments: file names of training and test data");
		}
		String[][] trainingData = parseCSV(args[0]);
		String[][] testData = parseCSV(args[1]);
		ID3 classifier = new ID3();
		System.out.println("debugging! We just instantiated classifier");
		classifier.train(trainingData);
		classifier.printTree();
		classifier.classify(testData);
	} // main()

} // class ID3
