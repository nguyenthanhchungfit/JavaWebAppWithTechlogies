import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import javax.swing.JPanel;
 
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
 
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JList;

public class MainForm {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm window = new MainForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	TipMessage Tipmsg =  new TipMessage();
	public MainForm() {
		initialize();
	}
	Clip clip;
	AudioDevice audio;
	Point lasPoint = null;
	boolean isPlay = false;
	Color bcPlay = new Color(251,97,31);
	int kbps =128;
	int audioLength=0;
	JPlayer player;
	boolean isPlaying =false;
	boolean isStop = true;
	Thread audioThread = null;
	URL url;
	private JLabel bPlay ;
	private JLabel lTitle ;
	private JLabel lblX;
	private JLabel bVolumeMute ;
	SlidePanel slBoard;
	private boolean isChangeTime= false;
	private int  mPostion = 0;
	
	private float CurrenVolume = 0.0f;
	private boolean isMute = false;
	private boolean isReplay = false;
	
	private JPanel PlayPanel ;
	private JLabel label;
	private JLabel bGuiPlay;

	private JList list_1;
	private JPanel MainPaint ;
	private JScrollPane PanelList;
	private mouseclick mouseEventClick ;
	private final String defTitle = "Get Link ZingMp3";
	private int isGui = 0;
	private int CurentPlay =-1;
	private  boolean bPlayIsMouseActive = false;
	private BufferedImage bffImgVolume ;
	private BufferedImage bffImgReplay ;
	private BufferedImage bffImgCDgui ;
	private BufferedImage bffImgDownload ;
	private  Clipboard clipb ;
	private JLabel bGet ;
	
