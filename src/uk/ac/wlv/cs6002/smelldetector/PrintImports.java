package uk.ac.wlv.cs6002.smelldetector;

import com.sun.source.tree.ImportTree;

public class PrintImports extends CodeSmellDetector {

	public PrintImports() {
		super("");
	}
	
	@Override
    public Object visitImport(ImportTree node, Void p) {
            this.addSmell(node, node.toString());
            return super.visitImport(node, p);
    }
	
}
