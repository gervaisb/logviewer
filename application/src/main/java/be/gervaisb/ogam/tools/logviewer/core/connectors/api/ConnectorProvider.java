package be.gervaisb.ogam.tools.logviewer.core.connectors.api;

import be.gervaisb.ogam.tools.logviewer.core.LogPage;
import be.gervaisb.ogam.tools.logviewer.core.Session;

public interface ConnectorProvider<E extends ConnectorEditor<C>, C extends Connector> {

	E createEditor();
	
	C createConnector();

	String getName();

	String getDescription();
	
	boolean support(Connector connector);
	
	LogPage open(Session<C> session);
}
