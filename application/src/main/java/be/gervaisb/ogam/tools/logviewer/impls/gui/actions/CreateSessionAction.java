package be.gervaisb.ogam.tools.logviewer.impls.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import be.gervaisb.ogam.tools.logviewer.LogViewerController;
import be.gervaisb.ogam.tools.logviewer.core.Session;
import be.gervaisb.ogam.tools.logviewer.impls.gui.SessionEditor;
import be.gervaisb.ogam.tools.logviewer.impls.gui.resources.Resources;

public class CreateSessionAction extends AbstractAction {
	private static final long serialVersionUID = 6757832993102067328L;
	private final LogViewerController controller;
	
	public CreateSessionAction(final LogViewerController controller) {
		super("New", Resources.getIcon("Session.New"));
		this.controller = controller;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final Session session = SessionEditor.create();
		if ( session!=null ) {
			controller.store(session);
			try {
				controller.open(session);
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
