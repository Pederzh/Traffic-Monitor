package MainPackage;

import AppUtente.ApplicazioneDataManager;
import AppUtente.GestoreInterfaccia;
import Common.Traffico;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import AppSistema.GestoreNotificheApp;
import AppSistema.Notifica;
import AppSistema.Segnalazione;

import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.awt.event.ItemEvent;
import javax.swing.JList;

import GestoriTraffico.GestoreNotifiche;
import GestoriTraffico.GestoreTraffico;
import GestoriTraffico.IRmiGestoreNotifiche;
import GestoriTraffico.IRmiGestoreTraffico;


public class MainGUI {
	
	//ELEMENTI INTERNI GUI
	private JFrame frmTrafficMonitorApp;
	private JTextField textUsername;
	private JPasswordField textPassword;
	
	//ELEMENTI MODIFICABILI GUI
	private JPanel panelAutenticazione;
	private JPanel panelPaginaPrincipale;
	private JPanel panelSegnalazione;
	private JPanel panelListaNotifiche;
	private JPanel panelHeader;
	private JLabel lblVarNotificaStatoTraffico;
	private JLabel lblVarNotificaVia;
	private JButton btnChiudiNotifica;
	private JPanel panelNotifica;
	private JLabel lblVarPosizione;
	private JButton btnSegnalazione;
	private JButton btnListaNotifiche;
	private JButton btnInviaSegnalazione;
	private DefaultListModel<String> notifiche;
	
	//CLASSI ESTERNE
	private static GestoreInterfaccia gInterfaccia;
	private static GestoreNotificheApp gNotifiche;
	private static ApplicazioneDataManager appManager;
	private static Segnalazione segn;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws RemoteException{
		EventQueue.invokeLater(new Runnable() {
		public void run() {
				try {
					gInterfaccia = new GestoreInterfaccia();
					gNotifiche = new GestoreNotificheApp();
					appManager = new ApplicazioneDataManager();
					segn = new Segnalazione();
					System.out.println("1");
					gInterfaccia.setGNotifiche(gNotifiche);
					gInterfaccia.setSegnalazione(segn);
					gNotifiche.setGInterfaccia(gInterfaccia);
					gInterfaccia.setAppManager(appManager);
					System.out.println("2");
					MainGUI window = new MainGUI();
					System.out.println("3");
					gInterfaccia.setGUI(window);
					gNotifiche.setGUI(window);
					window.frmTrafficMonitorApp.setVisible(true);
					System.out.println("4");
					// inizio rmi app->sistema
						//per segnalazione
						Registry registry0 = LocateRegistry.getRegistry("127.0.0.1", 12345);
						IRmiGestoreTraffico serverIGT = (IRmiGestoreTraffico) registry0.lookup("GestoreTraffico");
						segn.setServer(serverIGT);
						
						
						//per dire al sistema centrale che ho cambiato posizione, quindi per richiedere le notifiche riguardo la nuova posizione
						Registry registry = LocateRegistry.getRegistry("127.0.0.1", 12346);
						IRmiGestoreNotifiche serverIGN = (IRmiGestoreNotifiche) registry.lookup("GestoreNotifiche");
					// fine rmi 
					// inizio rmi sistema->app
						System.out.println("5");
						//creazione porta
						boolean errore = true;
						int tmp = 0; //numero applicazione
						while (errore == true && tmp<100)
						{
							try {
								tmp++;
								registry = LocateRegistry.createRegistry(12346+tmp);
								registry.rebind("Applicazione", gNotifiche);
								System.out.println("Client online");
								serverIGN.registraClient(tmp);
								errore=false;
								
							} catch (Exception e1) {
								e1.printStackTrace();
							} 
						}
					gNotifiche.setServer(serverIGN);
					gNotifiche.setIo(tmp);
					gNotifiche.getServer().ciao(tmp);
					// fine rmi
					

					
				} catch (Exception e) {
			e.printStackTrace();
			}
		}
});
	}
				

