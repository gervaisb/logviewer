package be.gervaisb.ogam.tools.logviewer.impls.gui.presenters.table;

import be.gervaisb.ogam.tools.logviewer.core.LogEntry;
import be.gervaisb.ogam.tools.logviewer.impls.gui.presenters.Presenter;

/**
 * {@link Presenter} who display log entries on structured lines.
 * When a log entry contains a stack trace, the stack trace is displayed within 
 * a popup area who pops automatically when the cursor stay 500 millis on the 
 * line or from a contextual menu.
 */
public class TablePresenter extends Presenter {

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void append(LogEntry entry) {
		// TODO Auto-generated method stub
		
	}

}
