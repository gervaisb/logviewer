package be.gervaisb.ogam.tools.logviewer.impls.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class CleanTabsPanel extends JPanel {
	private static final long serialVersionUID = 7133666135355109003L;
	
	private final JPanel contents;
	private final JPanel buttons;
	
	public CleanTabsPanel() {
		super(new BorderLayout(0, 0));
		
		add(contents = new JPanel(new CardLayout(0, 0)), BorderLayout.CENTER);
		add(buttons = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0)), 
				BorderLayout.PAGE_END);
	}
	
	public void add(final String title, final JComponent content) {
		int index = buttons.getComponentCount()+1;
		contents.add(content, Integer.toString(index));
		buttons.add(new Button(title, index));
		
		if ( index==1 ) {
			show(index);
		}
	}
	
	protected void show(final int tab) {
		final CardLayout cards = (CardLayout) contents.getLayout();
		cards.show(contents, Integer.toString(tab));
		
		for (int i=0; i<buttons.getComponentCount(); i++) {
			((Button) buttons.getComponent(i)).setSelected(i==tab-1);
		}
	}
	
	private final class Button extends JToggleButton implements ActionListener {
		private static final long serialVersionUID = -3571600102317976944L;
		private final int index;
		
		public Button(final String title, final int index) {
			super(title);
			setContentAreaFilled(false);
			setBorderPainted(true);
			this.index = index;
			addActionListener(this);
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			CleanTabsPanel.this.show(index);
		}
		
		@Override
		protected void paintComponent(Graphics graphics) {
			super.paintComponent(graphics);
			if ( isSelected() ) {
				graphics.setColor(SystemColor.activeCaptionBorder);
				graphics.drawRect(2, 2, getWidth()-4, getHeight()-4);
				
				graphics.setColor(SystemColor.controlHighlight);
			}
		}
		
	}

}
