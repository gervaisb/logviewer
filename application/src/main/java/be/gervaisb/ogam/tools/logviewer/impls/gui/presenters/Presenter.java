package be.gervaisb.ogam.tools.logviewer.impls.gui.presenters;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import be.gervaisb.ogam.tools.logviewer.core.LogEntry;

/**
 * A {@link Presenter} is used to display logs to the user. The user may have 
 * the possibility to choose between many different presenters.
 */
public abstract class Presenter extends JPanel {
	private static final long serialVersionUID = -615127591308998954L;

	public Presenter() {
		super(new BorderLayout(0, 0));
	}
	
	public abstract void clear();
	
	public abstract void append(LogEntry entry);
	
}
