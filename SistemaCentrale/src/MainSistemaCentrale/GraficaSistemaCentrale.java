package MainSistemaCentrale;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.SystemColor;
import java.awt.event.MouseWheelListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import java.awt.event.MouseWheelEvent;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import DatiTraffico.*;
import GestoriTraffico.*;

import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JTextPane;

public class GraficaSistemaCentrale {

	private JFrame frame;
	private JPanel Mappa = new JPanel();
	private JPanel panel = new JPanel();
	private JPanel panel_3 = new JPanel();
	private JPanel panel_1 = new JPanel();
	private JPanel panel_4 = new JPanel();
	private JPanel regolatore = new JPanel();
	private JPanel panel_10 = new JPanel();
	JPanel Elenco = new JPanel();
	private JButton btnNewButton = new JButton("CARICA MAPPA");
	private static String strada[][]; // quadrati 5x5 metri
	private static String mappa[][]; // quadrati 5x5 metri
	private static int larg=7; // centimetri
	private ArrayList<QuadratoVia> lista = new ArrayList<QuadratoVia>();
	private JTextField textField;
	private boolean stampa=false;
	private char selectedVia= ' ';
	private char selectedSeg= ' ';
	private int xMap=0;
	private int yMap=0;
	private int xMouse=0;
	private int yMouse=0;
	private int yPunt=0;
	private int xPunt=0;
	private int proporzione=0;
	private char direzione='u';
	private boolean inAzione=false;
	private boolean inSelezione=false;
	private int posizioneApp=0;
	private int ordineElenco=-1;
	private boolean inSelezioneElenco=false;
	private boolean closePanel=false;
	
	private static GestoreTraffico gt;
	private static GestoreNotifiche gn;
	private static GestoreClient gc;
	
