package org.glob3.mobile.generated; 
//
//  Attribute.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//





public abstract class Attribute
{
  protected String _name;
  protected int _id;

  protected boolean _wasSet;
  protected boolean _dirty;

  public void dispose()
  {
  }
  public Attribute(String name, int id)
  {
     _name = name;
     _id = id;
     _wasSet = false;
     _dirty = false;
  }

  public final String getName()
  {
     return _name;
  }
  public final int getID()
  {
     return _id;
  }

  public abstract void applyChanges(GL gl);
}