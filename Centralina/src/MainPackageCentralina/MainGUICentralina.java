package MainPackageCentralina;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;

import Centralina.CentralinaDataManager;
import Centralina.CentralinaStradaleASensoreSingolo;
import Centralina.ID;
import Centralina.SensoDiMarcia;
import Centralina.Tipologia;
import GestoriTraffico.IRmiGestoreTraffico;

import javax.swing.JButton;

public class MainGUICentralina {

	private static int nDato;
	private static boolean occupato;
	private static Timer timer;
	private static Timer timer2;
	private static int velRilevata;
	private static int n;
	private static int tmp;
	
	private JFrame frame;
	private static CentralinaDataManager db;
	
	private static CentralinaStradaleASensoreSingolo c1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					db = new CentralinaDataManager();
					MainGUICentralina window = new MainGUICentralina();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void accendi(String via, int limiteVelocita) {
		//on click su imposta via
		System.out.println("Via centralina impostata");

		try {
			
			Registry registry = LocateRegistry.getRegistry("127.0.0.1", 12345);
			c1 = new CentralinaStradaleASensoreSingolo(via, limiteVelocita, Tipologia.SECONDARIA, SensoDiMarcia.AB);
			
			IRmiGestoreTraffico server = (IRmiGestoreTraffico) registry.lookup("GestoreTraffico");
			c1.setServer(server);
			
			c1.getServer().ciao();
			
			c1.accendi();
			System.out.println("Centralina online");
			
			/* ESTRAZIONE DATI PER LA SIMULAZIONE */
			
			simulazione(limiteVelocita, 5);
		    
		    
		    
		    
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int setVel(int vel, int quanto)
	{
		if (vel==0)
		{
			vel=3;
		}
		vel=vel+quanto;
		if (vel>5)vel=5;
		if (vel<0)vel=0;
		return vel;
	}

	/**
	 * Create the application.
	 */
	public MainGUICentralina() {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		Color grigio = Color.decode("#2A282D");
		Color rosso = Color.decode("#DB514C");
		
		frame = new JFrame();
		frame.getContentPane().setBackground(grigio);
		frame.getContentPane().setForeground(Color.WHITE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Centralina Stradale");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(65, 18, 342, 38);
		lblNewLabel.setFont(new Font(".SF NS Text", Font.BOLD, 32));
		frame.getContentPane().add(lblNewLabel);
		frame.setBounds(100, 100, 450, 300);
		
		JComboBox<String> comboBoxPosizione = new JComboBox<String>();
		//System.out.println("a");
		ArrayList<String> vie = new ArrayList<String>(getDB().popolaComboBoxPosizione());
		//sort per via escludendo i segmenti (primi 3 caratteri)
		Collections.sort(vie, Comparator.comparing(s -> s.substring(4)));  
		//popolamento con le vie nel db
        for (String via : vie ) {
        	comboBoxPosizione.addItem(via);
        }
        comboBoxPosizione.setBounds(125, 122, 199, 27);
        frame.getContentPane().add(comboBoxPosizione);
        
        JLabel labelVIa = new JLabel("");
        labelVIa.setForeground(Color.WHITE);
        labelVIa.setFont(new Font(".SF NS Text", Font.BOLD, 22));
        labelVIa.setBounds(67, 51, 342, 38);
        frame.getContentPane().add(labelVIa);
        
		JButton btnNewButton = new JButton("Imposta posizione");
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(rosso);
		btnNewButton.setOpaque(true);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setFont(new Font(".SF NS Text", Font.PLAIN, 13));
		//on click action
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String v = (String) comboBoxPosizione.getSelectedItem();
				//TODO qui setti la tua posizione
				btnNewButton.setEnabled(false);
				comboBoxPosizione.setEnabled(false);
				
				accendi(v.substring(3), getDB().getLimiteVelocita(v));
				
				labelVIa.setText(v.substring(3));
				
			}
		});
        btnNewButton.setBounds(143, 212, 165, 29);
        frame.getContentPane().add(btnNewButton);
        

        

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public CentralinaDataManager getDB() { return this.db; }
	
	public static void simulazione(int limiteVelocita, int vel)
	{
		tmp=0;
		velRilevata=vel;
				n = (int)(Math.random()*100);
				//System.out.println(n);
				if (n>=40 && n<70) 
				{
					if (n%2==0) 
					{
						velRilevata=setVel(velRilevata,1);
					}
					else
					{
						velRilevata=setVel(velRilevata,-1);
					}
				}
				else if (n>=70 && n<85) 
				{
					if (n%2==0) 
					{
						velRilevata=setVel(velRilevata,2);
					}
					else
					{
						velRilevata=setVel(velRilevata,-2);
					}
				}
				else if (n>=85 && n<95) 
				{
					if (n%2==0) 
					{
						velRilevata=setVel(velRilevata,3);
					}
					else
					{
						velRilevata=setVel(velRilevata,-3);
					}
				}
				else if (n>=95) 
				{
					if (n%2==0) 
					{
						velRilevata=setVel(velRilevata,4);
					}
					else
					{
						velRilevata=setVel(velRilevata,-4);
					}
				}
				else
				{
					velRilevata=setVel(velRilevata,0);
				}
				
				timer = new Timer();
				nDato=0;
				System.out.println("velocita : "+velRilevata);
				tmp=velRilevata*limiteVelocita/5;
				tmp=tmp*10;
				if (tmp==0)tmp=limiteVelocita/7;
				occupato=false;
				Timer timer2= new Timer();
				if (velRilevata==0)
				{
					
				}
			    timer2.schedule(new TimerTask() {
			        int nn=0;
			            @Override
			            public void run() {
			            	if (nn==0 && occupato==false)
			            	{
			            		occupato=true;
			            		c1.nuovaRilevazione(new Timestamp (System.currentTimeMillis()), ID.A);
			            		nDato=1;
			            		nn=1;
			            	}
			            	else
			            	{
			            		c1.nuovaRilevazione(new Timestamp (System.currentTimeMillis()), ID.B);
			            		occupato=false;
			            		nn=0;
			    			    System.out.println("media : " + (c1.getVMedia()/c1.getRilevazioni()));
			            		simulazione(limiteVelocita, velRilevata);
			            		timer2.cancel();
			            		timer2.purge();
			            	}
			            }
			    }, 4000, (45000/tmp)*36/10);
	}
}
