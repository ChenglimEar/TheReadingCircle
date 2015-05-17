package com.picostuff.readingcircle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

import org.junit.Assert;
import org.junit.Test;

public class ReadingCircleSwingApp {

	public static void main(String[] args) {
		try {
			System.out.println(new File(".").getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new ReadingCircleSwingApp().start();
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
	
	public ReadingCircleSwingApp() {
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
			JPanel msg = new JPanel(new FlowLayout());
			msg.add(new JLabel("Welcome to the Reading Circle.  Please identify yourself to get started."));
			JButton identify = new JButton("Identify Me");
			identify.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					gotoAccount();
				}
			});
			msg.add(identify);
			page.add(msg,BorderLayout.CENTER);
			updateHeader(new String[] {"home", "sign in"},0);
		} else {
			page.add(new JLabel("Hi, " + reader));
			updateHeader(new String[] {"home", "account","readers","collection"},0);
		}
		page.revalidate();
		f.pack();
		f.repaint();
	}
	
	public void gotoAccount() {
		sidebar.removeAll();
		page.removeAll();
		String reader = session.get("reader");
		JPanel form = new JPanel();
		form.setLayout(new BorderLayout());
		final JTextField readerField = new JTextField();
		JPanel row = new JPanel();
		row.setLayout(new BorderLayout());
		row.add(new JLabel("name"),BorderLayout.LINE_START);
		row.add(readerField,BorderLayout.CENTER);
		form.add(row,BorderLayout.PAGE_START);
		
		JButton submit = new JButton("submit");
		submit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// save the information and update the page asynchronously
				String name = readerField.getText();
				session.put("reader", name);
				try {
					sdk.addReader(name);
				} catch (Exception e) {
					// TODO: return problem to user instead of bypassing it
					sdk.updateReader(name);
				}
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
		
		if (reader == null) {
			updateHeader(new String[] {"home", "sign in"},1);
		} else {
			readerField.setText(reader);
			updateHeader(new String[] {"home", "account","readers","collection"},1);
		}
		f.pack();
		f.repaint();
	}
	
	public void gotoReaders() {
		sidebar.removeAll();
		final JList views = new JList(new String[] {"Summary","Add Reader"});
		views.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				String selection = (String)views.getSelectedValue();
				if (selection.equals("Summary")) {
					page.removeAll();
					page.add(new JList(sdk.getReaders()),BorderLayout.CENTER);
					page.revalidate();
				} else if (selection.equals("Add Reader")) {
					page.removeAll();
					JPanel form = new JPanel();
					form.setLayout(new BorderLayout());
					final JTextField readerField = new JTextField();
					JPanel row = new JPanel();
					row.setLayout(new BorderLayout());
					row.add(new JLabel("name"),BorderLayout.LINE_START);
					row.add(readerField,BorderLayout.CENTER);
					form.add(row,BorderLayout.PAGE_START);
					
					JButton submit = new JButton("submit");
					submit.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							// save the information and update the page asynchronously
							String name = readerField.getText();
							try {
								sdk.addReader(name);
							} catch (Exception e) {
								// TODO: return problem to user instead of bypassing it
								sdk.updateReader(name);
							}
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									gotoReaders();
								}
							});
						}
					});
					JPanel buttonRow = new JPanel();
					buttonRow.setLayout(new BorderLayout());
					buttonRow.add(submit, BorderLayout.LINE_END);
					form.add(buttonRow,BorderLayout.PAGE_END);
					page.add(form,BorderLayout.PAGE_START);
					page.revalidate();
				}
				f.pack();
				f.repaint();
			}
		});
		sidebar.add(views);
		views.setSelectedIndex(0);
		//page.removeAll();
		//page.add(new JList(sdk.getReaders()),BorderLayout.CENTER);
		f.pack();
		f.repaint();
	}
	
	public void gotoCollection() {
		sidebar.removeAll();
		//sidebar.add(new JList(new String[] {"Summary","Add Item"}));
		final JList views = new JList(new String[] {"Summary","Add Item"});
		views.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				String selection = (String)views.getSelectedValue();
				if (selection.equals("Summary")) {
					page.removeAll();
					page.add(new JList(sdk.getItemNames(sdk.getCollection())),BorderLayout.CENTER);
					page.revalidate();
				} else if (selection.equals("Add Item")) {
					page.removeAll();
					JPanel form = new JPanel();
					form.setLayout(new BorderLayout());
					final JTextField nameField = new JTextField();
					JPanel row = new JPanel();
					row.setLayout(new BorderLayout());
					row.add(new JLabel("name"),BorderLayout.LINE_START);
					row.add(nameField,BorderLayout.CENTER);
					form.add(row,BorderLayout.PAGE_START);
					
					JButton submit = new JButton("submit");
					submit.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							// save the information and update the page asynchronously
							String name = nameField.getText();
							try {
								sdk.addItem(name);
							} catch (Exception e) {
								// TODO: return problem to user instead of bypassing it
								sdk.updateItem(name);
							}
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									gotoCollection();
								}
							});
						}
					});
					JPanel buttonRow = new JPanel();
					buttonRow.setLayout(new BorderLayout());
					buttonRow.add(submit, BorderLayout.LINE_END);
					form.add(buttonRow,BorderLayout.PAGE_END);
					page.add(form,BorderLayout.PAGE_START);
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
					} else if (choice.equals("sign in")) {
						gotoAccount();
					} else if (choice.equals("account")) {
						gotoAccount();
					} else if (choice.equals("readers")) {
						gotoReaders();
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
	
}
