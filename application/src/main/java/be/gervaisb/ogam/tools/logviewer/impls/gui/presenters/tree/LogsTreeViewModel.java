package be.gervaisb.ogam.tools.logviewer.impls.gui.presenters.tree;

import java.util.LinkedList;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import be.gervaisb.ogam.tools.logviewer.core.LogEntry;

class LogsTreeViewModel implements TreeModel {

	/** Maximum number of lines that can be stored in the model */
	private static final int MAX_SIZE = 500; 
	private static final Object ROOT = new String("<!ROOT!>");
	
	private final EventListenerList listeners = new EventListenerList();
	private final LinkedList<LogEntry> entries = new LinkedList<LogEntry>();
	
	public void add(final LogEntry entry) {
		entries.add(entry);
		if ( entries.size()>MAX_SIZE ) {
			fireEntryRemoved(entries.pop());
		}
		fireEntryAdded(entry);
	}	
	
	public void clear() {
		entries.clear();
		
		TreeModelListener[] lstnrs = listeners.getListeners(TreeModelListener.class);
		TreeModelEvent event = null;
		for (int i=lstnrs.length-1; i>=0; i--) {
			if ( event==null ) {
				event = new TreeModelEvent(this, new Object[]{ROOT});
			}
			lstnrs[i].treeStructureChanged(event);
		}
	}
	
	@Override
	public Object getRoot() {
		return ROOT;
	}

	@Override
	public boolean isLeaf(Object node) {
		if ( node.equals(ROOT) )
			return false;
		if ( node instanceof LogEntry )
			return !((LogEntry) node).hasStackTrace();
		// node instanceof StackTraceElement
		return true;
	}
	
	@Override
	public int getChildCount(Object parent) {
		int count = 0;
		if ( parent instanceof LogEntry ) {
			LogEntry entry = (LogEntry) parent;
			count = entry.hasStackTrace()
					?entry.getStacktrace().length:0;
		} else if ( ROOT.equals(parent) ){
			count = entries.size();
		}
		return count;
	}
	
	@Override
	public Object getChild(Object parent, int index) {
		Object child = null;
		if ( parent instanceof LogEntry ) {
			child = ((LogEntry) parent).getStacktrace()[index];
		} else {
			child = entries.get(index);
		}
		return child;
	}
	
	@Override
	public int getIndexOfChild(Object parent, Object child) {
		int index = -1;
		if ( parent instanceof LogEntry ) {
			StackTraceElement[] stacktrace = ((LogEntry) parent).getStacktrace();
			for (int i=0; index==-1 && i<stacktrace.length; i++) {
				if ( stacktrace[i].equals(child) )
					index = i;
			}
		} else {
			for (int i=0; index==-1 && i<entries.size(); i++) {
				if ( entries.get(i).equals(child) )
					index = i;
			}
		}
		return index;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) { /**/ }

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listeners.add(TreeModelListener.class, l);		
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(TreeModelListener.class, l);
	}

	private void fireEntryRemoved(final LogEntry entry) {
		TreeModelListener[] lstnrs = listeners.getListeners(TreeModelListener.class);
		TreeModelEvent event = null;
		for (int i=lstnrs.length-1; i>=0; i--) {
			if ( event==null ) {
				event = new TreeModelEvent(this, new Object[]{ROOT}, new int[]{0}, new Object[]{entry});
			}
			lstnrs[i].treeNodesRemoved(event);
		}
	}
	
	private void fireEntryAdded(final LogEntry entry) {
		TreeModelListener[] lstnrs = listeners.getListeners(TreeModelListener.class);
		TreeModelEvent event = null;
		for (int i=lstnrs.length-1; i>=0; i--) {
			if ( event==null ) {
				event = new TreeModelEvent(this, new Object[]{ROOT}, new int[]{entries.size()-1}, new Object[]{entry});
			}
			lstnrs[i].treeNodesInserted(event);
		}
	}
	
}
