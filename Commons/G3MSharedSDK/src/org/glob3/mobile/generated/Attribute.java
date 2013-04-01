package org.glob3.mobile.generated; 
//
//  Attribute.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//





public class Attribute
{
  private String _name;
  private int _id;
  public Attribute(String name, int id)
  {
     _name = name;
     _id = id;
  }

  public final String getName()
  {
     return _name;
  }
  public final int getID()
  {
     return _id;
  }
}