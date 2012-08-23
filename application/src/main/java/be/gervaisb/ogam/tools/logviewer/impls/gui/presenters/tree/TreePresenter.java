package be.gervaisb.ogam.tools.logviewer.impls.gui.presenters.tree;

import javax.swing.JScrollPane;

import be.gervaisb.ogam.tools.logviewer.core.LogEntry;
import be.gervaisb.ogam.tools.logviewer.impls.gui.presenters.Presenter;

/**
 * Present the logs on a two level tree where the first level is the log entry 
 * with his date, severity and message and the second level is the stack trace 
 * when any.
 * @see LogsTreeView#InternalTreeCellRenderer for rendering details
 */
public class TreePresenter extends Presenter {
	private static final long serialVersionUID = -2257645481143389050L;
	
	private final LogsTreeViewModel model;
	
	public TreePresenter() {
		add(new JScrollPane(new LogsTreeView(model = new LogsTreeViewModel())));
	}
	
	@Override
	public void append(LogEntry entry) {
		model.add(entry);
	}
	
	@Override
	public void clear() {
		model.clear();
	}
	
}
