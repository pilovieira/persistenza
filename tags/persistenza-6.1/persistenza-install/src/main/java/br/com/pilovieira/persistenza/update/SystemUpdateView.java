package br.com.pilovieira.persistenza.update;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import br.com.pilovieira.persistenza.script.ScriptGroupManager;
import br.com.pilovieira.persistenza.script.ScriptLogger;

public class SystemUpdateView extends JFrame {

	private static final long serialVersionUID = 7512955137864695979L;

	private JPanel contentPane;
	private JList<String> listScripts;
	private JButton btnInstall;

	private ScriptGroupManager groupManager;
	private TextQueryLog textQueryLog;

	public SystemUpdateView(ScriptGroupManager groupManager) {
		this.groupManager = groupManager;
		
		setTitle("Atualizador do sistema");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		contentPane.setLayout(new MigLayout("", "[grow]", "[][grow][grow][]"));
		
		JLabel labelContador = new JLabel("Scripts de atualiza\u00E7\u00E3o:");
		contentPane.add(labelContador, "cell 0 0");
		
		listScripts = new JList<String>();
		JScrollPane scroll = new JScrollPane(listScripts);
		
		getContentPane().add(scroll, "cell 0 1,grow");
		
		textQueryLog = new TextQueryLog();
		textQueryLog.setEditable(false);
		scroll = new JScrollPane(textQueryLog);
		scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
			}
		});
		contentPane.add(scroll, "cell 0 2,grow");
		
		btnInstall = new JButton("Instalar Scripts");
		getContentPane().add(btnInstall, "cell 0 3,alignx right");
		btnInstall.addActionListener(new Install());
		
		countScripts();
		
		setVisible(true);
	}
	
	private void countScripts() {
		DefaultListModel<String> listModel = new DefaultListModel<String>();

		for (String value : groupManager.countScripts())
			listModel.addElement(value);
		
		listScripts.setModel(listModel);
	}
	
	private class TextQueryLog extends JTextPane implements ScriptLogger {
		private static final long serialVersionUID = -2459606560635644398L;

		@Override
		public void log(String log) {
			setText(getText() + "\r\n\r\n" + log);
		}
	}
	
	private class Install implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			btnInstall.setEnabled(false);
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						groupManager.installScripts(textQueryLog);
						JOptionPane.showMessageDialog(null, "Sistema atualizado com sucesso!");
						JOptionPane.showMessageDialog(null, "É necessário reiniciar o sistema. Clique em OK para finalizar e abra-o novamente.");
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
						ex.printStackTrace();
					}
					
					System.exit(0);
				}
			}).start();
		}

	}

}
