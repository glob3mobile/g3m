package org.glob3.mobile.generated; 
//
//  Mark.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Mark.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



public class Mark
{
  private final String _name;
  private final String _description;
  private final String _imageFileName;
  private final Geodetic3D _position ;

  public Mark(String name, String description, String imageFileName, Geodetic3D position)
  {
	  _name = name;
	  _description = description;
	  _imageFileName = imageFileName;
	  _position = new Geodetic3D(position);

  }

  public void dispose()
  {
  }

  public final String getName()
  {
	return _name;
  }

}