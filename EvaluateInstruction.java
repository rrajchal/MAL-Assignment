import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
/**
 * The EvaluateInstruction class will check syntax and pattern error for the MAL project
 * The MAL project is provided by Dr. J. Gurka, Metropolitan State University
 * All the error messages will be displayed in console and in the output file
 * @author Rajesh
 * 2/5/2018
 */
public class EvaluateInstruction {
	// counting errors, it could be changed from any class
	static int numberOfErrors = 0;
	
	/**
	 * This function will print the number of lines in the file
	 * and calls the evaluateLine function for each line
	 * @param arrayListOfArrayListOfline
	 * @param bufferWriter 
	 * @throws IOException 
	 */
	public void evaluate(ArrayList <ArrayList<String>> arrayListOfArrayListOfline, BufferedWriter bufferWriter) throws IOException{
		if (!arrayListOfArrayListOfline.isEmpty()){
			System.out.println("\nNumber of lines in the file: " + arrayListOfArrayListOfline.size());
			bufferWriter.newLine();
			bufferWriter.write("Number of lines in the file: " + arrayListOfArrayListOfline.size());
			bufferWriter.newLine();
			for (int i = 0; i < arrayListOfArrayListOfline.size(); i++){
				evaluateLine(arrayListOfArrayListOfline.get(i), i+1, bufferWriter);
			}
		}
		System.out.println("Total errors: " + numberOfErrors);
		bufferWriter.write("Total errors: " + numberOfErrors);
		bufferWriter.newLine();
	}
	
	/**
	 * The evaluateLine function will call checkSyntaxError to find the errors
	 * @param arrayListOfALine
	 * @param lineNumber
	 * @throws IOException 
	 */
	private void evaluateLine(ArrayList<String> arrayListOfALine, int lineNumber, BufferedWriter bufferWriter) throws IOException {
		boolean syntaxError = false;
		// if the string is empty or starts with ;, it will not evaluate the line
		if (!arrayListOfALine.get(0).equals("") && !arrayListOfALine.get(0).contains(";")){
			syntaxError = checkSyntaxError(arrayListOfALine, bufferWriter);
			if (!syntaxError){
				numberOfErrors++;
				printTheLine(arrayListOfALine, bufferWriter); 
				System.out.println("---Error in line #: " + lineNumber);
				bufferWriter.write("---Error in line #: " + lineNumber);
				bufferWriter.newLine();;
			}	
		}
	}

	/**
	 * This function prints the strings of an arrayList in a line
	 * @param arrayListOfALine
	 * @param bufferWriter 
	 * @throws IOException 
	 */
	private void printTheLine(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) throws IOException {
		for (String s : arrayListOfALine){
			System.out.print(s + " ");
			bufferWriter.write(s + " ");
		}
			
	}

	/**
	 * This function checks for various instructions and calls functions accordingly
	 * @param arrayListOfALine
	 * @return
	 */
	private boolean checkSyntaxError(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) throws IOException {
		//System.out.println(arrayListOfALine); 	// should prints only the codes

		if (arrayListOfALine.get(0).contains(","))
		{
			showError("The first instruction cannot contain a comma.");
			bufferWriter.write("The first instruction cannot contain a comma.: ");
			return false;
		}
		if (arrayListOfALine.get(0).endsWith(":"))	// if the first word ends with ':'
			return checkLabel(arrayListOfALine, bufferWriter);
		else{
			switch (arrayListOfALine.get(0)){
				case "MOVE":
					return checkMOVE(arrayListOfALine, bufferWriter);
				case "MOVEI":
					return checkMOVEI(arrayListOfALine, bufferWriter);
				case "ADD":
					return checkADD(arrayListOfALine, bufferWriter);
				case "INC":
					return checkINC(arrayListOfALine, bufferWriter);
				case "SUB":
					return checkSUB(arrayListOfALine, bufferWriter);
				case "DEC":
					return checkDEC(arrayListOfALine, bufferWriter);
				case "MUL":
					return checkMUL(arrayListOfALine, bufferWriter);
				case "DIV":
					return checkDIV(arrayListOfALine, bufferWriter);
				case "BEQ":
					return checkBEQ(arrayListOfALine, bufferWriter);
				case "BLT":
					return checkBLT(arrayListOfALine, bufferWriter);
				case "BGT":
					return checkBGT(arrayListOfALine, bufferWriter);
				case "BR":
					return checkBR(arrayListOfALine, bufferWriter);
				case "END":
					return checkEND(arrayListOfALine, bufferWriter);
				default:
					showError("Invalid opcode");
					bufferWriter.write("Invalid opcode: ");
					return false;
			}
		}
	}

