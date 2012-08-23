package be.gervaisb.ogam.tools.logviewer.impls.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import be.gervaisb.ogam.tools.logviewer.LogViewerApplication;
import be.gervaisb.ogam.tools.logviewer.LogViewerController;
import be.gervaisb.ogam.tools.logviewer.core.Session;
import be.gervaisb.ogam.tools.logviewer.impls.gui.resources.Resources;

public class OpenSessionAction extends AbstractAction {
	
	private final LogViewerController controller;
	private final Session session;
	
	public OpenSessionAction(final LogViewerController controller) {
		this(controller, null);
	}
	
	public OpenSessionAction(final LogViewerController controller, final Session session) {
		super(session!=null?session.getName():"Open", Resources.getIcon("Session.Open"));
		this.controller = controller;
		this.session = session;
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Session selected = session!=null?session:select();
		try {
			controller.open(selected);
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private Session select() {
		return null;
	}

}
