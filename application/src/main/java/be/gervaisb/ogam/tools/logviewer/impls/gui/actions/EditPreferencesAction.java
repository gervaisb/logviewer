package be.gervaisb.ogam.tools.logviewer.impls.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import be.gervaisb.ogam.tools.logviewer.LogViewerApplication;
import be.gervaisb.ogam.tools.logviewer.LogViewerController;
import be.gervaisb.ogam.tools.logviewer.impls.gui.resources.Resources;

public class EditPreferencesAction extends AbstractAction {
	
	private final LogViewerApplication application;
	
	public EditPreferencesAction(final LogViewerApplication application) {
		super("Edit", Resources.getIcon("Preferences.Edit"));
		this.application = application;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
			
	}

}