	/**
	 * This is a halting instruction
	 * @param arrayListOfALine
	 * @param bufferWriter 
	 * @return a boolean value
	 */
	private boolean checkEND(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) {
		// this should be the word by itself
		if (!(arrayListOfALine.size() == 1))
			return false;
		return true;
	}

	private boolean checkLabel(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) throws IOException {
		// checking for label whether it contains only characters ending with a colon
		if (!arrayListOfALine.get(0).matches("[a-zA-z]+[:]")){
			showError("Label must contain only letters ending with a colon.");
			bufferWriter.write("Label must contain only letters ending with a colon.: ");
			return false;
		}
		// if the instruction starts with a label, it must have at least two instruction words
		if (arrayListOfALine.size() < 2){
			showError ("Too less instruction!");
			bufferWriter.write("Too less instruction!: ");
			return false;
		}
		
		// checking if the label is less than 6 characters including a colon
		if (arrayListOfALine.get(0).length() > 6){
			showError("The label must be of maximum 5 letters.");
			bufferWriter.write("The label must be of maximum 5 letters.: ");
			return false;
		}
		
		// making a new array for the arrayList without the label
		ArrayList <String> newArray = new ArrayList<String>();
		
		// copying the elements of arrayListOfALine except of the label (first element)
		for (int i = 1; i < arrayListOfALine.size(); i++)
			newArray.add(arrayListOfALine.get(i));
		
		// without the label the syntax could be any type including MOVE, MOVEI, ...
		return checkSyntaxError(newArray, bufferWriter);
	}

	/**
	 * This function checks for a pattern and syntax [BR lab]
	 * @param arrayListOfALine
	 * @return a boolean value
	 */
	private boolean checkBR(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) throws IOException {
		// checking if the instruction has more or less than two
		if (arrayListOfALine.size() != 2)
		{
			showError("Instruction error");
			bufferWriter.write("Instruction error: ");
			return false;
		}
		// checking for label whether it contains only characters
		if (!arrayListOfALine.get(1).matches("[a-zA-z]+")){
			showError("The label can contain only letters.");
			return false;
		}		
		// checking if the label is less than 5 characters
		if (arrayListOfALine.get(1).length() > 5){
			showError("The label must be of maximum 5 letters.");
			bufferWriter.write("The label must be of maximum 5 letters.: ");
			return false;
		}
		return true;	// if everything before is not true, it will return true
	}

	/**
	 * This function checks for a pattern and syntax [BGT s1, s2, lab]
	 * @param arrayListOfALine
	 * @return a boolean value
	 */
	private boolean checkBGT(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) throws IOException {
		// this is same as the checkBEQ(arrayListOfALine) function
		return checkBEQ(arrayListOfALine, bufferWriter) ; 
	}

	/**
	 * This function checks for a pattern and syntax [BLT s1, s2, lab]
	 * @param arrayListOfALine
	 * @return a boolean value
	 */
	private boolean checkBLT(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) throws IOException {
		// this is same as the checkBEQ(arrayListOfALine) function
		return checkBEQ(arrayListOfALine, bufferWriter) ; 
	}

