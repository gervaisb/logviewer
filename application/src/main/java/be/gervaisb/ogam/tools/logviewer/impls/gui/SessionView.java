package be.gervaisb.ogam.tools.logviewer.impls.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import be.gervaisb.ogam.tools.logviewer.core.LogEntry;
import be.gervaisb.ogam.tools.logviewer.core.LogEntry.Severity;
import be.gervaisb.ogam.tools.logviewer.core.LogPage;
import be.gervaisb.ogam.tools.logviewer.impls.gui.presenters.Presenter;
import be.gervaisb.ogam.tools.logviewer.impls.gui.presenters.table.TablePresenter;
import be.gervaisb.ogam.tools.logviewer.impls.gui.presenters.text.TextPresenter;
import be.gervaisb.ogam.tools.logviewer.impls.gui.presenters.tree.TreePresenter;

/**
 * A session view is a view who display logs for a session. A session view 
 * contains all availables {@link Presenter}s and let the user switch between
 * them.
 *
 */
public class SessionView extends JPanel {
	
	private static final long serialVersionUID = 5670110532641866495L;
	
	private final boolean[] severities = new boolean[Severity.values().length];
	private final List<Presenter> presenters = new ArrayList<Presenter>();
	private final Reader reader;
	
	public SessionView(final String title, final LogPage page) {
		super(new BorderLayout());
		for (final Severity severity : Severity.values()) {
			enable(severity);
		}		
		
		final CleanTabsPanel presentations = new CleanTabsPanel();
				
		Presenter treePresenter = createTreePresenter();
		Presenter textPresenter = createTextPresenter();
		Presenter tablePresenter = createTablePresenter();
		presentations.add("Tree", treePresenter);
		presentations.add("Plain", textPresenter);
		presentations.add("Table", tablePresenter);
		
		presenters.add(treePresenter);
		presenters.add(textPresenter);
		presenters.add(tablePresenter);
		
		add(presentations);
		
		setName(title);
		
		new Thread(reader = new Reader(page)).start();
	}
	
	public void enable(final Severity severity) {
		severities[severity.ordinal()] = true;
	}
	
	public void disable(final Severity severity) {
		severities[severity.ordinal()] = false;
	}
	
	public boolean isEnabled(final Severity severity) {
		return severities[severity.ordinal()];
	}
	
	public void dispose() {
		reader.stop();
	}
	
	private Presenter createTreePresenter() {
		return new TreePresenter();
	}
	
	private Presenter createTextPresenter() {
		return new TextPresenter();
	}
	
	private Presenter createTablePresenter() {
		return new TablePresenter();
	}

	private class Reader implements Runnable {
		
		private boolean started = false;
		private final LogPage page;		
		
		public Reader(final LogPage page) {
			this.page = page;
		}
		
		public void stop() {
			page.close();
			started = false;
		}
		
		@Override
		public void run() {
			started = true;
			LogEntry entry = null;
			while ( started ) {
				if ( (entry = page.next())!=null && isEnabled(entry.getSeverity()) ) {
					for (final Presenter presenter : presenters) {
						presenter.append(entry);
					}	
				}
			}	
		}		
		
	}
	
}
