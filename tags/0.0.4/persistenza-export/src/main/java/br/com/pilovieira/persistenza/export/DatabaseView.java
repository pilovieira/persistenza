package br.com.pilovieira.persistenza.export;

import static br.com.pilovieira.persistenza.db.Database.CONNECTION_PASSWORD;
import static br.com.pilovieira.persistenza.db.Database.CONNECTION_URL;
import static br.com.pilovieira.persistenza.db.Database.CONNECTION_USERNAME;
import static java.lang.System.setProperty;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import br.com.pilovieira.persistenza.PersistenzaManager;
import br.com.pilovieira.persistenza.db.Postgre;

public class DatabaseView extends JFrame {

	private static final long serialVersionUID = 7172207715791063240L;

	private JPanel contentPane;
	
	private JTextField textUrl;
	private JTextField textUser;
	private JTextField textPassword;
	private JButton btnOk;

	public DatabaseView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 210);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		getContentPane().setLayout(new MigLayout("", "[grow]", "[][][][][][][grow]"));
		
		JLabel lblDatabaseUrl = new JLabel("Database URL:");
		contentPane.add(lblDatabaseUrl, "cell 0 0");
		
		textUrl = new JTextField();
		textUrl.setText("jdbc:postgresql://localhost:5432/");
		contentPane.add(textUrl, "cell 0 1,growx,aligny top");
		textUrl.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username:");
		contentPane.add(lblUsername, "cell 0 2");
		
		textUser = new JTextField();
		contentPane.add(textUser, "cell 0 3,growx");
		textUser.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		contentPane.add(lblPassword, "cell 0 4");
		
		textPassword = new JTextField();
		contentPane.add(textPassword, "cell 0 5,growx");
		textPassword.setColumns(10);
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				load();
				dispose();
			}
		});
		contentPane.add(btnOk, "cell 0 6,alignx center");
		
		setVisible(true);
	}

	private void load() {
		setProperty(CONNECTION_URL, textUrl.getText());
		setProperty(CONNECTION_USERNAME, textUser.getText());
		setProperty(CONNECTION_PASSWORD, textPassword.getText());
		
		PersistenzaManager.loadDatabase(Postgre.class);
		
		LoaderView loaderView = new LoaderView();
		loaderView.setVisible(true);
		
		loaderView.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosed(WindowEvent e) {
				rebuild();
			}
		});
	}
	
	private void rebuild() {
		setVisible(true);
	}
}