	/**
	 * This function checks for a pattern and syntax [BEQ s1, s2, lab]
	 * @param arrayListOfALine
	 * @return a boolean value
	 */
	private boolean checkBEQ(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) throws IOException {
		// checking if the instruction has more or less than four
		if (arrayListOfALine.size() != 4)
		{
			showError("Instruction error");
			bufferWriter.write("Instruction error: ");
			return false;
		}
		// checking if the first source location ends with a comma
		if (!checkForComma(arrayListOfALine.get(1))){
			showError("The source (first) must ends with a comma.");
			bufferWriter.write("The source (first) must ends with a comma.: ");
			return false;
		}
		// checking if the second source location ends with a comma
		if (!checkForComma(arrayListOfALine.get(2))){
			showError("The source (second) must ends with a comma.");
			bufferWriter.write("The source (second) must ends with a comma.: ");
			return false;
		}		
		// checking if the first source location has integers/register ending with a comma
		if (checkForComma(arrayListOfALine.get(1))){
			String wordWithoutComma = arrayListOfALine.get(1).substring(0, arrayListOfALine.get(1).length()-1);
			// the word should either contain register or octal value or a source name
			if (!wordWithoutComma.matches("[0-7]+") && !checkForRegister(wordWithoutComma)
					&& !checkForSource(wordWithoutComma)){
				showError("Invalid Source");
				bufferWriter.write("Invalid Source: ");
				return false;
			}
		}		
		// checking if the second source location has integers/register ending with a comma
		if (checkForComma(arrayListOfALine.get(2))){
			String wordWithoutComma = arrayListOfALine.get(2).substring(0, arrayListOfALine.get(2).length()-1);
			// the word should either contain register or octal value or a source name
			if (!wordWithoutComma.matches("[0-7]+") && !checkForRegister(wordWithoutComma)
					&& !checkForSource(wordWithoutComma)){
				showError("Invalid Source");
				bufferWriter.write("Invalid Source: ");
				return false;
			}
		}
		// checking for label whether it contains only characters
		if (!arrayListOfALine.get(3).matches("[a-zA-z]+")){
			showError("The label can contain only letters.");
			bufferWriter.write("The label can contain only letters.: ");
			return false;
		}
		// checking if the label is less than 5 characters
		if (arrayListOfALine.get(3).length() > 5){
			showError("The label must be of maximum 5 letters.");
			bufferWriter.write("The label must be of maximum 5 letters.: ");
			return false;
		}
		return true;	// if everything before is not true, it will return true
	}

	/**
	 * This function checks for a pattern and syntax [DIV s1, s2, d]
	 * @param arrayListOfALine
	 * @param bufferWriter 
	 * @return a boolean value
	 */
	private boolean checkDIV(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) throws IOException {
		// this is same as the checkAdd(arrayListOfALine) function
		return checkADD(arrayListOfALine, bufferWriter);
	}

	/**
	 * This function checks for a pattern and syntax [MUL s1, s2, d]
	 * @param arrayListOfALine
	 * @param bufferWriter 
	 * @return a boolean value
	 */
	private boolean checkMUL(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) throws IOException {
		// this is same as the checkAdd(arrayListOfALine) function
		return checkADD(arrayListOfALine, bufferWriter);
	}
	
	/**
	 * This function checks for a pattern and syntax [DEC s]
	 * @param arrayListOfALine
	 * @param bufferWriter 
	 * @return a boolean value
	 * @throws IOException 
	 */
	private boolean checkDEC(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) throws IOException {
		// the check is same as the function checkINC(arrayListOfALine)
		return checkINC(arrayListOfALine, bufferWriter);
	}

	/**
	 * This function checks for a pattern and syntax [SUB s1, s2, d]
	 * @param arrayListOfALine
	 * @param bufferWriter 
	 * @return a boolean value
	 */
	private boolean checkSUB(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) throws IOException {
		// this is same as the checkAdd(arrayListOfALine) function
		return checkADD(arrayListOfALine, bufferWriter);
	}

