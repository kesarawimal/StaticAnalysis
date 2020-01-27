package uk.ac.wlv.cs6002.smelldetector;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.Tree;

/** Static analyser to find variable names on the left hand side of a ==.
 * 
 * @author snim2
 */
public class EqualityComparisonChecker extends CodeSmellDetector {

	public EqualityComparisonChecker() {
		super("Variables should be on the right hand side of an == expression");
		return;
	}

	@Override
	public Object visitBinary(BinaryTree node, Void p) {
		// Visit a "binary" expression -- one with two operands and a
		// binary operator of some kind.
		if (node.getKind() == Tree.Kind.EQUAL_TO) { // == operator
			if (node.getLeftOperand() instanceof IdentifierTree) {
				this.addSmell(node);
			}
		}
		// ALWAYS call the visit method in the superclass.
		return super.visitBinary(node, p);
	}

}
