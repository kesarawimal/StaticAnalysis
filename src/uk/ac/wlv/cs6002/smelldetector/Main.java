package uk.ac.wlv.cs6002.smelldetector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import uk.ac.wlv.cs6002.testsmelldetector.FakeJavaFileObject;

/** This simple example shows you how to apply a static analyser to Java code.
 * 
 * You should test your static analysers using JUnit4. See the package
 * {@link uk.ac.wlv.cs6002.testsmelldetector} for some examples.
 * 
 * @author snim2
 * @see FileAnalyser
 * @see FakeJavaFileObject
 * @see CodeSmell
 * @see CodeSmellDetector
 */
public class Main {

		
	public static void main(String[] args) throws IOException {
		
		// A static analyser that we want to try out.
		CodeSmellDetector analyser = new EqualityComparisonChecker();
		
		// Filename for our fake Java file.
		String filename = "TestMe.java";

		// Program to go in the fake file. 
		// This must be valid Java code and NOT have any compiler errors!
		String program = "import javax.swing.*;\nimport java.io.*;"
				+ "public class TestMe {\n"
				+ "    public static void main(String[] args) {\n"
				+ "        int a = 5;\n"
				+ "        if (a == 0) System.out.println(\"Zero!\");\n"
				+ "    }\n"
				+ "}";


		// Create a fake Java file.
		FakeJavaFileObject testFile = new FakeJavaFileObject(filename, program);

		// Apply the static analyser to the fake test file. 
		FileAnalyser.analyseFile(analyser, testFile);


		List<CodeSmell> smells = analyser.getSmells();
		final String RED_BACKGROUND = "\033[41m";
		final String RESET_COLOR = "\033[0m";
		final String BLACK_BOLD = "\033[1;30m";

		String[] lines = program.split("\n");

		int[] lineNums = new int[smells.size()+1];
		int lineCnt = 0;
		for (CodeSmell s : smells){
			lineNums[lineCnt] = Math.toIntExact(s.getLineno());
			lineCnt++;
		}

		for (int j = 0; j < lines.length; j++){
			int finalJ = j;
			if (Arrays.stream(lineNums).anyMatch(i -> i == finalJ +1)){
				System.out.print(RED_BACKGROUND);
				System.out.print(BLACK_BOLD);
			}else {
				System.out.print(RESET_COLOR);
			}
			System.out.println(lines[j]);
		}

		// Print out the results of the analyser.
		System.out.println("The following imports were found in the test file:");
		System.out.println(analyser.toString());
	}
}