	/**
	 * This function checks for a pattern and syntax [INC s]
	 * @param arrayListOfALine
	 * @param bufferWriter 
	 * @return a boolean value
	 * @throws IOException 
	 */
	private boolean checkINC(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) throws IOException {
		if (arrayListOfALine.size() != 2)
		{
			showError("Instruction error");
			bufferWriter.write("Instruction error: ");
			return false;
		}
		// the source should either contain an or octal value or register number
		if (!arrayListOfALine.get(1).matches("[0-7]+") && !checkForRegister(arrayListOfALine.get(1)) 
				&& !(checkForSource(arrayListOfALine.get(1)))){
			showError("Invalid Source");
			bufferWriter.write("Invalid Source: ");
			return false;
		}
		return true;
	}

	/**
	 * This function checks for a pattern and syntax [ADD s1, s2, d]
	 * @param arrayListOfALine
	 * @param bufferWriter 
	 * @return a boolean value
	 */
	private boolean checkADD(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) throws IOException {
		// checking if the instruction has more or less than four
		if (arrayListOfALine.size() != 4)
		{
			showError("Instruction error");
			bufferWriter.write("Instruction error: ");
			return false;
		}
		// checking if the first source location ends with a comma
		if (!checkForComma(arrayListOfALine.get(1))){
			showError("The source (first) must ends with a comma.");
			bufferWriter.write("The source (first) must ends with a comma.: ");
			return false;
		}
		// checking if the second source location ends with a comma
		if (!checkForComma(arrayListOfALine.get(2))){
			showError("The source (second) must ends with a comma.");
			bufferWriter.write("The source (second) must ends with a comma.: ");
			return false;
		}
		
		// checking if the first source location has integers/register ending with a comma
		if (checkForComma(arrayListOfALine.get(1))){
			String wordWithoutComma = arrayListOfALine.get(1).substring(0, arrayListOfALine.get(1).length()-1);
			// the word should either contain register or an octal value or a source name
			if (!wordWithoutComma.matches("[0-7]+") && !checkForRegister(wordWithoutComma)
					&& !checkForSource(wordWithoutComma)){
				showError("Invalid Source");
				bufferWriter.write("Invalid Source: ");
				return false;
			}
		}
		
		// checking if the second source location has integers/register ending with a comma
		if (checkForComma(arrayListOfALine.get(2))){
			String wordWithoutComma = arrayListOfALine.get(2).substring(0, arrayListOfALine.get(2).length()-1);
			// the word should either contain register or an octal value or a source name
			if (!wordWithoutComma.matches("[0-7]+") && !checkForRegister(wordWithoutComma)
					&& !checkForSource(wordWithoutComma)){
				showError("Invalid Source");
				bufferWriter.write("Invalid Source: ");
				return false;
			}
		}
		
		// checking if the destination is a register 
		if (!checkForRegister(arrayListOfALine.get(3)) && !checkForDestination(arrayListOfALine.get(3), bufferWriter))
			return false;
		return true;	// if everything before is not true, it will return true
	}

	/**
	 * This function checks if the syntax is exactly like [MOVE, s, d]
	 * @param arrayListOfALine
	 * @param bufferWriter 
	 * @return a boolean value
	 */
	private boolean checkMOVEI(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) throws IOException {
		// checking if the instruction has more or less than three
		if (arrayListOfALine.size() != 3)
		{
			showError("Instruction error");
			bufferWriter.write("Instruction error: ");
			return false;
		}
		// checking if the source location ends with a comma
		else if (!checkForComma(arrayListOfALine.get(1))){
			showError("The integer must ends with a comma.");
			bufferWriter.write("The integer must ends with a comma.: ");
			return false;
		}
		// checking if the source location has integers ending with a comma
		if (checkForComma(arrayListOfALine.get(1))){
			String wordWithoutComma = arrayListOfALine.get(1).substring(0, arrayListOfALine.get(1).length()-1);
			if (!wordWithoutComma.matches("[0-7]+")){
				showError("Source should contain octal values.");
				bufferWriter.write("Source should contain octal values.: ");
				return false;
			}
		}
		// checking for destination location R0-R7
		if (!checkForRegister(arrayListOfALine.get(2)) && !checkForDestination(arrayListOfALine.get(2), bufferWriter)){
			showError("Invalid Destination");
			bufferWriter.write("Invalid Destination: ");
			return false;
		}
			
		return true;	// if everything before is not true, it will return true
	}
	
