package br.com.pilovieira.persistenza.export;

import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import br.com.pilovieira.persistenza.PersistenzaManager;
import br.com.pilovieira.persistenza.db.ConnectionData;
import br.com.pilovieira.persistenza.db.PostgreSql;

public class EntityExporterView extends JFrame {

	private static final long serialVersionUID = -8002378347630063831L;
	private JPanel contentPane;
	private JTextField textPathJar;
	private JButton btnExportar;
	private JList<String> listEntidades;
	private JLabel lblEntidades;
	
	private File selectedFile;
	private EntityExporter exporter;
	private JCheckBox chkDrop;
	private JCheckBox chkCreate;
	private JTextField textUrl;
	private JTextField textUser;
	private JTextField textPass;
	private JTextField textOptions;
	private JLabel lblUrl;
	private JLabel lblUser;
	private JLabel lblPass;
	private JLabel lblOptions;
	private JPanel panelDbProperties;
	private JPanel panelEntityExporter;

	public static void main(String[] args) {
		new EntityExporterView();
	}
	
	public EntityExporterView() {
		setTitle("Persistenza Export");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		createPanelDbProperties();
		createPanelEntityExporter();
		setVisible(true);
	}

	private void createPanelDbProperties() {
		panelDbProperties = new JPanel();
		panelDbProperties.setBorder(new TitledBorder(null, "Database Properties", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelDbProperties, "cell 0 0,grow");
		panelDbProperties.setLayout(new MigLayout("", "[][grow]", "[][][][]"));
		
		lblUrl = new JLabel("URL:");
		panelDbProperties.add(lblUrl, "flowy,cell 0 0,alignx right");
		
		textUrl = new JTextField("jdbc:postgresql://localhost:5432/myDataBase");
		panelDbProperties.add(textUrl, "cell 1 0,growx");
		
		lblUser = new JLabel("USER:");
		panelDbProperties.add(lblUser, "cell 0 1,alignx right");
		
		textUser = new JTextField();
		panelDbProperties.add(textUser, "cell 1 1,growx");
		
		lblPass = new JLabel("PASS:");
		panelDbProperties.add(lblPass, "cell 0 2,alignx right");
		
		textPass = new JTextField();
		panelDbProperties.add(textPass, "cell 1 2,growx");
		
		lblOptions = new JLabel("OPTIONS:");
		panelDbProperties.add(lblOptions, "cell 0 3,alignx right");
		
		textOptions = new JTextField();
		panelDbProperties.add(textOptions, "cell 1 3,growx");
	}

	private void createPanelEntityExporter() {
		panelEntityExporter = new JPanel();
		panelEntityExporter.setBorder(new TitledBorder(null, "Entity Exporter", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelEntityExporter, "cell 0 1,grow");
		panelEntityExporter.setLayout(new MigLayout("", "[grow]", "[][][grow][]"));
		
		JLabel lblJarParaExportar = new JLabel("JAR para exportar:");
		panelEntityExporter.add(lblJarParaExportar, "flowx,cell 0 0");
		
		textPathJar = new JTextField();
		panelEntityExporter.add(textPathJar, "cell 0 0,growx");
		
		JButton btnProcurar = new JButton("Procurar");
		panelEntityExporter.add(btnProcurar, "cell 0 0");
		
		lblEntidades = new JLabel("Entidades:");
		panelEntityExporter.add(lblEntidades, "cell 0 1");
		btnProcurar.addActionListener(new FileLoader());

		listEntidades = new JList<String>();
		JScrollPane scroll = new JScrollPane(listEntidades);
		
		panelEntityExporter.add(scroll, "cell 0 2,grow");
		
		chkDrop = new JCheckBox("Drop");
		panelEntityExporter.add(chkDrop, "flowx,cell 0 3,alignx right");
		
		chkCreate = new JCheckBox("Create");
		panelEntityExporter.add(chkCreate, "cell 0 3,alignx right");
		
		btnExportar = new JButton("Exportar");
		panelEntityExporter.add(btnExportar, "cell 0 3,alignx right");
		btnExportar.addActionListener(new Export());
	}
	
	private class FileLoader implements ActionListener {

		private static final String MSG_INVALID_FILE = "Você precisa escolher um arquivo .jar para exportar classes.";

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				loadDatabase();
				loadEntities();
			} catch (Exception ex) {
				showMessageDialog(EntityExporterView.this, ex.getMessage());
				ex.printStackTrace();
			}
		}
		
		private void loadDatabase() {
			ConnectionData connectionData = new ConnectionData(getUrl(), textUser.getText(), textPass.getText());
			PersistenzaManager.setDatabaseManager(new PostgreSql(connectionData));
		}

		private String getUrl() {
			String url = textUrl.getText();
			if (!textOptions.getText().isEmpty())
				url += "?" + textOptions.getText();
			return url;
		}

		private void loadEntities() {
			selectFile();
			refreshClassLoader();
			createExporter();
			listEntities();
		}
		
		private void selectFile() {
			JFileChooser fileChooser = new JFileChooser();
			int choose = fileChooser.showSaveDialog(null);
			
			File file = fileChooser.getSelectedFile();
			
			if (choose != APPROVE_OPTION || !file.getAbsolutePath().endsWith(".jar"))
				throw new RuntimeException(MSG_INVALID_FILE);

			textPathJar.setText(file.getAbsolutePath());
			selectedFile = file;
		}

		private void refreshClassLoader() {
			PersistenzaClassLoader classLoader = new PersistenzaClassLoader(selectedFile);
			Thread.currentThread().setContextClassLoader(classLoader);
		}

		private void createExporter() {
			exporter = new EntityExporter(selectedFile);
		}
		
		private void listEntities() {
			DefaultListModel<String> listModel = new DefaultListModel<String>();
			for (String key : exporter.getEntities())
				listModel.addElement(key);
			listEntidades.setModel(listModel);
		}
	}

	private class Export implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				exporter.export(chkDrop.isSelected(), chkCreate.isSelected());
				showMessageDialog(EntityExporterView.this, "Exportado com sucesso!");
			} catch (Exception ex) {
				showMessageDialog(EntityExporterView.this, ex.getMessage());
				ex.printStackTrace();
			}
		}
		
	}
}
