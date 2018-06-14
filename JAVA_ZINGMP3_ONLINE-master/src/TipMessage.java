 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
 

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

 

public class TipMessage {

	private JFrame frame;
	public static class scr
	{
		public  int Width=1024;
		public  int Height=768;
	}
	public   scr Desktop = new scr();
	public TipMessage() {
		initialize();
	}
	JLabel lblNewLabel,lblNewLabel_1 ;
	public boolean isExiting = false;
	public boolean isStart = false;	
	private int PosY = 0;
	private int PosX = 0;
	private void initialize() {
		
		Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
		try
		{
			Desktop.Width = (int)screenSize.getWidth();
			Desktop.Height = (int)screenSize.getHeight();
		}
		catch (NullPointerException e)
		{
			initialize();
		}
		frame = new JFrame();
		
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.setBounds(100, 100, 300, 100);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		 
		frame.getContentPane().setBackground(new Color(35,71,91));
		frame.getContentPane().setLayout(null);
		frame.setOpacity(0.5f);
		lblNewLabel = new JLabel("Thông báo");
		lblNewLabel.setBounds(10, 0, 294, 33);
		lblNewLabel.setFont(new Font("tahoma",1,12));
		lblNewLabel.setForeground(Color.WHITE);
		frame.getContentPane().add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setBounds(20, 27, 320, 62);
		lblNewLabel_1.setFont(new Font("tahoma",0,12));
		lblNewLabel_1.setForeground(Color.WHITE);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblX = new JLabel("X", SwingConstants.CENTER);
		lblX.setBounds(280,1, 16, 16);
		lblX.setForeground(Color.WHITE);
		frame.getContentPane().add(lblX);;
		lblX.setBackground(new Color(35,71,91));
		lblX.setOpaque(true);
		lblX.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseEntered(MouseEvent arg0) {
	    		lblX.setBackground(Color.RED);
	    		frame.setOpacity(1.0f);
	    	}
	    	@Override
	    	public void mouseExited(MouseEvent e) {
	    		lblX.setBackground(new Color(35,71,91));
	    	}
	    	@Override
	    	public void mouseReleased(MouseEvent e) {
	    		frame.setVisible(false);
	    	}
	    });
		
		frame.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseEntered(MouseEvent arg0) {
	    		if (!isExiting)
	    		frame.setOpacity(1.0f);
	    	}
	    	@Override
	    	public void mouseExited(MouseEvent e) {
	    		if (!isExiting)
	    		frame.setOpacity(0.5f);
	    	}
	    	@Override
	    	public void mouseReleased(MouseEvent e) {
	    		DoExit();
	    	}
	    });
		Thread th = new Thread()
		{
			@Override
			public void run()
			{
				
				while(true)
				{
					
					
					try {
						Thread.sleep(50);
						if (isStart)
						if (PosY>0) 
						{
						 
							PosY-=(PosY/10);
							frame.setLocation( Desktop.Width-5-300, Desktop.Height-120-PosY);
							
						}
						else PosY=0;
						if (isExiting)
						{
							if (PosX<400) 
							{
								PosX+=PosX+1;
								 frame.setLocation( Desktop.Width-5-300+PosX, Desktop.Height-120-PosY);
							}
							else 
							{
								PosX=410;
								isExiting = false;
								frame.setVisible(false);
							}
							
							
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
	
		};
		 th.start();
		 
	}
	Timer timerel;
	public void Start(String Title,String Text)
	{
	 
	 
		 PosY  =  Desktop.Height-130;
		isExiting = false;
		isStart = true;
		SetText(Title,Text);
		frame.setVisible(true);
		if (timerel==null)
		{
			
		timerel = new Timer(5000, new ActionListener() {
 			@Override
 			
 			public void actionPerformed(ActionEvent arg0) {
 			
 				DoExit();
 				timerel.stop();
 			}
          });
		timerel.start();
		}
		else
		{
			if (timerel.isRunning())
				timerel.stop();
			timerel.restart();
		}
		
	}
	void SetText(String Title,String Text)
	{
		lblNewLabel.setText(Title);
		lblNewLabel_1.setText("<HTML>"+Text+"</HTML>");
	}
	void DoExit()
	{
		if (timerel!=null)
		if (timerel.isRunning())
			timerel.stop();
		PosX=0;
		timerel.stop();
		isStart = false;
		isExiting = true;

	}
}
