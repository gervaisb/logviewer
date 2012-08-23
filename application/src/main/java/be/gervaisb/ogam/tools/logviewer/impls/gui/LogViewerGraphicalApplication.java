package be.gervaisb.ogam.tools.logviewer.impls.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicButtonUI;

import be.gervaisb.ogam.tools.logviewer.LogViewerApplication;
import be.gervaisb.ogam.tools.logviewer.LogViewerController;
import be.gervaisb.ogam.tools.logviewer.core.LogEntry.Severity;
import be.gervaisb.ogam.tools.logviewer.core.LogPage;
import be.gervaisb.ogam.tools.logviewer.impls.gui.actions.CreateSessionAction;
import be.gervaisb.ogam.tools.logviewer.impls.gui.actions.EditPreferencesAction;
import be.gervaisb.ogam.tools.logviewer.impls.gui.actions.ManageSessionsAction;
import be.gervaisb.ogam.tools.logviewer.impls.gui.actions.OpenSessionAction;
import be.gervaisb.ogam.tools.logviewer.impls.gui.resources.Resources;

public class LogViewerGraphicalApplication extends LogViewerApplication {

	public final static int DEFAULT_HISTORY_SIZE = 5;
	public final static int DEFAULT_VIEW_MODE = LogViewerGraphicalApplication.VIEW_MODE_TABS;

	private final static int VIEW_MODE_TABS = 1;
	private final static int VIEW_MODE_FRAMES = 2;
	
	private final LogViewerController controller;
	private Action[] severityFilterActions;
	private SessionView currentView;
	private JComponent container;
	JFrame frame;	
	
	public LogViewerGraphicalApplication(final LogViewerController controller) {
		this.controller = controller;
		this.controller.setApplication(this);
	}
	
