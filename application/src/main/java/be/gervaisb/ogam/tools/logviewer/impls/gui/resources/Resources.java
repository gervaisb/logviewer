package be.gervaisb.ogam.tools.logviewer.impls.gui.resources;

import java.net.URL;

import javax.swing.ImageIcon;

public final class Resources {

	private Resources() { /**/ }
	
	public static ImageIcon getIcon(final String name) {
		URL url = Resources.class.getClassLoader().getResource(new StringBuilder()
			.append(Resources.class.getPackage().getName().replace('.', '/'))
			.append("/icons/").append(name).append(".png").toString());
		if ( url==null ) {
			System.err.println("No icon found for name \""+name+"\"");
			return null;
		}
		return new ImageIcon(url);
	}
}
