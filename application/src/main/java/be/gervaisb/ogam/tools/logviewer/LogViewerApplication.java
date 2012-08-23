package be.gervaisb.ogam.tools.logviewer;

import be.gervaisb.ogam.tools.logviewer.core.LogPage;
import net.java.zixle.fabric.Application;

public abstract class LogViewerApplication extends Application {

	@Override
	public String getName() {
		return "Log viewer";
	}

	public abstract void show(LogPage page);

}
