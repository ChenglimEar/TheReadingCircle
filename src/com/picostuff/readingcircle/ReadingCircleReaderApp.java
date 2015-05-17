package com.picostuff.readingcircle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.picostuff.readingcircle.repo.Item;

public class ReadingCircleReaderApp {

	public static void main(String[] args) {
		try {
			System.out.println(new File(".").getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new ReadingCircleReaderApp().start();
	}
	 
	//////////////
	// instance
	
	private ReadingCircle sdk;
	private Map<String, String> session;
	
	private JFrame f;
	private JPanel header;
	private JComboBox pages;
	private JPanel sidebar;
	private JPanel page;
	
	public ReadingCircleReaderApp() {
		sdk = new ReadingCircle();
		session = new HashMap<String, String>();
	}
	
	public void start() {

		// frame
		f = new JFrame("Reading Circle");
		final JPanel p = new JPanel();
		header = new JPanel();
		header.setLayout(new BorderLayout());
		final JPanel body = new JPanel();
		page = new JPanel();
		page.setLayout(new BorderLayout());
		sidebar = new JPanel();
		final JPanel footer = new JPanel();
		f.getContentPane().add(p);
		p.setLayout(new BorderLayout());
		p.add(header, BorderLayout.PAGE_START);
		p.add(body, BorderLayout.CENTER);
		body.setLayout(new BorderLayout());
		body.add(sidebar,BorderLayout.LINE_START);
		body.add(page,BorderLayout.CENTER);
		p.add(footer, BorderLayout.PAGE_END);
				
		// go to home page
		gotoHome();
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension size = new Dimension(400,300);
		f.setMinimumSize(size);
		
		// layout
		f.pack();
		f.setVisible(true);
	}

	public void gotoHome() {
		sidebar.removeAll();
		page.removeAll();
		String reader = session.get("reader");
		if (reader == null) {
			JPanel form = new JPanel();
			form.setLayout(new BorderLayout());
			final JTextField readerField = new JTextField();
			JPanel row = new JPanel();
			row.setLayout(new BorderLayout());
			String errormsg = session.get("errormsg");
			if (errormsg != null) {
				row.add(new JLabel(errormsg),BorderLayout.PAGE_START);
				session.remove("errormsg");
			}
			row.add(new JLabel("name"),BorderLayout.LINE_START);
			row.add(readerField,BorderLayout.CENTER);
			form.add(row,BorderLayout.PAGE_START);
			
			JButton submit = new JButton("login");
			submit.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// save the information and update the page asynchronously
					String name = readerField.getText();
					if (sdk.readerExists(name))
						session.put("reader", name);
					else 
						session.put("errormsg", "failed to login");
					// TODO: set an error message to display for failed login
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							gotoHome();
						}
					});
				}
			});
			JPanel buttonRow = new JPanel();
			buttonRow.setLayout(new BorderLayout());
			buttonRow.add(submit, BorderLayout.LINE_END);
			form.add(buttonRow,BorderLayout.PAGE_END);
			page.add(form,BorderLayout.PAGE_START);
			
			updateHeader(new String[] {"home"},0);
		} else {
			page.add(new JLabel("Hi, " + reader));
			updateHeader(new String[] {"home", "collection"},0);
		}
		page.revalidate();
		f.pack();
		f.repaint();
	}
	
	public void gotoCollection() {
		sidebar.removeAll();
		final JList views = new JList(new String[] {"All","Currently Reading", "At My Desk", "Unknowns"});
		views.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				String reader = session.get("reader");
				String selection = (String)views.getSelectedValue();
				if (selection.equals("All")) {
					page.removeAll();
					page.add(makeItemsPanel(sdk.getCollection()),BorderLayout.CENTER);
					page.revalidate();
				} else if (selection.equals("Currently Reading")) {
					page.removeAll();
					page.add(makeItemsPanel(sdk.getCurrentlyReading(reader)),BorderLayout.CENTER);
					page.revalidate();
				} else if (selection.equals("At My Desk")) {
					page.removeAll();
					page.add(makeItemsPanel(sdk.getAtMyDesk(reader)),BorderLayout.CENTER);
					page.revalidate();
				} else if (selection.equals("Unknowns")) {
					page.removeAll();
					page.add(makeItemsPanel(sdk.getUnknowns()),BorderLayout.CENTER);
					page.revalidate();
				}
				f.pack();
				f.repaint();
			}
		});
		sidebar.add(views);
		views.setSelectedIndex(0);
		//page.removeAll();
		//page.add(new JList(sdk.getCollection()),BorderLayout.CENTER);
		f.pack();
		f.repaint();
	}
	
	public void gotoCollectionAfterCheckin(String itemName) {
		try {
			sdk.checkinItem(itemName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gotoCollection();
	}
	
	public void gotoCollectionAfterCheckout(String itemName) {
		String reader = session.get("reader");
		try {
			sdk.checkoutItem(itemName,reader);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gotoCollection();
	}
	
	public void gotoCollectionAfterMarkAtMyDesk(String itemName) {
		String reader = session.get("reader");
		try {
			sdk.markItemAtMyDesk(itemName, reader);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gotoCollection();
	}
	
	public void gotoCollectionAfterMarkUnknown(String itemName) {
		try {
			sdk.markItemUnknown(itemName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gotoCollection();
	}
	
	private void updateHeader(String[] choices, int selectedIndex) {
		JLabel siteName = new JLabel("Reading Circle");
		pages = new JComboBox(choices);
		pages.setSelectedIndex(selectedIndex);
		pages.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String choice = (String)pages.getSelectedItem();
				if (choice != null) {
					if (choice.equals("home")) {
						gotoHome();
					} else if (choice.equals("collection")) {
						gotoCollection();
					}
				}
			}
		});
		header.removeAll();
		header.add(siteName,BorderLayout.LINE_START);
		header.add(pages,BorderLayout.LINE_END);
		header.revalidate();

	}
	
	private JPanel makeItemsPanel(List<Item> items) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		for (Item item:items) {
			panel.add(makeItemPanel(item));
		}
		return panel;
	}
	
	private JPanel makeItemPanel(Item item) {
		String title = item.getTitle();
		String reader = item.getReader();
		String status = item.getStatus();
		String currentReader = session.get("reader");
		if (reader != null) {
			if (status == null) {
				status = "<failed to retriev status>";
			}
		}
		if (title == null)
			title = "<failed to retrieve title>";
		JPanel panel = new JPanel();
		panel.add(new JLabel(title));
		panel.add(new JLabel(reader));
		panel.add(new JLabel(status));
		if (reader != null) {
			if (reader.equals(currentReader)) {
				if (status.equals("reading"))
					panel.add(makeItemCheckin(item));
				if (status.equals("done")) {
					panel.add(makeItemCheckout(item));
					panel.add(makeItemUnknown(item));
				}
			} else {
				if (status.equals("done"))
					panel.add(makeItemCheckout(item));
			}
		} else {
			panel.add(makeItemCheckout(item));
			panel.add(makeItemAtMyDesk(item)); // checkin with a change in reader or checkout+checkin
		}
			
		return panel;
	}
	
	private JButton makeItemCheckin(final Item item) {
		JButton button = new JButton("checkin");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				gotoCollectionAfterCheckin(item.getName());
				
			}
		});
		return button;
	}
	
	private JButton makeItemCheckout(final Item item) {
		JButton button = new JButton("checkout");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				gotoCollectionAfterCheckout(item.getName());
			}
		});
		return button;
	}
	
	private JButton makeItemAtMyDesk(final Item item) {
		JButton button = new JButton("at my desk");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				gotoCollectionAfterMarkAtMyDesk(item.getName());
			}
		});
		return button;
	}

	private JButton makeItemUnknown(final Item item) {
		JButton button = new JButton("mark lost");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				gotoCollectionAfterMarkUnknown(item.getName());
			}
		});
		return button;
	}

}