	/**
	 * This function checks if the syntax is exactly like [MOVEI, v, d]
	 * @param arrayListOfALine
	 * @param bufferWriter 
	 * @return a boolean value
	 */
	private boolean checkMOVE(ArrayList<String> arrayListOfALine, BufferedWriter bufferWriter) throws IOException {
		// checking if the instruction has more or less than three
		if (arrayListOfALine.size() != 3)
		{
			showError("Instruction error");
			bufferWriter.write("Instruction error: ");
			return false;
		}
		// checking if the source location ends with a comma
		else if (!checkForComma(arrayListOfALine.get(1))){
			showError("The source must ends with a comma.");
			bufferWriter.write("The source must ends with a comma.: ");
			return false;
		}
		// checking if the source location has integers ending with a comma
		if (checkForComma(arrayListOfALine.get(1))){
			String wordWithoutComma = arrayListOfALine.get(1).substring(0, arrayListOfALine.get(1).length()-1);
			// should match R0-R7 or integers or source name
			if (!wordWithoutComma.matches("[0-7]+") && !checkForRegister(wordWithoutComma) 
					&& !checkForSource(wordWithoutComma)){
				showError("Invalid source in Move.");
				bufferWriter.write("Invalid source in Move.: ");
				return false;
			}
		}
		// checking for destination location R0-R7
		if (!checkForRegister(arrayListOfALine.get(2)) && !checkForDestination(arrayListOfALine.get(2), bufferWriter)){
			showError("Invalid Destination");
			bufferWriter.write("Invalid Destination: ");
			return false;
		}
			
		return true;	// if everything before is not true, it will return true
	}

	/**
	 * This function will print the error message
	 * @param errorMessage
	 */
	private void showError(String errorMessage) {
		System.out.print(errorMessage + ": ");
	}
	
	/**
	 * A register must have two characters. The first character must be
	 * R and the second character must be from 0 to 7
	 * @param register
	 * @return a boolean value
	 */
	private boolean checkForRegister(String register) {
		if (register.length() == 2){
			if (register.charAt(0) != 'R'){
				//showError("Register name should start with R.");
				return false;
			}
				
			else {
				String number = Character.toString(register.charAt(1));
				if (!number.matches("[0-7]")) {
					return false;
				}
			}
		}
		else	// if register is not of two characters
			return false;

		return true;
	}
	
	/**
	 * This function checks if a word contains a comma
	 * @param register
	 * @return
	 */
	private boolean checkForComma (String wordWithComma) {
		if (wordWithComma.contains(","))
			return true;
		return false;
	}
	
	/**
	 * checkForSource will check for letters, size, and a comma
	 * @param source
	 * @return
	 */
	private boolean checkForSource(String source){
		// checking if the instruction has more than 5 characters
		if (source.length() > 5)
			return false;
		else if (!source.matches("[a-zA-z]+"))
			return false;
		return true;
	}
	
	/**
	 * checkForDestination will check for letters, size, and a comma
	 * @param source
	 * @return
	 */
	private boolean checkForDestination(String destination, BufferedWriter bufferWriter) throws IOException {
		// checking if the instruction has more than 5 characters
		if (destination.length() > 5){
			return false;
		}
		// checking if the source location ends with a comma
		if (destination.contains(",")){
			showError("Comma error");
			bufferWriter.write("Comma error: ");
			return false;
		}
		if (!destination.matches("[a-zA-z]+"))
			return false; 
		return true;
	}
}