package be.gervaisb.ogam.tools.logviewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

import be.gervaisb.ogam.tools.logviewer.core.Session;
import be.gervaisb.ogam.tools.logviewer.core.connectors.api.Connector;
import be.gervaisb.ogam.tools.logviewer.core.connectors.api.ConnectorProvider;

public class LogViewerController {

	private final List<Session<? extends Connector>> sessions = new ArrayList<>();
	private final List<Session<? extends Connector>> actives = new ArrayList<>();
	
	private LogViewerApplication application;
	
	public void setApplication(LogViewerApplication application) {
		this.application = application;
	}
	
	public Iterable<Session<? extends Connector>> getLatestsSessions(int max) {
		return Collections.emptyList();
	}

	public void store(Session<? extends Connector> session) {
		this.sessions.add(session);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void open(Session<? extends Connector> session) throws Throwable {
		for (ConnectorProvider<?, ?> provider : ServiceLoader.load(ConnectorProvider.class)) {
			if ( provider.support(session.getConnector()) ) {
				application.show( provider.open((Session) session) );
				actives.add(session);
				return;
			}
		}
		throw new IllegalArgumentException("No provider found for session "+session);
	}

}
