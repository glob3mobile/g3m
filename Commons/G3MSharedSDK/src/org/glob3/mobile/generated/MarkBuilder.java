package org.glob3.mobile.generated;
//
//  MarkBuilder.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//

//
//  MarkBuilder.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//




//class Mark;
//class MarkOutfit;
//class MarkAnchor;
//class IImageFactory;
//class Geodetic3D;
//class MarkUserData;
//class MarkTouchListener;


/**
 * Builds Marks step by step. What belongs to one mark (position, outfits, user
 * data, touch listener) is consumed by build(); the rest stays as the default
 * for the next marks.
 */
public class MarkBuilder
{
  private AltitudeMode _altitudeMode;
  private double _minDistanceToCamera;
  private double _maxDistanceToCamera;
  private boolean _zoomInAppears;

  private Geodetic3D _position;
  private java.util.ArrayList<MarkOutfit> _outfits = new java.util.ArrayList<MarkOutfit>();
  private MarkUserData _userData;
  private boolean _autoDeleteUserData;
  private MarkTouchListener _touchListener;
  private boolean _autoDeleteTouchListener;


  // what belongs to the mark just built goes with it
  private void clearMarkProperties()
  {
    if (_position != null)
       _position.dispose();
    _position = null;
  
    _outfits.clear();
  
    _userData = null;
    _autoDeleteUserData = false;
  
    _touchListener = null;
    _autoDeleteTouchListener = false;
  }

  /** defaults: ABSOLUTE, no distance limits, zoom in on appearing */
  public MarkBuilder()
  {
     _altitudeMode = AltitudeMode.ABSOLUTE;
     _minDistanceToCamera = 0;
     _maxDistanceToCamera = 0;
     _zoomInAppears = true;
     _position = null;
     _userData = null;
     _autoDeleteUserData = false;
     _touchListener = null;
     _autoDeleteTouchListener = false;
  }

  public void dispose()
  {
    if (_position != null)
       _position.dispose();
  
    for (int i = 0; i < _outfits.size(); i++)
    {
      if (_outfits.get(i) != null)
         _outfits.get(i).dispose();
    }
  
    if (_autoDeleteUserData)
    {
      if (_userData != null)
         _userData.dispose();
    }
    if (_autoDeleteTouchListener)
    {
      if (_touchListener != null)
         _touchListener.dispose();
    }
  }

  public final void setAltitudeMode(AltitudeMode altitudeMode)
  {
    _altitudeMode = altitudeMode;
  }

  /** the mark is hidden farther than this; 0: no limit */
  public final void setMinDistanceToCamera(double minDistanceToCamera)
  {
    _minDistanceToCamera = minDistanceToCamera;
  }

  /** the mark is hidden nearer than this; 0: no limit */
  public final void setMaxDistanceToCamera(double maxDistanceToCamera)
  {
    _maxDistanceToCamera = maxDistanceToCamera;
  }

  public final void setZoomInAppears(boolean zoomInAppears)
  {
    _zoomInAppears = zoomInAppears;
  }

  public final void setPosition(Geodetic3D position)
  {
    if (_position != null)
       _position.dispose();
    _position = new Geodetic3D(position);
  }

  /** the first outfit added is the largest; anchor NULL: the mark keeps its own anchor */
  public final void addOutfit(IImageFactory imageFactory, MarkAnchor anchor)
  {
    _outfits.add(new MarkOutfit(imageFactory, anchor));
  }

  public final void addOutfit(IImageFactory imageFactory)
  {
    addOutfit(imageFactory, null);
  }

  public final void setUserData(MarkUserData userData, boolean autoDeleteUserData)
  {
    if (_autoDeleteUserData)
    {
      if (_userData != null)
         _userData.dispose();
    }
    _userData = userData;
    _autoDeleteUserData = autoDeleteUserData;
  }

  public final void setTouchListener(MarkTouchListener touchListener, boolean autoDeleteTouchListener)
  {
    if (_autoDeleteTouchListener)
    {
      if (_touchListener != null)
         _touchListener.dispose();
    }
    _touchListener = touchListener;
    _autoDeleteTouchListener = autoDeleteTouchListener;
  }

  /** needs a position and at least one outfit */
  public final Mark build()
  {
    if (_position == null)
    {
      throw new RuntimeException("MarkBuilder: the mark has no position");
    }
    if (_outfits.isEmpty())
    {
      throw new RuntimeException("MarkBuilder: the mark has no outfit");
    }
  
    Mark mark = new Mark(_outfits, _position, _altitudeMode, _minDistanceToCamera, _maxDistanceToCamera, _userData, _autoDeleteUserData, _touchListener, _autoDeleteTouchListener, _zoomInAppears);
  
    clearMarkProperties();
  
    return mark;
  }

}