	@Override
	protected void init() {
		frame = new JFrame(getName());
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				exit();
			}
		});
		frame.setIconImage(Resources.getIcon("Logo").getImage());
	
        frame.setJMenuBar(createMenuBar());
        frame.add(createToolBar(), BorderLayout.PAGE_START);
        frame.add(container = createContent());

        frame.setMinimumSize(new Dimension(640, 480));
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
       
        
       // add(new SessionView("Sample", new RandomLogPage()));
        
	}
	
	@Override
	public void show(LogPage page) {
		add(new SessionView(page.getName(), page));		
	}
	
	
	protected JMenuBar createMenuBar() {
		JMenuBar bar = new JMenuBar();
		
		JMenu sessions = bar.add(new JMenu("Sessions"));
		sessions.addSeparator();
		final Action itmCreateSess = new CreateSessionAction(controller);
		final Action itmOpenSess = new OpenSessionAction(controller);
		final Action itmManageSess = new ManageSessionsAction(controller);
		/*((JideMenu) sessions).setPopupMenuCustomizer(new PopupMenuCustomizer() {			
			@Override
			public void customize(JPopupMenu menu) {
				menu.removeAll();
				for (Session latest : controller.getLatestsSessions(getPreferences().getInt("sessions_history", DEFAULT_HISTORY_SIZE))) {
					menu.add(new OpenSessionAction(controller, latest));
				}
				menu.addSeparator();
				menu.add(itmCreateSess);
				menu.add(itmOpenSess);
				menu.add(itmManageSess);
			}
		});*/
		sessions.add(itmCreateSess);
		sessions.add(itmOpenSess);
		sessions.add(itmManageSess);
		
		JMenu preferences = bar.add(new JMenu("Preferences"));
		preferences.add(new EditPreferencesAction(this));		
		return bar;
	}
	
	private void add(final SessionView view) {		
		switch (getPreferences().getInt("view_mode", DEFAULT_VIEW_MODE)) {
		case VIEW_MODE_FRAMES:
			addDesktopView(view);
			break;
		default:
			addTabbedView(view);
		}
	}
	
	private void addDesktopView(final SessionView view) {		
		JInternalFrame frame = new JInternalFrame(view.getName(), true, true, true, true);
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				view.dispose();
			}
		});
		frame.setContentPane(view);
		frame.pack();
		frame.setVisible(true);
		
		((JDesktopPane) container).add(frame);
	}
	
	private void addTabbedView(final SessionView view) {
		JTabbedPane tabs = (JTabbedPane) container;
		tabs.addTab(view.getName(), view);
		tabs.setTabComponentAt(tabs.getTabCount()-1, 
				new InternalCloseableTabComponent(tabs));
	}
	
	protected JToolBar createToolBar() {
		JToolBar bar = new JToolBar();
		
		severityFilterActions = new Action[Severity.values().length];
		for (Severity severity : Severity.values()) {
			Action action = new EnableOrDisableSeverityAction(severity);
			severityFilterActions[severity.ordinal()] = action;
			bar.add(new JCheckBox(action));	
			bar.add(new JLabel(" "));
		}
		
		bar.addSeparator();
		bar.add(new JButton(Resources.getIcon("Share")));
		bar.add(new JButton(Resources.getIcon("Print")));
		
		return bar;
	}
	
	protected JComponent createContent() {
		switch (getPreferences().getInt("view_mode", DEFAULT_VIEW_MODE)) {
		case VIEW_MODE_FRAMES:
			return createDesktopContent();
		default:
			return createTabbedContent();
		}		
	}
	
	protected JTabbedPane createTabbedContent() {		
		final JTabbedPane content = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		content.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
				onViewChange((SessionView)content.getSelectedComponent());				
			}
		});
		return content;
	}
	
	protected JDesktopPane createDesktopContent() {		
		return new JDesktopPane();
	}
	
	public SessionView getCurrentView() {
		return currentView;
	}
	
	private void onViewChange(final SessionView view) {
		this.currentView = view;
		for (Severity severity : Severity.values()) {
			severityFilterActions[severity.ordinal()]
					.putValue(Action.SELECTED_KEY, currentView.isEnabled(severity));
		}
	}
	
	
	// ~ Inner classes ---------------------------------------------------------
	
	private final class EnableOrDisableSeverityAction extends AbstractAction {
		private static final long serialVersionUID = 486832209550679581L;
		
		private final Severity severity;
		
		public EnableOrDisableSeverityAction(final Severity severity) {
			super(severity.toString());
			this.severity = severity;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean selected = ((JCheckBox) e.getSource()).isSelected(); 
			if ( selected ) {
				getCurrentView().enable(severity);
			} else {
				getCurrentView().disable(severity);
			}
			putValue(SELECTED_KEY, selected);
		}
	}
	
	private final static class InternalCloseableTabComponent extends JPanel {
			private static final long serialVersionUID = 6641353619140413773L;
			private final JTabbedPane pane;
		 
		    public InternalCloseableTabComponent(final JTabbedPane pane) {
		        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		        if (pane == null) {
		            throw new NullPointerException("TabbedPane is null");
		        }
		        this.pane = pane;
		        setOpaque(false);
		         
		        //make JLabel read titles from JTabbedPane
		        final JLabel label = new JLabel() {
					private static final long serialVersionUID = -3526756609440718190L;

					public String getText() {
		                int i = pane.indexOfTabComponent(InternalCloseableTabComponent.this);
		                if (i != -1) {
		                    return pane.getTitleAt(i);
		                }
		                return null;
		            }
		        };
		        
		         
		        add(label);
		        //add more space between the label and the button
		        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		        //tab button
		        final JButton button = new TabButton();
		        button.setVisible(false);
		        add(button);
		        //add more space to the top of the component
		        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		        
		        pane.addChangeListener(new ChangeListener() {			
					@Override
					public void stateChanged(ChangeEvent e) {
						if ( pane.getTabCount()<=0 )
							return;
						
						int index = pane.getSelectedIndex();					
						
						if ( InternalCloseableTabComponent.this.equals(pane.getTabComponentAt(index)) ) {
							button.setVisible(true);
						} else {
							button.setVisible(false);
						}
					}
				});
		    }
		 
		    private class TabButton extends JButton implements ActionListener {
				private static final long serialVersionUID = -9076857514117044273L;

				public TabButton() {
		            int size = 17;
		            setPreferredSize(new Dimension(size, size));
		            setToolTipText("close this tab");
		            //Make the button looks the same for all Laf's
		            setUI(new BasicButtonUI());
		            //Make it transparent
		            setContentAreaFilled(false);
		            //No need to be focusable
		            setFocusable(false);
		            setBorder(BorderFactory.createEtchedBorder());
		            setBorderPainted(false);
		            //Making nice rollover effect
		            //we use the same listener for all buttons
		            addMouseListener(buttonMouseListener);
		            setRolloverEnabled(true);
		            //Close the proper tab by clicking the button
		            addActionListener(this);
		        }
		 
		        public void actionPerformed(ActionEvent e) {
		            int i = pane.indexOfTabComponent(InternalCloseableTabComponent.this);
		            if (i != -1) {
		            	SessionView view = (SessionView) pane.getComponentAt(i);
		            	view.dispose();
		                pane.remove(i);
		            }
		        }
		 
		        //we don't want to update UI for this button
		        public void updateUI() {
		        }
		 
		        //paint the cross
		        protected void paintComponent(Graphics g) {
		            super.paintComponent(g);
		            Graphics2D g2 = (Graphics2D) g.create();
		            //shift the image for pressed buttons
		            if (getModel().isPressed()) {
		                g2.translate(1, 1);
		            }
		            
		            g2.setColor(Color.GRAY);
		            if (getModel().isRollover()) {
		                g2.setColor(Color.BLACK);
		            }
		            int delta = 6;
		            g2.setStroke(new BasicStroke(1));
		            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
		            g2.setStroke(new BasicStroke(2));
		            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);            
		            g2.dispose();
		        }
		    }
		 
		    private final MouseListener buttonMouseListener = new MouseAdapter() {
		        public void mouseEntered(MouseEvent e) {
		            Component component = e.getComponent();
		            if (component instanceof AbstractButton) {
		                AbstractButton button = (AbstractButton) component;
		                button.setBorderPainted(true);
		            }
		        }
		 
		        public void mouseExited(MouseEvent e) {
		            Component component = e.getComponent();
		            if (component instanceof AbstractButton) {
		                AbstractButton button = (AbstractButton) component;
		                button.setBorderPainted(false);
		            }
		        }
		    };
		}
}
