import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.net.*;
import java.io.*;
import java.util.*;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import java.security.*;
import javax.swing.border.*;

class nodetab extends JPanel implements ActionListener,ItemListener
{
	JRadioButton mode,jr1,jr2;
	JTextArea jta,jtakey;
	JButton jbsend,jbclear,jbkey;
	JComboBox jcdest;
	static boolean status=false;
	nodeA myparent;
	FileInputStream fis;
	int ch;
	String nodeB="",nodeD="";
	JPanel jproute;
	Vector route=new Vector();
	String key="";
	String rkey="";
	
	
	nodetab(nodeA parent)
	{
		super();
		
		myparent=parent;
		new nodeAServer(myparent);
			
			
		mode=new JRadioButton("NORMAL NODE");
		mode.setForeground(Color.red);
		
		JLabel jldest=new JLabel("Select Destination");
		jcdest=new JComboBox();
		jcdest.addItem("--SELECT--");
		jcdest.addItem("nodeB");
		jcdest.addItem("nodeC");
		jcdest.addItem("nodeD");
		
		jproute=new JPanel();
		jproute.setLayout(null);
		jproute.setBorder(new TitledBorder( new EtchedBorder(), "Select Route"));
		
		jr1=new JRadioButton();
		jr2=new JRadioButton();
		
		jproute.add(jr1);
		jproute.add(jr2);
				
		jr1.setBounds(10,20,200,20);
		jr2.setBounds(10,50,200,20);
		
		ButtonGroup bg=new ButtonGroup();
		bg.add(jr1);
		bg.add(jr2);
		
			
		jbkey=new JButton("Generate Key");
		
			
		JLabel jlmsg=new JLabel("Type the Message:");
		jta=new JTextArea();
		JScrollPane jsp=new JScrollPane(jta);
		
		JLabel jlkey=new JLabel("Key:");
		jtakey=new JTextArea();
		jtakey.setEditable(false);
		JScrollPane jspkey=new JScrollPane(jtakey);
		
		
		jbsend=new JButton("Send");
		jbclear=new JButton("Clear");
		
		this.setLayout(null);
		
		this.add(mode);
		this.add(jldest);
		this.add(jcdest);
		this.add(jproute);
		this.add(jbkey);
		this.add(jlmsg);
		this.add(jsp);
		this.add(jlkey);
		this.add(jspkey);
		this.add(jbsend);
		this.add(jbclear);
		
		mode.setBounds(10,10,150,20);
		jldest.setBounds(10,50,150,20);
		jcdest.setBounds(170,50,100,20);
		jproute.setBounds(10,80,250,80);
		jlmsg.setBounds(10,170,200,20);
		jsp.setBounds(10,200,200,50);
		jlkey.setBounds(230,170,150,20);
		jspkey.setBounds(230,200,150,50);
		jbkey.setBounds(230,260,150,30);
		jbsend.setBounds(10,260,200,30);
		jbclear.setBounds(10,300,200,30);
		
		this.setSize(250,400);
		mode.addItemListener(this);
		jbsend.addActionListener(this);
		jbclear.addActionListener(this);
		jcdest.addActionListener(this);
		jbkey.addActionListener(this);
		
		readaddr();
	}
	
