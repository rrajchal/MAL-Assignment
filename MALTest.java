
import java.util.ArrayList;

/**
 * Mini Assembly Language (MAL) Project
 * CSI 3210 - Spring 2018 - J. Gurka
 * @author Rajesh
 * 1/29/2018
 */
public class MALTest {

	/**
	 * This is the main class calls the ArrayListOfLines by providing a file name.
	 * From the file, the ArrayListOfLines class returns an arrayList of the arrayList of strings.
	 * Then, it calls the EvaluateInstruction class that will evaluate the pattern and syntax.
	 * If there are any errors in the code, it will be printed on the console. 
	 * main entry point
	 * @param args
	 */
	public static void main(String[] args) {
		// creating an object of the class ArrayListOfLines and EvaluateInstruction
		ArrayListOfLines input = new ArrayListOfLines();
		EvaluateInstruction evaluateInstruction = new EvaluateInstruction();
		
		// creating an arrayList of the arrayList of String
		ArrayList <ArrayList<String>> arrayListOfArrayListOfline = new ArrayList<ArrayList<String>>();
		
		// calling a function to get arrayList of arrayList of strings
		arrayListOfArrayListOfline = input.getLines("MALProgram1.txt");
		
		// calling a function to evaluate the strings in the arrayList
		evaluateInstruction.evaluate(arrayListOfArrayListOfline);
	}
}
