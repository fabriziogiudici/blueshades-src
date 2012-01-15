/***********************************************************************************************************************
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 **********************************************************************************************************************/

package it.tidalwave.blueargyle.profileevaluation.ui.impl.charts.netbeans;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutionException;
import java.awt.EventQueue;
import java.awt.Graphics;
import javax.swing.SwingWorker;
import lombok.extern.slf4j.Slf4j;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.Painter;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public abstract class DeferredCreationPainterPanel extends JXPanel
  {
    public DeferredCreationPainterPanel()
      {
        assert EventQueue.isDispatchThread();
      }
    
    @Override
    public void paint (final @Nonnull Graphics g) 
      {
        assert EventQueue.isDispatchThread();
        
        if (getBackgroundPainter() == null)
          {
            new SwingWorker<Painter, Void>() 
              {
                @Override @Nonnull
                protected Painter doInBackground() 
                  {
                    return createPainter();
                  }

                @Override
                protected void done()   
                  {
                    try 
                      {
                        setOpaque(false);
                        setBackgroundPainter(get());
                        repaint();
                      }
                    catch (InterruptedException e) 
                      {
                        log.error("", e);
                      }
                    catch (ExecutionException e)
                      {
                        log.error("", e);
                      }
                  }
              }.execute();
          }
        
        super.paint(g);
      }
    
    @Nonnull
    protected abstract Painter createPainter();
  }
