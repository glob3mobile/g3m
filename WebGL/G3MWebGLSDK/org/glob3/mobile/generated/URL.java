package org.glob3.mobile.generated; 
//
//  URL.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  URL.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



public class URL
{
  private final String _path;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  URL operator =(URL that);


  public URL(URL that)
  {
	  _path = that._path;

  }

  public URL()
  {
	  _path = "";
  }

  public URL(String path)
  {
	  _path = path;
  }

  public URL(URL parent, String path)
  {
	  _path = parent.getPath() + "/" + path;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getPath() const
  public final String getPath()
  {
	return _path;
  }

  public static URL nullURL()
  {
	return new URL("__NULL__");
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isNull() const
  public final boolean isNull()
  {
	return (_path.equals("__NULL__"));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEqualsTo(const URL& that) const
  public final boolean isEqualsTo(URL that)
  {
	return (_path.equals(that._path));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
  
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add("URL(").add(getPath()).add(")");
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

}