	public void load_Resource()
	{
		try {
			clipb = Toolkit.getDefaultToolkit().getSystemClipboard();
			
			bffImgVolume = ImageIO.read(this.getClass().getResourceAsStream("Audio.png"));
			bffImgReplay = ImageIO.read(this.getClass().getResourceAsStream("Replay.png"));
			bffImgCDgui = ImageIO.read(this.getClass().getResourceAsStream("CDGui.png")); 
			bffImgDownload=  ImageIO.read(this.getClass().getResourceAsStream("iconDownload.png")); 
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	public void reloadPlayer()
	{
		try {
		 
			player = new JPlayer(url);
		} catch (JavaLayerException  e) {
			
			e.printStackTrace();
		}
		 
	}
	FloatControl control = null;
	Mixer mixer ;
	private JLabel bAutoReplay;
	private JTextField inputLink;
	private void getVolumeControl()
	{
		for(Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
			 mixer = AudioSystem.getMixer(mixerInfo);
			try {
				mixer.open();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
			
			for(Line.Info lineInfo : mixer.getTargetLineInfo()) {
				try {
					Line line = mixer.getLine(lineInfo);
					if (!lineInfo.toString().contains("Master"))
					{
				 
					line.open();
					if(line.isControlSupported(FloatControl.Type.VOLUME)) {
					
						control = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
						
					 
					}
					}
				} catch (LineUnavailableException e) {
					
				}
			}
		}
	}

	private void change_gui(int isGui)
	{
		if (isGui==2)
		{
			this.isGui =2;
			MainPaint.setVisible( false  );
			PlayPanel.setVisible( true);
			PlayPanel.add(lblX);
			PlayPanel.add(lTitle);
			r2StartEffectp=200;
			if (listSong==null) return;
			if(listSong.size() <= CurentPlay ) CurentPlay = 0;
			if (listSong.size()<= 0) return;
			Song song = listSong.get(CurentPlay);
			lTitle.setText(song.name);
			
		}
		else {
			r1StartEffectp= 200;
			this.isGui =1;
			MainPaint.setVisible(  true);
			PlayPanel.setVisible( false);
			MainPaint.add(lblX);
			MainPaint.add(lTitle);
			lTitle.setText(defTitle);
		}
	}
	private final int width = 250; 
	private void initialize() {
		mouseEventClick = new mouseclick();
		load_Resource();
		try {
			url =null;
			audio = FactoryRegistry.systemRegistry().createAudioDevice();
			
			//int Size =  getFileSize(url);
			//audioLength =(Size-2024*8)/414;//((Size-1024)/(kbps/8));

			Decoder a = new Decoder();
	 
		
			
			// or player.play
		} catch ( JavaLayerException    e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		frame = new JFrame();
	
		frame.setBounds(100, 100, width, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setUndecorated(true);
		lTitle = new JLabel(defTitle);
		lTitle.setBounds(5, 5, 269, 21);
		lTitle.setFont(new Font("Tahoma",0,14));
		lTitle.setForeground(Color.WHITE);
		frame.setOpacity(0.9f);
		
		lblX = new JLabel("x");
		lblX.setBounds(width-11, 5, 11, 11);
		lblX.setForeground(Color.white);
		lblX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		lTitle.addMouseListener(mouseEventClick);
		lblX.addMouseListener(mouseEventClick);
	 
		lTitle.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				Point Current = arg0.getPoint();
				if (lasPoint==null) lasPoint = Current;
				Point framp = frame.getLocation();
				Point newPoint = new Point(framp.x+Current .x - lasPoint.x,framp.y+Current .y - lasPoint.y);
				frame.setLocation(newPoint);
				
			}
		});
		
		 create_paine_0();
		 create_panel_1();
		 change_gui(1);
	 
		// PlayPanel
		
		//ArrayList<Song> listsong =gnGetLinkMp3.getAlbum("http://mp3.zing.vn/album/Nhung-Bai-Hat-Hay-Nhat-Cua-Le-Quyen-Le-Quyen/ZWZ9D090.html");
	//	 System.out.println(listsong.size());
	}
	private ArrayList<Song> listSong = null;
	class listNoteData extends AbstractListModel
	{

			@Override
			public int getSize() {
				if (listSong==null) return 0;
				return listSong.size();
			}
			@Override
			public Object getElementAt(int index) {
				Song song = listSong.get(index);
				if ( song.name ==null) return "";
				String name = song.name +" - "+ song.artist;
			 
				return name ;
			}
		
		
	}
	class listCellRender extends DefaultListCellRenderer
	{
	  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	  {
		 
		 
		  JLabel  label = (JLabel) super.getListCellRendererComponent(
	        list, value, index, isSelected, cellHasFocus);
		   
			label.setText((String)value);	  
			label.setPreferredSize(new Dimension(200,30));
	        label.setHorizontalTextPosition(SwingConstants.RIGHT);
	        label.setBorder(BorderFactory.createEmptyBorder());
	      
	        if (isSelected)
	        {
	        	label.setOpaque(true);
	        	label.setBackground( Color.decode("#ffa040"));
	        }
	        else
	        {
	        	setOpaque(false); 
	        }
   
	    return label;  
	  }
	}
	private JLabel bDownload;
	private boolean cpOpen = false;
	private int r1StartEffectp = 200;
	private int r2StartEffectp = 200;
	private JPanel Sil1_panel;
	private void create_paine_0()
	{

		MainPaint = new JPanel();
		MainPaint.setBounds(0, 0, width, 400);
		frame.getContentPane().add(MainPaint);
		MainPaint.setLayout(null);
		MainPaint.add(lblX);
		MainPaint.add(lTitle);
		inputLink = new JTextField();
		inputLink.setBounds(10-r1StartEffectp, 29, width-70, 28);
		MainPaint.add(inputLink);
		inputLink.setForeground(Color.white);
		inputLink.setBackground(new Color(35,71,91));
		inputLink.setBorder(BorderFactory.createEmptyBorder());
		
		
		
		bGuiPlay = new JLabel(new ImageIcon(bffImgCDgui));
		bGuiPlay.setBounds(width-50, -2, 30, 30);
		bGuiPlay.setForeground(Color.white);
		bGuiPlay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		bGuiPlay.addMouseListener(mouseEventClick);
		MainPaint.add(bGuiPlay);
		label = new JLabel("");
		
		bGet = new JLabel("GET",JLabel.CENTER){
			
			@Override
			public void paint(Graphics g)
			{
				
				((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(bGet.getBackground());
				g.fillRoundRect(0, 0, this.getWidth(),this.getHeight(),10,10);
				super.paint(g);
			}
		};
		bGet.setBackground(bcPlay);
		bGet.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		bGet.setBounds(width-54+r1StartEffectp, 28, 48, 28);
		MainPaint.add(bGet);
		bGet.setForeground(Color.white);
		bGet.addMouseListener(mouseEventClick);
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 10, 10);
		panel.setOpaque(false);
		PanelList = new JScrollPane(panel);
		PanelList.setBounds(10, 60, width-10, 330);
		PanelList.setBorder(BorderFactory.createEmptyBorder());
		PanelList.setBackground(new Color(44,88,113));
		PanelList.getVerticalScrollBar().setUI(new ScrollnewStyle());
		PanelList.setOpaque(false);
		PanelList.getViewport().setOpaque(false);
		MainPaint.add(PanelList);
		MainPaint.setBackground(new Color(44,88,113));
		panel.setLayout(new BorderLayout(0, 0));
		list_1 = new JList();
		panel.add(list_1);
	 
		list_1.setModel(new listNoteData() );
		list_1.setBounds(0, 0, 1, 1);
		list_1.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        JList list = (JList)evt.getSource();
		        if (evt.getClickCount() == 2) {
		        	if (cpOpen) return;
		        			
		            int index = list.getSelectedIndex();
		            
		            if (CurentPlay== index) 
		            	{
			            	change_gui( 2);
			            	return;
		            	}
		            CurentPlay=index;
		            cpOpen = true;
					if(listSong.size() <= CurentPlay ) CurentPlay = 0;
					if (listSong.size()<= 0) return;
					isStop=true;
					Song song = listSong.get(CurentPlay);
					try {
							change_gui( 2);
							cpOpen = false;
							lTitle.setText(song.name);
							if (player !=null) player.close();
							url = new URL(song.sourceList);
							int Size =  getFileSize(url);
							audioLength =(Size-2024*8)/414;
							Thread.sleep(50);
							player = new JPlayer(url);
							player.play();
							isPlaying = true;
							isStop=false;
							 player.setAutoReplay(isReplay);
							 CurrentLyric = null;
						
					} catch (JavaLayerException | IOException | InterruptedException e) {
					 
						e.printStackTrace();
					}
		       
		        }
		    }
		});
		 //http://mp3.zing.vn/album/Bang-Xep-Hang-Bai-Hat-Au-My-Tuan-24-2017/ZOZDE8C7.html
		list_1.setCellRenderer(new listCellRender());
		list_1.setOpaque(false);
		list_1.setForeground(Color.white);
		
		label.setBounds(5, 70, 280, width-5);
		label.setText("<HTML><div style=\"color:white;\"><div style=\"font-size:28px\">Get Link MP3</div><div style=\"font-size:12px; text-align:right;\">Giấy Nháp</div><div style=\"font-size:12px;text-align:right;\">Version 1.0.3</div></div></HTML>");
		MainPaint.add(label);
	}
	
