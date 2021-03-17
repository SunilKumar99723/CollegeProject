import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

class nodeC extends JFrame implements ActionListener
{
	JButton jbexit;
	static JProgressBar jp;
	
	nodeC()
	{
		super("NODE C");
		createwin();
		this.setSize(500,550);
		this.setVisible(true);
		
		new Mobility();
		
	}
	
	void createwin()
	{
		Container cp=this.getContentPane();
		cp.setLayout(null);
		
		JLabel jlstrength=new JLabel("Signal Strength");
		jp=new JProgressBar();
		jp.setStringPainted(true);
		
		JLabel jltitle=new JLabel("NODE C",JLabel.CENTER);
		jltitle.setFont(new Font("Dialog",Font.BOLD,18));
		
		JTabbedPane jtp=new JTabbedPane();
		jtp.addTab("Select Mode",new nodetab(this));
		jtp.addTab("Received Msg",new msgtab());
		
		jbexit=new JButton("Exit");
		
		cp.add(jltitle);
		cp.add(jlstrength);
		cp.add(jp);
		cp.add(jtp);
		cp.add(jbexit);
		
		jltitle.setBounds(0,0,200,30);
		jlstrength.setBounds(200,40,100,20);
		jp.setBounds(310,40,100,20);
		jtp.setBounds(10,70,400,400);
		jbexit.setBounds(100,480,80,30);
		jbexit.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		System.exit(0);
	}
	
	public static void main(String args[])
	{
		new nodeC();
	}
}