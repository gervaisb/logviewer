package be.gervaisb.ogam.tools.logviewer.core.connectors.api;

import javax.swing.JPanel;

public abstract class ConnectorEditor<C extends Connector> extends JPanel {
	private static final long serialVersionUID = 3742357656412738548L;

	public abstract C create();
	
	public abstract C edit(C connector);
	
}
