package uk.ac.wlv.cs6002.smelldetector;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;

public class ToStringChecker extends CodeSmellDetector {

	/** The class that is currently being analysed in the AST. */ 
	private ClassTree classNode = null;
	
	public ToStringChecker() {
		super("toString() method not overridden from java.lang.Object");
		return;
	}

	@Override
	public Object visitClass(ClassTree node, Void p) {
		// When we visit a class we assume it doesn't have a toString() method
		// and add a suitable object to the code smells list.
		this.classNode = node;
		this.addSmell(node);
		// ALWAYS call the visit method in the superclass.
		return super.visitClass(node, p);
	}
	
	@Override
	public Object visitMethod(MethodTree node, Void p) {
		// If we find a method called toString, we remove the code smell
		// we added to the list when we visited this class.
		if (node.getName().toString().equals("toString")) {
			this.removeSmell(this.classNode);
		}
		// ALWAYS call the visit method in the superclass.
		return super.visitMethod(node, p);
	}

}