	void readaddr()
	{
		try
		{
			fis=new FileInputStream("nodeB.txt");
			while((ch=fis.read())!=-1)
			nodeB+=(char)ch;
			nodeB.trim();
			System.out.println("Address of node B is: "+nodeB);
			
			fis=new FileInputStream("nodeD.txt");
			while((ch=fis.read())!=-1)
			nodeD+=(char)ch;
			nodeD.trim();
			System.out.println("Address of node D is: "+nodeD);
			
			
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void itemStateChanged(ItemEvent ie)
	{
		if (mode.isSelected())
		{
			mode.setText("HACKER MODE")	;
			mode.setForeground(Color.blue);
			status=true;
		}
		else
		{
			mode.setText("NORMAL MODE")	;
			mode.setForeground(Color.red);
			status=false;
		}
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource()==jbkey)
		{
			new generatekey(this);	
		}
		else
		if (ae.getSource()==jcdest)
		{
			String dest=(String)jcdest.getSelectedItem();
			dest.trim();
			
			if (!dest.equals("--SELECT--"))
			{
				jr1.setText("");
				jr2.setText("");
				
				JOptionPane.showMessageDialog(myparent,"Route request started..");
				route=sendroutereq(dest);
				
				if (route.size()==2)
				{
					jr1.setText(route.get(0).toString());
					jr2.setText(route.get(1).toString());
				}
				else
				{
					jr1.setText(route.get(0).toString());
				}
				
			}
			
		}
		else
		if (ae.getSource()==jbsend)
		{
			rkey="";
			String msg=jta.getText().trim();
			String dest=(String)jcdest.getSelectedItem();
			dest.trim();
			String selroute="";
			
			if(route.size()!=0)
			{
			
			if (jr1.isSelected())
			selroute=route.get(0).toString().trim();
			else
			if (jr2.isSelected())
			selroute=route.get(1).toString().trim();
			
			key.trim();
			}
			else
			JOptionPane.showMessageDialog(null,"Select Route.");
			
			if (!dest.equals("--SELECT--") && !msg.equals("") && !selroute.equals("") && key.length()==8)
			{
				try
				{
					
					sendmsg(selroute,msg,dest);
							
							
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
					
										
			}
			else
			{
				if (dest.equals("--SELECT--"))
				JOptionPane.showMessageDialog(null,"Select Destination First");
				else
				if(msg.equals(""))
				JOptionPane.showMessageDialog(null,"Cannot send empty message. ");
				else
				if(selroute.equals(""))
				JOptionPane.showMessageDialog(null,"Select the Route. ");
				else
				JOptionPane.showMessageDialog(null,"No Key Found for encryption!");
			}	
		}
		else
		if (ae.getSource()==jbclear)
		{
			jta.setText("");
		}
		
		
	}
	
	
	
	Vector sendroutereq(String destination)
	{
		Vector result=new Vector();
		String route="";
		try
		{
			
			if (destination.equals("nodeB"))
			{
				String finalmsg="ROUTEREQUEST&nodeA&"+destination;
				System.out.println(finalmsg);
				
				Socket s=new Socket(nodeB,2500);
				DataOutputStream dout=new DataOutputStream(s.getOutputStream());
				dout.writeUTF(finalmsg);
				
				DataInputStream din=new DataInputStream(s.getInputStream());
				route=din.readUTF();
							
				result.add("nodeA@"+route);
				
				dout.close();
				din.close();
				s.close();			
				
			}
			else
			if (destination.equals("nodeD"))
			{
				String finalmsg="ROUTEREQUEST&nodeA&"+destination;
				System.out.println(finalmsg);
				
				Socket s=new Socket(nodeD,4500);
				DataOutputStream dout=new DataOutputStream(s.getOutputStream());
				dout.writeUTF(finalmsg);
				
				DataInputStream din=new DataInputStream(s.getInputStream());
				route=din.readUTF();
							
				result.add("nodeA@"+route);
				
				dout.close();
				din.close();
				s.close();
					
			}
			else
			if (destination.equals("nodeC"))
			{
				String finalmsg="ROUTEREQUEST&nodeA&"+destination;
				System.out.println(finalmsg);
				
					
				
				Socket s1=new Socket(nodeB,2500);
				DataOutputStream dout1=new DataOutputStream(s1.getOutputStream());
				dout1.writeUTF(finalmsg);
				
				DataInputStream din1=new DataInputStream(s1.getInputStream());
				route=din1.readUTF();
								
				result.add("nodeA@"+route);
				
				dout1.close();
				din1.close();
				s1.close();
				
				Socket s2=new Socket(nodeD,4500);
				DataOutputStream dout2=new DataOutputStream(s2.getOutputStream());
				dout2.writeUTF(finalmsg);
				
				DataInputStream din2=new DataInputStream(s2.getInputStream());
				route=din2.readUTF();
								
				result.add("nodeA@"+route);
				
				dout2.close();
				din2.close();
				s2.close();
				
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return result;
	}
	
	
	void sendmsg(String route1,String msg,String destination)
	{
		try
		{
			
			char keyarr[]=key.toCharArray();
			for (int i=0;i<keyarr.length;i++)
			if (keyarr[i]=='0')
			rkey+="1";
			else
			rkey+="0";
			
			JOptionPane.showMessageDialog(null,"Final Key: "+rkey);
			
			String finalmsg="MESSAGE&"+route1+"&"+key+"&"+encrypt(msg);	
			
			Vector v=new Vector();
		//	String routearray[]=route.split("^");
			StringTokenizer st=new StringTokenizer(route1,"@") ;
			while(st.hasMoreTokens())
			v.add(st.nextToken());
			
			if (v.get(1).toString().equals("nodeB"))
			{
				Socket s=new Socket(nodeB,2500);
				DataOutputStream dout=new DataOutputStream(s.getOutputStream());
				dout.writeUTF(finalmsg);
				
				DataInputStream din=new DataInputStream(s.getInputStream());
				String reply=din.readUTF();
				
				
							
				JOptionPane.showMessageDialog(null,"Message Successfully sent to "+destination);
			}
			else
			{
				Socket s=new Socket(nodeD,4500);
				DataOutputStream dout=new DataOutputStream(s.getOutputStream());
				dout.writeUTF(finalmsg);
				
				DataInputStream din=new DataInputStream(s.getInputStream());
				String reply=din.readUTF();
						
							
				JOptionPane.showMessageDialog(null,"Message Successfully sent to "+destination);
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	

	
	
	String encrypt(String req)
	{
		String msg="";
				
		try
		{
			Cipher cipher= Cipher.getInstance("DES");
			SecretKeySpec spec= new SecretKeySpec(rkey.getBytes(), "DES");
			cipher.init(Cipher.ENCRYPT_MODE, spec);
			byte messageArray[]=req.getBytes("UTF8");
			messageArray= cipher.doFinal(messageArray);
			msg=new BASE64Encoder().encode(messageArray);
			
			
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return msg;
	}
	
	
	String decrypt(String sender,String msg)
	{
		boolean invalid=true;
		
		do
		{
		
		try
		{
			String key=JOptionPane.showInputDialog(myparent,"Enter the Key of node "+sender);
			key.trim();
			Cipher cipher= Cipher.getInstance("DES");
			SecretKeySpec spec= new SecretKeySpec(key.getBytes(), "DES");
			cipher.init(Cipher.DECRYPT_MODE, spec);
			byte messageArray[]=new BASE64Decoder().decodeBuffer(msg);
			messageArray= cipher.doFinal(messageArray);
			
			msg="";
			for (int i=0;i<messageArray.length;i++)
			msg+=(char) messageArray[i];
			
			msg.trim();
			
			invalid=false;
			
		}
		catch(Exception e)
		{
			System.out.println(e);
			invalid=true;
		}
		
		}
		while(invalid);
		return msg;
	}
	
	
}