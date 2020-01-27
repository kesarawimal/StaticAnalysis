package uk.ac.wlv.cs6002.smelldetector;

import java.io.IOException;

import javax.tools.JavaCompiler;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;


import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Trees;
import com.sun.tools.javac.util.List;

public class FileAnalyser {

	/** Apply a given code smell detector to a given Java file. 
	 *
	 * @param analyser static analyser to apply to the given file
	 * @param file Java file to be analysed
	 * @throws IOException if the source code file cannot be read
	 */
	public static void analyseFile(CodeSmellDetector analyser, SimpleJavaFileObject file) throws IOException {
		// Get the standard system compiler for this platform.
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		// Create a new, non-standard, compiler task.
		JavacTask task = (JavacTask)compiler.getTask(null, null, null, null, 
				null, List.of(file));
		// Parse the file into an AST.
		Iterable<? extends CompilationUnitTree> asts = task.parse();
		// Set the compilation task to be an analyser.
		task.analyze();
		Trees trees = Trees.instance(task);
		// Run the smell detector over the current AST.

		// Ensure the AST visitor knows the line number of each node. 
		analyser.setSourcePositions(trees.getSourcePositions());
		for (CompilationUnitTree ast : asts) {
			// Give the visitor the current AST.
			analyser.setCompilationUnit(ast);
			// Run the AST visitor over the AST. 
			analyser.scan(ast, null);
		}
		return;
	}

}
