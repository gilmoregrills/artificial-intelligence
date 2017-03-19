// ECS629/759 Assignment 2 - ID3 Skeleton Code
// Author: Simon Dixon

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

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
	private String checked;
	private String[] checkList;

	public ID3() {
		attributes = 0;
		examples = 0;
		decisionTree = null;
		data = null;
		strings = null;
		stringCount = null;
		checked = "checked";
		checkList = null;
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
		indexStrings(trainingData);
		printStrings();
		checkList = data[0].clone();
		decisionTree = new TreeNode(null, 0);
		buildTree(decisionTree, data);;


	} // train()
	/**
	 * this should take a node and add it to the tree, first node will always
	 * be root and the rest of the calls will be on the queue of TreeNodes
	 * that train() should have prepared
	 **/
	/**
	 * TO DO:
	 * -swap the execution of the main if/else to account for times where the information gain of
	 *  every split is 0 (if all are 0, set all columns to "checked", before geting to the if
	 *  totalEntropy/isChecked 
	 * -work out exactly why the calcentropy/information gain blocks sometimes return NaN, 
	 *  current solution is just a workaround
	 * -fix weird tree attribute names
	 **/
	private void buildTree(TreeNode node, String[][] dataSet) {
		//indexStrings(dataSet);
		double totalEntropy = calcEntropy(dataSet);
		System.out.println("totalEntropy of this dataset is: "+totalEntropy);	
		//stores the entropy of a sub-dataset split on a given attribute
		double[] potentialGain = new double[attributes];
		double[] subSetEntropy;
		double[] instanceCount;
		double rows = examples-1;
		double comparator = 0; 
		int bestAttribute = 0;
			
		//for each attribute not yet split on, calculate potential information gain
		for (int i = 0; i < dataSet[0].length-1; i++) {//-1 to avoid testing class
			System.out.println("		Testing attribute: "+i+" which is: "+dataSet[0][i]);
			if (checkList[i].equals(checked)) {
				System.out.println("			already checked attribute");
				potentialGain[i] = 0;
			} else {
				//first nested for loop readies the arrays
				potentialGain[i] = 0;
				instanceCount = new double[stringCount[i]];
				subSetEntropy = new double[stringCount[i]];
				for (int j = 0; j < stringCount[i]; j++) {
				//for each potential value of current attribute	
					String[][] subSet = createSubset(dataSet, i, j);
					subSetEntropy[j] = calcEntropy(subSet);
			       	instanceCount[j] = attributeCounter(subSet, i, j);
			       	//System.out.println(Arrays.deepToString(subSet));
				}
				//second loop calculates information gain
				potentialGain[i] = totalEntropy;
				for (int k = 0; k < subSetEntropy.length; k++) {	
					potentialGain[i] -= (instanceCount[k]/rows*subSetEntropy[k]);
				}
				//workaround for a current problem, not permanent - fix math error
				if (Double.isNaN(potentialGain[i])) {
					potentialGain[i] = 0;
				}
				potentialGain[i] = Math.abs(potentialGain[i]);
				//if this is the highest gain so far, store the attribute index
				if (potentialGain[i] > comparator) {
					comparator = potentialGain[i];
					bestAttribute = i;
				}
				System.out.println("			information gain of attribute "+dataSet[0][i]+" is: "+potentialGain[i]);

			}
		}
		//if it's a leaf, the class is the the most common attribute in class column of the subset
		if (totalEntropy <= 0.0 || totalEntropy == -0.0 || isChecked(checkList) || dataSet.length == 1) {
			int leafClass = 0;
			int instances = 0;
			if (dataSet.length != 1) {
				for (int y = 0; y < stringCount[attributes-1]; y++) {
					if (instances < attributeCounter(dataSet, dataSet[0].length-1, y)) {
						instances = attributeCounter(dataSet, dataSet[0].length-1, y);
						leafClass = y;
					}
				}
				node.value = leafClass;
			} else {				
				node.value = leafClass;
			//node.children = null;	
			}
			System.out.println("leaf!");
			return;
		} else {
			//if it's not a leaf then split on the best attribute, create a treenode
			//and make a call buildTree again, passing each of those 
			System.out.println("THE BEST attribute to split on is: "+dataSet[0][bestAttribute]+" it is "+potentialGain[bestAttribute]);
			node.value = bestAttribute;
			node.children = new TreeNode[stringCount[bestAttribute]];

			for (int l = 0; l < stringCount[bestAttribute]; l++) {
				String[][] newSet = createSubset(dataSet, bestAttribute, l);
				checkList[bestAttribute] = checked;
				node.children[l] = new TreeNode(null, 0);
				System.out.println("Making recursive call "+l+" ouf of "+stringCount[bestAttribute]+
								   ", splitting on: "+dataSet[0][bestAttribute]+" = "+strings[bestAttribute][l]+
								   "\nThis produces the following array: \n"+Arrays.deepToString(newSet));
				buildTree(node.children[l], newSet);
			}
		}// else
	} // buildTree

	boolean isChecked(String[] headings) {
		int counter = 0;
		for (int i = 0; i < headings.length-1; i++) {
			if (headings[i].equals(checked)) {
				counter++;
			}		
		}
		boolean returnValue = (counter == headings.length-1) ? true : false;
		return returnValue;
	}// isChecked

	/**
	 * Takes an input dataset and returns a dataset trimmed based on a 
	 * particular attribute, value pair (might need updating to also trim
	 * out the attribute column once used if I'm to make this general enough)
	 **/
	String[][] createSubset(String[][] dataSet, int attr, int val) {
		String value = strings[attr][val];
		System.out.println("			value is: "+strings[attr][val]);
		int attCount = attributeCounter(dataSet, attr, val);
		String[][] subSet = new String[attCount+1][dataSet[0].length-1];//THIS WAS -2 SHOULD IT BE???
		int rowCount = 1;
		int rows = dataSet.length;
		subSet[0] = dataSet[0];
		for (int i = 1; i < rows; i++) {
			if (dataSet[i][attr].equals(value)) {
				subSet[rowCount] = dataSet[i];
				rowCount++;
			}
		}
		return subSet;
		
	}// createSubset()

	/**
	 * Pass the array you want to calculate the entropy of, assumes the class
	 * is always the last column, at the moment it will take arrays with a varying
	 * number of rows and columns, and will always pull the number of classes
	 * from the stringCount variable
	 **/
	double calcEntropy(String[][] dataSet) {
		double rows = dataSet.length-1;
		double columns = dataSet[0].length-1;
		double[] classInstances = new double[stringCount[attributes-1]]; //should always be int[2] in test
		for (int i = 0; i < stringCount[attributes-1]; i++) {
		//loops through each class, checks number of instances of that class
			String checkClass = strings[attributes-1][i];
			classInstances[i] = attributeCounter(dataSet, attributes-1, i);
		}
		/*
		 * hardcoded version for testing, this will only work on sets with 2 classes:
		 * double testEntropy;
		 * testEntropy = (-xlogx(classInstances[0]/rows) -xlogx(classInstances[1]/rows));
		 * the production version assumes minimum one class, and is essentially appending 
		 * chunks of math onto the single-class calculation
		 */
		double entropy = -xlogx(classInstances[0]/rows);
		//System.out.println(classInstances[0]+" log2 "+rows);
		for (int k = 1; k < classInstances.length; k++) {
			entropy -= (xlogx(classInstances[k]/rows));
		//	System.out.println(classInstances[k]+" log2 "+rows);	
		}
		//System.out.println("entropy = "+entropy);
		return Math.abs(entropy);	
	}// calcEntropy

	/**
	 * I've had to do this in two seperate functions now so it deserves its own one
	 * takes a dataset, attribute, and value and returns the number of occurrences 
	 * of that specific attribute in the given dataset.
	 **/
	int attributeCounter(String[][] dataSet, int attr, int val) {
		//System.out.println(Arrays.deepToString(dataSet));
		int counter = 0; 
		String value = strings[attr][val];
		if (dataSet.length == 1) {
			return counter;
		}
		for (int i = 1; i < dataSet.length; i++) {
			if (dataSet[i][attr].equals(value)) {
				counter++;
			}
		}		
		return counter;
	}// attributeCounter
	
	/**
	 * this kinda replaces the strings[][] array, and will majorly slow things down
	 * as I'm likely to be calling on it A LOT
	 * because I'm chopping off the array columns too I need a method to return to me
	 * the possible variables in an attribute column, pass a data array in, and you
	 * get an array containing each possible value
	 **/
	String[] attributeValues(String[][] dataSet, int attr) {
		ArrayList<String> attVals = new ArrayList<String>();	
		for (int i = 1; i < dataSet.length; i++) {
			if (!attVals.contains(dataSet[i][attr])) {
				attVals.add(dataSet[i][attr]);
			}
		}
		String[] returnArray = attVals.toArray(new String[0]);
		return returnArray;	
	}// attributeValues

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
		classifier.train(trainingData);
		classifier.printTree();
		classifier.classify(testData);
	} // main()

} // class ID3
