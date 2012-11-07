package org.glob3.mobile.generated; 
//
//  IJSONParser.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  IJSONParser.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
//#define JSONParser IJSONParser.instance()

public abstract class IJSONParser
{

  private static IJSONParser _instance = null;


  public static void setInstance(IJSONParser parser)
  {
	if (_instance != null)
	{
	  ILogger.instance().logWarning("Warning, IJSONParser instance set two times\n");
	}
	_instance = parser;
  }

  public static IJSONParser instance()
  {
	return _instance;
  }


  public void dispose()
  {
  }

  public abstract JSONBaseObject parse(String json);

  public abstract JSONBaseObject parse(IByteBuffer buffer);

  public void deleteJSONData(JSONBaseObject object)
  {
	if (object != null)
		object.dispose();
  }

}