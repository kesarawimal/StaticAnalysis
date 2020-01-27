package uk.ac.wlv.cs6002.smelldetector;
/** A simple class representing a code smell in a particular location.
 * 
 * @author snim2
 *
 */
public class CodeSmell {

	/** File that the violation occurred in. */
	private final String filename;

	/** Line number that the smell occurs on. */
	private final long lineno;

	/** Description of the code smell. */
	private final String smell;

	public CodeSmell(String filename, long lineno, String violation) {
		super();
		this.filename = filename;
		this.lineno = lineno;
		this.smell = violation;
		return;
	}
	
	private String getFilename() {
		return this.filename;
	}

	public long getLineno() {
		return this.lineno;
	}

	private String getSmell() {
		return this.smell;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof CodeSmell)) {
			return false;
		}
		CodeSmell otherSmell = (CodeSmell) other;
		if (this.lineno == otherSmell.getLineno()
				&& this.filename.equals(otherSmell.getFilename())
				&& this.smell.equals(otherSmell.getSmell())) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return this.filename + ":" + this.lineno + ":" + this.smell;
	}

}