	private JLabel LyricDraw;
	
	private BufferedImage lyricCanvas = new BufferedImage(width,100,BufferedImage.TYPE_INT_ARGB); 
	private Graphics2D gLyric2D = lyricCanvas.createGraphics();
	private Lyric.lyrictime CurrentLyric = null;
	private void create_panel_1()
	{
		PlayPanel = new JPanel() ;
		PlayPanel.setBackground(new Color(44,88,113));
		frame.getContentPane().add(PlayPanel);
		PlayPanel.setLayout(null);
		PlayPanel.add(lblX);
		PlayPanel.add(lTitle);
		PlayPanel.setBounds(0, 0, width, 400);
		bPlay = new JLabel()
		{
			@Override
			public void paintChildren(Graphics g)
			{
				g.setColor(new Color(63,126,160));
				Graphics2D g2 = (Graphics2D)g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				  
				g2.setColor(new Color(34,69,89));
				
				g2.fillArc(10, 10, 160, 160, 0, 360);
				g2.setColor(new Color(166,218,232));
				g2.fillArc( 12, 12, 156, 156, 90,(int)(mPostion/(float)audioLength*360.0) );
			
				//g2.setColor(bcPlay);
			
			//	g2.fillArc(21, 21, 140, 140, 300, 80);
				
			//	g2.setColor(new Color(252,145,98));
				g2.setColor(Color.WHITE);
				g2.fillArc(20, 20, 140, 140, 0, 380);
				g2.setColor(new Color(34,69,89));
				if (bPlayIsMouseActive)  g2.setColor(bcPlay);
				if (!isPlaying)
				{
					int []Pointx = new int[]{50,113,113};
					int []Pointy = new int[]{90,130,50};
					g2.fillPolygon(Pointx, Pointy, 3);;
				}
				else
				{
				g2.fillRect(65, 55, 15,70);
				g2.fillRect(100, 55, 15,70);
				}
			
				super.paintChildren(g);
			}
		};
		bPlay.setOpaque(false);
		bPlay.setBounds(0, 0, 180, 180);
	
		PlayPanel.add(bPlay);
		bPlay.addMouseListener(mouseEventClick);
		
		
		LyricDraw = new JLabel(new ImageIcon(lyricCanvas));
		LyricDraw.setBounds(0, 240, width,100);
		gLyric2D.setBackground(new Color(44,88,113));
		PlayPanel.add(LyricDraw);
		
		Sil1_panel = new JPanel();
		Sil1_panel.setBackground(new Color(34,69,89));
		Sil1_panel.setBounds(0, 305, width, 95);
		PlayPanel.add(Sil1_panel);
		Sil1_panel.setLayout(null);
		
		SlidePanel slBoard = new SlidePanel(0,0,width-120,5){
			@Override
			public void ChangeValue(int value)
			{
				
				 super.ChangeValue(value);
				 control.setValue(value/200.0f);
				 isMute = false;
				 bVolumeMute.repaint();
				
			}
		};
		slBoard.setBounds(107, 22, width-120, 20);
		slBoard.setMaxValue(200);
		Sil1_panel.add(slBoard);
	
		
		getVolumeControl();
		CurrenVolume = control.getValue();
		slBoard.setData((int)(CurrenVolume*200));
		
		bVolumeMute = new JLabel(new ImageIcon(bffImgVolume)){
			@Override
			public void paint(Graphics g)
			{
				super.paint(g);
				
				if ( isMute)
				{
					g.setColor(Color.red);
					 ((Graphics2D)g).setStroke(new BasicStroke(3));
					g.drawLine(10, 10, 20, 20);
				}
				 
				
			}
		};
		bVolumeMute.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		bVolumeMute.setBounds(77, 15, 30, 30);
		Sil1_panel.add(bVolumeMute);
		
		bAutoReplay = new JLabel(new ImageIcon(bffImgReplay));
		bAutoReplay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		bAutoReplay.setBounds(48, 15, 30, 30);
		Sil1_panel.add(bAutoReplay);
		bVolumeMute.addMouseListener(mouseEventClick);
		bAutoReplay.addMouseListener(mouseEventClick);
		////
		bDownload = new JLabel(new ImageIcon(bffImgDownload));
		Sil1_panel.add(bDownload);
		bDownload.setBounds(10, 15, 30, 30);
		bDownload.addMouseListener(mouseEventClick);
		bDownload.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		
		
		bPlay.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				Point point = arg0.getPoint();
				if (isChangeTime)
				{
					double angle =  pointToAngle(point,new Point(180/2,180/2));
					int value = (int)(angle/360.0*audioLength);
					CurrenVolume = value;
					mPostion = value;
					bPlay.repaint();
				}
				super.mouseMoved(arg0);

			}
			public void mouseMoved(MouseEvent arg0)
			{
				if (bPlay == arg0.getSource())
				{
					Point point = arg0.getPoint();
					double pos = pointtodist(point,new Point(180/2,180/2));
					if (pos < 90) 	bPlay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					else 	bPlay.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					if (pos<70)
					{
						if (!bPlayIsMouseActive)
						{
							bPlayIsMouseActive =true;
							
							bPlay.repaint();
						}
					}
					else  
					{
						if (bPlayIsMouseActive)
						{
							bPlayIsMouseActive = false;
							bPlay.repaint();
						}
						
					}
						
					
				}
				
			}
		});
		 
		Thread t = new Thread(){
			@Override
			public void run()
			
			{ 
				while(true)
				{
					 
					try {
						Thread.sleep(20);
						if (r1StartEffectp>0)
						{
							r1StartEffectp-=r1StartEffectp/5+1;
							 
							bGet.setLocation(width-54+r1StartEffectp, 28);
							inputLink.setLocation(10-r1StartEffectp, 29);
							label.setLocation(5, r1StartEffectp+100);
							PanelList.setLocation(5, r1StartEffectp+60);
						}
						else r1StartEffectp=0;
						if (r2StartEffectp>0)
						{
							r2StartEffectp-=r2StartEffectp/5+1;
							bPlay.setLocation(width/2-90+r2StartEffectp,90);
							Sil1_panel.setLocation(0, 340+r2StartEffectp);
						}
						else r2StartEffectp=0;
						if (isChangeTime||player==null) continue;
						
						if (  player.playerStatus == JPlayer.PLAYING)
						{
							int milisecond = player.getMiliseconds();
							if (CurrentLyric!=null)
							{
								
								mPostion = player.getPosition();
								gLyric2D.clearRect(0, 0, width, 100);
								
								Font gfont = new Font("Tahoma",0,15);
								FontMetrics metrics = gLyric2D.getFontMetrics(gfont);
								int curent = (int) ((milisecond - CurrentLyric.time)/(float)(CurrentLyric.next-CurrentLyric.time)*CurrentLyric.Text.length());
								int [] width2 = metrics.getWidths();
								int total = 0;
								int tlWidth =  metrics.stringWidth(CurrentLyric.Text)/2;
								int height = 0;
								for (int i=0;i<CurrentLyric.Text.length();i++)
								{
									if (i<curent) gLyric2D.setColor(new Color(251,97,31));
									else gLyric2D.setColor(Color.WHITE);
									String str = CurrentLyric.Text.substring(i, i+1);
									
									gLyric2D.drawString(str, width/2 - tlWidth+total , 40+height);
									total+= metrics.stringWidth(str);
						
								
								}
								LyricDraw.repaint();
							}
							else 
							{
								Song song = listSong.get(CurentPlay);
								if (song.objLyric!=null)
								CurrentLyric = song.objLyric.getTextOn(milisecond);
							}
							if (CurrentLyric!=null)
							if ((CurrentLyric.next<=milisecond&&CurrentLyric.next>CurrentLyric.time)||milisecond<=CurrentLyric.time)
							{
								Song song = listSong.get(CurentPlay);
								if (song.objLyric!=null)
								CurrentLyric = song.objLyric.getTextOn(milisecond);
							}
							
						}
						
						if (player.playerStatus == JPlayer.FINISHED&&mPostion>0&&!isStop)
						{
							CurrentLyric = null;
							isPlaying = false;
							player.close();
							mPostion =0 ;
							CurentPlay++;
							if(listSong.size() <= CurentPlay ) CurentPlay = 0;
							if (listSong.size()<= 0) return;
							Song song = listSong.get(CurentPlay);
							lTitle.setText(song.name);
							url = new URL(song.sourceList);
							int Size =  getFileSize(url);
							audioLength =(Size-2024*8)/414;
							Thread.sleep(50);
							player.lastURL = url;
							player.rePlay(0);
							isPlaying = true;
							player.setAutoReplay(isReplay);
						}
						bPlay.repaint();
					} catch (InterruptedException | JavaLayerException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		};
		t.start();
		
	}
	
	
	
	
	
	
	private int getFileSize(URL url) {
	    HttpURLConnection conn = null;
	    try {
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("HEAD");
	        conn.getInputStream();
	        return conn.getContentLength();
	    } catch (IOException e) {
	        return -1;
	    } finally {
	        conn.disconnect();
	    }
	}
	class mouseclick  extends MouseAdapter
	{

	 

		@Override
		public void mouseEntered(MouseEvent arg0) {
			if (!isReplay)
			 if (bAutoReplay == arg0.getSource())
			 {
				 bAutoReplay.setBackground(new Color(0,0,0,80));
				 bAutoReplay.setOpaque(true);
			 }
			 else if (bGet== arg0.getSource())
			 {
				 bGet.setBackground(Color.red);
				 bGet.repaint();
			 }
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			if (!isReplay)
			 if (bAutoReplay == arg0.getSource())
			 {
				 bAutoReplay.setBackground(new Color(0,0,0,0));
			   bAutoReplay.setOpaque(false);
			 }
			 else if (bGet== arg0.getSource())
			 {
				 bGet.setBackground(bcPlay);
				 bGet.repaint();
			 }
			 if (bPlay==arg0.getSource())
			 {
		
						bPlayIsMouseActive = false;
						bPlay.repaint();
	
			 }
		}
		

		@Override
		public void mousePressed(MouseEvent arg0) {
			 if (arg0.getSource() == bPlay)
					
			 {
				Point point = arg0.getPoint();
				double pos = pointtodist(point,new Point(180/2,180/2));
				if (pos>=70)
				{
					isChangeTime = true;
					
					double angle =  pointToAngle(point,new Point(180/2,180/2));
					int value = (int)(angle/360.0*audioLength);
					mPostion = value;
					bPlay.repaint();
				}
				
			 }else if (lTitle==arg0.getSource())
			 {
				 
				 lasPoint = arg0.getPoint();
			 }
			
			  
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			 
			 if (arg0.getSource() == bPlay)
				
			 {
				Point point = arg0.getPoint();
				double pos = pointtodist(point,new Point(180/2,180/2));
			  
		 
				 if (pos<70&&isChangeTime!=true)
				 {
						
					if (isPlaying) 
					{
							player.pause();
						
					}
					else
					{
						
						if (isStop) reloadPlayer();
						try {
							 
							player.setAutoReplay(isReplay);
							player.play();
						} catch (JavaLayerException e) {
							
						e.printStackTrace();
						}
						isStop = false;
					}
					isPlaying=!isPlaying;
		 
					bPlay.repaint();
				 }
				 else
				 {
					isChangeTime = false;
					double angle =  pointToAngle(point,new Point(180/2,180/2));
					int value = (int)(angle/360.0*audioLength);
					mPostion = value;
					if (isStop) reloadPlayer();
					try {
						player.setAutoReplay(isReplay);
					
						player.play();
						player.seek(value);;
						isPlaying = true;  
					} catch (JavaLayerException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					isStop = false;
				
				 
					bPlay.repaint();
				 }
				 
			 
			 }else if (lblX == arg0.getSource()) 
			 {
				 if (isGui==2) change_gui(1);
				 else
				 System.exit(1);
				 
			 }
			 else if (bVolumeMute == arg0.getSource())
			 {
				 isMute = !isMute;
				 if (isMute)
				 control.setValue(0.0f);
				 else
					 control.setValue(CurrenVolume);
				
				 bVolumeMute.repaint();
			 }
			 else if (bAutoReplay == arg0.getSource())
			 {
				 isReplay= !isReplay;
				 if (isReplay)
				 {
					 bAutoReplay.setBackground(new Color(0,0,0,80));
					 bAutoReplay.setOpaque(true);
				 }
				 else
				 {
					 bAutoReplay.setBackground(new Color(0,0,0,0));
					 bAutoReplay.setOpaque(false);
					
				 }
				 if (player!=null)
				 player.setAutoReplay(isReplay);
			 }
			 else if (bGet== arg0.getSource())
			 {
				 String url = inputLink.getText();
				 label.setVisible(true);
				 list_1.setVisible(false);
				 label.setText("<HTML><div style=\"color:white;font-size:16px\">Đang Thực hiện yêu cầu. Xin chờ</div></div></HTML>");
				 new Thread(){
					 public void run()
					 {
				
				 int check = gnGetLinkMp3.checkLink(url);
				 if (check!=0)
				 {
					 if (check==1)
					 {
						 if (listSong==null) listSong = new ArrayList<Song>();
						 listSong.clear();
						 Song song = gnGetLinkMp3.getSong(url);
						 listSong.add(song);
					 }
					 else
					 {
						 listSong = gnGetLinkMp3.getAlbum(url);
						 
					 }
					 
				 }
				 list_1.setVisible(true);
				 label.setVisible(false);
				 list_1.setModel(new listNoteData() );
				 if (listSong==null || listSong.size()<=0 )
				 Tipmsg.Start("Thông báo", " Danh sách trống. Kiểm tra lại link liên kết và đường truyền của bạn");
				 else
				 Tipmsg.Start("Hoàn thành", " Tìm thấy "+listSong.size()+" bài hát");
				 
					 }}
				 .start();
				 
				//lTitle.setText(defTitle);
				
			 }
			 else if (bGuiPlay== arg0.getSource())
			 {
				 change_gui( 2);
			 }
			 else if (bDownload == arg0.getSource())
			 {
				 if (player==null) return;
				 if (listSong.size()<0) return;
				 if (CurentPlay <0 || CurentPlay>listSong.size() ) return;
				 int kbps = 320;
				 String link  = null;
				
				 Song song = listSong.get(CurentPlay);
				 
					 if (song.apiLink[0] != null)
					 {
						 kbps=320;
						 link = song.apiLink[0];
					 }
					 else if (song.apiLink[1] != null)
					 {
						 kbps=128;
						 link = song.apiLink[1];
					 }
				 
				 if (link==null|| (link.length()<3))
				 {
					 kbps=128;
					 link = song.sourceList;
				 }
				 Transferable tText = new StringSelection(link);
				 clipb.setContents(tText, null);
				 Tipmsg.Start("Hoàn thành", " Đã copy link tải nhạc "+kbps+" kbps vào clipboard");
				 
			 }
				 
		}
	}
	protected double pointtodist(Point p1,Point p2)
	{
		return p1.distance(p2);
	}
	protected double pointToAngle(Point p1,Point p2)
	{
		Point newp = new Point(p1.x-p2.x,p1.y-p2.y);
		
		double angle = Math.acos(-newp.y/newp.distance(0,0));
		if (newp.x>0) angle = 2*Math.PI-angle;
		return Math.toDegrees(angle) ;
	}
}
