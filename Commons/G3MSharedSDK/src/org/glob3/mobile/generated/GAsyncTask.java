package org.glob3.mobile.generated; 
//
//  IThreadUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 30/08/12.
//
//

//
//  IThreadUtils.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 30/08/12.
//
//




public abstract class GAsyncTask
{
///#ifdef C_CODE
  public void dispose()
  {
  }
///#endif
///#ifdef JAVA_CODE
//  void dispose();
///#endif

  public abstract void runInBackground(G3MContext context);

  public abstract void onPostExecute(G3MContext context);

}