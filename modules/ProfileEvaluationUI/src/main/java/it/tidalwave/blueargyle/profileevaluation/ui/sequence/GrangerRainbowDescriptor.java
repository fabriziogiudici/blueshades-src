/***********************************************************************************************************************
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 **********************************************************************************************************************/
package it.tidalwave.blueargyle.profileevaluation.ui.sequence;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
public class GrangerRainbowDescriptor extends SequenceSetDescriptorSupport
  {
    public GrangerRainbowDescriptor()
      {
        super("Granger rainbow", 
              "Look at the chart and find out whether there are banding artifacts, strange color blotches or " +
              "areas where transitions are not smooth. Note that this is a very demanding test and it's very unlikely " +
              "that a perfect result is achieved.");
      }
  }
