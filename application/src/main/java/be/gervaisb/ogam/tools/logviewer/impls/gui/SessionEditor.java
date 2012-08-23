package be.gervaisb.ogam.tools.logviewer.impls.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import be.gervaisb.ogam.tools.logviewer.core.Session;
import be.gervaisb.ogam.tools.logviewer.core.connectors.api.Connector;
import be.gervaisb.ogam.tools.logviewer.core.connectors.api.ConnectorEditor;
import be.gervaisb.ogam.tools.logviewer.core.connectors.api.ConnectorProvider;

public class SessionEditor extends JDialog {
	
	private static final long serialVersionUID = -4289155704638154965L;
	
	@SuppressWarnings("unchecked")
	public static <C extends Connector> Session<C> edit() {
		SessionEditor editor = new SessionEditor();
		editor.setTitle("Edit session");
		editor.pack();
		editor.setVisible(true);
		
		return (Session<C>) editor.session;
	}
	
	@SuppressWarnings("unchecked")
	public static <C extends Connector> Session<C> create() {
		SessionEditor editor = new SessionEditor();
		editor.setTitle("Create new session");
		editor.pack();
		editor.setVisible(true);
		
		return (Session<C>) editor.session;
	}
	
	private final JButton btnCancel = new JButton(new AbstractAction("Cancel") {
		private static final long serialVersionUID = -5511903325786787909L;

		@Override public void actionPerformed(ActionEvent e) {
			SessionEditor.this.dispose();				
		}
	});
	private final JPanel cards = new JPanel(new CardLayout(0, 0));
	private final JPanel providerEditorContainer;
	
	private ConnectorProvider<?, ?> provider = null;
	private Session<?> session = null;
	private String name = null;
	
	private SessionEditor() {	
		super(((LogViewerGraphicalApplication) LogViewerGraphicalApplication.getInstance()).frame, "", true);
		setContentPane(cards);
		
		cards.add(createStep1(), "1");
		cards.add(createStep2(providerEditorContainer = new JPanel(new BorderLayout())), "2");
		cards.add(createStep3(), "3");
	}
	
	@Override
	public void setVisible(boolean visible) {
		if ( visible ) {
			pack();
			setLocationRelativeTo(getOwner());
		}
		super.setVisible(visible);
	}
	
	private JPanel createStep1() {
		final List<ConnectorProvider<?, ?>> model = new ArrayList<>();
		for (ConnectorProvider<?, ?> provider : ServiceLoader.load(ConnectorProvider.class)) {
			model.add(provider);
		}
		
		final JList<ConnectorProvider<?, ?>> providers = new JList<ConnectorProvider<?, ?>>(
				model.toArray(new ConnectorProvider<?, ?>[model.size()]));		
		providers.setVisibleRowCount(model.size()>3?3:model.size());
		providers.setOpaque(false);
		providers.setCellRenderer(new ListCellRenderer<ConnectorProvider<?, ?>>() {
			private final Border unselectedBorder = BorderFactory.createEmptyBorder(6, 6, 6, 6);
			private final Border selectedBorder = BorderFactory.createTitledBorder("");
			private final JPanel line = new JPanel(new GridBagLayout());
			private final JRadioButton radio = new JRadioButton();
			private final JLabel description = new JLabel();
			{
				line.setOpaque(false);
				
				radio.setOpaque(false);
				radio.setFont(radio.getFont().deriveFont((float)(radio.getFont().getSize()+2)));
				description.setForeground(Color.GRAY);
				
				GridBagConstraints c = new GridBagConstraints();
				c.anchor = GridBagConstraints.FIRST_LINE_START;
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0; c.gridy = 0;
				c.weightx = 0.5;
				
				c.insets = new Insets(2, 3, 1, 3);				
				line.add(radio, c);  c.gridy++; 
				
				c.insets = new Insets(0, 15, 4, 3);
				line.add(description, c);				
			}
			
			@Override
			public Component getListCellRendererComponent(JList<? extends ConnectorProvider<?, ?>> list,
					ConnectorProvider<?, ?> provider, int index, boolean isSelected, boolean cellHasFocus) {
				
				radio.setText(provider.getName());
				if ( provider.getDescription()!=null ) {
					description.setText(provider.getDescription());
				}
				
				if ( isSelected ) {
					radio.setSelected(true);
					line.setBorder(selectedBorder);
				} else {
					radio.setSelected(false);
					line.setBorder(unselectedBorder);
				}
				
				return line;
			}
		});
		
		final JScrollPane scroll = new JScrollPane(providers);
		scroll.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
		
		final JPanel title = new JPanel(new FlowLayout(FlowLayout.LEADING));
		title.add(new JLabel("Choose the session type :"));
		
		final Box body = Box.createVerticalBox();
		body.add(title);
		body.add(scroll);
		
		final JButton btnNext = new JButton(new AbstractAction("Next >") {		
			private static final long serialVersionUID = -5427044608944277530L;

			@Override public void actionPerformed(ActionEvent e) {
				provider = (ConnectorProvider<?, ?>) providers.getSelectedValue();
				if ( provider!=null ) {
					providerEditorContainer.removeAll();
					providerEditorContainer.add(provider.createEditor(), BorderLayout.CENTER);
				}
					CardLayout layout = (CardLayout) cards.getLayout();
					layout.show(cards, "2");
					pack();
				//}
			}
		});
		//btnNext.setEnabled(false);
		
		final Buttons buttons = new Buttons();
		buttons.add(btnCancel);
		buttons.add(btnNext);
		
		final JPanel container = new JPanel(new BorderLayout());
		container.add(buttons, BorderLayout.PAGE_END);
		container.add(body);
		
		providers.addListSelectionListener(new ListSelectionListener() {			
			@Override public void valueChanged(ListSelectionEvent e) {
				btnNext.setEnabled(providers.getSelectedValue()!=null);
			}
		});
		
		return container;
	}
	
