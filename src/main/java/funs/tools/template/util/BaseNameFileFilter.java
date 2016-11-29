package funs.tools.template.util;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.AbstractFileFilter;

public class BaseNameFileFilter extends AbstractFileFilter {

	/** The filename prefixes to search for */
	private final String[] baseNames;

	/** Whether the comparison is case sensitive. */
	private final IOCase caseSensitivity;

	/**
	 * Constructs a new Prefix file filter for a single prefix.
	 * 
	 * @param prefix
	 *            the prefix to allow, must not be null
	 * @throws IllegalArgumentException
	 *             if the prefix is null
	 */
	public BaseNameFileFilter(String baseName) {
		this(baseName, IOCase.SENSITIVE);
	}

	/**
	 * Constructs a new Prefix file filter for a single prefix specifying
	 * case-sensitivity.
	 * 
	 * @param prefix
	 *            the prefix to allow, must not be null
	 * @param caseSensitivity
	 *            how to handle case sensitivity, null means case-sensitive
	 * @throws IllegalArgumentException
	 *             if the prefix is null
	 * @since Commons IO 1.4
	 */
	public BaseNameFileFilter(String baseName, IOCase caseSensitivity) {
		if (baseName == null) {
			throw new IllegalArgumentException("The baseName must not be null");
		}
		this.baseNames = new String[] { baseName };
		this.caseSensitivity = (caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity);
	}

	/**
	 * Checks to see if the filename starts with the prefix.
	 * 
	 * @param file
	 *            the File to check
	 * @return true if the filename starts with one of our prefixes
	 */
	@Override
	public boolean accept(File file) {
		String baseName = FilenameUtils.getBaseName(file.getName());
		for (String baseName2 : this.baseNames) {
			if (caseSensitivity.checkEquals(baseName, baseName2)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks to see if the filename starts with the prefix.
	 * 
	 * @param file
	 *            the File directory
	 * @param name
	 *            the filename
	 * @return true if the filename starts with one of our prefixes
	 */
	@Override
	public boolean accept(File file, String name) {
		String baseName = FilenameUtils.getBaseName(file.getName());
		for (String baseName2 : baseNames) {
			if (caseSensitivity.checkStartsWith(baseName, baseName2)) {
				return true;
			}
		}
		return false;
	}

}
