package uk.ac.wlv.cs6002.testsmelldetector;

import junit.framework.Assert;
import org.junit.Test;
import uk.ac.wlv.cs6002.smelldetector.CodeSmell;
import uk.ac.wlv.cs6002.smelldetector.CodeSmellDetector;
import uk.ac.wlv.cs6002.smelldetector.EqualityComparisonChecker;
import uk.ac.wlv.cs6002.smelldetector.FileAnalyser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Unit tests for the equality comparison checker.
 * 
 * @author snim2
 */
public class TestEqualityComparisonChecker {

	private final CodeSmellDetector analyser = new EqualityComparisonChecker();

	// Filename for our fake Java file.
	private final String filename = "TestMe.java";
	private final String path = "/" + filename;
	
	private FakeJavaFileObject createFile(String program) {
		return new FakeJavaFileObject(this.filename, program);
	}

	@Test
	public void testVariableOnRHS() throws IOException {
		// This program does NOT contain the code smell.
		String program = "public class TestMe {\n" // Line 1.
				+ "    public static void main(String[] args) {\n" // Line 2.
				+ "        int a = 0;\n" // Line 3.
				+ "        int b = 5;\n" // Line 4.
				+ "        while (0 == a && b > 0) {\n" // Line 5.
				+ "            System.out.println(b);\n" // Line 6.
				+ "            b -= 1;\n" // Line 7.
				+ "        }\n" // Line 8.
				+ "    }\n" // Line 9.
				+ "}"; // Line 10.

		// Apply the static analyser to the fake test file. 
		FileAnalyser.analyseFile(analyser, createFile(program));
		List<CodeSmell> smells = analyser.getSmells();


		// Analyser should find no code smells.
		Assert.assertEquals(0, smells.size());
	}

	@Test
	public void testVariableOnLHS() throws IOException {
		// This program DOES contain the code smell.
		String program = "public class TestMe {\n" // Line 1.
				+ "    public static void main(String[] args) {\n" // Line 2.
				+ "        int a = 0;\n" // Line 3.
				+ "        int b = 5;\n" // Line 4.
				+ "        while (a == 0 && b > 0) {\n" // Line 5.
				+ "            System.out.println(b);\n" // Line 6.
				+ "            b -= 1;\n" // Line 7.
				+ "        }\n" // Line 8.
				+ "    }\n" // Line 9.
				+ "}"; // Line 10.

		// Apply the static analyser to the fake test file. 
		FileAnalyser.analyseFile(analyser, createFile(program));
		
		// Get the smells the analyser has found.
		List<CodeSmell> smells = analyser.getSmells();
		
		// Analyser should find one code smell.
		Assert.assertEquals(1, smells.size());
		
		// First (and only) smell should be on line 5.
		CodeSmell expected = new CodeSmell(path, 5, analyser.getSmellDescription());
		Assert.assertEquals(expected, smells.get(0));
	}
	
	@Test
	public void testSeveralVariablesOnLHS() throws IOException {
		// This program DOES contain the code smell (twice).
		String program = "public class TestMe {\n" // Line 1.
				+ "    public static void main(String[] args) {\n" // Line 2.
				+ "        int a = 0;\n" // Line 3.
				+ "        int b = 5;\n" // Line 4.
				+ "        while (a == 0 && b > 0) {\n" // Line 5.
				+ "            System.out.println(a);\n" // Line 6.
				+ "            b -= 1;\n" // Line 7.
				+ "        }\n" // Line 8.
				+ "if (b == 4) System.out.println(b);\n" // Line 9.
				+ "    }\n" // Line 10.
				+ "}"; // Line 11.

		// Apply the static analyser to the fake test file. 
		FileAnalyser.analyseFile(analyser, createFile(program));
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


		//System.out.println(smells.get(0).getLineno());
		// Analyser should find two code smells.
		Assert.assertEquals(2, smells.size());
		
		// First smell should be on line 5.
		CodeSmell expected0 = new CodeSmell(path, 5, analyser.getSmellDescription());
		Assert.assertEquals(expected0, smells.get(0));
		
		// Second smell should be on line 9.
		CodeSmell expected1 = new CodeSmell(path, 9, analyser.getSmellDescription());
		Assert.assertEquals(expected1, smells.get(1));
	}

}
