package be.gervaisb.ogam.tools.logviewer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import be.gervaisb.ogam.tools.logviewer.core.Session;
import be.gervaisb.ogam.tools.logviewer.impls.gui.LogViewerGraphicalApplication;
import be.gervaisb.ogam.tools.logviewer.impls.tui.LogViewerTerminalApplication;

/**
 * <pre>
 * java -jar launcher.jar [[-s &lt;name&gt;]|[-c [n]] [path]]
 * </pre>
 * <dl>
 *  <dt>-s  <dd>Open the graphical viewer with the given session.
 * 	<dt>-c	<dd>Capture. Read the last <em>n</em> entries from the log file 
 * 			and stop. When the file contains minder than <em>n</em> lines the
 * 			viewer stop.<br />
 * 			If <em>n</em> is missing, the last 100 entries are reads.
 *  <dt>path<dd>Path to the log file to read. The path can be a complete 
 *  		{@link URL} or a local file.
 * </dl>
 * <b>Note :</b> When <em>-s</em> is found as argument the viewer start is ui 
 * 	and the other arguments are ignoreds.
 */
public class Launcher {

	public static void main(String[] args) throws Throwable {
		LogViewerController controller = new LogViewerController();
		
		if ( args.length==0 ) {
			new LogViewerGraphicalApplication(controller).start();
		} else if ( args.length==2 && "-s".equals(args[0]) ) {
			new LogViewerGraphicalApplication(controller).start();
			controller.open(findSessionByName(controller, args[1]));
		} else {
			LogViewerApplication app = new LogViewerTerminalApplication();
			app.start();
			if ( "-c".equals(args[0]) ) {
				URL path = null;
				int entries = 100;
				if ( args.length==3 ) {
					entries = Integer.parseInt(args[1]);
					path = makeUrl(args[2]);
				} else {
					path = makeUrl(args[1]);
				}
				controller.open(new LimitedSession(entries, createSession(path)));
			} else if ( args.length==1 ){
				controller.open(createSession(makeUrl(args[2])));	
			}			
		}
	}
	
	private static Session findSessionByName(LogViewerController controller, String name) {
		return null;
	}
	
	private static URL makeUrl(final String path) {
		try {
			return new URL(path);
		} catch (MalformedURLException e) {
			File file = new File(path);
			if ( file.exists() ) {
				try {
					return file.toURI().toURL();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		throw new IllegalArgumentException();
	}
	
	private static Session createSession(final URL url) {
		return null;
	}
	
	private static final class LimitedSession extends Session {

		private final Session<?> wrapped;
		
		public LimitedSession(int entries, Session wrapped) {
			super(wrapped.getName(), wrapped.getConnector());
			this.wrapped = wrapped;
		}
		
	}
}
