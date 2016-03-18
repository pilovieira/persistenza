package br.com.pilovieira.persistenza;

import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Map;
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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.serializers.JavaCodeSerializer;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import br.com.pilovieira.persistenza.Database;
import br.com.pilovieira.persistenza.PersistenzaManager;

import javax.swing.JCheckBox;

public class ModuleInstallView extends JFrame {

	private static final long serialVersionUID = -8002378347630063831L;
	private JPanel contentPane;
	private JTextField textPathJar;
	private JButton btnInstall;
	private JList<String> listScripts;
	private JLabel lblScripts;
	
	private File selectedFile;
	private JTextField textUrl;
	private JTextField textUser;
	private JTextField textPass;
	private JComboBox<Class<? extends Database>> dropDatabases;
	private JLabel lblUrl;
	private JLabel lblUser;
	private JLabel lblPass;
	private JLabel lblOptions;
	private JLabel lblDatabase;
	private JPanel panelDbProperties;
	private JPanel panelEntityExporter;
	
	private ScriptGroupManager groupManager;
	private JButton btnConnect;
	private JLabel lblTool;
	private JButton btnToolInstall;
	private JCheckBox chkSsl;
	
	public static void main(String[] args) {
		new ModuleInstallView();
	}
	
	public ModuleInstallView() {
		setTitle("Persistenza Install");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 500);
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
		panelDbProperties.setLayout(new MigLayout("", "[][grow]", "[][][][][][]"));
		
		lblUrl = new JLabel("URL:");
		panelDbProperties.add(lblUrl, "flowy,cell 0 0,alignx right");
		
		textUrl = new JTextField("jdbc:postgresql://localhost:5432/myDataBase");
		panelDbProperties.add(textUrl, "cell 1 0,growx");
		
		lblUser = new JLabel("USER:");
		panelDbProperties.add(lblUser, "cell 0 1,alignx right");
		
		textUser = new JTextField();
		textUser.setText("postgres");
		panelDbProperties.add(textUser, "cell 1 1,growx");
		
		lblPass = new JLabel("PASS:");
		panelDbProperties.add(lblPass, "cell 0 2,alignx right");
		
		textPass = new JTextField();
		panelDbProperties.add(textPass, "cell 1 2,growx");
		
		lblOptions = new JLabel("OPTIONS:");
		panelDbProperties.add(lblOptions, "cell 0 3,alignx right");
		
		lblDatabase = new JLabel("DATABASE:");
		panelDbProperties.add(lblDatabase, "cell 0 4,alignx trailing");
		
		dropDatabases = new JComboBox<Class<? extends Database>>();
		panelDbProperties.add(dropDatabases, "cell 1 4,growx");
		
		lblTool = new JLabel("NAO CONECTADO");
		lblTool.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTool.setForeground(Color.RED);
		panelDbProperties.add(lblTool, "flowx,cell 1 5");
		
		btnToolInstall = new JButton("Instalar");
		btnToolInstall.addActionListener(new ToolInstaller());
		btnToolInstall.setVisible(false);
		panelDbProperties.add(btnToolInstall, "cell 1 5,growx");
		
		btnConnect = new JButton("Conectar");
		btnConnect.addActionListener(new DatabaseLoader());
		panelDbProperties.add(btnConnect, "cell 1 5,alignx right");
		
		chkSsl = new JCheckBox("SSL");
		panelDbProperties.add(chkSsl, "cell 1 3");
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

	private void createPanelEntityExporter() {
		panelEntityExporter = new JPanel();
		panelEntityExporter.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Module Installer", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelEntityExporter, "cell 0 1,grow");
		panelEntityExporter.setLayout(new MigLayout("", "[grow]", "[][][grow][]"));
		
		JLabel lblJarParaExportar = new JLabel("Jar para instalar:");
		panelEntityExporter.add(lblJarParaExportar, "flowx,cell 0 0");
		
		textPathJar = new JTextField();
		panelEntityExporter.add(textPathJar, "cell 0 0,growx");
		
		JButton btnProcurar = new JButton("Procurar");
		panelEntityExporter.add(btnProcurar, "cell 0 0");
		
		lblScripts = new JLabel("Scripts:");
		panelEntityExporter.add(lblScripts, "cell 0 1");
		btnProcurar.addActionListener(new ScriptLoaderListener());

		listScripts = new JList<String>();
		JScrollPane scroll = new JScrollPane(listScripts);
		
		panelEntityExporter.add(scroll, "cell 0 2,grow");
		
		btnInstall = new JButton("Instalar Scripts");
		panelEntityExporter.add(btnInstall, "cell 0 3,alignx right");
		btnInstall.addActionListener(new Install());
	}
	
	private class ToolInstaller implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			install();
			refreshInstallerTool();
		}

		private void install() {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(ScriptGroup.class);
			SchemaExport exporter = new SchemaExport(config);
			exporter.create(true, true);
		}
	}
	
	private class DatabaseLoader implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			setDatabase();
			refreshInstallerTool();
		}
		
		private void setDatabase() {
			try {
				@SuppressWarnings("unchecked")
				Constructor<? extends Database> constructor = ((Class<? extends Database>)dropDatabases.getSelectedItem()).getConstructor(String.class, String.class, String.class);
				Database database = constructor.newInstance(textUrl.getText(), textUser.getText(), textPass.getText());
				database.setSsl(chkSsl.isSelected());
				PersistenzaManager.setDatabase(database);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private class ScriptLoaderListener implements ActionListener {

		private static final String MSG_INVALID_FILE = "Voc� precisa escolher um arquivo .jar para exportar classes.";

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				PersistenzaManager.load();
				loadScripts();
				countScripts();
			} catch (Exception ex) {
				showMessageDialog(ModuleInstallView.this, ex.getMessage());
				ex.printStackTrace();
			}
		}

		private void loadScripts() {
			selectFile();
			Map<String, Set<Script>> scripts = new ScriptLoader().load(selectedFile);
			groupManager = new ScriptGroupManager(scripts);
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
		
		private void countScripts() {
			DefaultListModel<String> listModel = new DefaultListModel<String>();

			for (String value : groupManager.countScripts())
				listModel.addElement(value);
			
			listScripts.setModel(listModel);
		}
	}

	private class Install implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				groupManager.installScripts();
				showMessageDialog(ModuleInstallView.this, "Exportado com sucesso!");
			} catch (Exception ex) {
				showMessageDialog(ModuleInstallView.this, ex.getMessage());
				ex.printStackTrace();
			}
		}

	}
	
	private void refreshInstallerTool() {
		try {
			Connection c = PersistenzaManager.getConnection();
			DatabaseMetaData dbm = c.getMetaData();  
			ResultSet rs = dbm.getTables(null, null, ScriptGroup.class.getSimpleName().toLowerCase(), null);
			boolean installed = rs.next();
			
			btnToolInstall.setVisible(!installed);
			lblTool.setText(installed ? "CONECTADO E INSTALADO" : "CONECTADO E NAO INSTALADO");
			lblTool.setForeground(installed ? Color.BLUE : Color.RED);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
