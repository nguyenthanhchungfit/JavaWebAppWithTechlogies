import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
 
public class SlidePanel extends JPanel {
	int width, height;
	
	BufferedImage canvas ;
	Graphics2D hG = null;
	Color bgColor = new Color(30,60,80);
	private int value = 10;
	private JLabel lb;
	private int Maxvalue = 100;
	private boolean isMouseClick = false;
	public void ChangeValue(int Value)
	{
		this.value = Value;
	}
	public SlidePanel(int x,int y,int w,int h)
	{
		 
		canvas = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		width = w; height = h;
		lb = new JLabel(new ImageIcon(canvas));
		lb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lb.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent arg0) {
				int x  = arg0.getX();
				value = (int)(x/(float)width*Maxvalue);
				isMouseClick = true;
				Draw();
				
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				isMouseClick = false;
				ChangeValue( value);
			}
		});
		
		lb.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				int x  = arg0.getX();
				
				if (x>width )x = width;
				if (x<0) x = 0;
				value = (int)(x/(float)width*Maxvalue);
				//ChangeValue( (int)(x/(float)width*Maxvalue));
				Draw();

			}
		});
		hG = canvas.createGraphics();
		hG.setBackground(bgColor);
		hG.clearRect(0, 0, w, h);
		Draw();
		this.setOpaque(false);
		this.add(lb);
		this.setBounds(x, y, w, h);
		
		 
	}
	public void setData(int value)
	{
		if (isMouseClick) return;
		if (value>Maxvalue)value =Maxvalue;
		this.value = value;
		Draw();
		
	}
	public void setMaxValue(int value)
	{
		Maxvalue = value;
	}
	public int getValue()
	{
		 return this.value;
	}
	private void Draw()
	{
		hG.clearRect(0, 0, width, height);
		hG.setColor(new Color(166,218,232));
		hG.fillRect(1,1,(int)(value/(float)Maxvalue*(width-2)) , height-2);
		lb.repaint();
	}
}
