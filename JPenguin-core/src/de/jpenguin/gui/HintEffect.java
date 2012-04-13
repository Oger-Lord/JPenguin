package de.jpenguin.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.effects.impl.*;


public class HintEffect
  implements EffectImpl
{
  private HintController hc;

  public void activate(Nifty niftyParam, Element element, EffectProperties parameter)
  {
      hc=element.getNifty().getScreen("start").findControl("hintPanel", HintController.class);
      
      if(parameter.get("hintKey") != null)
      {
          hc.setHintKey((String)parameter.get("hintKey"));
      }else{
        hc.setHint((String)parameter.get("hintText"));
      }
  }

  public void execute(Element element, float normalizedTime, Falloff falloff, NiftyRenderEngine r)
  {
     //   System.out.println("Ececute Hint");
  }

  public void deactivate() {
        hc.removeHint();
  }

}