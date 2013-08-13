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



public class URL extends Disposable
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

  /**
   Creates an URL.
   
   @param escapePath Escape the given path (true) or take it as it is given (false)
   */
  public URL(String path, boolean escapePath)
  {
     _path = escapePath ? escape(path) : path;
  }

  public URL(URL parent, String path)
  {
     _path = parent.getPath() + "/" + path;
  }

  public void dispose()
  {
    JAVA_POST_DISPOSE
  }

  public final String getPath()
  {
    return _path;
  }

  public static URL nullURL()
  {
    return new URL("__NULL__", false);
  }

  public final boolean isNull()
  {
    return (_path.equals("__NULL__"));
  }

  public final boolean isEqualsTo(URL that)
  {
    return (_path.equals(that._path));
  }

  public static final String FILE_PROTOCOL = "file:///";

  public final boolean isFileProtocol()
  {
    return (IStringUtils.instance().beginsWith(_path, FILE_PROTOCOL));
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("URL(");
    isb.addString(getPath());
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public static String escape(String path)
  {
  //    std::string escapedURL = IStringUtils::instance()->replaceSubstring(path, "%", "%25");
      String escapedURL = IStringUtils.instance().replaceSubstring(path, "\n", "%0A");
      escapedURL = IStringUtils.instance().replaceSubstring(escapedURL, " ", "%20");
      escapedURL = IStringUtils.instance().replaceSubstring(escapedURL, "\"", "%22");
      escapedURL = IStringUtils.instance().replaceSubstring(escapedURL, "-", "%2D");
      escapedURL = IStringUtils.instance().replaceSubstring(escapedURL, ".", "%2E");
      escapedURL = IStringUtils.instance().replaceSubstring(escapedURL, "<", "%3C");
      escapedURL = IStringUtils.instance().replaceSubstring(escapedURL, ">", "%3E");
      escapedURL = IStringUtils.instance().replaceSubstring(escapedURL, "\\", "%5C");
      escapedURL = IStringUtils.instance().replaceSubstring(escapedURL, "^", "%5E");
      escapedURL = IStringUtils.instance().replaceSubstring(escapedURL, "_", "%5F");
      escapedURL = IStringUtils.instance().replaceSubstring(escapedURL, "`", "%60");
      escapedURL = IStringUtils.instance().replaceSubstring(escapedURL, "{", "%7B");
      escapedURL = IStringUtils.instance().replaceSubstring(escapedURL, "|", "%7C");
      escapedURL = IStringUtils.instance().replaceSubstring(escapedURL, "}", "%7D");
      escapedURL = IStringUtils.instance().replaceSubstring(escapedURL, "~", "%7E");
  
      return escapedURL;
  }


  @Override
	public int hashCode() {
		return _path.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final URL other = (URL) obj;
    if (_path.equals(other._path)) {
      return true;
    }
    return false;
	}

}