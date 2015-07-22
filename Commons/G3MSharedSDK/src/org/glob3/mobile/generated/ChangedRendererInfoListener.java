package org.glob3.mobile.generated; 
//
//  ChangedRendererInfoListener.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 21/04/14.
//
//

//
//  ChangedRendererInfoListener.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 21/04/14.
//
//


//class Renderer;
//class Info;


public interface ChangedRendererInfoListener
{

  void dispose();

  void changedRendererInfo(int rendererIdentifier, java.util.ArrayList<Info> info);

 }