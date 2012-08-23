package be.gervaisb.ogam.tools.logviewer.impls.gui.presenters.text;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import be.gervaisb.ogam.tools.logviewer.core.LogEntry;

class LogsTextViewModel extends DefaultStyledDocument {
	private static final long serialVersionUID = 364725658308354695L;

	private final DateFormat format = new SimpleDateFormat("hh:mm:ss''SSS dd-MM-yyyy");
	
	private final Style severity;
	private final Style date;
	private final Style text;
	
	private int lines = 0;
	private int offset = 0;	
	
	public LogsTextViewModel() {
		text = getStyle("default");
		StyleConstants.setFontFamily(text, "Serif");
		
		date = addStyle("date", text);
		StyleConstants.setForeground(date, Color.GRAY);
		
		severity = addStyle("severity", text);
		StyleConstants.setBold(severity, true);
	}
	
	int getLines() {
		return lines;
	}
	
	public void clear() {
		try {
			remove(0, getLength());
		} catch (BadLocationException e) {/**/}
	}
	
	public void add(LogEntry entry) {
		try {			
			String value = format.format(entry.getDate());
			insertString(offset, value, date);
			offset += value.length();
			
			value = " ["+entry.getSeverity()+"]";
			insertString(offset, value, severity);
			offset += value.length();
			
			value = entry.getText()+"\n";
			insertString(offset, value, text);
			offset += value.length();
			
			for (int i=0; entry.hasStackTrace() && i<entry.getStacktrace().length; i++) {
				value = "     "+entry.getStacktrace()[i]+"\n";
				insertString(offset, value, text);
				offset += value.length();
			}
		} catch (BadLocationException e) {/**/}
	}
	
	@Override
	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {		
		for (int index = 0; (index = str.indexOf("\n", index+1))!=-1; lines++) {
			// Update the "lines" counter inside the for loop itself
		}
		super.insertString(offs, str, a);
	}
}
