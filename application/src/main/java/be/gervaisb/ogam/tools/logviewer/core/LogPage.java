package be.gervaisb.ogam.tools.logviewer.core;


/**
 * Contract for any object who represent a log file. It has only on method who 
 * return the next entry from the file.
 * 
 * @author bgervais
 *
 */
public interface LogPage {
	
	/**
	 * Return the next entry from the page. Usually, this is a blocking method 
	 * who wait until a new entry appear.
	 */
	public LogEntry next();

	public void close();
	
	public String getName();
	
}
