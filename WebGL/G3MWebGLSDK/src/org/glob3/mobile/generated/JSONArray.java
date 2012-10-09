package org.glob3.mobile.generated; 
//
//  JSONArray.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  JSONArray.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public class JSONArray extends JSONBaseObject
{
  private java.util.ArrayList<JSONBaseObject> _entries = new java.util.ArrayList<JSONBaseObject>();

  public final JSONArray getArray()
  {
	return this;
  }
  public void dispose()
  {
	for (int i = 0; i<_entries.size(); i++)
	{
	  if (_entries.get(i) != null)
		  _entries.get(i).dispose();
	}
	_entries.clear();
  }
  public final JSONBaseObject getElement(int index)
  {
	return _entries.get(index);
  }
  public final int getSize()
  {
	return _entries.size();
  }

  public final void appendElement (JSONBaseObject object)
  {
	_entries.add(object);
  }
}