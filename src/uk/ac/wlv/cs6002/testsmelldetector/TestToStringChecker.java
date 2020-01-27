package uk.ac.wlv.cs6002.testsmelldetector;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import uk.ac.wlv.cs6002.smelldetector.CodeSmell;
import uk.ac.wlv.cs6002.smelldetector.CodeSmellDetector;
import uk.ac.wlv.cs6002.smelldetector.FileAnalyser;
import uk.ac.wlv.cs6002.smelldetector.ToStringChecker;

/** Unit tests for the toString() checker.
 * 
 * @author snim2
 */
public class TestToStringChecker {

	private final CodeSmellDetector analyser = new ToStringChecker();

	// Filename for our fake Java file.
	private final String filename = "TestMe.java";
	private final String path = "/" + filename;
	
	private FakeJavaFileObject createFile(String program) {
		return new FakeJavaFileObject(this.filename, program);
	}

	@Test
	public void testToStringPresent() throws IOException {
		// This program does NOT contain the code smell.
		String program = "public class TestMe {\n" // Line 1.
				+ "    public String toString() { return \"TestMe\"; }\n"
				+ "}"; // Line 3.

		// Apply the static analyser to the fake test file. 
		FileAnalyser.analyseFile(analyser, createFile(program));
		List<CodeSmell> smells = analyser.getSmells();
		
		// Analyser should find zero code smells.
		Assert.assertEquals(0, smells.size());
	}

	@Test
	public void testToStringNotPresent() throws IOException {
		// This program DOES contain the code smell.
		String program = "public class TestMe {\n" // Line 1.
				+ "    public String flibble() { return \"TestMe\"; }\n"
				+ "}"; // Line 3.

		// Apply the static analyser to the fake test file. 
		FileAnalyser.analyseFile(analyser, createFile(program));
		List<CodeSmell> smells = analyser.getSmells();
		
		// Analyser should find one code smell.
		Assert.assertEquals(1, smells.size());
		
		// First smell should be on line 1.
		CodeSmell expected = new CodeSmell(path, 1, analyser.getSmellDescription());
		Assert.assertEquals(expected, smells.get(0));
	}

}
