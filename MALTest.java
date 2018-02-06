import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
/**
 * Mini Assembly Language (MAL) Project
 * CSI 3210 - Spring 2018 - J. Gurka
 * @author Rajesh
 * 2/5/2018
 */
public class MALTest {

	/**
	 * This is the main class calls the ArrayListOfLines by providing a file name.
	 * From the file, the ArrayListOfLines class returns an arrayList of the 
	 * arrayList of strings. 	 * Then, it calls the EvaluateInstruction class that 
	 * will evaluate the pattern and syntax. If there are any errors in the code, 
	 * it will be printed on the console. 
	 * main entry point
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// creating an object of the class ArrayListOfLines and EvaluateInstruction
		ArrayListOfLines input = new ArrayListOfLines();
		EvaluateInstruction evaluateInstruction = new EvaluateInstruction();
		
		// creating an arrayList of the arrayList of String
		ArrayList <ArrayList<String>> listOfline = new ArrayList<ArrayList<String>>();
		
		String inputFile = "MALProgram2.txt";
		
		// for output file
		String [] inputFileWithoutExt = inputFile.split("\\.");
		String outputFile = inputFileWithoutExt[0] + "-output.txt";
		FileWriter fileWriter = new FileWriter(outputFile);
		BufferedWriter bufferWriter= new BufferedWriter(fileWriter);
		
		// calling a function to get arrayList of arrayList of strings
		listOfline = input.getLines(inputFile, bufferWriter);
		
		
		// calling a function to evaluate the strings in the arrayList
		evaluateInstruction.evaluate(listOfline, bufferWriter);
		
		if (bufferWriter != null)
			bufferWriter.close();
		if (fileWriter != null)
			fileWriter.close();
	}
}