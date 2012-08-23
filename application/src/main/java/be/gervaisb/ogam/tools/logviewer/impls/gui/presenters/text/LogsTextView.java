package be.gervaisb.ogam.tools.logviewer.impls.gui.presenters.text;

import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class LogsTextView extends JTextPane {
	private static final long serialVersionUID = 5948461229169608473L;
		
	public LogsTextView(final LogsTextViewModel model) {
		super(model);
		
		model.addDocumentListener(new DocumentListener() {		
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				// FIXME : Scroll to last line
//				FontMetrics metric = getFontMetrics(getFont());
//				scrollRectToVisible(
//						new Rectangle(0, metric.getHeight()*model.getLines(), getWidth(), metric.getHeight()));
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {/*Ignore*/}
			
			@Override
			public void changedUpdate(DocumentEvent e) {/*Ignore*/}
		});
	}	
	
}
