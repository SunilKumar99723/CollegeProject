import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


class msgtab extends JPanel
{
	static JTextArea jta;
	
	msgtab()
	{
		super();
		this.setLayout(null);
		jta=new JTextArea();
		JScrollPane jsp=new JScrollPane(jta);
		
		this.add(jsp);
		jsp.setBounds(10,10,350,300);
		
		this.setSize(250,400);
	}
}