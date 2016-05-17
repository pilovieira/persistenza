package br.com.pilovieira.persistenza;

import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.serializers.JavaCodeSerializer;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class EntityExporterView extends JFrame {

	private static final long serialVersionUID = -8002378347630063831L;
	private JPanel contentPane;
	private JTextField textPathJar;
	private JButton btnExportar;
	private JList<String> listEntidades;
	private JLabel lblEntidades;
	
	private File selectedFile;
	private EntityExporter exporter;
	private JPanel panelEntityExporter;
	private JComboBox<Class<? extends Database>> dropDatabases;
	private JLabel lblDatabase;
	private JLabel lblScriptFile;
	private JTextField textOutputFilePath;
	private JButton button;

	public static void main(String[] args) {
		new EntityExporterView();
	}
	
	public EntityExporterView() {
		setTitle("Persistenza SQL Exporter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[grow]"));
		setLocationRelativeTo(null);
		
		createPanelEntityExporter();
		setVisible(true);
	}

	private void createPanelEntityExporter() {
		panelEntityExporter = new JPanel();
		panelEntityExporter.setBorder(new TitledBorder(null, "Entity Exporter", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelEntityExporter, "cell 0 0,grow");
		panelEntityExporter.setLayout(new MigLayout("", "[grow]", "[][][][][grow][]"));
		
		lblDatabase = new JLabel("DATABASE:");
		panelEntityExporter.add(lblDatabase, "flowx,cell 0 0");
		
		JLabel lblJarParaExportar = new JLabel("JAR para exportar:");
		panelEntityExporter.add(lblJarParaExportar, "flowx,cell 0 1");
		
		textPathJar = new JTextField();
		panelEntityExporter.add(textPathJar, "cell 0 1,growx");
		
		JButton btnProcurar = new JButton("Procurar");
		panelEntityExporter.add(btnProcurar, "cell 0 1");
		
		lblScriptFile = new JLabel("Arquivo de sa\u00EDda:");
		panelEntityExporter.add(lblScriptFile, "flowx,cell 0 2");
		
		lblEntidades = new JLabel("Entidades contempladas:");
		panelEntityExporter.add(lblEntidades, "cell 0 3");
		btnProcurar.addActionListener(new FileLoader());

		listEntidades = new JList<String>();
		JScrollPane scroll = new JScrollPane(listEntidades);
		
		panelEntityExporter.add(scroll, "cell 0 4,grow");
		
		btnExportar = new JButton("Exportar");
		panelEntityExporter.add(btnExportar, "cell 0 5,alignx right");
		
		textOutputFilePath = new JTextField();
		panelEntityExporter.add(textOutputFilePath, "cell 0 2,growx");
		
		button = new JButton("Procurar");
		button.addActionListener(new SelectPath());
		panelEntityExporter.add(button, "cell 0 2");
		
		dropDatabases = new JComboBox<Class<? extends Database>>();
		panelEntityExporter.add(dropDatabases, "cell 0 0,growx");
		dropDatabases.addActionListener(new LoadDatabase());
		btnExportar.addActionListener(new Export());
		
		populateDatabases();
	}
	
	private void populateDatabases() {
		ConfigurationBuilder config = new ConfigurationBuilder();
		config.setUrls(ClasspathHelper.forClassLoader());
		config.setScanners(new TypeAnnotationsScanner(), new SubTypesScanner());
		config.setSerializer(new JavaCodeSerializer());
		Reflections reflections = new Reflections(config);
		Set<Class<? extends Database>> subtypes = reflections.getSubTypesOf(Database.class);
		for (Class<? extends Database> dbClass : subtypes)
			dropDatabases.addItem(dbClass);
	}
	
	private class LoadDatabase implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				@SuppressWarnings("unchecked")
				Constructor<? extends Database> constructor = ((Class<? extends Database>)dropDatabases.getSelectedItem()).getConstructor(String.class, String.class, String.class);
				Database database = constructor.newInstance("", "", "");
				database.loadProperties();
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}
	
	private class FileLoader implements ActionListener {

		private static final String MSG_INVALID_FILE = "Você precisa escolher um arquivo .jar para exportar classes.";

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				loadEntities();
			} catch (Exception ex) {
				showMessageDialog(EntityExporterView.this, ex.getMessage());
				ex.printStackTrace();
			}
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
			exporter = new EntityExporter();
		}
		
		private void listEntities() {
			DefaultListModel<String> listModel = new DefaultListModel<String>();
			for (String key : exporter.getEntities())
				listModel.addElement(key);
			listEntidades.setModel(listModel);
		}
	}
	
	private class SelectPath implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.showSaveDialog(null);
			
			File file = fileChooser.getSelectedFile();
			textOutputFilePath.setText(file.getAbsolutePath() + ".txt");
		}
	}

	private class Export implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				exporter.export(textOutputFilePath.getText());
				showMessageDialog(EntityExporterView.this, "Exportado com sucesso!");
			} catch (Exception ex) {
				showMessageDialog(EntityExporterView.this, ex.getMessage());
				ex.printStackTrace();
			}
		}
		
	}
}
