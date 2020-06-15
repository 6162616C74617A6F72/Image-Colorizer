package colorizer;

import java.awt.EventQueue;

/**
 * Author: (Alex) Olexandr Matveyev
 * @author Olexandr Matveyev
 */
public class Main 
{
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					GUI gUI = new GUI();
					gUI.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
}