	private JPanel createStep2(final JPanel body) {
		final JTextField fldName = new JTextField(40);
		
		final JPanel header = new JPanel(new FlowLayout());
		header.add(new JLabel("Session name :"));
		header.add(fldName);
		
		JButton btnPrevious = new JButton(new AbstractAction("< Previous") {
			private static final long serialVersionUID = 3526979592031525234L;

			@Override public void actionPerformed(ActionEvent e) {
				provider = null;
				name = null;
				CardLayout layout = (CardLayout) cards.getLayout();
				layout.show(cards, "1");
			}
		});
		
		JButton btnNext = new JButton(new AbstractAction("Next >") {
			private static final long serialVersionUID = 3526979592031578234L;

			@Override public void actionPerformed(ActionEvent e) {
				name = fldName.getText();
				CardLayout layout = (CardLayout) cards.getLayout();
				layout.show(cards, "3");
			}
		});
		
		final Buttons footer = new Buttons();
		footer.add(btnCancel);
		footer.add(btnPrevious);
		footer.add(btnNext);				
			
		final JPanel container = new JPanel(new BorderLayout());
		container.add(header, BorderLayout.PAGE_START);
		container.add(footer, BorderLayout.PAGE_END);
		container.add(body, BorderLayout.CENTER);
		
		return container;
	}
	
	private JPanel createStep3() {
		JButton btnSave = new JButton(new AbstractAction("Save") {		
			private static final long serialVersionUID = 8032515329367412929L;

			@SuppressWarnings("unchecked")
			@Override public void actionPerformed(ActionEvent e) {
				Connector connector = ((ConnectorEditor<Connector>) providerEditorContainer.getComponent(0)).create();
				session = new Session<Connector>(name, connector);
				SessionEditor.this.dispose();	
			}
		});
		
		final Buttons footer = new Buttons();
		footer.add(btnCancel);				
		footer.add(btnSave);
		
		final JPanel container = new JPanel(new BorderLayout());
		container.add(footer, BorderLayout.PAGE_END);
		container.add(new JLabel("Step 3"), BorderLayout.CENTER);
		return container;
	}
	
	
	// ~ Inner classes ---------------------------------------------------------
	
	private final static class Buttons extends JPanel {
		private static final long serialVersionUID = 8658417342043460621L;

		public Buttons() {
			super(new FlowLayout(FlowLayout.TRAILING));
			setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
			setBackground(getBackground().brighter());
		}		
	}
		
}
