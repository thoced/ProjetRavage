package coreGUISwing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import coreNet.INetManagerCallBack;
import coreNet.NetAddUnity;
import coreNet.NetHeader;
import coreNet.NetHeader.TYPE;
import coreNet.NetHello;
import coreNet.NetManager;
import coreNet.NetMoveUnity;
import coreNet.NetSynchronize;

import java.awt.Color;

import javax.swing.UIManager;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import org.jsfml.graphics.Texture;
import org.newdawn.slick.Graphics;

import java.awt.BufferCapabilities;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;

public class menuDialogRavage extends JDialog implements ActionListener, INetManagerCallBack
{
	private JTextField tNickName;
	private JTextField tIp;
	private JButton bLaunch;
	private JTextArea editorConsole;
	private JButton bHelloWorld;
	
	private StringBuilder builderString;
	private JComboBox cFlags;
	
	private NetManager netManager;
	
	private menuThread mt;
	
	public menuDialogRavage(JFrame frame,String titre,boolean modal,NetManager netmanager)
	{
		super(frame,titre,modal);
		setAlwaysOnTop(true);
		setAutoRequestFocus(false);
		
		netManager = netmanager;
	
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setBackground(Color.GRAY);
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		// ajout dans le managernet
		NetManager.attachCallBack(this);
		
		this.setSize(640,480);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Projet Ravage !!!");
		lblNewLabel.setBounds(10, 24, 186, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Nick Name:\r\n");
		lblNewLabel_1.setBounds(10, 49, 133, 14);
		getContentPane().add(lblNewLabel_1);
		
		tNickName = new JTextField();
		tNickName.setBounds(201, 46, 153, 20);
		getContentPane().add(tNickName);
		tNickName.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBounds(10, 91, 46, 14);
		getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Ip adresse du joueur adverse:");
		lblNewLabel_3.setBounds(10, 74, 186, 14);
		getContentPane().add(lblNewLabel_3);
		
		tIp = new JTextField();
		tIp.setBounds(201, 71, 153, 20);
		getContentPane().add(tIp);
		tIp.setColumns(10);
		
		editorConsole = new JTextArea();
		editorConsole.setLineWrap(true);
		editorConsole.setDoubleBuffered(true);
		editorConsole.setAlignmentX(Component.LEFT_ALIGNMENT);
		JScrollPane scrollPane = new JScrollPane(editorConsole);
		scrollPane.setBounds(10, 222, 604, 166);
		getContentPane().add(scrollPane);
		
		bHelloWorld = new JButton("Hello !!!");
		bHelloWorld.setBorderPainted(false);
		bHelloWorld.setBorder(UIManager.getBorder("Button.border"));
		bHelloWorld.setForeground(Color.BLACK);
		bHelloWorld.setBackground(Color.GRAY);
		bHelloWorld.setActionCommand("HELLO");
		bHelloWorld.addActionListener(this);
		bHelloWorld.setBounds(10, 142, 89, 23);
		getContentPane().add(bHelloWorld);
		
		bLaunch = new JButton("Lancer le Jeu !!!");
		bLaunch.setBackground(Color.GRAY);
		bLaunch.addActionListener(this);
		bLaunch.setActionCommand("LAUNCH");
		bLaunch.setBounds(481, 408, 133, 23);
		getContentPane().add(bLaunch);
		
		JLabel lblNewLabel_4 = new JLabel("Flags");
		lblNewLabel_4.setBounds(364, 49, 68, 14);
		getContentPane().add(lblNewLabel_4);
		
		cFlags = new JComboBox();
		cFlags.setModel(new DefaultComboBoxModel(new String[] {"NORD-EST", "NORD-OUEST", "SUD-EST", "SUD-OUEST"}));
		cFlags.setBounds(404, 46, 168, 20);
		getContentPane().add(cFlags);
		
		builderString = new StringBuilder();
		
		 mt = new menuThread(netManager);
		 mt.start();

	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		
		mt.interrupt();
		
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		
		
		if(e.getActionCommand().equals("LAUNCH"))
		{
			// on positionne le flag de départ
			switch(cFlags.getSelectedIndex())
			{
			case 0 :  	NetManager.setPosxStartFlag(340);
						NetManager.setPosyStartFlag(33);
						break;
			case 1 :	NetManager.setPosxStartFlag(20);
						NetManager.setPosyStartFlag(12);
						break;
			case 2 : 	NetManager.setPosxStartFlag(363);
						NetManager.setPosyStartFlag(214);
						break;
			case 3 : 	NetManager.setPosxStartFlag(27);
						NetManager.setPosyStartFlag(218);
						break;
						
			
			}
			
			this.setVisible(false);
			this.dispose();
		}
		if(e.getActionCommand().equals("HELLO"))
		{
			try
			{
				// on configure l'adresse ip du joueur adverse
				NetManager.configureIp(tIp.getText());
				// création du message Hello
				NetHello hello = new NetHello();
				hello.setMessage("Bien le bonjour, je serai positionné en " + cFlags.getSelectedItem().toString() );
				hello.setNickName(tNickName.getText());
				NetHeader header = new NetHeader();
				header.setMessage(hello);
				header.setTypeMessage(TYPE.HELLO);
				// emission
				NetManager.SendMessage(header);
					
			} catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e1.getMessage());
			}
			
		}
		
	}
	@Override
	public void onHello(NetHello hello)
	{
		// un message hello arrive, je vais l'afficher dans la console
		
		builderString.append(hello.getNickName() + " dit : Hello !!! : " + hello.getMessage());
		editorConsole.setText(builderString.toString() + System.getProperty("line.separator"));
		
	}
	@Override
	public void onAddUnity(NetAddUnity unity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMoveUnity(NetMoveUnity unity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSynchronize(NetSynchronize sync) {
		// TODO Auto-generated method stub
		
	}
}
