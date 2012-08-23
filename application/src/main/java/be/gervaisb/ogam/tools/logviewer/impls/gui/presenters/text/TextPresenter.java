package be.gervaisb.ogam.tools.logviewer.impls.gui.presenters.text;

import javax.swing.JScrollPane;

import be.gervaisb.ogam.tools.logviewer.core.LogEntry;
import be.gervaisb.ogam.tools.logviewer.impls.gui.presenters.Presenter;

/**
 * Display the logs in a simple text, with a little styling fro the date and 
 * log level.
 * @see LogsTextViewModel for style attributes.
 */
public class TextPresenter extends Presenter {
	private static final long serialVersionUID = -6568513025963807596L;
	
	private final LogsTextViewModel model;
	
	public TextPresenter() {
		add(new JScrollPane(new LogsTextView(model = new LogsTextViewModel())));
	}
	
	@Override
	public void clear() {
		model.clear();
	}

	@Override
	public void append(LogEntry entry) {
		model.add(entry);
	}

}
