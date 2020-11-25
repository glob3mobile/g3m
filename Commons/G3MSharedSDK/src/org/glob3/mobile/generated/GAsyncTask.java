package org.glob3.mobile.generated;
//
//  GAsyncTask.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/18/16.
//
//

//
//  GAsyncTask.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/18/16.
//
//


//class G3MContext;


public abstract class GAsyncTask
{
  public void dispose()
  {
  }

  public abstract void runInBackground(G3MContext context);

  public abstract void onPostExecute(G3MContext context);
}