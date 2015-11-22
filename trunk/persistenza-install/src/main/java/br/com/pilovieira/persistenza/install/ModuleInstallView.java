package br.com.pilovieira.persistenza.install;

import static com.google.common.io.Closeables.closeQuietly;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
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
import br.com.pilovieira.persistenza.PersistenzaManager;
import br.com.pilovieira.persistenza.db.PostgreSql;

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
	private JTextField textOptions;
	private JLabel lblUrl;
	private JLabel lblUser;
	private JLabel lblPass;
	private JLabel lblOptions;
	private JPanel panelDbProperties;
	private JPanel panelEntityExporter;
	
	private ScriptGroupManager groupManager;
	
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
		panelDbProperties.setLayout(new MigLayout("", "[][grow]", "[][][][]"));
		
		lblUrl = new JLabel("URL:");
		panelDbProperties.add(lblUrl, "flowy,cell 0 0,alignx right");
		
		textUrl = new JTextField("jdbc:postgresql://localhost:5432/Commerciale");
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
		
		textOptions = new JTextField();
		panelDbProperties.add(textOptions, "cell 1 3,growx");
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
		
		btnInstall = new JButton("Instalar");
		panelEntityExporter.add(btnInstall, "cell 0 3,alignx right");
		btnInstall.addActionListener(new Install());
	}
	
	private class ScriptLoaderListener implements ActionListener {

		private static final String MSG_INVALID_FILE = "Você precisa escolher um arquivo .jar para exportar classes.";


		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				loadDatabase();
				loadScripts();
				countScripts();
			} catch (Exception ex) {
				showMessageDialog(ModuleInstallView.this, ex.getMessage());
				ex.printStackTrace();
			}
		}

		private void loadDatabase() {
			PersistenzaManager.setDatabaseManager(new PostgreSql(getUrl(), textUser.getText(), textPass.getText()));
			PersistenzaManager.load();
		}

		private String getUrl() {
			String url = textUrl.getText();
			if (!textOptions.getText().isEmpty())
				url += "?" + textOptions.getText();
			return url;
		}

		private void loadScripts() {
			selectFile();
			Map<String, Set<Script>> scripts = new ScriptLoader().load(selectedFile);
			groupManager = new ScriptGroupManager(scripts);
			groupManager.refreshGroups();
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
				installScripts();
				showMessageDialog(ModuleInstallView.this, "Exportado com sucesso!");
			} catch (Exception ex) {
				showMessageDialog(ModuleInstallView.this, ex.getMessage());
				ex.printStackTrace();
			}
		}

		private void installScripts() {
			Map<ScriptGroup, List<Script>> installScripts = groupManager.getInstallScripts();
			
			for (ScriptGroup group : installScripts.keySet())
				for (Script script : installScripts.get(group)) {
					List<String> queries = getQueries(script);
					executeQueries(queries);
					groupManager.setLast(group, script);
				}
		}

		private List<String> getQueries(Script script) {
			InputStream scriptStream = script.getScriptStream();

			InputStreamReader is = new InputStreamReader(scriptStream);
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(is);

			try {
				String read = br.readLine();
				
				while (read != null) {
					sb.append(read);
					read = br.readLine();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				closeQuietly(br);
				closeQuietly(is);
			}
			
			return Arrays.asList(sb.toString().split(";"));
		}
		
		private void executeQueries(List<String> queries) {
			try {
				Connection connection = PersistenzaManager.getConnection();
		        
		        connection.setAutoCommit(false);
		        Statement statement = connection.createStatement();

		        for (String query : queries) 
		        	statement.executeUpdate(query);
		        
		        connection.commit();
		    } catch (Exception ex) {
		        throw new RuntimeException(ex);
		    }
		}
		
	}
}
