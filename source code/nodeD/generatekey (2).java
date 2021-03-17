class generatekey extends Thread
{
	nodetab obj;
	
	generatekey(nodetab n)
	{
		super();
		obj=n;
		start();
	}
	
	public void run()
	{
		try
		{
			obj.key="";
			int prev=Mobility.rValue;
			
			while (obj.key.length()<8)
			{
				Thread.sleep(5000);
				int curr=Mobility.rValue;
				
				if (curr>prev)
				obj.key+="1";
				else
				obj.key+="0";
				
				obj.jtakey.setText(obj.key);
				
				prev=curr;
				
			}	
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}