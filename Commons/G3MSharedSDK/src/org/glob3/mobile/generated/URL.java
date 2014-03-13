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

  private static String concatenatePath(URL parent, String path)
  {
    final IStringUtils iu = IStringUtils.instance();

    String result = iu.replaceSubstring(parent.getPath() + "/" + path, "//", "/");
    if (iu.beginsWith(result, "http:/"))
    {
      result = "http://" + iu.substring(result, 6);
    }

    return result;
  }


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
  public URL(String path)
  {
     this(path, false);
  }
  public URL(String path, boolean escapePath)
  {
     _path = escapePath ? escape(path) : path;
  }

  public URL(URL parent, String path)
  {
     _path = concatenatePath(parent, path);
  }

  public void dispose()
  {
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

  public final boolean isEquals(URL that)
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
  @Override
  public String toString() {
    return description();
  }

  public static String escape(String path)
  {
    final IStringUtils su = IStringUtils.instance();
    String result = su.replaceSubstring(path, "\n", "%0A");
    result = su.replaceSubstring(result, " ", "%20");
    result = su.replaceSubstring(result, "\"", "%22");
    result = su.replaceSubstring(result, "-", "%2D");
    result = su.replaceSubstring(result, ".", "%2E");
    result = su.replaceSubstring(result, "<", "%3C");
    result = su.replaceSubstring(result, ">", "%3E");
    result = su.replaceSubstring(result, "\\", "%5C");
    result = su.replaceSubstring(result, "^", "%5E");
    result = su.replaceSubstring(result, "_", "%5F");
    result = su.replaceSubstring(result, "`", "%60");
    result = su.replaceSubstring(result, "{", "%7B");
    result = su.replaceSubstring(result, "|", "%7C");
    result = su.replaceSubstring(result, "}", "%7D");
    result = su.replaceSubstring(result, "~", "%7E");
    return result;
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