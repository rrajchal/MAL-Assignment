import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * ArrayListOfLines class takes a directory of the file as a string and evaluate 
 * each line in the file. Each line will be split into words separated by a comma
 * or spaces. These words will be collected as elements of a arrayList. Each line
 * will be converted into arrayList of words and the arrayList will be added to 
 * another arrayList. If a line starts ';' it will be added to the arrayList, 
 * but if the line contains ';' in the middle, the later part will not be added. 
 * Comments begin with a semi-colon and end with EOL. However, if EOL is missing 
 * at the end of the line, it will pop up a warning message. Missing EOL will not
 * count towards error. The warning will also be written in the output file. 
 * So, if the lines in the file is: 
 *    ; Some codes
 *    MOVEI s, R0    ; first summation number (temporary index) 
 *    MOVEI 10, R1   ; last summation number, R1 = 10
 * The arrayList will be: [[; Some codes], [MOVEI, s,, R0], [MOVEI, 10,, R1]]
 * @author Rajesh
 */
public class ArrayListOfLines {
	/**
	 * This function takes a file name as a string and converts the lines of 
	 * texts into an arrayList of the arrayList of strings
	 * @param file
	 * @return arrayList of arrayList
	 */
	public ArrayList<ArrayList <String>> getLines(String file, BufferedWriter bufferWriter) throws IOException{
		// The name of the file to open.
		String fileName = file;
		// To read a line from the file
		String line = null;
		
		// Making an arrayList of the ArrayList of lines
		ArrayList<ArrayList <String>> listOfLines = new ArrayList<ArrayList <String>>();
		boolean commaAtEnd = false;

		try {
			// FileReader reads characters from the file
			FileReader fileReader = new FileReader(fileName);
			// BufferedReader reads text from character-input stream
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			// this while loop will continue until the end of the file
			while ((line = bufferedReader.readLine()) != null) {
				if (line.isEmpty()){
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(line);
					listOfLines.add(temp);
					continue;
				}
				if (!line.isEmpty()) { // this will ignore an empty line						
					// ignores if the first character is ; (rest of the code will not run if true)
					if (line.charAt(0) == ';'){
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(line);
						listOfLines.add(temp);
						if (!line.endsWith("EOL")){
							System.out.print(line);
							bufferWriter.write(line);
							System.out.println("---Warning: Missed EOL after comments");
							bufferWriter.write("---Warning: Missed EOL after comments");
							bufferWriter.newLine();
						}	
						continue;
					}
						
					// split the line at ; and consider only the first statement
					if (line.contains(";")){
						if (!line.endsWith("EOL")){
							System.out.print(line);
							bufferWriter.write(line);
							System.out.println("---Warning: Missed EOL after comments");
							bufferWriter.write("---Warning: Missed EOL after comments");
							bufferWriter.newLine();
						}
						String [] splitSemiColon = line.split("\\;");
						line = splitSemiColon[0];
					}
					// to see if the line ends with ,					
					if (line.charAt(line.length()-1) == ',')
							commaAtEnd = true;
						
					// String tokenization to split one or more spaces, but not tab
					String[] wordsInALine = line.split("[ ]+"); 				
				
					// wordsArrayList is a list of words without spaces, words may contain comma
					ArrayList<String> wordsArrayList = new ArrayList<String>(Arrays.asList(wordsInALine));
					// If comma is present in the middle of the word, it will count as different words
					ArrayList<String> wordsInLine = new ArrayList<String>();

					// separating words (including a comma) if they contain comma
					for (int i = 0; i < wordsArrayList.size(); i++) {
						// if the word is just a comma, it will be appended to the previous word
						if (i < wordsArrayList.size()-1){
							if (wordsArrayList.get(i+1).equals(","))
							{
								String newWord = wordsArrayList.get(i) + ",";
								wordsArrayList.set(i+1, "");	// set the next string to an empty string
								wordsArrayList.set(i, newWord);
							}
						}
						// checking if there are words with comma(s) in the middle of a word like R1,R2
						if (!wordsArrayList.get(i).equals(",") && wordsArrayList.get(i).contains(",") && 
								(wordsArrayList.get(i).indexOf(",")) != wordsArrayList.get(i).length()-1 &&  // checking the comma at the end of the line
								(wordsArrayList.get(i).indexOf(",") != wordsArrayList.get(i).length()-1 &&	// checking the comma at the end of the line and before that
								(wordsArrayList.get(i).indexOf(",")) != wordsArrayList.get(i).length()-2)) {
							// split the words at comma	
							String subWords[] = wordsArrayList.get(i).split("\\,");
							// put the comma back to the separated word except for the 
							for (int j = 0; j < subWords.length; j++) {
								String subword = subWords[j];
								if (j != subWords.length - 1 || commaAtEnd) { 
									subword = subword + ",";
									wordsInLine.add(subword);
								} 
								else
									wordsInLine.add(subword);
							}
						}
						else{
							if (!wordsArrayList.get(i).isEmpty())
								wordsInLine.add(wordsArrayList.get(i));
						}	
					}
					listOfLines.add(wordsInLine);
				}
				commaAtEnd = false;
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error with opening file '" + fileName + "'");
		} catch (IOException e) {
			System.out.println("Error reading file '" + fileName + "'");
		}
		return listOfLines;
	}
}