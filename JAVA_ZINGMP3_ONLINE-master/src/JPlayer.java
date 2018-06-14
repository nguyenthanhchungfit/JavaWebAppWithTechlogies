


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

public class JPlayer
{
  private int frame = 0;
  private Bitstream bitstream;
  private Decoder decoder;
  private AudioDevice audio;
  private boolean closed = false;
  private boolean complete = false;
  private int lastPosition = 0;
  public final static int NOTSTARTED = 0;
  public final static int PLAYING = 1;
  public final static int PAUSED = 2;
  public final static int FINISHED = 3;
  public int playerStatus = NOTSTARTED;
  private boolean isAutoReplay = false;
  public void setAutoReplay(boolean pl)
  {
	  isAutoReplay = pl;
  }
  URL lastURL = null; 
  public JPlayer(URL paramInputStream)
    throws JavaLayerException
  {
	
	InputStream stream = null;
	while (stream==null)
	{
		try {
			stream = paramInputStream.openStream();
	 
		} catch (IOException e) {
			stream = null;
		}
	}
	  bitstream = new Bitstream(stream);
	   decoder = new Decoder();
	  FactoryRegistry localFactoryRegistry = FactoryRegistry.systemRegistry();
	  audio = localFactoryRegistry.createAudioDevice();
 
	 audio.open(decoder);
     lastURL = paramInputStream;
  }
  
  public JPlayer(InputStream paramInputStream, AudioDevice paramAudioDevice)
    throws JavaLayerException
  {
    bitstream = new Bitstream(paramInputStream);
    decoder = new Decoder();
    if (paramAudioDevice != null)
    {
      audio = paramAudioDevice;
    }
    else
    {
      FactoryRegistry localFactoryRegistry = FactoryRegistry.systemRegistry();
      audio = localFactoryRegistry.createAudioDevice();
    }
    audio.open(decoder);
  }
  
  
  public void play() throws JavaLayerException {
    
          switch (playerStatus) {
              case NOTSTARTED:
                  final Runnable r = new Runnable() {
                      public void run() {
                         
							try {
								play(1);
							} catch (JavaLayerException | IOException e) {
								
									e.printStackTrace();
							}
					 
                      }
                  };
                  final Thread t = new Thread(r);
                  t.setDaemon(true);
                  t.setPriority(Thread.MAX_PRIORITY);
                  playerStatus = PLAYING;
                  t.start();
                  break;
              case PAUSED:
                  resume();
                  break;
              default:
                  break;
          }
      
  }
  public boolean pause() {
     
  if (playerStatus == PLAYING) {
      playerStatus = PAUSED;
  }
  return playerStatus == PAUSED;
   
  }

  public boolean resume()
  {
	  if (playerStatus == PAUSED) {
          playerStatus = PLAYING;
        
      }
	  
      return playerStatus == PLAYING;
  }
  public boolean play(int paramInt1, int paramInt2)
  throws JavaLayerException, IOException
  {
    boolean bool = true;
    int i = paramInt1;
    while ((i-- > 0) && (bool)) {
      bool = skipFrame();
      
      lastPosition++;
    }
    return play(Integer.MAX_VALUE);
  }

  protected boolean skipFrame()
  throws JavaLayerException
  {
    Header localHeader = bitstream.readFrame();
    if (localHeader == null) {
    
      return false;
    }
     
    bitstream.closeFrame();
    return true;
  }  
  public void rePlay(int skip)  throws JavaLayerException, IOException
  {  
	 
	 
	  synchronized (this)
      {
		  
		  bitstream.close();
		  this.close();
		  InputStream stream = null;
		  while (stream==null)
			{
				try {
					stream = lastURL.openStream();
			 
				} catch (IOException e) {
					stream = null;
				}
			}
	    bitstream = new Bitstream( stream);
	    try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
		 
			e.printStackTrace();
		}
	  
	    decoder = new Decoder();
	    FactoryRegistry localFactoryRegistry = FactoryRegistry.systemRegistry();
	    audio = localFactoryRegistry.createAudioDevice();
	    audio.open(decoder);
	   
	    boolean bool = true;
	    int i = skip;
	    lastPosition = 0;
	    while ((i-- > 0) && (bool)) {
	      bool = skipFrame();
	      lastPosition++;
	    }
	    playerStatus= NOTSTARTED;
		 
	    play();
      }
  }
  public void seek(int pos) throws JavaLayerException, IOException
  {
	  playerStatus = PAUSED;
	  synchronized (this)
      {
	  if (pos< this.lastPosition)  rePlay( pos) ;
	  else
		{
		  
		  	boolean bool = true;
		    int i = pos-lastPosition;
		    while ((i-- > 0) && (bool)) {
		      bool = skipFrame();
		      lastPosition++;
		    }
		}
	  playerStatus= PLAYING;
      }
  }
  
  public boolean play(int paramInt)
    throws JavaLayerException, IOException
  {
	 
	 boolean bool ;
	 while (bool = decodeFrame())
	 {
		 
		 while (playerStatus==PAUSED)
		 {
			 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
	
	 }
    /*if (!bool)
    {
      AudioDevice localAudioDevice = audio;
      if (localAudioDevice != null)
      {
        localAudioDevice.flush();
        synchronized (this)
        {
          complete = (!closed);
          close();
        }
      }
    }*/
	 System.out.println("finish");
	 if (isAutoReplay)  {rePlay(1);return true;}
	 else playerStatus = FINISHED;
    return bool;
  }
  
  public synchronized void close()
  {
	playerStatus = NOTSTARTED;
    AudioDevice localAudioDevice = audio;
    if (localAudioDevice != null)
    {
      closed = true;
      audio = null;
      localAudioDevice.close();
    
      try
      {
        bitstream.close();
      }
      catch (BitstreamException localBitstreamException) {}
    }
  }
 
  public synchronized boolean isComplete()
  {
		playerStatus = FINISHED;
    return complete;
  }
  private int curentsize = 0;
	  //http://mp3.zing.vn/bai-hat/Dau-Mua-Trung-Quan-Idol/ZW67W08A.html
	  public int getPosition()
  {
    int i = lastPosition;
    return i;
  }
  public int getMiliseconds()
  {
	  return miliseconds;
  }
  private int miliseconds = 0;
  protected boolean decodeFrame()
    throws JavaLayerException
  {
	  if (playerStatus == FINISHED) return false;
    try
    {
      AudioDevice localAudioDevice = audio;
      if (localAudioDevice == null) {
        return false;
      }
      Header localHeader = bitstream.readFrame();
      
     ;
     //  System.out.println( "time: "+ localHeader.framesize);
      if (localHeader == null) {
        return false;
      }
      miliseconds  = (int)localHeader.total_ms(lastPosition*localHeader.framesize);
      SampleBuffer localSampleBuffer = (SampleBuffer)decoder.decodeFrame(localHeader, bitstream);
      curentsize = localHeader.framesize;
 
      synchronized (this)
      {
        localAudioDevice = audio;
        if (localAudioDevice != null) {
          localAudioDevice.write(localSampleBuffer.getBuffer(), 0, localSampleBuffer.getBufferLength());
          this.lastPosition+=1;
        }
      }
      bitstream.closeFrame();
      
    }
    catch (RuntimeException localRuntimeException)
    {
      throw new JavaLayerException("Exception decoding audio frame", localRuntimeException);
    }
    return true;
  }
  
}
