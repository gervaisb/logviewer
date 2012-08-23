package be.gervaisb.ogam.tools.logviewer.impls.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import be.gervaisb.ogam.tools.logviewer.LogViewerController;

public class ManageSessionsAction extends AbstractAction {
	
	private final LogViewerController controller;
	
	public ManageSessionsAction(final LogViewerController controller) {
		super("Manage");
		this.controller = controller;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
			
	}

}
