import java.net.*;
import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.swing.*;
import java.util.*;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import java.security.*;

class nodeAServer implements Runnable
{
	nodeA myparent;
	FileInputStream fis;
	int ch;
	String nodeB="",nodeD="";
	String lastkey="";
	
	nodeAServer(nodeA parent)
	{
		myparent=parent;
		readaddr();
		Thread t=new Thread(this);
		t.start();
	}
	
	public void run()
	{
		try
		{
			ServerSocket ss=new ServerSocket(1500);
			
			while(true)
			{
				Socket soc=ss.accept();
			
				DataInputStream din=new DataInputStream(soc.getInputStream());
				String req=din.readUTF();
				String reqarray[]=req.split("&");
				
							
				
				if (reqarray[0].equals("ROUTEREQUEST"))
				{
					if (reqarray[2].equals("nodeA"))
					{
						String reply="nodeA";
						DataOutputStream dout=new DataOutputStream(soc.getOutputStream());
						dout.writeUTF(reply);
						dout.close();
						
					}
					else
					if (reqarray[2].equals("nodeB"))
					{
						Socket soc1=new Socket(nodeB,2500);
						DataOutputStream dout1=new DataOutputStream(soc1.getOutputStream());
						dout1.writeUTF(req);
						
						DataInputStream din1=new DataInputStream(soc1.getInputStream());
						String route1=din1.readUTF();
												
						route1="nodeA@"+route1;
						
												
						DataOutputStream dout=new DataOutputStream(soc.getOutputStream());
						dout.writeUTF(route1);
						
						dout.close();
						
					}
					else
					if (reqarray[2].equals("nodeD"))
					{
						Socket soc1=new Socket(nodeD,4500);
						DataOutputStream dout1=new DataOutputStream(soc1.getOutputStream());
						dout1.writeUTF(req);
						
						DataInputStream din1=new DataInputStream(soc1.getInputStream());
						String route1=din1.readUTF();
												
						route1="nodeA@"+route1;
						
												
						DataOutputStream dout=new DataOutputStream(soc.getOutputStream());
						dout.writeUTF(route1);
						
						dout.close();
						
					}
				}
				else
				if (reqarray[0].equals("MESSAGE"))
				{
					
					System.out.println(reqarray[1]);
					Vector v=new Vector();
					StringTokenizer st=new StringTokenizer(reqarray[1],"@") ;
					while(st.hasMoreTokens())
					v.add(st.nextToken());
					
					if (v.size()==3)
					{
						
						
						if (v.get(2).toString().equals("nodeA"))
						{
							msgtab.jta.append("\n\nRoute: "+reqarray[1]);
							msgtab.jta.append("\nDate & Time: "+new java.util.Date().toString());
							msgtab.jta.append("\nSecret Key: "+reqarray[2]);
							msgtab.jta.append("\nEnc Msg: "+reqarray[3]);
							String msg=decrypt(reqarray[2],reqarray[3]);
							msgtab.jta.append("\nMessage: "+msg);
						
							String reply="MESSAGEREPLY";
							DataOutputStream dout=new DataOutputStream(soc.getOutputStream());
							dout.writeUTF(reply);
							dout.close();
							
						}
						else
						if (v.get(2).toString().equals("nodeB"))
						{
							Socket soc1=new Socket(nodeB,2500);
							DataOutputStream dout1=new DataOutputStream(soc1.getOutputStream());
							dout1.writeUTF(req);
							
							DataInputStream din1=new DataInputStream(soc1.getInputStream());
							String msg=din1.readUTF();
							
							DataOutputStream dout=new DataOutputStream(soc.getOutputStream());
							dout.writeUTF(msg);
							
							dout1.close();
							din1.close();
							dout.close();
							soc1.close();
							
							
						}
						else
						if (v.get(2).toString().equals("nodeD"))
						{
							Socket soc1=new Socket(nodeD,4500);
							DataOutputStream dout1=new DataOutputStream(soc1.getOutputStream());
							dout1.writeUTF(req);
							
							DataInputStream din1=new DataInputStream(soc1.getInputStream());
							String msg=din1.readUTF();
							
							DataOutputStream dout=new DataOutputStream(soc.getOutputStream());
							dout.writeUTF(msg);
							
							dout1.close();
							din1.close();
							dout.close();
							soc1.close();
							
							
						}
						
					}
					else
					{
						msgtab.jta.append("\n\nRoute: "+reqarray[1]);
						msgtab.jta.append("\nDate & Time: "+new java.util.Date().toString());
						msgtab.jta.append("\nSecret Key: "+reqarray[2]);
						msgtab.jta.append("\nEnc Msg: "+reqarray[3]);
						String msg=decrypt(reqarray[2],reqarray[3]);
						msgtab.jta.append("\nMessage: "+msg);
						
						String reply="MESSAGEREPLY";
						DataOutputStream dout=new DataOutputStream(soc.getOutputStream());
						dout.writeUTF(reply);
						dout.close();
					}
					
					
					
					
				}
				din.close();
				soc.close();
			}	
			
		}
		catch(Exception e)
		{
			System.out.println("nodeBServer: "+e);
			e.printStackTrace();
		}
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
	
	
	String encrypt(String msg)
	{
		
		try
		{
			while(true)
			{
			
			String key=JOptionPane.showInputDialog(myparent,"Enter the Encryption Key(8 chars)  ");
			key.trim();
			if (key.length()==8)
			{
			
				Cipher cipher= Cipher.getInstance("DES");
				SecretKeySpec spec= new SecretKeySpec(key.getBytes(), "DES");
				cipher.init(Cipher.ENCRYPT_MODE, spec);
				byte messageArray[]=msg.getBytes("UTF8");
				messageArray= cipher.doFinal(messageArray,0,messageArray.length);
				msg=new BASE64Encoder().encode(messageArray);
				break;
			}
			else
			continue;
			}
			
			
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return msg;
	}
	
	String decrypt(String key,String msg)
	{
		boolean invalid=true;
		
	
		
		try
		{
			String rkey="";
			char keyarr[]=key.toCharArray();
			for (int i=0;i<keyarr.length;i++)
			if (keyarr[i]=='0')
			rkey+="1";
			else
			rkey+="0";
			
			rkey.trim();
			
			Cipher cipher= Cipher.getInstance("DES");
			SecretKeySpec spec= new SecretKeySpec(rkey.getBytes(), "DES");
			cipher.init(Cipher.DECRYPT_MODE, spec);
			byte messageArray[]=new BASE64Decoder().decodeBuffer(msg);
			messageArray= cipher.doFinal(messageArray);
			msg="";
			for (int i=0;i<messageArray.length;i++)
			msg+=(char) messageArray[i];
			msg.trim();
			
			
			
		}
		catch(Exception e)
		{
			System.out.println(e);
			JOptionPane.showMessageDialog(null,"Invalid Key Entered");
		}
		
		
	
		return msg;
	}
}