 

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;
 

public class ScrollnewStyle extends BasicScrollBarUI {
	Color Background =new Color(44,88,113);
	public ScrollnewStyle()
	{
	 
	}
	public ScrollnewStyle(Color m)
	{
		
		Background = m;
	}
	
	 @Override
	    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
	        g.setColor(Color.white);
	        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        ((Graphics2D) g).fillRoundRect(r.x+3,r.y,10,r.height,15,15);
	    }
	  @Override
	    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
		  g.setColor(  Background );
	        ((Graphics2D) g).fillRect(r.x,r.y,r.width,r.height);
	    }
		
  @Override
   protected JButton createDecreaseButton(int orientation) {
       return b;
   }
  private JButton b = new JButton() {
       @Override
       public Dimension getPreferredSize() {
           return new Dimension(0, 0);
       }
   };

   @Override
   protected JButton createIncreaseButton(int orientation) {
       return b;
   }
}