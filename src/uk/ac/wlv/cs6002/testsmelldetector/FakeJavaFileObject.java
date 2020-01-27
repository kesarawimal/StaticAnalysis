package uk.ac.wlv.cs6002.testsmelldetector;

import java.net.URI;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

/**
 * Fake file objects. Java compilers expect to be given a file object,
 * rather than a String. Our test data is stored as String objects, so this
 * class is used to fool the compiler into thinking it is dealing with a
 * file rather than a String.
 * 
 * @author snim2
 */
public class FakeJavaFileObject extends SimpleJavaFileObject {

	/** Content of the fake file. */
	private final String content;

	/**
	 * Create a new fake file.
	 * 
	 * The file contents must be valid Java. The filename must be the name
	 * of the Java class in {@link content}, plus the <code>.java</code>
	 * extension.
	 * 
	 * @param filename
	 *            name of the file, must end in .java
	 * @param content
	 *            content of the file, must be valid Java code
	 */
	public FakeJavaFileObject(String filename, String content) {
		super(URI.create("myfo:/" + filename), JavaFileObject.Kind.SOURCE);
		this.content = content;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return this.content;
	}
}
