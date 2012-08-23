package be.gervaisb.ogam.tools.logviewer.core;

import java.io.Serializable;

import be.gervaisb.ogam.tools.logviewer.core.connectors.api.Connector;

public class Session<C extends Connector> implements Serializable {
	private static final long serialVersionUID = 5856043457541223651L;
	
	private final C connector;
	private final String name;

	public Session(final String name, final C connector) {
		this.connector = connector;
		this.name = name;
	}
	
	public final String getName() {
		return name;
	}	
	
	public final C getConnector() {
		return connector;
	}
}
