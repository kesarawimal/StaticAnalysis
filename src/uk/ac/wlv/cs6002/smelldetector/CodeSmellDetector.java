package uk.ac.wlv.cs6002.smelldetector;
import java.util.LinkedList;
import java.util.List;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePathScanner;

/** Detect a possible code smell in a Java program.
 * 
 * This class should be extended and the subclass should override
 * the <code>visit</code> methods of the {@link TreePathScanner} class.
 * 
 * @author snim2
 */
public class CodeSmellDetector extends TreePathScanner<Object, Void> {

	/** A description of the code smell. */
	private final String smell;
	
	/** All code smells detected by this object. */
	protected final List<CodeSmell> smells = new LinkedList<CodeSmell>();
	
	/** Abstract syntax tree provided by the standard Java compiler. */
	protected CompilationUnitTree compilationUnit;
	
	/** Object mapping AST nodes to line numbers in the original files. */
	private SourcePositions sourcePositions;

	public CodeSmellDetector(String smell) {
		super();
		this.smell = smell;
		return;
	}

	private CodeSmell createCodeSmell(Tree tree) {
		return createCodeSmell(tree, this.smell);
	}

	private CodeSmell createCodeSmell(Tree tree, String smell) {
		// Name of the original file the AST was parsed from.
		String filename = this.compilationUnit.getSourceFile().getName();
		// Where the code smell occurs in the original file.
		long startPosition = this.sourcePositions.getStartPosition(
				this.compilationUnit, tree);
		// Line number in the original file.
		long lineno = compilationUnit.getLineMap().getLineNumber(startPosition);
		return new CodeSmell(filename, lineno, smell);
	}
	
	/** Add a new code smell to the list of detected code smells.
	 * 
	 * @param tree AST node where the code smell was detected.
	 */
	protected void addSmell(Tree tree) {
		// Add new smell to the list of detected smells.
		this.smells.add(this.createCodeSmell(tree));
		return;
	}

	/** Return list of smells found by this smell detector.
	 * 
	 * @return the smells
	 */
	public List<CodeSmell> getSmells() {
		return smells;
	}

	/** Add a new code smell to the list of detected code smells.
	 * 
	 * @param tree AST node where the code smell was detected.
	 * @param smell a description of the code smell.
	 */
	protected void addSmell(Tree tree, String smell) {
		// Add new smell to the list of detected smells.
		this.smells.add(this.createCodeSmell(tree, smell));
		return;
	}
	
	/** Remove a code smell from the list of detected code smells.
	 * 
	 * @param tree AST node where the code smell was "detected".
	 */
	protected void removeSmell(Tree tree) {
		this.smells.remove(this.createCodeSmell(tree));
		return;
	}

	/** Set the AST which the visitor should walk over.
	 * 
	 * @param file AST generated by the standard system compiler.
	 */
	public void setCompilationUnit(CompilationUnitTree file) {
		this.compilationUnit = file;
		return;
	}

	/** Set the source position object which maps AST nodes to line numbers.
	 * 
	 * @param sourcePositions
	 */
	public void setSourcePositions(SourcePositions sourcePositions) {
		this.sourcePositions = sourcePositions;
		return;
	}
	
	/** We need this because Java does not have a sensible join method.
     * 
     * @param strings list of strings to be joined together 
     * @param sep a string separator such as ","
     * @return single string joined with sep
     */
    private String join(List<String> strings, String sep) {
        String joined = "";
        int numStrings = 0;
        for (String item : strings) {
            joined += item;
            if (++numStrings < strings.size())
                joined += sep;
        }
        return joined;
    }
	
	@Override
	public String toString() {
		List <String> smell_s = new LinkedList<String>();
		for (CodeSmell smell : smells) {
			smell_s.add(smell.toString());
		}
		return this.join(smell_s, "\n");
	}

	/** Return a description of the smell this detector looks for.
	 * @return the smell
	 */
	public String getSmellDescription() {
		return smell;
	}
}