	/**
	 * Create the application.
	 */
	public MainGUI() {
		//
		
		//----
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Color grigio = Color.decode("#2A282D");
		Color grigioHeader = Color.decode("#323136");
		Color rosso = Color.decode("#DB514C");
		frmTrafficMonitorApp = new JFrame();
		frmTrafficMonitorApp.getContentPane().setBackground(Color.DARK_GRAY);
		frmTrafficMonitorApp.setBackground(grigio);
		frmTrafficMonitorApp.setTitle("Traffic Monitor App");
		frmTrafficMonitorApp.setResizable(false);
		frmTrafficMonitorApp.setBounds(100, 100, 367, 659);
		frmTrafficMonitorApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTrafficMonitorApp.getContentPane().setLayout(null);
		
		panelAutenticazione = new JPanel();
		panelAutenticazione.setBackground(grigio);
		panelAutenticazione.setBounds(0, 0, 367, 659);
		frmTrafficMonitorApp.getContentPane().add(panelAutenticazione);
		panelAutenticazione.setLayout(null);
		
		textUsername = new JTextField();
		textUsername.setBounds(102, 342, 165, 35);
		panelAutenticazione.add(textUsername);
		textUsername.setColumns(10);
		
		JButton btnAccedi = new JButton("Accedi");
		btnAccedi.setForeground(Color.WHITE);
		btnAccedi.setBackground(rosso);
		btnAccedi.setOpaque(true);
		btnAccedi.setBorderPainted(false);
		btnAccedi.setFont(new Font(".SF NS Text", Font.PLAIN, 13));
		//on click action
		btnAccedi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gInterfaccia.accedi(textUsername.getText(), String.valueOf(textPassword.getPassword()));
			}
		});
		btnAccedi.setBounds(102, 461, 165, 50);
		panelAutenticazione.add(btnAccedi);
		
		textPassword = new JPasswordField();
		textPassword.setBounds(102, 402, 165, 35);
		panelAutenticazione.add(textPassword);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font(".SF NS Text", Font.PLAIN, 13));
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setBounds(102, 326, 78, 16);
		panelAutenticazione.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font(".SF NS Text", Font.PLAIN, 13));
		lblPassword.setBounds(102, 389, 78, 16);
		panelAutenticazione.add(lblPassword);
		
		JLabel lblseNonSei = new JLabel("(se non sei ancora registrato, lo sarai automaticamente)");
		lblseNonSei.setFont(new Font(".SF NS Text", Font.PLAIN, 10));
		lblseNonSei.setForeground(Color.WHITE);
		lblseNonSei.setBounds(56, 523, 273, 16);
		panelAutenticazione.add(lblseNonSei);
		
		JLabel lblLogo = new JLabel("");
		Image logo = new ImageIcon(this.getClass().getResource("/logo.png")).getImage();
		lblLogo.setIcon(new ImageIcon(logo));
		lblLogo.setBounds(0, 0, 367, 296);
		panelAutenticazione.add(lblLogo);
		
		JPanel panel = new JPanel();
		panel.setBounds(333, 172, 10, 10);
		panelAutenticazione.add(panel);
		
		
		panelPaginaPrincipale = new JPanel();
		panelPaginaPrincipale.setBackground(grigio);
		panelPaginaPrincipale.setBounds(0, 0, 367, 659);
		frmTrafficMonitorApp.getContentPane().add(panelPaginaPrincipale);
		panelPaginaPrincipale.setLayout(null);
		Image logosm = new ImageIcon(this.getClass().getResource("/logo-sm.png")).getImage();
		
		panelSegnalazione = new JPanel();
		panelSegnalazione.setBounds(0, 238, 367, 389);
		panelPaginaPrincipale.add(panelSegnalazione);
		panelSegnalazione.setBackground(grigio);
		panelSegnalazione.setLayout(null);
		
		JComboBox<Traffico> comboBoxLivelloTraffico = new JComboBox<Traffico>();
		Traffico[] stati = Traffico.values(); 
        for (Traffico stato : stati) {
        	comboBoxLivelloTraffico.addItem(stato);
        }
		comboBoxLivelloTraffico.setBounds(75, 149, 231, 32);
		panelSegnalazione.add(comboBoxLivelloTraffico);
		
		btnInviaSegnalazione = new JButton("Invia Segnalazione");
		btnInviaSegnalazione.setForeground(Color.WHITE);
		btnInviaSegnalazione.setBackground(rosso);
		btnInviaSegnalazione.setOpaque(true);
		btnInviaSegnalazione.setBorderPainted(false);
		btnInviaSegnalazione.setFont(new Font(".SF NS Text", Font.PLAIN, 13));
		btnInviaSegnalazione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Traffico t = (Traffico) comboBoxLivelloTraffico.getSelectedItem();
				gInterfaccia.segnalaTraffico(t);
				//System.out.println(comboBoxLivelloTraffico.getSelectedIndex()); 
			}
		});
		btnInviaSegnalazione.setBounds(106, 236, 165, 50);
		panelSegnalazione.add(btnInviaSegnalazione);
		
		JLabel lblLivelloDiTraffico = new JLabel("Livello di Traffico");
		lblLivelloDiTraffico.setForeground(Color.WHITE);
		lblLivelloDiTraffico.setFont(new Font(".SF NS Text", Font.PLAIN, 13));
		lblLivelloDiTraffico.setBounds(75, 133, 110, 16);
		panelSegnalazione.add(lblLivelloDiTraffico);
		
		JLabel lblSegnalazione = new JLabel("Segnalazione");
		lblSegnalazione.setForeground(Color.WHITE);
		lblSegnalazione.setFont(new Font(".SF NS Text", Font.BOLD, 32));
		lblSegnalazione.setBounds(6, 30, 231, 39);
		panelSegnalazione.add(lblSegnalazione);
		
		JLabel lblIlSegmentoNon = new JLabel("Il segmento non viene preso in considerazione per la segnalazione");
		lblIlSegmentoNon.setForeground(Color.WHITE);
		lblIlSegmentoNon.setFont(new Font(".SF NS Text", Font.PLAIN, 10));
		lblIlSegmentoNon.setBounds(6, 71, 325, 16);
		panelSegnalazione.add(lblIlSegmentoNon);
		
		btnSegnalazione = new JButton("Segnalazione");
		btnSegnalazione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gInterfaccia.mostraFormSegnalazione();
			}
		});
		btnSegnalazione.setOpaque(true);
		btnSegnalazione.setForeground(Color.WHITE);
		btnSegnalazione.setFont(new Font(".SF NS Text", Font.BOLD, 13));
		btnSegnalazione.setBorderPainted(false);
		btnSegnalazione.setBackground(grigio);
		btnSegnalazione.setBounds(0, 74, 182, 50);
		panelPaginaPrincipale.add(btnSegnalazione);
		
		btnListaNotifiche = new JButton("Lista Notifiche");
		btnListaNotifiche.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gInterfaccia.mostraListaNotifiche();
			}
		});
		btnListaNotifiche.setOpaque(true);
		btnListaNotifiche.setForeground(Color.WHITE);
		btnListaNotifiche.setFont(new Font(".SF NS Text", Font.BOLD, 13));
		btnListaNotifiche.setBorderPainted(false);
		btnListaNotifiche.setBackground(rosso);
		btnListaNotifiche.setBounds(184, 74, 183, 50);
		panelPaginaPrincipale.add(btnListaNotifiche);
		
		panelHeader = new JPanel();
		panelHeader.setBackground(grigioHeader);
		panelHeader.setBounds(0, 0, 367, 75);
		panelPaginaPrincipale.add(panelHeader);
		panelHeader.setLayout(null);
		
		JLabel lblLogosm = new JLabel("");
		lblLogosm.setBounds(67, 0, 249, 75);
		panelHeader.add(lblLogosm);
		lblLogosm.setIcon(new ImageIcon(logosm));
		
	    panelNotifica = new JPanel();
		panelNotifica.setBounds(0, 0, 367, 75);
		panelPaginaPrincipale.add(panelNotifica);
		panelNotifica.setBorder(new LineBorder(rosso, 5));
		panelNotifica.setLayout(null);
		panelNotifica.setBackground(new Color(47, 44, 49));
		
		btnChiudiNotifica = new JButton("x");
		btnChiudiNotifica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gInterfaccia.nascondiNotifica();
			}
		});
		btnChiudiNotifica.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		btnChiudiNotifica.setBounds(341, 7, 16, 16);
		panelNotifica.add(btnChiudiNotifica);
		
		lblVarNotificaStatoTraffico = new JLabel("Traffico:");
		lblVarNotificaStatoTraffico.setForeground(Color.WHITE);
		lblVarNotificaStatoTraffico.setBounds(9, 34, 348, 35);
		panelNotifica.add(lblVarNotificaStatoTraffico);
		
		lblVarNotificaVia = new JLabel("In: ");
		lblVarNotificaVia.setForeground(Color.WHITE);
		lblVarNotificaVia.setBounds(100, 7, 170, 35);
		panelNotifica.add(lblVarNotificaVia);
		
		JLabel lblNotifica = new JLabel("Notifica");
		lblNotifica.setForeground(Color.WHITE);
		lblNotifica.setFont(new Font(".SF NS Text", Font.BOLD, 20));
		lblNotifica.setBounds(9, 6, 89, 32);
		panelNotifica.add(lblNotifica);
		
		JComboBox<String> comboBoxPosizione = new JComboBox<String>();
		ArrayList<String> vie = new ArrayList<String>(gInterfaccia.getAppManager().popolaComboBoxPosizione());
		//sort per via escludendo i segmenti (primi 3 caratteri)
		Collections.sort(vie, Comparator.comparing(s -> s.substring(4)));  
		//popolamento con le vie nel db
        for (String via : vie ) {
        	comboBoxPosizione.addItem(via);
        }
		comboBoxPosizione.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String v = (String) comboBoxPosizione.getSelectedItem();
					gInterfaccia.posizioneCambiata(v);
				}
			}
		});
		
		comboBoxPosizione.setBounds(175, 176, 171, 27);
		panelPaginaPrincipale.add(comboBoxPosizione);
		
		lblVarPosizione = new JLabel("Sei in: ");
		lblVarPosizione.setForeground(Color.WHITE);
		lblVarPosizione.setFont(new Font(".SF NS Text", Font.BOLD, 15));
		lblVarPosizione.setBounds(6, 156, 332, 16);
		panelPaginaPrincipale.add(lblVarPosizione);
		
		JLabel lblLeCifreDavanti = new JLabel("Le cifre davanti alla posizione rappresentano il segmento della via.");
		lblLeCifreDavanti.setFont(new Font(".SF NS Text", Font.PLAIN, 10));
		lblLeCifreDavanti.setForeground(Color.WHITE);
		lblLeCifreDavanti.setBounds(15, 200, 325, 16);
		panelPaginaPrincipale.add(lblLeCifreDavanti);
		
		JLabel lblServePerRicevere = new JLabel("Serve per ricevere notifiche più corrette inerenti alle vie adiacenti.");
		lblServePerRicevere.setForeground(Color.WHITE);
		lblServePerRicevere.setFont(new Font(".SF NS Text", Font.PLAIN, 10));
		lblServePerRicevere.setBounds(15, 215, 325, 16);
		panelPaginaPrincipale.add(lblServePerRicevere);
		
		JLabel lblCambia = new JLabel("Puoi cambiare posizione:");
		lblCambia.setForeground(Color.WHITE);
		lblCambia.setBounds(13, 171, 157, 35);
		panelPaginaPrincipale.add(lblCambia);
		
		panelListaNotifiche = new JPanel();
		panelListaNotifiche.setBackground(grigio);
		panelListaNotifiche.setBounds(0, 228, 367, 389);
		panelPaginaPrincipale.add(panelListaNotifiche);
		//frmTrafficMonitorApp.getContentPane().add(panelListaNotifiche);
		panelListaNotifiche.setLayout(null);
		
		notifiche = new DefaultListModel<String>();
		
		JList listaNotifiche = new JList();
		listaNotifiche.setModel(notifiche);
		listaNotifiche.setBackground(grigioHeader);
		listaNotifiche.setFont(new Font(".SF NS Text", Font.PLAIN, 14));
		listaNotifiche.setForeground(Color.WHITE);
		listaNotifiche.setBounds(10, 109, 347, 305);
		listaNotifiche.setVisibleRowCount(16);
		panelListaNotifiche.add(listaNotifiche);
		
		JLabel lblListaNotifiche = new JLabel("Lista Notifiche");
		lblListaNotifiche.setForeground(Color.WHITE);
		lblListaNotifiche.setFont(new Font(".SF NS Text", Font.BOLD, 32));
		lblListaNotifiche.setBounds(6, 44, 250, 39);
		panelListaNotifiche.add(lblListaNotifiche);
		JLabel lblRelativeAncheAlle = new JLabel("Relative anche alle vie adiacenti alla posizione corrente.");
		lblRelativeAncheAlle.setForeground(Color.WHITE);
		lblRelativeAncheAlle.setFont(new Font(".SF NS Text", Font.PLAIN, 10));
		lblRelativeAncheAlle.setBounds(10, 82, 291, 16);
		panelListaNotifiche.add(lblRelativeAncheAlle);
		
		getPanelPaginaPrincipale().setVisible(false);
		
	}
	
	public JPanel getPanelAutenticazione() { return this.panelAutenticazione; }
	
	public JPanel getPanelPaginaPrincipale() { return this.panelPaginaPrincipale; }
	
	public JPanel getPanelSegnalazione() { return this.panelSegnalazione; }
	
	public JPanel getPanelListaNotifiche() { return this.panelListaNotifiche; }
	
	public JPanel getPanelHeader() { return this.panelHeader; } 
	
	public JPanel getPanelNotifica() { return this.panelNotifica; }
	
	public JLabel getNotificaTraffico() { return this.lblVarNotificaStatoTraffico; }
	
	public JLabel getNotificaVia() { return this.lblVarNotificaVia; }
	
	public JLabel getLabelPosizione() { return this.lblVarPosizione; }
	
	public JButton getBtnSegnalazione() { return this.btnSegnalazione; }
	
	public JButton getBtnInviaSegn() { return this.btnInviaSegnalazione; }

	public JButton getBtnListaNotifiche() { return this.btnListaNotifiche; }
	
	public DefaultListModel<String> getListaNotifiche() { return this.notifiche; }

}
