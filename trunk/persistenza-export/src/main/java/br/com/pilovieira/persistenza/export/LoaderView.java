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

import net.miginfocom.swing.MigLayout;

public class LoaderView extends JFrame {

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

	public LoaderView() {
		setTitle("Persistenza Export");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[][][][grow][]"));
		
		JLabel lblJarParaExportar = new JLabel("JAR para exportar:");
		contentPane.add(lblJarParaExportar, "cell 0 0");
		
		textPathJar = new JTextField();
		contentPane.add(textPathJar, "flowx,cell 0 1,growx,aligny top");
		textPathJar.setColumns(10);
		
		JButton btnProcurar = new JButton("Procurar");
		btnProcurar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					loadFile();
				} catch (Exception ex) {
					showMessageDialog(LoaderView.this, ex.getMessage());
				}
			}
		});
		contentPane.add(btnProcurar, "cell 0 1");
		
		btnExportar = new JButton("Exportar");
		btnExportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chkDrop.isSelected())
					exporter.drop();
				if (chkCreate.isSelected())
					exporter.create();
				showMessageDialog(LoaderView.this, "Exportado com sucesso!");
			}
		});
		
		lblEntidades = new JLabel("Entidades:");
		contentPane.add(lblEntidades, "cell 0 2");

		listEntidades = new JList<String>();
		JScrollPane scroll = new JScrollPane(listEntidades);
		
		contentPane.add(scroll, "cell 0 3,grow");
		
		chkDrop = new JCheckBox("Drop");
		contentPane.add(chkDrop, "flowx,cell 0 4,alignx right");
		
		chkCreate = new JCheckBox("Create");
		contentPane.add(chkCreate, "cell 0 4,alignx right");
		contentPane.add(btnExportar, "cell 0 4,alignx right");
	}
	
	private void loadFile() {
		selectedFile = pathChoosed();
		
		refreshClassLoader();
		
		textPathJar.setText(selectedFile.getAbsolutePath());
		exporter = new EntityExporter(selectedFile);
		
		listEntities();
	}

	private void refreshClassLoader() {
		PersistenzaClassLoader classLoader = new PersistenzaClassLoader(selectedFile);
		Thread.currentThread().setContextClassLoader(classLoader);
	}

	private void listEntities() {
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for (String key : exporter.getEntities())
			listModel.addElement(key);
		listEntidades.setModel(listModel);
	}



	private File pathChoosed() {
		JFileChooser fileChooser = new JFileChooser();
        int choose = fileChooser.showSaveDialog(null);
        
        File selectedFile = fileChooser.getSelectedFile();

        if (choose == APPROVE_OPTION && selectedFile.getAbsolutePath().endsWith(".jar"))
			return selectedFile;
        
        throw new RuntimeException("Você precisa escolher um arquivo .jar para exportar classes.");
	}
}
