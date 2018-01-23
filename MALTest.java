import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MALTest {

	public static void main(String[] args) {
		// The name of the file to open.
		String fileName = "MALCode.txt";
		int lineNumber = 0;
		// To read a line from the file
		String line = null;
		ArrayList<String> wordsInLine = new ArrayList<String>();
		boolean commaAtEnd = false;

		try {
			// FileReader reads characters from the file
			FileReader fileReader = new FileReader(fileName);

			// BufferedReader reads text from character-input stream
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			// this while loop will continue until the end of the file
			while ((line = bufferedReader.readLine()) != null) {
				lineNumber++;
				
				if (!line.isEmpty()) { // this will ignore an empty line
					System.out.println(line);
					
					// ignores if the first character is ; (rest of the code will not run if true)
					if (line.charAt(0) == ';')
						continue;
					
					if (line.contains(";")){
						String [] splitSemiColon = line.split("\\;");
						line = splitSemiColon[0];
					}
					
					// TODO check if the line starts with a space or if the line looks empty but has spacebars 
					
					
					
						
					
					if (line.charAt(line.length()-1) == ',')
							commaAtEnd = true;
						
					// String tokenization to split one or more spaces, but not tab
					String[] words = line.split("[ ]+"); 
					
					// wordsArrayList is a list of words without spaces
					ArrayList<String> wordsArrayList = new ArrayList<String>(Arrays.asList(words));

					// separating words (including a comma) if they contain comma
					for (int i = 0; i < wordsArrayList.size(); i++) { 
						// checking if there are words with comma(s) in the middle
						if (wordsArrayList.get(i).contains(",") && 
								wordsArrayList.get(i).indexOf(",") != wordsArrayList.get(i).length()-1 &&
								(wordsArrayList.get(i).indexOf(",") != wordsArrayList.get(i).length()-1 &&
								wordsArrayList.get(i).indexOf(",") != wordsArrayList.get(i).length()-2)) {
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
						
						else
							wordsInLine.add(wordsArrayList.get(i));
					}
				}
				commaAtEnd = false;
			}
			for (String s : wordsInLine) {
				System.out.println(s);
			}
			
			System.out.println("Total lines in the file: " + lineNumber);

			bufferedReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error with opening file '" + fileName + "'");
		} catch (IOException e) {
			System.out.println("Error reading file '" + fileName + "'");
		}
	}

	private static void checkInput(String line) {
		// TODO Auto-generated method stub

	}

}
