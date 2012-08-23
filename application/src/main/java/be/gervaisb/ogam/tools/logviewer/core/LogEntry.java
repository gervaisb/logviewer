package be.gervaisb.ogam.tools.logviewer.core;

import java.util.Date;

/**
 * Describe an entry from a {@link LogPage}.
 * @author bgervais
 *
 */
public class LogEntry implements Comparable<LogEntry> {

	public enum Severity { Trace, Debug, Info, Warning, Error }
	
	private final StackTraceElement[] stacktrace;
	private final Severity severity;	
	private final String text;
	private final Date date;
	
	public LogEntry(final Date date, final Severity severity, final String text) {
		this(date, severity, text, null);
	}
	
	public LogEntry(final Date date, final Severity severity, final String text, final StackTraceElement[] stacktrace) {
		this.stacktrace = stacktrace;
		this.severity = severity;
		this.date = date;
		this.text = text;
	}
	
	public Date getDate() {
		return date;
	}
	
	public Severity getSeverity() {
		return severity;
	}
	
	public String getText() {
		return text;
	}
	
	public boolean hasStackTrace() {
		return stacktrace!=null;
	}
	
	public StackTraceElement[] getStacktrace() {
		return stacktrace;
	}
	
	@Override
	public int compareTo(LogEntry o) {
		int comparison = date.compareTo(o.date);
		if ( comparison==0 )
			comparison = o.severity.ordinal()-severity.ordinal();
		return comparison;
	}
	
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder()
			.append(date).append(" [").append(severity).append("] ")
			.append(text);
		if ( hasStackTrace() ) {
			for (StackTraceElement element : stacktrace) {
				string.append("\n\tat ").append(element.getMethodName())
					.append('(').append(element.getClassName());
				if ( element.getLineNumber()>0 )
					string.append(':').append(element.getLineNumber());
				string.append(')');					
			}
		}
		return string.toString();
	}
}