	/**
	 * Launch the application.
	 * @throws RemoteException 
	 */
	public static void main(String[] args) throws RemoteException {
		
		GraficaSistemaCentrale window = new GraficaSistemaCentrale();
		gt = new GestoreTraffico();
		gn = new GestoreNotifiche();
		gc = new GestoreClient();
		gt.setGestoreClient(gc);
		gn.setGestoreClient(gc);
		gn.setGestoreTraffico(gt);
		gt.setGrafica(window);
		
		strada=new String [101][20];
		for (int i =0; i<101; i++) {
			for (int j=0; j<20; j++) {
				strada[i][j]="";
			}
		}
		
		// mappa
		String mappa1[][]= { 
				{ " " ," " ," " ," " ," " ," " ," " ,"bA"," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " },
				{ " " ," " ," " ," " ," " ," " ," " ,"b" ,"zA","z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"z" ,"zB"},
				{ " " ," " ," " ," " ," " ," " ," " ,"b" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"xA"," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"JB"," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"jB"," " ," " },
				{ " " ,"U" ,"U" ,"U" ,"U" ,"U" ,"UA","b" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"x" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"JA"," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"j" ," " ," " },
				{ " " ,"U" ," " ," " ," " ," " ," " ,"b" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ,"x" ," " ," " ," " ,"j" ," " ," " },
				{ " " ,"U" ," " ," " ," " ," " ," " ,"b" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"x" ," " ," " ," " ,"j" ," " ," " },
				{ " " ,"U" ,"U" ,"U" ,"U" ,"U" ," " ,"b" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"x" ," " ," " ," " ,"j" ," " ," " },
				{ " " ," " ," " ," " ," " ,"U" ," " ,"b" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"xB"," " ," " ," " ,"jA"," " ," " },
				{ " " ," " ," " ," " ," " ,"U" ," " ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"b" ,"bB","a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"aB"},
				{ " " ,"U" ,"U" ,"U" ,"U" ,"U" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"vB"," " ," " ," " ," " ," " ," " ," " ," " ,"a" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"HA"," " ," " ," " ," " ," " ," " ," " },
				{ " " ,"U" ," " ," " ," " ,"VA","V" ,"V" ,"V" ,"V" ,"V" ,"V" ,"V" ,"V" ,"V" ,"V" ,"V" ,"V" ,"V" ," " ,"v" ," " ,"P" ,"P" ,"P" ,"P" ,"P" ,"P" ," " ,"a" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"HB"," " ," " ," " ," " ," " ," " ," " },
				{ " " ,"U" ," " ," " ," " ," " ," " ,"ZA"," " ," " ,"XA"," " ," " ,"YA"," " ," " ," " ," " ,"V" ," " ,"v" ," " ,"P" ," " ," " ," " ," " ,"P" ," " ,"a" ," " ,"L" ,"L" ,"L" ,"L" ,"LB"," " ,"G" ,"G" ,"G" ,"G" ,"G" ,"G" ,"G" ,"G" ,"G" ,"G" ,"G" ," " ," " },
				{ " " ,"U" ,"U" ,"U" ,"U" ,"U" ," " ,"Z" ," " ," " ,"X" ," " ," " ,"Y" ," " ," " ," " ," " ,"V" ," " ,"v" ," " ,"P" ," " ," " ," " ," " ,"P" ," " ,"a" ," " ,"L" ," " ," " ," " ," " ," " ,"G" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"G" ," " ," " },
				{ " " ," " ," " ," " ," " ,"U" ," " ,"Z" ," " ," " ,"X" ," " ," " ,"Y" ,"Y" ,"Y" ,"Y" ,"YB","V" ," " ,"v" ," " ,"P" ,"RA","R" ,"R" ,"RB","P" ," " ,"a" ," " ,"L" ," " ," " ," " ," " ," " ,"G" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"G" ," " ," " },
				{ " " ," " ," " ," " ," " ,"U" ," " ,"Z" ," " ," " ,"X" ," " ," " ," " ," " ," " ," " ," " ,"V" ," " ,"v" ," " ,"P" ," " ,"TA"," " ," " ,"P" ," " ,"a" ," " ,"L" ," " ,"O" ,"O" ,"OB"," " ,"G" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"G" ," " ," " },
				{ " " ,"U" ,"U" ,"U" ,"U" ,"U" ," " ,"Z" ," " ," " ,"X" ,"X" ,"X" ,"X" ,"X" ,"X" ,"X" ,"XB","V" ," " ,"v" ," " ,"P" ," " ,"T" ," " ," " ,"P" ," " ,"a" ," " ,"L" ," " ,"O" ," " ," " ," " ,"G" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"G" ," " ," " },
				{ " " ,"U" ," " ," " ," " ," " ," " ,"Z" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"V" ," " ,"v" ," " ,"P" ," " ,"T" ," " ," " ,"P" ," " ,"a" ," " ,"LA"," " ,"OA"," " ," " ," " ,"G" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"G" ," " ," " },
				{ " " ,"U" ," " ," " ," " ," " ," " ,"Z" ,"Z" ,"Z" ,"Z" ,"Z" ,"Z" ,"Z" ,"Z" ,"Z" ,"Z" ,"ZB","V" ,"VB","v" ,"PA","P" ," " ,"T" ,"T" ," " ,"P" ,"PB","a" ,"IA","I" ,"I" ,"I" ,"I" ,"I" ,"IB","G" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"G" ," " ," " },
				{ " " ,"UB"," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"v" ," " ,"QA"," " ," " ,"T" ," " ,"QB"," " ,"a" ," " ," " ," " ,"NA"," " ,"MA"," " ,"G" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"G" ," " ," " },
				{ "oA","o" ,"o" ,"o" ,"o" ,"o" ,"o" ,"o" ,"o" ,"o" ,"o" ,"o" ,"o" ,"o" ," " ," " ," " ," " ," " ," " ,"v" ," " ,"Q" ," " ," " ,"T" ," " ,"Q" ," " ,"a" ," " ," " ," " ,"N" ," " ,"M" ," " ,"G" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"G" ," " ," " },
				{ " " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"o" ," " ," " ," " ," " ," " ," " ,"v" ," " ,"Q" ," " ," " ,"TB"," " ,"Q" ," " ,"a" ," " ,"NB","N" ,"N" ," " ,"M" ," " ,"G" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"G" ," " ," " },
				{ " " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"o" ," " ," " ," " ," " ," " ," " ,"v" ," " ,"Q" ,"SA","S" ,"S" ,"SB","Q" ," " ,"a" ," " ," " ," " ," " ," " ,"M" ," " ,"G" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"G" ," " ," " },
				{ " " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"o" ,"vA","v" ,"v" ,"v" ,"v" ,"v" ,"v" ," " ,"Q" ," " ," " ," " ," " ,"Q" ," " ,"a" ," " ," " ," " ," " ," " ,"M" ," " ,"G" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"G" ," " ," " },
				{ " " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"o" ," " ," " ," " ," " ,"WB"," " ," " ," " ,"Q" ," " ," " ," " ," " ,"Q" ," " ,"a" ," " ,"MB","M" ,"M" ,"M" ,"M" ," " ,"G" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"G" ," " ," " },
				{ " " ," " ," " ,"t" ,"t" ,"t" ,"t" ,"t" ,"t" ," " ," " ," " ," " ,"o" ," " ," " ," " ," " ,"W" ," " ," " ," " ,"Q" ,"Q" ,"Q" ,"Q" ,"Q" ,"Q" ," " ,"a" ," " ," " ," " ," " ," " ," " ," " ,"G" ,"G" ,"G" ,"GB"," " ,"G" ,"G" ,"G" ,"G" ,"G" ,"G" ," " ," " },
				{ " " ," " ," " ,"t" ," " ," " ," " ," " ,"t" ," " ," " ," " ," " ,"o" ," " ," " ," " ," " ,"W" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"a" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"G" ," " ," " ," " ," " ," " ," " ," " },
				{ " " ," " ," " ,"t" ," " ," " ," " ," " ,"t" ," " ," " ," " ," " ,"o" ," " ," " ," " ," " ,"W" ,"W" ,"W" ,"W" ,"W" ,"W" ,"W" ,"W" ," " ," " ," " ,"a" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"G" ," " ," " ," " ," " ," " ," " ," " },
				{ " " ," " ," " ,"t" ," " ," " ," " ," " ,"t" ," " ," " ," " ," " ,"o" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"W" ," " ," " ," " ,"a" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"G" ," " ," " ," " ," " ," " ," " ," " },
				{ " " ," " ," " ,"tB"," " ," " ," " ," " ,"tA"," " ," " ," " ," " ,"o" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"WA"," " ," " ," " ,"a" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"GA"," " ," " ," " ," " ," " ," " ," " },
				{ " " ,"p" ,"p" ,"p" ,"p" ," " ," " ,"p" ,"p" ,"p" ,"p" ," " ," " ,"o" ,"AB","A" ,"A" ,"A" ,"A" ,"A" ,"AA","a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"a" ,"lA","l" ,"l" ,"lB","i" ,"i" ,"i" ,"i" ,"i" ,"i" ,"i" ,"i" ,"i" ,"i" ,"i" ,"i" ,"i" ,"i" ,"i" ,"iB"},
				{ " " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"o" ," " ,"BA"," " ,"CA"," " ,"DA"," " ,"a" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"i" ," " ," " ," " ," " ," " ," " ," " ,"EA"," " ," " ," " ," " ," " ," " ," " },
				{ " " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"o" ," " ,"B" ," " ,"C" ," " ,"D" ," " ,"a" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"i" ," " ," " ," " ," " ," " ," " ," " ,"E" ," " ," " ," " ," " ," " ," " ," " },
				{ " " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"o" ," " ,"B" ," " ,"C" ," " ,"D" ," " ,"a" ," " ,"d" ,"d" ,"d" ,"d" ,"d" ," " ,"c" ,"cB","nA"," " ," " ,"i" ," " ," " ," " ," " ," " ," " ," " ,"E" ," " ," " ," " ," " ," " ," " ," " },
				{ " " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"o" ," " ,"B" ," " ,"C" ," " ,"D" ," " ,"a" ," " ,"d" ," " ,"eA"," " ,"d" ," " ,"c" ," " ,"n" ," " ," " ,"i" ," " ," " ," " ," " ," " ," " ," " ,"E" ," " ," " ," " ," " ," " ," " ," " },
				{ " " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"o" ," " ,"B" ," " ,"C" ," " ,"D" ," " ,"a" ," " ,"d" ," " ,"e" ," " ,"d" ," " ,"c" ," " ,"n" ,"mB","mA","i" ," " ," " ,"E" ,"E" ,"E" ,"E" ,"E" ,"E" ," " ,"EB","E" ,"E" ,"E" ," " ," " },
				{ " " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"p" ,"p" ,"pA","o" ," " ,"B" ," " ,"C" ," " ,"D" ," " ,"a" ," " ,"d" ," " ,"e" ,"eB","d" ," " ,"c" ," " ,"n" ," " ," " ,"i" ," " ," " ,"E" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"E" ," " ," " },
				{ " " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"qB"," " ," " ,"o" ," " ,"B" ," " ,"C" ," " ,"D" ," " ,"a" ," " ,"d" ," " ," " ," " ,"d" ," " ,"c" ," " ,"n" ," " ," " ,"i" ," " ," " ,"E" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"E" ," " ," " },
				{ " " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"q" ," " ," " ,"o" ," " ,"B" ," " ,"C" ," " ,"DB"," " ,"a" ," " ,"d" ," " ," " ,"d" ,"d" ," " ,"c" ," " ,"n" ," " ," " ,"i" ," " ," " ,"E" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"E" ," " ," " },
				{ " " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"q" ," " ," " ,"o" ," " ,"B" ," " ,"C" ," " ," " ," " ,"a" ," " ,"dA"," " ," " ,"dB"," " ," " ,"c" ," " ,"n" ,"nB"," " ,"i" ,"FA","FB","E" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"E" ," " ," " },
				{ " " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"q" ," " ," " ,"o" ," " ,"B" ," " ,"C" ," " ," " ," " ,"a" ,"cA","c" ,"c" ,"c" ,"c" ,"c" ,"c" ,"c" ," " ," " ," " ," " ,"i" ," " ," " ,"E" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"E" ," " ," " },
				{ " " ,"p" ," " ," " ,"p" ," " ," " ,"p" ," " ," " ,"q" ," " ," " ,"o" ," " ,"B" ," " ,"CB"," " ," " ," " ,"a" ," " ," " ,"fA"," " ,"gA"," " ," " ," " ," " ," " ," " ," " ,"i" ," " ," " ,"E" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"E" ," " ," " },
				{ " " ,"pB"," " ," " ,"p" ,"p" ,"p" ,"p" ," " ," " ,"q" ," " ," " ,"o" ," " ,"B" ," " ," " ," " ," " ," " ,"a" ," " ," " ,"f" ," " ,"g" ," " ," " ," " ," " ," " ," " ," " ,"i" ," " ," " ,"E" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"E" ," " ," " },
				{ " " ,"qA"," " ," " ," " ," " ," " ," " ," " ," " ,"q" ," " ," " ,"o" ," " ,"B" ," " ," " ,"u" ,"u" ,"uB","a" ," " ," " ,"f" ," " ,"g" ,"g" ,"g" ,"g" ,"g" ,"g" ,"g" ,"gB","i" ," " ," " ,"E" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"E" ," " ," " },
				{ " " ,"q" ," " ," " ," " ," " ," " ," " ," " ," " ,"q" ," " ," " ,"o" ," " ,"BB"," " ," " ,"u" ," " ," " ,"a" ," " ," " ,"f" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"i" ," " ," " ,"E" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"E" ," " ," " },
				{ " " ,"q" ," " ," " ," " ," " ," " ," " ," " ," " ,"q" ," " ," " ,"o" ," " ," " ," " ," " ,"u" ," " ," " ,"a" ," " ," " ,"f" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"i" ," " ," " ,"E" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"E" ," " ," " },
				{ " " ,"q" ,"q" ,"q" ,"q" ,"q" ,"q" ,"q" ,"q" ,"q" ,"q" ," " ," " ,"o" ,"uA","u" ,"u" ,"u" ,"u" ," " ," " ,"a" ," " ," " ,"f" ,"f" ,"f" ,"f" ,"f" ,"f" ,"f" ,"f" ,"f" ,"fB","i" ," " ," " ,"E" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"E" ," " ," " },
				{ " " ," " ," " ," " ,"sB"," " ," " ,"rA"," " ," " ," " ," " ," " ,"o" ," " ," " ," " ," " ," " ," " ," " ,"a" ," " ," " ," " ," " ," " ," " ,"hA"," " ," " ," " ," " ," " ,"i" ," " ," " ,"E" ," " ," " ," " ," " ," " ," " ," " ," " ," " ,"E" ," " ," " },
				{ " " ," " ," " ," " ,"s" ," " ," " ,"r" ," " ," " ," " ," " ," " ,"o" ," " ," " ," " ," " ," " ," " ," " ,"a" ," " ," " ," " ," " ," " ," " ,"h" ," " ," " ," " ," " ," " ,"i" ," " ," " ,"E" ,"E" ,"E" ,"E" ,"E" ,"E" ,"E" ,"E" ,"E" ,"E" ,"E" ," " ," " },
				{ " " ," " ," " ," " ,"s" ," " ," " ,"r" ," " ," " ," " ," " ," " ,"o" ," " ," " ," " ," " ," " ," " ," " ,"a" ," " ," " ," " ," " ," " ," " ,"h" ," " ," " ," " ," " ," " ,"i" ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " },
				{ " " ," " ," " ," " ,"sA"," " ," " ,"rB"," " ," " ," " ," " ," " ,"oB"," " ," " ," " ," " ," " ," " ," " ,"aA"," " ," " ," " ," " ," " ," " ,"h" ," " ," " ," " ," " ," " ,"iA"," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " ," " }};
		
		mappa=mappa1;
		
		// RMI
		Registry registry = null;
		try {
			registry = LocateRegistry.createRegistry(12345);
			registry.rebind("GestoreTraffico", gt);
			registry = LocateRegistry.createRegistry(12346);
			registry.rebind("GestoreNotifiche", gn);
			System.out.println("Server online");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		// GRAFICA
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	
	
	public GraficaSistemaCentrale() {
		initialize();
	}


	
	public int getVisualizzazioneGrafica()
	{
		return this.posizioneApp;
	}
	
	public void ridisegna()
	{
		if (posizioneApp==2)
		{
			stampaMatriceStrada(panel.getGraphics());
		}
		else if (posizioneApp==1)
		{
			selectedVia=' ';
			stampaMatrice2D(panel_3.getGraphics(),xPunt,yPunt,proporzione);
		}
	}
	
	
	private void initialize() {
		
		Color grigio = Color.decode("#2A282D");
		Color rosso = Color.decode("#DB514C");
		
		frame = new JFrame();
		frame.getContentPane().setEnabled(false);
		frame.getContentPane().setBackground(grigio);
		frame.setBounds(100, 100, 668, 787);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(null);
		panel_6.setBackground(grigio);
		panel_6.setBounds(0, 33, 668, 140);
		frame.getContentPane().add(panel_6);
		panel_6.setLayout(null);
		
		JPanel opz1 = new JPanel();
		opz1.setBackground(grigio);
		opz1.setBounds(32, 6, 111, 55);
		panel_6.add(opz1);
		opz1.setLayout(null);
		
		JLabel lblMenu = new JLabel("MENU");
		lblMenu.setForeground(new Color(255, 255, 255));
		lblMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblMenu.setBounds(6, 6, 99, 43);
		opz1.add(lblMenu);
		
		JPanel opz2 = new JPanel();
		opz2.setBackground(grigio);
		opz2.setBounds(155, 6, 111, 55);
		panel_6.add(opz2);
		opz2.setLayout(null);
		
		JLabel lblMappa = new JLabel("MAPPA");
		lblMappa.setHorizontalAlignment(SwingConstants.CENTER);
		lblMappa.setForeground(Color.WHITE);
		lblMappa.setBounds(6, 6, 99, 43);
		opz2.add(lblMappa);
		
		JPanel opz3 = new JPanel();
		opz3.setBackground(grigio);
		opz3.setBounds(278, 6, 111, 55);
		panel_6.add(opz3);
		opz3.setLayout(null);
		
		JLabel lblStrada = new JLabel("STRADA");
		lblStrada.setHorizontalAlignment(SwingConstants.CENTER);
		lblStrada.setForeground(Color.WHITE);
		lblStrada.setBounds(6, 6, 99, 43);
		opz3.add(lblStrada);
		
		JPanel opz4 = new JPanel();
		opz4.setBackground(grigio);
		opz4.setBounds(401, 6, 111, 55);
		panel_6.add(opz4);
		opz4.setLayout(null);
		
		JLabel lblElenco = new JLabel("ELENCO");
		lblElenco.setHorizontalAlignment(SwingConstants.CENTER);
		lblElenco.setForeground(Color.WHITE);
		lblElenco.setBounds(6, 6, 99, 43);
		opz4.add(lblElenco);
		
		JPanel opz5 = new JPanel();
		opz5.setBackground(grigio);
		opz5.setBounds(524, 6, 111, 55);
		panel_6.add(opz5);
		opz5.setLayout(null);
		
		JLabel lblInfo = new JLabel("INFO");
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfo.setForeground(Color.WHITE);
		lblInfo.setBounds(6, 6, 99, 43);
		opz5.add(lblInfo);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_7.setBackground(rosso);
		panel_7.setBounds(6, 90, 656, 44);
		panel_6.add(panel_7);
		panel_7.setLayout(null);
		
		JLabel lblTrafficMonitor = new JLabel("TRAFFIC MONITOR");
		lblTrafficMonitor.setFont(new Font("Lucida Grande", Font.BOLD, 24));
		lblTrafficMonitor.setForeground(Color.WHITE);
		lblTrafficMonitor.setHorizontalAlignment(SwingConstants.CENTER);
		lblTrafficMonitor.setBounds(6, 5, 644, 33);
		panel_7.add(lblTrafficMonitor);
		
		panel_6.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if (panel_6.getY()<0)
				{
					closePanel=false;
					inAzione=true;
					Timer timer = new Timer();
			        timer.schedule(new TimerTask() {
			            public void run() {
			            	panel_6.setBounds(0, panel_6.getY()+1, 668, 140);
			            	if (panel_6.getY()>=-2 || closePanel==true)
			            	{
			            		panel_6.setBounds(0, 0, 668, 140);
			            		if (closePanel==true)
			            		{
			            			panel_6.setBounds(0, -84, 668, 140);
			            		}
			            		timer.cancel();
			            		timer.purge();
			            		inAzione=false;
			            		return;
			            	}
			            }
			        }, 1, 1);
				}
			}
			public void mouseExited(MouseEvent e) {
				closePanel=true;
				panel_6.setBounds(0, -84, 668, 140);
			}
		});
		opz1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setApp(0);
			}
		});
		opz2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setApp(1);
			}
		});
		opz3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setApp(2);
			}
		});
		opz4.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setApp(3);
			}
		});
		opz5.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setApp(4);
			}
		});
		
		opz1.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				inSelezione=true;
				if (inAzione==false) {
					panel_6.setBounds(0, 0, 668, 140);
					lblMenu.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
				}
			}
			public void mouseExited(MouseEvent e) {
				panel_6.setBounds(0, 0, 668, 140);
				lblMenu.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
			}
		});
		opz2.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if (inAzione==false) {
					panel_6.setBounds(0, 0, 668, 140);
					lblMappa.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
				}
			}
			public void mouseExited(MouseEvent e) {
				panel_6.setBounds(0, 0, 668, 140);
				lblMappa.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
			}
		});
		opz3.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if (inAzione==false) {
					panel_6.setBounds(0, 0, 668, 140);
					lblStrada.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
				}
			}
			public void mouseExited(MouseEvent e) {
				panel_6.setBounds(0, 0, 668, 140);
				lblStrada.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
			}
		});
		opz4.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if (inAzione==false) {
					panel_6.setBounds(0, 0, 668, 140);
					lblElenco.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
				}
			}
			public void mouseExited(MouseEvent e) {
				panel_6.setBounds(0, 0, 668, 140);
				lblElenco.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
			}
		});
		opz5.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if (inAzione==false) {
					panel_6.setBounds(0, 0, 668, 140);
					lblInfo.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
				}
			}
			public void mouseExited(MouseEvent e) {
				panel_6.setBounds(0, 0, 668, 140);
				lblInfo.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
			}
		});
		
		textField = new JTextField();
		textField.setBackground(Color.WHITE);
		textField.setEditable(false);
		textField.setBounds(689, 6, 431, 26);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Hiragino Sans", Font.PLAIN, 25));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(6, 6, 626, 46);
		
		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		panel_1.setBackground(rosso);
		panel_1.setBounds(6, 60, 650, 107);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setToolTipText("");
		panel_2.setBackground(grigio);
		panel_2.setBounds(6, 5, 638, 96);
		panel_2.setLayout(null);
		panel_2.add(lblNewLabel);
		panel_1.add(panel_2);
		
		JLabel lblNewLabel_5 = new JLabel("");
		lblNewLabel_5.setForeground(Color.WHITE);
		lblNewLabel_5.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblNewLabel_5.setBackground(Color.WHITE);
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setBounds(6, 53, 626, 37);
		panel_2.add(lblNewLabel_5);
		panel_1.setVisible(false);
		panel_6.setBounds(0, -84, 668, 140);
		
		JPanel button = new JPanel();
		button.setBounds(13, 216, 34, 12);
		button.setBackground(Color.WHITE);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_5.setBackground(SystemColor.menu);
		panel_5.setBounds(28, 30, 4, 200);
		
		JLabel lblx = new JLabel("4x4");
		lblx.setHorizontalAlignment(SwingConstants.CENTER);
		lblx.setBounds(0, 6, 61, 16);
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				
				boolean trovato=false;
				String xString=Integer.toString(e.getX());
				String via="";
				String statoVia="";
				float t1;
				float t2;
				int t3;
				int t4;
				int x=e.getX();
				int y=e.getY();
				float x1=0;
				float x2=0;
				
				if (lista.size()>0)
				{
					for (int i=0; i<lista.size(); i++)
					{
						if ((y<=lista.get(i).getY(1) && y>=lista.get(i).getY(2)) ||
							(y>=lista.get(i).getY(1) && y<=lista.get(i).getY(2))) {
							// le coordinate y sono corrette
							// calcolo la coordinata x delle rette estremanti
							t1=y-lista.get(i).getY(2);
							t2=lista.get(i).getY(1)-lista.get(i).getY(2);
							t3=lista.get(i).getX(1)-lista.get(i).getX(2);
							t4=lista.get(i).getX(2);
							x1=(t1/t2)*t3+t4;
							t1=y-lista.get(i).getY(3);
							t2=lista.get(i).getY(4)-lista.get(i).getY(3);
							t3=lista.get(i).getX(4)-lista.get(i).getX(3);
							t4=lista.get(i).getX(3);
							x2=(t1/t2)*t3+t4;
							if ((x<=x1 && x>=x2) || (x>=x1 && x<=x2))
							{
								via=new String();
								via=gn.identificatoreToVia(lista.get(i).getNome());
								if (gt.getTraffico(via)==null)
								{
									statoVia="indefinito";
								}
								else if (gt.getTraffico(via).getTraffico()==Traffico.BLOCCATO)
								{
									statoVia="TRAFFICO BLOCCATO";
								}
								else if (gt.getTraffico(via).getTraffico()==Traffico.ELEVATO)
								{
									statoVia="TRAFFICO ELEVATO";
								}
								else if (gt.getTraffico(via).getTraffico()==Traffico.MEDIO)
								{
									statoVia="traffico MEDIO";
								}
								else if (gt.getTraffico(via).getTraffico()==Traffico.SCORREVOLE)
								{
									statoVia="traffico scorrevole";
								}
								else if (gt.getTraffico(via).getTraffico()==Traffico.ASSENTE)
								{
									statoVia="traffico assente";
								}
								lblNewLabel.setText(via);
								lblNewLabel_5.setText(statoVia);
								trovato=true;
								break;
							}
						}
						if (trovato==true) break;
					}
					if (trovato==false) {
						lblNewLabel.setText("");
						lblNewLabel_5.setText("");
					}
				}	
			}
			public void mouseDragged(MouseEvent e) {
				
				int x=e.getX();
				int y=e.getY();
				
				if (x-xMouse>=30) {
					if (xMap>0)xMap--;
					costruzioneMatriceStrada(xMap, yMap, direzione);
					stampaMatriceStrada(panel.getGraphics());
					xMouse=e.getX();
				}
				if (x-xMouse<=-30) {
					if (xMap<49)xMap++;
					costruzioneMatriceStrada(xMap, yMap, direzione);
					stampaMatriceStrada(panel.getGraphics());
					xMouse=e.getX();
				}
				if (y-yMouse>=30) {
					if (yMap>0)yMap--;
					costruzioneMatriceStrada(xMap, yMap, direzione);
					stampaMatriceStrada(panel.getGraphics());
					yMouse=e.getY();
				}
				if (y-yMouse<=-30) {
					if (yMap<49)yMap++;
					costruzioneMatriceStrada(xMap, yMap, direzione);
					stampaMatriceStrada(panel.getGraphics());
					yMouse=e.getY();
				}
				
				
			}
		});
		panel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				xMouse=e.getX();
				yMouse=e.getY();
			}
		});
		panel.setBackground(Color.WHITE);
		panel.setBounds(6, 170, 650, 500);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		frame.getContentPane().add(panel_3);
		
		JLabel lblNewLabel_3 = new JLabel(" ");
		JLabel lblNewLabel_6 = new JLabel(" ");
		JLabel lblNewLabel_7 = new JLabel(" ");
		
		panel_3.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				if (stampa==true)
				{
					int correttezza=0;
					Traffico traffico=null;
					StatoTraffico statoTraffico=new StatoTraffico();
					Graphics gr=panel_3.getGraphics();
					int xq[]= new int [4];
					int yq[]= new int [4];
					int x=e.getX();
					int y=e.getY();
					int contSame=0;
					int contDiff=0;
					String nome="";
					x=(x-x%(600/proporzione))/(600/proporzione);
					y=(y-y%(600/proporzione))/(600/proporzione);
					x=x+xPunt;
					y=y+yPunt;
					if (selectedVia!= mappa[y][x].charAt(0))
					{
						lblNewLabel_3.setText(" ");
						lblNewLabel_6.setText(" ");
						lblNewLabel_7.setText(" ");
						if (mappa[y][x]!=" ")
						{
							nome=new String();
							nome=nome+mappa[y][x].charAt(0);
							nome=gn.identificatoreToVia(nome);
							lblNewLabel_3.setText(nome);
							statoTraffico=gt.getTraffico(nome);
							if (gt.getTraffico(nome)!=null)
							{
								traffico=gt.getTraffico(nome).getTraffico();
								correttezza=gt.getTraffico(nome).getCorrettezza();
								if (traffico==Traffico.BLOCCATO)lblNewLabel_6.setText("Traffico: BLOCCATO");
								if (traffico==Traffico.ELEVATO)lblNewLabel_6.setText("Traffico: ELEVATO");
								if (traffico==Traffico.MEDIO)lblNewLabel_6.setText("Traffico: MEDIO");
								if (traffico==Traffico.SCORREVOLE)lblNewLabel_6.setText("Traffico: SCORREVOLE");
								if (traffico==Traffico.ASSENTE)lblNewLabel_6.setText("Traffico: ASSENTE");
								lblNewLabel_7.setText("Correttezza: " + Integer.toString(correttezza));
							}
							else
							{
								lblNewLabel_6.setText("Traffico: ???");
								lblNewLabel_7.setText("Correttezza: ???");
							}
							for (int i=xPunt; i<proporzione+xPunt; i++)
							{
								for (int j=yPunt; j<proporzione+yPunt;j++)
								{
									if (mappa[j][i].charAt(0)==mappa[y][x].charAt(0))
									{
										xq[0]=(i-xPunt)*(600/proporzione);
										xq[1]=(i-xPunt+1)*(600/proporzione);
										xq[2]=(i-xPunt+1)*(600/proporzione);
										xq[3]=(i-xPunt)*(600/proporzione);
										yq[0]=(j-yPunt)*(600/proporzione);
								        yq[1]=(j-yPunt)*(600/proporzione);
								        yq[2]=(j-yPunt+1)*(600/proporzione);
								        yq[3]=(j-yPunt+1)*(600/proporzione);
								        if (mappa[j][i].length()>1) {
								        	// punto di partenza della via 
								        	if (mappa[j][i].charAt(1)=='A') 
								        	{
									        	gr.setColor(Color.BLACK);
									        	gr.fillPolygon(xq, yq, 4);
									        	gr.setColor(Color.BLACK);
								    		    gr.drawPolygon(xq, yq, 4);
								        	}
								        	// punto di arrivo della via
								        	else
								        	{
						    		        	if (statoTraffico!=null)
						    		        	{
						    		        		traffico=statoTraffico.getTraffico();
							    		        	if (traffico==Traffico.BLOCCATO)
							    		        	{
							    		        		gr.setColor(new Color(178, 34, 43));
							    		        	}
							    		        	else if (traffico==Traffico.ELEVATO)
							    		        	{
							    		        		gr.setColor(new Color(255, 102, 51));
							    		        	}
							    		        	else if (traffico==Traffico.MEDIO)
							    		        	{
							    		        		gr.setColor(new Color(255, 204, 102));
							    		        	}
							    		        	else if (traffico==Traffico.SCORREVOLE)
							    		        	{
							    		        		gr.setColor(new Color(204, 255, 102));
							    		        	}
							    		        	else if (traffico==Traffico.ASSENTE)
							    		        	{
							    		        		gr.setColor(new Color(102, 255, 102));
							    		        	}
							    		        	gr.fillPolygon(xq, yq, 4);
											        gr.setColor(Color.BLACK);
									    		    gr.drawPolygon(xq, yq, 4);
						    		        	}
						    		        	else
						    		        	{
						    		        		gr.setColor(new Color(173, 216, 230));
						    		        		gr.fillPolygon(xq, yq, 4);
											        gr.setColor(Color.BLACK);
									    		    gr.drawPolygon(xq, yq, 4);
						    		        	}
								        	}
								        }
								        else
								        {
								        	// si verifica se è un incrocio
								        	contSame=0;
								        	contDiff=0;
								        	if (j>0) 
								        	{
								        		if (mappa[j-1][i]!=" ")
								        		{
									        		if (mappa[j-1][i].charAt(0)==mappa[j][i].charAt(0)) contSame++;
									        		if (mappa[j-1][i].charAt(0)!=mappa[j][i].charAt(0)) contDiff++;
								        		}
								        	}
								        	if (j<49) 
								        	{
								        		if (mappa[j+1][i]!=" ")
								        		{
									        		if (mappa[j+1][i].charAt(0)==mappa[j][i].charAt(0)) contSame++;
									        		if (mappa[j+1][i].charAt(0)!=mappa[j][i].charAt(0)) contDiff++;
								        		}
								        	}
								        	if (i>0) 
								        	{
								        		if (mappa[j][i-1]!=" ")
								        		{
									        		if (mappa[j][i-1].charAt(0)==mappa[j][i].charAt(0)) contSame++;
									        		if (mappa[j][i-1].charAt(0)!=mappa[j][i].charAt(0)) contDiff++;
								        		}
								        	}
								        	if (i<49) 
								        	{
								        		if (mappa[j][i+1]!=" ")
								        		{
									        		if (mappa[j][i+1].charAt(0)==mappa[j][i].charAt(0)) contSame++;
									        		if (mappa[j][i+1].charAt(0)!=mappa[j][i].charAt(0)) contDiff++;
								        		}
								        	}
								        	// è un incrocio se same>=2 && diff>=1
								        	if (contSame>=2 && contDiff>=1)
								        	{
										        gr.setColor(Color.GRAY);
						    		        	gr.fillPolygon(xq, yq, 4);
										        gr.setColor(Color.BLACK);
								    		    gr.drawPolygon(xq, yq, 4);
								        	}
								        	else if (statoTraffico!=null)
					    		        	{
					    		        		traffico=statoTraffico.getTraffico();
						    		        	if (traffico==Traffico.BLOCCATO)
						    		        	{
						    		        		gr.setColor(new Color(178, 34, 43));
						    		        	}
						    		        	else if (traffico==Traffico.ELEVATO)
						    		        	{
						    		        		gr.setColor(new Color(255, 102, 51));
						    		        	}
						    		        	else if (traffico==Traffico.MEDIO)
						    		        	{
						    		        		gr.setColor(new Color(255, 204, 102));
						    		        	}
						    		        	else if (traffico==Traffico.SCORREVOLE)
						    		        	{
						    		        		gr.setColor(new Color(204, 255, 102));
						    		        	}
						    		        	else if (traffico==Traffico.ASSENTE)
						    		        	{
						    		        		gr.setColor(new Color(102, 255, 102));
						    		        	}
						    		        	gr.fillPolygon(xq, yq, 4);
										        gr.setColor(Color.BLACK);
								    		    gr.drawPolygon(xq, yq, 4);
					    		        	}
					    		        	else
					    		        	{
					    		        		gr.setColor(new Color(173, 216, 230));
					    		        		gr.fillPolygon(xq, yq, 4);
										        gr.setColor(Color.BLACK);
								    		    gr.drawPolygon(xq, yq, 4);
					    		        	}
								        }
									}
									else if (mappa[j][i].charAt(0)==selectedVia && selectedVia!=' ') {
										xq[0]=(i-xPunt)*(600/proporzione);
										xq[1]=(i-xPunt+1)*(600/proporzione);
										xq[2]=(i-xPunt+1)*(600/proporzione);
										xq[3]=(i-xPunt)*(600/proporzione);
										yq[0]=(j-yPunt)*(600/proporzione);
								        yq[1]=(j-yPunt)*(600/proporzione);
								        yq[2]=(j-yPunt+1)*(600/proporzione);
								        yq[3]=(j-yPunt+1)*(600/proporzione);
							        	gr.setColor(Color.WHITE);
							        	gr.fillPolygon(xq, yq, 4);
						    		    gr.setColor(Color.BLACK);
						    		    gr.drawPolygon(xq, yq, 4);
									}
								}
							}
							selectedVia=mappa[y][x].charAt(0);
						}
						else if (selectedVia!=' ')
						{
							for (int i=xPunt; i<proporzione+xPunt; i++)
							{
								for (int j=yPunt; j<proporzione+yPunt;j++)
								{
									if (mappa[j][i].charAt(0)==selectedVia) {
										xq[0]=(i-xPunt)*(600/proporzione);
										xq[1]=(i-xPunt+1)*(600/proporzione);
										xq[2]=(i-xPunt+1)*(600/proporzione);
										xq[3]=(i-xPunt)*(600/proporzione);
										yq[0]=(j-yPunt)*(600/proporzione);
								        yq[1]=(j-yPunt)*(600/proporzione);
								        yq[2]=(j-yPunt+1)*(600/proporzione);
								        yq[3]=(j-yPunt+1)*(600/proporzione);
								        gr.setColor(Color.WHITE);
						    		    gr.fillPolygon(xq, yq, 4);
						    		    gr.setColor(Color.BLACK);
						    		    gr.drawPolygon(xq, yq, 4);
									}
								}
							}
							selectedVia=' ';
						}
					}
				}
			}
			public void mouseDragged(MouseEvent e) {
				
				int x=e.getX();
				int y=e.getY();
				
				if (x-xMouse>=30) {
					if( xPunt>0)
					{
						xPunt--;
						stampaMatrice2D(panel_3.getGraphics(), xPunt, yPunt, proporzione);
					}
					xMouse=e.getX();
				}
				if (x-xMouse<=-30) {
					if( xPunt+proporzione<50)
					{
						xPunt++;
						stampaMatrice2D(panel_3.getGraphics(), xPunt, yPunt, proporzione);
					}
					xMouse=e.getX();
				}
				if (y-yMouse>=30) {
					if( yPunt>0)
					{
						yPunt--;
						stampaMatrice2D(panel_3.getGraphics(), xPunt, yPunt, proporzione);
					}
					yMouse=e.getY();
				}
				if (y-yMouse<=-30) {
					if( yPunt+proporzione<50)
					{
						yPunt++;
						stampaMatrice2D(panel_3.getGraphics(), xPunt, yPunt, proporzione);
					}
					yMouse=e.getY();
				}
				
				
			}
		});
		panel_3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Graphics gr=panel_3.getGraphics();
				if (stampa==true)
				{
					int x=e.getX();
					int y=e.getY();
					x=(x-x%12)/12;
					y=(y-y%12)/12;
					String via=mappa[y][x];
					if(mappa[y][x]!=" ")
					{
						for (int i =0; i<101; i++) {
							for (int j=0; j<20; j++) {
								strada[i][j]="";
							}
						}
						x=-1;
						// ricerca del punto di partenza A
						for (int i =0; i<50; i++) {
							for (int j=0; j<50; j++) {
								if (mappa[j][i].charAt(0)==via.charAt(0) && mappa[j][i].length()>1)
								{
									if (mappa[j][i].charAt(1)=='A') {
										x=i;
										y=j;
									}
								}
							}
						}
						// ricerca direzione strada
						char direzione=' ';
						if (x>0) 
						{
							if (mappa[y][x-1].charAt(0)==mappa[y][x].charAt(0)) direzione='l';
						}
						if (x<49) 
						{
							if (mappa[y][x+1].charAt(0)==mappa[y][x].charAt(0)) direzione='r';
						}
						if (y<49) 
						{
							if (mappa[y+1][x].charAt(0)==mappa[y][x].charAt(0)) direzione='d';
						}
						if (y>0) 
						{
							if (mappa[y-1][x].charAt(0)==mappa[y][x].charAt(0)) direzione='u';
						}
						// l left
						// r right
						// u up
						// d down
						costruzioneMatriceStrada(x, y, direzione);
					}
				}
			}
			public void mousePressed(MouseEvent e) {
				xMouse=e.getX();
				yMouse=e.getY();
			}
		});
		panel_3.setBackground(new Color(192, 192, 192));
		panel_3.setLayout(null);
		
		panel_3.setVisible(false);
		btnNewButton.setEnabled(false);
		btnNewButton.setForeground(rosso);
		btnNewButton.setBackground(rosso);
		btnNewButton.setBounds(-42, 58, 743, 99);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		
		regolatore.setBackground(rosso);
		regolatore.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		regolatore.setBounds(489, 61, 600, 99);
		frame.getContentPane().add(regolatore);
		regolatore.setLayout(null);
		
		JLabel propLabel = new JLabel("1x");
		
		JPanel plus = new JPanel();
		plus.setEnabled(false);
		
		JPanel sub = new JPanel();
		sub.setForeground(Color.WHITE);
		sub.setBorder(new LineBorder(Color.BLACK));
		sub.setBackground(Color.GRAY);
		sub.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (proporzione!=10)
				{
					if (proporzione==50)
					{
						proporzione=40;
						propLabel.setText("1.3x");
						plus.setEnabled(true);
					}
					else if (proporzione==40)
					{
						proporzione=30;
						propLabel.setText("1.7x");
					}
					else if (proporzione==30)
					{
						proporzione=25;
						propLabel.setText("2x");
					}
					else if (proporzione==25)
					{
						proporzione=20;
						propLabel.setText("2.6x");
					}
					else if (proporzione==20)
					{
						proporzione=15;
						propLabel.setText("3.4x");
					}
					else if (proporzione==15)
					{
						proporzione=10;
						sub.setEnabled(false);
						propLabel.setText("4.2x");
					}
					stampaMatrice2D(panel_3.getGraphics(),xPunt,yPunt,proporzione);
				}
			}
		});
		sub.setBounds(575, 6, 19, 87);
		regolatore.add(sub);
		sub.setLayout(null);
		
		JLabel label = new JLabel("+");
		label.setForeground(Color.WHITE);
		label.setBounds(0, 0, 19, 87);
		sub.add(label);
		label.setBackground(Color.WHITE);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		plus.setBorder(new LineBorder(Color.BLACK));
		plus.setBackground(Color.GRAY);
		plus.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (proporzione!=50)
				{
					if (proporzione==40)
					{
						proporzione=50;
						propLabel.setText("1x");
						plus.setEnabled(false);
					}
					else if (proporzione==30)
					{
						proporzione=40;
						propLabel.setText("1.3x");
					}
					else if (proporzione==25)
					{
						proporzione=30;
						propLabel.setText("1.7x");
					}
					else if (proporzione==20)
					{
						proporzione=25;
						propLabel.setText("2x");
					}
					else if (proporzione==15)
					{
						proporzione=20;
						propLabel.setText("2.6x");
					}
					else if (proporzione==10)
					{
						proporzione=15;
						sub.setEnabled(true);
						propLabel.setText("3.4x");
					}
					while(xPunt+proporzione>50)
					{
						xPunt--;
					}
					while(yPunt+proporzione>50)
					{
						yPunt--;
					}
					stampaMatrice2D(panel_3.getGraphics(),xPunt,yPunt,proporzione);
				}	
			}
		});
		
		plus.setBounds(486, 6, 19, 87);
		regolatore.add(plus);
		plus.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("-");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setBackground(Color.GRAY);
		lblNewLabel_1.setBounds(0, 0, 19, 87);
		plus.add(lblNewLabel_1);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new LineBorder(Color.BLACK));
		panel_8.setBackground(UIManager.getColor("ToolBar.floatingForeground"));
		panel_8.setBounds(486, 6, 108, 87);
		regolatore.add(panel_8);
		panel_8.setLayout(null);
		
		propLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		propLabel.setBounds(20, 6, 69, 76);
		panel_8.add(propLabel);
		propLabel.setForeground(Color.WHITE);
		propLabel.setBackground(Color.WHITE);
		propLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new LineBorder(Color.BLACK));
		panel_9.setBackground(grigio);
		panel_9.setBounds(6, 6, 468, 87);
		regolatore.add(panel_9);
		panel_9.setLayout(null);
		
		lblNewLabel_3.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblNewLabel_3.setForeground(Color.WHITE);
		lblNewLabel_3.setBounds(6, 5, 456, 30);
		panel_9.add(lblNewLabel_3);
		
		lblNewLabel_6.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblNewLabel_6.setForeground(Color.WHITE);
		lblNewLabel_6.setBounds(6, 47, 456, 34);
		panel_9.add(lblNewLabel_6);
		
		lblNewLabel_7.setForeground(Color.WHITE);
		lblNewLabel_7.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblNewLabel_7.setBounds(6, 39, 456, 16);
		panel_9.add(lblNewLabel_7);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (posizioneApp==1)
				{
					stampa=true;
					btnNewButton.setVisible(false);
					regolatore.setVisible(true);
					regolatore.setBounds(35, 55, regolatore.getWidth(), regolatore.getHeight());
					stampaMatrice2D(panel_3.getGraphics(),xPunt,yPunt,proporzione);
				}
				else if (posizioneApp==2)
				{
					btnNewButton.setVisible(false);
					panel_1.setVisible(true);
					xMap=25;
					yMap=25;
					costruzioneMatriceStrada(25,25,'u');
					stampaMatriceStrada(panel.getGraphics());
				}
			}
		});
		btnNewButton.setText("");
		panel.setVisible(false);
		panel_3.setBounds(35, 160, 600, 600);
		panel_4.setBounds(1000, 100, panel_4.getWidth(), panel_4.getHeight());
		btnNewButton.setVisible(false);
		regolatore.setVisible(false);
		panel.setBounds(10, 185, 650, 500);
		panel_1.setBounds(10, 60, 650, 107);
		
		panel_10.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_10.setBackground(rosso);
		
		panel_10.setBounds(120, 520, 421, 430);
		frame.getContentPane().add(panel_10);
		panel_10.setLayout(null);
		
		JTextPane textPane = new JTextPane();
		textPane.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		textPane.setForeground(Color.WHITE);
		textPane.setBackground(grigio);
		textPane.setBounds(27, 28, 365, 269);
		textPane.setVisible(false);
		panel_10.add(textPane);
		
		JPanel panel_11 = new JPanel();
		panel_11.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_11.setBackground(grigio);
		panel_11.setBounds(6, 6, 409, 291);
		panel_10.add(panel_11);
		panel_11.setLayout(null);
		
		JLabel lblNewLabel_4 = new JLabel("BENVENUTO");
		lblNewLabel_4.setBounds(0, 32, 409, 54);
		panel_11.add(lblNewLabel_4);
		
		lblNewLabel_4.setForeground(Color.WHITE);
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setFont(new Font("Lucida Grande", Font.PLAIN, 36));
		Mappa.setBackground(grigio);
		
		Mappa.setBounds(-18, 30, 678, 450);
		frame.getContentPane().add(Mappa);
		Mappa.setLayout(null);
		
		JPanel o1 = new JPanel();
		o1.setBackground(grigio);
		o1.setBorder(new LineBorder(Color.WHITE));
		o1.setBounds(240, 132, 230, 46);
		Mappa.add(o1);
		o1.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("MAPPA");
		lblNewLabel_2.setForeground(Color.WHITE);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(6, 5, 218, 35);
		o1.add(lblNewLabel_2);
		
		JPanel o2 = new JPanel();
		o2.setBackground(grigio);
		o2.setLayout(null);
		o2.setBorder(new LineBorder(Color.WHITE));
		o2.setBounds(240, 190, 230, 46);
		Mappa.add(o2);
		
		JLabel lblStrada_1 = new JLabel("STRADA");
		lblStrada_1.setForeground(Color.WHITE);
		lblStrada_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblStrada_1.setBounds(6, 5, 218, 35);
		o2.add(lblStrada_1);
		
		JPanel o3 = new JPanel();
		o3.setBackground(grigio);
		o3.setLayout(null);
		o3.setBorder(new LineBorder(Color.WHITE));
		o3.setBounds(240, 248, 230, 46);
		Mappa.add(o3);
		
		JLabel lblElenco_1 = new JLabel("ELENCO");
		lblElenco_1.setForeground(Color.WHITE);
		lblElenco_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblElenco_1.setBounds(6, 5, 218, 35);
		o3.add(lblElenco_1);
		
		JPanel o4 = new JPanel();
		o4.setBackground(grigio);
		o4.setLayout(null);
		o4.setBorder(new LineBorder(Color.WHITE));
		o4.setBounds(240, 306, 230, 46);
		Mappa.add(o4);
		
		JLabel lblInfo_1 = new JLabel("INFO");
		lblInfo_1.setForeground(Color.WHITE);
		lblInfo_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfo_1.setBounds(6, 5, 218, 35);
		o4.add(lblInfo_1);
		
		o1.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				lblNewLabel_4.setText("");
				textPane.setVisible(true);
				textPane.setText("Selezione mappa: "
						+ "in questa schermata viene mostrata la mappa stradale della città. "
						+ "Scorrendo con il mouse sulle vie, è possibile visualizzarne il nome,"
						+ " lo stato del traffico la correttezza. Vengono mostrati anche il punto"
						+ " di partenza della via (NERO) e le intersezioni (GRIGIO). E' previsto un"
						+ " pulsante di zoom.");
				o1.setBounds(240-50, 132-10, 230+100, 46+20);
				lblNewLabel_2.setBounds(6, 5, 218+100, 35+20);
				lblNewLabel_2.setFont(new Font("Lucida Grande", Font.PLAIN, 21));
			}
			public void mouseExited(MouseEvent e) {
				textPane.setText("");
				lblNewLabel_4.setText("BENVENUTO");
				textPane.setVisible(false);
				resettaSelezioniMenu(o1,o2,o3,o4);
				lblNewLabel_2.setBounds(6, 5, 218, 35);
				lblNewLabel_2.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
				lblNewLabel_2.setForeground(Color.WHITE);
				lblStrada_1.setForeground(Color.WHITE);
				lblElenco_1.setForeground(Color.WHITE);
				lblInfo_1.setForeground(Color.WHITE);
			}
		});
		o2.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				lblNewLabel_4.setText("");
				textPane.setVisible(true);
				textPane.setText("Selezione strada: in questa schermata viene mostrata la strada"
						+ " in una visuale 3D viruale per una visione realistica della mappa. "
						+ " Lo stato del traffico viene rappresentato dal colore delle vie.");
				o2.setBounds(240-50, 190-10, 230+100, 46+20);
				lblStrada_1.setBounds(6, 5, 218+100, 35+20);
				lblStrada_1.setFont(new Font("Lucida Grande", Font.PLAIN, 21));
			}
			public void mouseExited(MouseEvent e) {
				textPane.setText("");
				lblNewLabel_4.setText("BENVENUTO");
				textPane.setVisible(false);
				resettaSelezioniMenu(o1,o2,o3,o4);
				lblStrada_1.setBounds(6, 5, 218, 35);
				lblStrada_1.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
				lblNewLabel_2.setForeground(Color.WHITE);
				lblStrada_1.setForeground(Color.WHITE);
				lblElenco_1.setForeground(Color.WHITE);
				lblInfo_1.setForeground(Color.WHITE);
			}
		});
		o3.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				lblNewLabel_4.setText("");
				textPane.setVisible(true);
				textPane.setText("Selezione elenco: in questa schermata è prevista la possibilità di "
						+ "effettuare un'interrogazione al sistema per la visualizzazione delle informazioni "
						+ "di una via e delle sue successive. (IN SVILUPPO)");
				o3.setBounds(240-50, 248-10, 230+100, 46+20);
				lblElenco_1.setBounds(6, 5, 218+100, 35+20);
				lblElenco_1.setFont(new Font("Lucida Grande", Font.PLAIN, 21));
			}
			public void mouseExited(MouseEvent e) {
				textPane.setText("");
				lblNewLabel_4.setText("BENVENUTO");
				textPane.setVisible(false);
				resettaSelezioniMenu(o1,o2,o3,o4);
				lblElenco_1.setBounds(6, 5, 218, 35);
				lblElenco_1.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
				lblNewLabel_2.setForeground(Color.WHITE);
				lblStrada_1.setForeground(Color.WHITE);
				lblElenco_1.setForeground(Color.WHITE);
				lblInfo_1.setForeground(Color.WHITE);
			}
		});
		o4.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				lblNewLabel_4.setText("");
				textPane.setVisible(true);
				textPane.setText("Selezione info: informazioni su chi siamo, perché lo siamo"
						+ " e cosa facciamo. (IN SVILUPPO)");
				o4.setBounds(240-50, 306-10, 230+100, 46+20);
				lblInfo_1.setBounds(6, 5, 218+100, 35+20);
				lblInfo_1.setFont(new Font("Lucida Grande", Font.PLAIN, 21));
			}
			public void mouseExited(MouseEvent e) {
				textPane.setText("");
				lblNewLabel_4.setText("BENVENUTO");
				textPane.setVisible(false);
				resettaSelezioniMenu(o1,o2,o3,o4);
				lblInfo_1.setBounds(6, 5, 218, 35);
				lblInfo_1.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
				lblNewLabel_2.setForeground(Color.WHITE);
				lblStrada_1.setForeground(Color.WHITE);
				lblElenco_1.setForeground(Color.WHITE);
				lblInfo_1.setForeground(Color.WHITE);
			}
		});
		
		o1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setApp(1);
			}
		});
		o2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setApp(2);
			}
		});
		o3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setApp(3);
			}
		});
		o4.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setApp(4);
			}
		});
		Mappa.setBounds(-20, 40, 678, 500);
		
		Elenco.setBackground(grigio);
		Elenco.setVisible(false);
		Elenco.setBounds(88, 125, 500, 587);
		frame.getContentPane().add(Elenco);
		Elenco.setLayout(null);
		JLabel infoOrdine = new JLabel("");
		infoOrdine.setVerticalAlignment(SwingConstants.TOP);
		infoOrdine.setFont(new Font("Lucida Grande", Font.BOLD, 10));
		
		infoOrdine.setForeground(Color.LIGHT_GRAY);
		infoOrdine.setHorizontalAlignment(SwingConstants.RIGHT);
		infoOrdine.setBounds(5, 40, 128, 23);
		Elenco.add(infoOrdine);
		
		JPanel ordini = new JPanel();
		ordini.setBorder(new LineBorder(new Color(0, 0, 0)));
		ordini.setBackground(rosso);
		ordini.setBounds(5, 19, 490, 40);
		Elenco.add(ordini);
		ordini.setLayout(null);
		
		JPanel ordine1 = new JPanel();
		JLabel labelOrdine1 = new JLabel("A-Z");
		labelOrdine1.setBackground(Color.LIGHT_GRAY);
		JPanel ordine4 = new JPanel();
		JLabel labelOrdine4 = new JLabel("t-T");
		JPanel ordine2 = new JPanel();
		JLabel labelOrdine2 = new JLabel("Z-A");
		JPanel ordine3 = new JPanel();
		JLabel labelOrdine3 = new JLabel("T-t");
		JPanel indicatore1 = new JPanel();
		indicatore1.setBackground(rosso);
		JPanel indicatore2 = new JPanel();
		indicatore2.setBackground(rosso);
		indicatore1.setVisible(false);
		indicatore2.setVisible(false);
		infoOrdine.setVisible(false);
		
		ordine1.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				labelOrdine1.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
				inSelezioneElenco=true;
				ordine1.setVisible(true);
				ordine2.setVisible(true);
				ordine3.setVisible(true);
				ordine4.setVisible(true);
				ordine4.setBounds(369, 0, 115, 40);
				labelOrdine4.setBounds(6, 0, 103, 40);
				ordine2.setBounds(127, 0, 115, 40);
				labelOrdine2.setBounds(6, 0, 103, 40);
				ordine3.setBounds(248, 0, 115, 40);
				labelOrdine3.setBounds(6, 0, 103, 40);
				ordine1.setBounds(6-6, -4, 115+13, 40+8);
				labelOrdine1.setBounds(6, 0, 103+13, 40+8);
				indicatore1.setVisible(true);
				indicatore2.setVisible(true);
				infoOrdine.setVisible(true);
				infoOrdine.setBounds(ordini.getX()+ordine1.getX(), 14, 125, 25);
				indicatore1.setBounds(ordini.getX()+ordine1.getX(), 64, 128, 5);
				indicatore2.setBounds(ordini.getX()+ordine1.getX(), 8, 128, 5);
			}
			public void mouseExited(MouseEvent e) {
				labelOrdine1.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
				ordine1.setBounds(6, 0, 115, 40);
				labelOrdine1.setBounds(6, 0, 103, 40);
				infoOrdine.setVisible(false);
				indicatore1.setVisible(false);
				indicatore2.setVisible(false);
				inSelezioneElenco=false;
				if (ordineElenco!=-1)
				{
					ordine1.setVisible(false);
					ordine2.setVisible(false);
					ordine3.setVisible(false);
					ordine4.setVisible(false);
				}
				if (ordineElenco==0)
				{
					ordine1.setBounds(6, 0, 478, 40);
					labelOrdine1.setBounds(0, 0, 478, 40);
					ordine1.setVisible(true);
				}
				else if (ordineElenco==1)
				{
					ordine2.setBounds(6, 0, 478, 40);
					labelOrdine2.setBounds(0, 0, 478, 40);
					ordine2.setVisible(true);
				}
				else if (ordineElenco==2)
				{
					ordine3.setBounds(6, 0, 478, 40);
					labelOrdine3.setBounds(0, 0, 478, 40);
					ordine3.setVisible(true);
				}
				else if (ordineElenco==3)
				{
					ordine4.setBounds(6, 0, 478, 40);
					labelOrdine4.setBounds(0, 0, 478, 40);
					ordine4.setVisible(true);
				}
			}
			public void mouseClicked(MouseEvent e) {
				infoOrdine.setText("< A-Z >");
				ordineElenco=0;
			}
		});
		ordine2.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				labelOrdine2.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
				ordine1.setVisible(true);
				ordine2.setVisible(true);
				ordine3.setVisible(true);
				ordine4.setVisible(true);
				ordine4.setBounds(369, 0, 115, 40);
				labelOrdine4.setBounds(6, 0, 103, 40);
				ordine1.setBounds(6, 0, 115, 40);
				labelOrdine1.setBounds(6, 0, 103, 40);
				ordine3.setBounds(248, 0, 115, 40);
				labelOrdine3.setBounds(6, 0, 103, 40);
				ordine2.setBounds(127-7, 0-4, 115+14, 40+8);
				labelOrdine2.setBounds(6, 0, 103+13, 40+8);
				indicatore1.setVisible(true);
				indicatore2.setVisible(true);
				infoOrdine.setVisible(true);
				infoOrdine.setBounds(ordini.getX()+ordine2.getX(), 14, 125, 25);
				indicatore1.setBounds(ordini.getX()+ordine2.getX(), 64, 128, 5);
				indicatore2.setBounds(ordini.getX()+ordine2.getX(), 8, 128, 5);
				inSelezioneElenco=true;
			}
			public void mouseExited(MouseEvent e) {
				ordine2.setBounds(127, 0, 115, 40);
				labelOrdine2.setBounds(6, 0, 103, 40);
				labelOrdine2.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
				indicatore1.setVisible(false);
				indicatore2.setVisible(false);
				infoOrdine.setVisible(false);
				inSelezioneElenco=false;
				if (ordineElenco!=-1)
				{
					ordine1.setVisible(false);
					ordine2.setVisible(false);
					ordine3.setVisible(false);
					ordine4.setVisible(false);
				}
				if (ordineElenco==0)
				{
					ordine1.setBounds(6, 0, 478, 40);
					labelOrdine1.setBounds(0, 0, 478, 40);
					ordine1.setVisible(true);
				}
				else if (ordineElenco==1)
				{
					ordine2.setBounds(6, 0, 478, 40);
					labelOrdine2.setBounds(0, 0, 478, 40);
					ordine2.setVisible(true);
				}
				else if (ordineElenco==2)
				{
					ordine3.setBounds(6, 0, 478, 40);
					labelOrdine3.setBounds(0, 0, 478, 40);
					ordine3.setVisible(true);
				}
				else if (ordineElenco==3)
				{
					ordine4.setBounds(6, 0, 478, 40);
					labelOrdine4.setBounds(0, 0, 478, 40);
					ordine4.setVisible(true);
				}
			}
			public void mouseClicked(MouseEvent e) {
				infoOrdine.setText("< Z-A >");
				ordineElenco=1;
			}
		});
		ordine3.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				labelOrdine3.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
				ordine1.setVisible(true);
				ordine2.setVisible(true);
				ordine3.setVisible(true);
				ordine4.setVisible(true);
				ordine4.setBounds(369, 0, 115, 40);
				labelOrdine4.setBounds(6, 0, 103, 40);
				ordine1.setBounds(6, 0, 115, 40);
				labelOrdine1.setBounds(6, 0, 103, 40);
				ordine2.setBounds(127, 0, 115, 40);
				labelOrdine2.setBounds(6, 0, 103, 40);
				ordine3.setBounds(248-7, -4, 115+14, 40+8);
				labelOrdine3.setBounds(6, 0, 103+13, 40+8);
				indicatore1.setVisible(true);
				indicatore2.setVisible(true);
				infoOrdine.setVisible(true);
				infoOrdine.setBounds(ordini.getX()+ordine3.getX(), 14, 125, 25);
				indicatore1.setBounds(ordini.getX()+ordine3.getX(), 64, 128, 5);
				indicatore2.setBounds(ordini.getX()+ordine3.getX(), 8, 128, 5);
				inSelezioneElenco=true;
			}
			public void mouseExited(MouseEvent e) {
				ordine3.setBounds(248, 0, 115, 40);
				labelOrdine3.setBounds(6, 0, 103, 40);
				labelOrdine3.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
				indicatore1.setVisible(false);
				indicatore2.setVisible(false);
				infoOrdine.setVisible(false);
				inSelezioneElenco=false;
				if (ordineElenco!=-1)
				{
					ordine1.setVisible(false);
					ordine2.setVisible(false);
					ordine3.setVisible(false);
					ordine4.setVisible(false);
				}
				if (ordineElenco==0)
				{
					ordine1.setBounds(6, 0, 478, 40);
					labelOrdine1.setBounds(0, 0, 478, 40);
					ordine1.setVisible(true);
				}
				else if (ordineElenco==1)
				{
					ordine2.setBounds(6, 0, 478, 40);
					labelOrdine2.setBounds(0, 0, 478, 40);
					ordine2.setVisible(true);
				}
				else if (ordineElenco==2)
				{
					ordine3.setBounds(6, 0, 478, 40);
					labelOrdine3.setBounds(0, 0, 478, 40);
					ordine3.setVisible(true);
				}
				else if (ordineElenco==3)
				{
					ordine4.setBounds(6, 0, 478, 40);
					labelOrdine4.setBounds(0, 0, 478, 40);
					ordine4.setVisible(true);
				}
			}
			public void mouseClicked(MouseEvent e) {
				infoOrdine.setText("< T-t >");
				ordineElenco=2;
			}
		});
		ordine4.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				inSelezioneElenco=true;
				ordine1.setVisible(true);
				ordine2.setVisible(true);
				ordine3.setVisible(true);
				ordine4.setVisible(true);
				ordine1.setBounds(6, 0, 115, 40);
				labelOrdine1.setBounds(6, 0, 103, 40);
				ordine2.setBounds(127, 0, 115, 40);
				labelOrdine2.setBounds(6, 0, 103, 40);
				ordine3.setBounds(248, 0, 115, 40);
				labelOrdine3.setBounds(6, 0, 103, 40);
				ordine4.setBounds(369-7, 0-4, 115+13, 40+8);
				labelOrdine4.setBounds(6, 0, 103+13, 40+8);
				indicatore1.setVisible(true);
				indicatore2.setVisible(true);
				infoOrdine.setVisible(true);
				infoOrdine.setBounds(ordini.getX()+ordine4.getX(), 14, 125, 25);
				indicatore1.setBounds(ordini.getX()+ordine4.getX(), 64, 128, 5);
				indicatore2.setBounds(ordini.getX()+ordine4.getX(), 8, 128, 5);
				labelOrdine4.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
			}
			public void mouseExited(MouseEvent e) {
				ordine4.setBounds(369, 0, 115, 40);
				labelOrdine4.setBounds(6, 0, 103, 40);
				labelOrdine4.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
				indicatore1.setVisible(false);
				indicatore2.setVisible(false);
				infoOrdine.setVisible(false);
				inSelezioneElenco=false;
				if (ordineElenco!=-1)
				{
					ordine1.setVisible(false);
					ordine2.setVisible(false);
					ordine3.setVisible(false);
					ordine4.setVisible(false);
				}
				if (ordineElenco==0)
				{
					ordine1.setBounds(6, 0, 478, 40);
					labelOrdine1.setBounds(0, 0, 478, 40);
					ordine1.setVisible(true);
				}
				else if (ordineElenco==1)
				{
					ordine2.setBounds(6, 0, 478, 40);
					labelOrdine2.setBounds(0, 0, 478, 40);
					ordine2.setVisible(true);
				}
				else if (ordineElenco==2)
				{
					ordine3.setBounds(6, 0, 478, 40);
					labelOrdine3.setBounds(0, 0, 478, 40);
					ordine3.setVisible(true);
				}
				else if (ordineElenco==3)
				{
					ordine4.setBounds(6, 0, 478, 40);
					labelOrdine4.setBounds(0, 0, 478, 40);
					ordine4.setVisible(true);
				}
			}
			public void mouseClicked(MouseEvent e) {
				infoOrdine.setText("< t-T >");
				ordineElenco=3;
			}
		});
		
		
		
		ordine1.setBackground(grigio);
		ordine1.setForeground(grigio);
		ordine1.setBorder(new LineBorder(new Color(0, 0, 0)));
		ordine1.setBounds(6, 0, 115, 40);
		ordini.add(ordine1);
		ordine1.setLayout(null);
		
		labelOrdine1.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		labelOrdine1.setForeground(Color.WHITE);
		labelOrdine1.setHorizontalAlignment(SwingConstants.CENTER);
		labelOrdine1.setBounds(6, 0, 103, 40);
		ordine1.add(labelOrdine1);
		
		ordine4.setLayout(null);
		ordine4.setForeground(grigio);
		ordine4.setBorder(new LineBorder(new Color(0, 0, 0)));
		ordine4.setBackground(grigio);
		ordine4.setBounds(369, 0, 115, 40);
		ordini.add(ordine4);
		
		labelOrdine4.setHorizontalAlignment(SwingConstants.CENTER);
		labelOrdine4.setForeground(Color.WHITE);
		labelOrdine4.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		labelOrdine4.setBounds(6, 0, 103, 40);
		ordine4.add(labelOrdine4);
		
		ordine2.setLayout(null);
		ordine2.setForeground(grigio);
		ordine2.setBorder(new LineBorder(new Color(0, 0, 0)));
		ordine2.setBackground(grigio);
		ordine2.setBounds(127, 0, 115, 40);
		ordini.add(ordine2);
		
		labelOrdine2.setHorizontalAlignment(SwingConstants.CENTER);
		labelOrdine2.setForeground(Color.WHITE);
		labelOrdine2.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		labelOrdine2.setBounds(6, 0, 103, 40);
		ordine2.add(labelOrdine2);
		
		ordine3.setLayout(null);
		ordine3.setForeground(grigio);
		ordine3.setBorder(new LineBorder(new Color(0, 0, 0)));
		ordine3.setBackground(grigio);
		ordine3.setBounds(248, 0, 115, 40);
		ordini.add(ordine3);
		
		labelOrdine3.setHorizontalAlignment(SwingConstants.CENTER);
		labelOrdine3.setForeground(Color.WHITE);
		labelOrdine3.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		labelOrdine3.setBounds(6, 0, 103, 40);
		ordine3.add(labelOrdine3);
		
		indicatore1.setBorder(new LineBorder(new Color(0, 0, 0)));
		indicatore1.setBounds(5, 64, 128, 5);
		Elenco.add(indicatore1);
		
		indicatore2.setBorder(new LineBorder(new Color(0, 0, 0)));
		indicatore2.setBounds(5, 8, 128, 5);
		Elenco.add(indicatore2);
		
		JLabel lblInFaseDi = new JLabel("IN FASE DI SVILUPPO");
		lblInFaseDi.setFont(new Font("Lucida Grande", Font.PLAIN, 28));
		lblInFaseDi.setHorizontalAlignment(SwingConstants.CENTER);
		lblInFaseDi.setForeground(new Color(255, 255, 255));
		lblInFaseDi.setBounds(5, 180, 490, 118);
		Elenco.add(lblInFaseDi);
		
	}
	
	
	
	
	
	private int calcoloAltezza(int posy)
	{
		if (posy==0) return 0;
		if (posy==1) return 100;
		if (posy==2) return 177;
		if (posy==3) return 236;
		if (posy==4) return 281;
		if (posy==5) return 316;
		if (posy==6) return 340;
		if (posy==7) return 362;
		if (posy==8) return 378;
		if (posy==9) return 390;
		if (posy==10) return 396;
		if (posy==11) return 406;
		if (posy==12) return 411;
		if (posy==13) return 415;
		if (posy==14) return 418;
		if (posy==15) return 421;
		if (posy==16) return 423;
		if (posy==17) return 425;
		if (posy==18) return 427;
		if (posy==19) return 428;
		return 429;
	}
	
	
	private void costruzioneMatriceStrada(int x, int y, char direzione)
	{
		for (int i =0; i<101; i++) {
			for (int j=0; j<20; j++) {
				strada[i][j]="";
			}
		}
		String sss=" ";
		sss=sss+direzione;
		textField.setText(sss);
		
		for (int i=0;i<101;i++)
		{
			for (int j=0;j<10; j++)
			{
				if (direzione=='l') {
					if (y+i-50>0 && y+i-50<50 && x-j>0)
					{
						strada[i][j]=mappa[y+i-50][x-j];
					}
				}
				if (direzione=='r') {
					if (y+i-50>0 && y+i-50<50 && x+j<50)
					{
						strada[i][j]=mappa[y+i-50][x+j];
					}
				}
				if (direzione=='u') { //ok
					if (x+i-50>0 && x+i-50<50 && y-j>0)
					{
						strada[i][j]=mappa[y-j][x+i-50];
					}
				}
				if (direzione=='d') { //ok
					if (x+i-50>0 && x+i-50<50 && y+j<50)
					{
						strada[i][j]=mappa[y+j][x+i-50];
					}
				}
			}
		}
	}
	
	
	public void stampaMatriceStrada(Graphics g)
	{
		lista=new ArrayList<QuadratoVia>();
		Traffico traffico;
		StatoTraffico statoTraffico;
		String via;
		QuadratoVia tmp;
        int x[]= new int [4];
        int y[]= new int [4];
        int hmin=0;
        int hmax=0;
        int lngmax=0;
        int lngmin=0;
        g.setColor(new Color(173, 216, 230));
        g.drawRect(0, 0, 700, 103);
        g.fillRect(0, 0, 700, 103);
        g.setColor(Color.BLACK);
        for (int i=0; i<101;i++) 
        {
        	for (int j=0; j<10;j++) 
        	{
        		if (true)
        		{
        			hmax=calcoloAltezza(j+1);
        			hmin=calcoloAltezza(j);
        			lngmax=calcoloLunghezzaMezza(hmax);
        			lngmin=calcoloLunghezzaMezza(hmin);
        			if (i<50)
        			{
        				x[0]=350-lngmax*2*(49-i)-lngmax;
        				x[1]=350-lngmin*2*(49-i)-lngmin;
        				x[2]=350-lngmin*2*(49-i)-lngmin*3;
        				x[3]=350-lngmax*2*(49-i)-lngmax*3;
        				y[0]=500-hmax;
	    		        y[1]=500-hmin;
	    		        y[2]=500-hmin;
	    		        y[3]=500-hmax;
        			}
        			else if (i>50)
        			{
        				x[0]=350+lngmax*2*(i-51)+lngmax;
        				x[1]=350+lngmin*2*(i-51)+lngmin;
        				x[2]=350+lngmin*2*(i-51)+lngmin*3;
        				x[3]=350+lngmax*2*(i-51)+lngmax*3;
        				y[0]=500-hmax;
	    		        y[1]=500-hmin;
	    		        y[2]=500-hmin;
	    		        y[3]=500-hmax;
        			}
        			else
        			{
        				x[0]=350+lngmax;
        				x[1]=350+lngmin;
        				x[2]=350-lngmin;
        				x[3]=350-lngmax;
        				y[0]=500-hmax;
	    		        y[1]=500-hmin;
	    		        y[2]=500-hmin;
	    		        y[3]=500-hmax;
        			}
    		        if (strada[i][j]!="" && strada[i][j]!=" ")
    		        {
    		        	if (strada[i][j].length()>1)
    		        	{
    		        		via=new String();
    		        		via=via+strada[i][j].charAt(0);
    		        	}
    		        	else
    		        	{
    		        		via=strada[i][j];
    		        	}
    		        	statoTraffico=gt.getTraffico(gn.identificatoreToVia(via));
    		        	if (statoTraffico!=null)
    		        	{
    		        		traffico=statoTraffico.getTraffico();
	    		        	if (traffico==Traffico.BLOCCATO)
	    		        	{
	    		        		g.setColor(new Color(178, 34, 43));
	    		        	}
	    		        	else if (traffico==Traffico.ELEVATO)
	    		        	{
	    		        		g.setColor(new Color(255, 102, 51));
	    		        	}
	    		        	else if (traffico==Traffico.MEDIO)
	    		        	{
	    		        		g.setColor(new Color(255, 204, 102));
	    		        	}
	    		        	else if (traffico==Traffico.SCORREVOLE)
	    		        	{
	    		        		g.setColor(new Color(204, 255, 102));
	    		        	}
	    		        	else if (traffico==Traffico.ASSENTE)
	    		        	{
	    		        		g.setColor(new Color(102, 255, 102));
	    		        	}
    		        	}
    		        	else
    		        	{
    		        		g.setColor(Color.WHITE);
    		        	}
    		        	tmp= new QuadratoVia();
	    		        tmp.setX(x[0], x[1], x[2], x[3]);
	    		        tmp.setY(y[0], y[1], y[2], y[3]);
	    		        if (strada[i][j].length()==1) tmp.setNome(strada[i][j]);
	    		        else tmp.setNome(strada[i][j].charAt(0));
	    		        tmp.setXYTabella(i, j);
	    		        
	    		        lista.add(tmp);
    		        }
    		        else if (strada[i][j]!=" ") g.setColor(new Color(128, 128, 128));
    		        else g.setColor(new Color(192, 192, 192));
    		        g.fillPolygon(x, y, 4);
        			g.setColor(Color.BLACK);
    		        g.drawPolygon(x, y, 4);
        		}
        	}
        }
	}
	
	
	private void stampaMatrice2D(Graphics gr, int x0, int y0, int dimensioni)
	{
		int xq[]= new int [4];
		int yq[]= new int [4];
		for (int i=x0; i<dimensioni+x0; i++)
		{
			for (int j=y0; j<dimensioni+y0; j++)
			{
				xq[0]=(i-x0)*600/dimensioni;
				xq[1]=(i+1-x0)*600/dimensioni;
				xq[2]=(i+1-x0)*600/dimensioni;
				xq[3]=(i-x0)*600/dimensioni;
				yq[0]=(j-y0)*600/dimensioni;
		        yq[1]=(j-y0)*600/dimensioni;
		        yq[2]=(j+1-y0)*600/dimensioni;
		        yq[3]=(j+1-y0)*600/dimensioni;
				if (mappa[j][i]!=" ") 
				{
					gr.setColor(Color.WHITE);
    		        gr.fillPolygon(xq, yq, 4);
    		        gr.setColor(Color.BLACK);
    		        gr.drawPolygon(xq, yq, 4);
				}
				else
				{
					gr.setColor(new Color(192, 192, 192));
    		        gr.fillPolygon(xq, yq, 4);
				}
			}
		}
	}
	
	
	
	private void resettaSelezioniMenu(JPanel p1, JPanel p2, JPanel p3, JPanel p4)
	{
		p1.setBounds(240, 132, 230, 46);
		p2.setBounds(240, 190, 230, 46);
		p3.setBounds(240, 248, 230, 46);
		p4.setBounds(240, 306, 230, 46);
	}
	
	
	
	private void setApp(int posizione)
	{
		if (posizione!=posizioneApp)
		{
			lista.clear();
			xMap=0;
			yMap=0;
			xMouse=0;
			yMouse=0;
			yPunt=0;
			xPunt=0;
			stampa=false;
			proporzione=50;
			direzione='u';
			ordineElenco=0;
			selectedVia=' ';
			selectedSeg=' ';
			// MENU
			if (posizione==0) {
				posizioneApp=0;
				Mappa.setVisible(true);
				panel.setVisible(false);
				panel_1.setVisible(false);
				panel_3.setVisible(false);
				regolatore.setVisible(false);
				btnNewButton.setVisible(false);
				panel_10.setVisible(true);
				Elenco.setVisible(false);
			}
			// MAPPA
			else if (posizione==1) {
				posizioneApp=1;
				panel.setVisible(false);
				panel_1.setVisible(false);
				Mappa.setVisible(false);
				panel_3.setVisible(true);
				regolatore.setVisible(false);
				btnNewButton.setEnabled(true);
				btnNewButton.setText("CARICA MAPPA");
				btnNewButton.setVisible(true);
				panel_10.setVisible(false);
				Elenco.setVisible(false);
			}
			// STRADA
			else if (posizione==2) {
				posizioneApp=2;
				panel.setVisible(true);
				panel_1.setVisible(false);
				Mappa.setVisible(false);
				panel_3.setVisible(false);
				panel_4.setVisible(false);
				btnNewButton.setEnabled(true);
				btnNewButton.setText("CARICA STRADA");
				btnNewButton.setVisible(true);
				regolatore.setVisible(false);
				panel_10.setVisible(false);
				Elenco.setVisible(false);
			}
			// ELENCO
			else if (posizione==3) {
				posizioneApp=3;
				panel.setVisible(false);
				panel_1.setVisible(false);
				Mappa.setVisible(false);
				panel_3.setVisible(false);
				panel_4.setVisible(false);
				btnNewButton.setEnabled(false);
				btnNewButton.setVisible(false);
				regolatore.setVisible(false);
				panel_10.setVisible(false);
				Elenco.setVisible(true);
			}
			// INFO
			else if (posizione==4) {
				posizioneApp=4;
				panel.setVisible(false);
				panel_1.setVisible(false);
				Mappa.setVisible(false);
				panel_3.setVisible(false);
				regolatore.setVisible(false);
				btnNewButton.setVisible(false);
				panel_10.setVisible(false);
			}
		}
	}
	
	
	
	private int calcoloLunghezzaMezza(int h)
	{
		return (75-h*75/429);
	}
}