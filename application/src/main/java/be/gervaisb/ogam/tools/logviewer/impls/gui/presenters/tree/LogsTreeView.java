package be.gervaisb.ogam.tools.logviewer.impls.gui.presenters.tree;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.util.Formatter;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeCellRenderer;

import be.gervaisb.ogam.tools.logviewer.core.LogEntry;
import be.gervaisb.ogam.tools.logviewer.impls.gui.resources.Resources;

class LogsTreeView extends JTree {
	private static final long serialVersionUID = -2345070638019944281L;	
	
	public LogsTreeView(final LogsTreeViewModel model) {
		super(model);
		setRowHeight(20);
		setRootVisible(false);		
		setCellRenderer(new InternalTreeCellRenderer());
	}
	
	private void ensureLastRowIsVisible() {
		scrollRectToVisible(new Rectangle(
				0, getRowCount()*getRowHeight(), getWidth(), getRowHeight()));
	}
	
	@Override
	protected TreeModelListener createTreeModelListener() {
		return new TreeModelListener() {
			@Override public void treeStructureChanged(TreeModelEvent e) {
				ensureLastRowIsVisible();
			}
			@Override public void treeNodesInserted(TreeModelEvent e) {
				ensureLastRowIsVisible();
			}
			
			@Override public void treeNodesRemoved(TreeModelEvent e) { /**/ }
			
			@Override public void treeNodesChanged(TreeModelEvent e) { /**/ }
		};
	}
	
	// ~ Inner classes ---------------------------------------------------------
	
	private class InternalTreeCellRenderer extends JPanel implements TreeCellRenderer {			
		private static final long serialVersionUID = -630521476633187690L;
		
		private final JLabel collapse = new JLabel();
		private final JLabel label = new JLabel();
		
		public InternalTreeCellRenderer() {
			super(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
						
			setBackground(UIManager.getColor("Tree.selectionBackground"));
			c.gridx = 0;
			add(collapse, c);
			collapse.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
			collapse.setSize(16, 16);
			collapse.setMinimumSize(collapse.getSize());
			collapse.setMaximumSize(collapse.getSize());
			collapse.setPreferredSize(collapse.getSize());
			
			c.gridx = 1;
			c.weightx = 1.0;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(label, c);
			
			ToolTipManager.sharedInstance().registerComponent(this);
		}
		
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			if ( value instanceof LogEntry ) {
				renderLogEntry((LogEntry) value);
			} else if ( value instanceof StackTraceElement ){
				renderStackTrace((StackTraceElement) value);
			}
			
			if ( leaf ) {
				collapse.setIcon(null);
			} else if ( expanded ) {
				collapse.setIcon(Resources.getIcon("Tree.Expand"));
			} else {
				collapse.setIcon(Resources.getIcon("Tree.Collapse"));
			}
			
			if ( selected ) {
				label.setForeground(UIManager.getColor("Tree.selectionForeground"));
				setBorder(UIManager.getBorder("Tree.selectionBorderColor"));
				setOpaque(true);				
			} else {				
				label.setForeground(UIManager.getColor("Tree.textForeground"));
				setBorder(BorderFactory.createEmptyBorder());
				setOpaque(false);
			}
			
			return this;
		}

		@SuppressWarnings("resource")
		private void renderLogEntry(LogEntry entry) {			
			label.setText(new Formatter().format(
					"%1$tH:%1$tM:%1$tS'%1$tL %1$tm-%1$tb-%1$tY [%2$s] %3$s", 
					entry.getDate(), entry.getSeverity(), entry.getText()
				).out().toString());			
			label.setToolTipText(entry.toString());
			label.setIcon(Resources.getIcon("Severity."+entry.getSeverity().name()));
		}
		
		private void renderStackTrace(StackTraceElement value) {
			label.setText(value.toString());
			label.setToolTipText(null);
			label.setIcon(null);			
		}		
	}

	
}
