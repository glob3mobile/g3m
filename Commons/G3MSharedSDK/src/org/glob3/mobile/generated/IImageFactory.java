package org.glob3.mobile.generated;
//
//  IImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

//
//  IImageFactory.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//


//class G3MContext;
//class IImageFactoryListener;
//class ChangedListener;

public interface IImageFactory
{

  void dispose();

  boolean isMutable();

  void create(G3MContext context, IImageFactoryListener listener, boolean deleteListener);

  void setChangeListener(ChangedListener listener);

}