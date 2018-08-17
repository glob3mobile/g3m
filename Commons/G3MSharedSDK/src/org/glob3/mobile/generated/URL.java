package org.glob3.mobile.generated;import java.util.*;

//
//  URL.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//

//
//  URL.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//



public class URL
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  URL operator =(URL that);

  private static String concatenatePath(URL parent, String path)
  {
	final IStringUtils iu = IStringUtils.instance();

	String result = parent._path + "/" + path;
	while (iu.indexOf(result, "//") >= 0)
	{
	  result = iu.replaceAll(result, "//", "/");
	}

	if (iu.beginsWith(result, "http:/"))
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  result = "http://" + iu.substring(result, result + 6);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  result = "http://" + iu.substring(result, result + 6);
//#endif
	}

	if (iu.beginsWith(result, "https:/"))
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  result = "https://" + iu.substring(result, result + 7);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  result = "https://" + iu.substring(result, result + 7);
//#endif
	}

	if (iu.beginsWith(result, "ws:/"))
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  result = "ws://" + iu.substring(result, result + 4);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  result = "ws://" + iu.substring(result, result + 4);
//#endif
	}

	if (iu.beginsWith(result, "wss:/"))
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  result = "wss://" + iu.substring(result, result + 5);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  result = "wss://" + iu.substring(result, result + 5);
//#endif
	}

	if (iu.beginsWith(result, "file:/"))
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  result = "file:///" + iu.substring(result, result + 6);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  result = "file:///" + iu.substring(result, result + 6);
//#endif
	}

	return result;
  }

  public final String _path;

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
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: explicit URL(const String& path, const boolean escapePath=false) : _path(escapePath ? escape(path) : path)
  public URL(String path, boolean escapePath)
  {
	  _path = escapePath ? escape(path) : path;
  }

  public URL(URL parent, String path)
  {
	  this(parent, path, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: URL(const URL& parent, const String& path, const boolean escapePath=false) : _path(concatenatePath(parent, escapePath ? escape(path) : path))
  public URL(URL parent, String path, boolean escapePath)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _path = concatenatePath(parent, escapePath ? escape(path) : path);
	  _path = concatenatePath(new URL(parent), escapePath ? escape(path) : path);
  }

  public void dispose()
  {
  }

//  std::string getPath() const {
//    return _path;
//  }

  public static URL nullURL()
  {
	return new URL("__NULL__", false);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isNull() const
  public final boolean isNull()
  {
	return (_path.equals("__NULL__"));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const URL& that) const
  public final boolean isEquals(URL that)
  {
	return (_path.equals(that._path));
  }

  public static final String FILE_PROTOCOL = "file:///";

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isFileProtocol() const
  public final boolean isFileProtocol()
  {
	return (IStringUtils.instance().beginsWith(_path, FILE_PROTOCOL));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString("URL(");
	isb.addString(_path);
	isb.addString(")");
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public String toString()
  {
	return description();
  }
//#endif

  public static String escape(String path)
  {
	final IStringUtils su = IStringUtils.instance();
	String result = su.replaceAll(path, "\n", "%0A");
	result = su.replaceAll(result, " ", "%20");
	result = su.replaceAll(result, "\"", "%22");
	result = su.replaceAll(result, "-", "%2D");
	result = su.replaceAll(result, ".", "%2E");
	result = su.replaceAll(result, "<", "%3C");
	result = su.replaceAll(result, ">", "%3E");
	result = su.replaceAll(result, "\\", "%5C");
	result = su.replaceAll(result, "^", "%5E");
	result = su.replaceAll(result, "_", "%5F");
	result = su.replaceAll(result, "`", "%60");
	result = su.replaceAll(result, "{", "%7B");
	result = su.replaceAll(result, "|", "%7C");
	result = su.replaceAll(result, "}", "%7D");
	result = su.replaceAll(result, "~", "%7E");
	return result;
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean operator <(const URL& that) const
//C++ TO JAVA CONVERTER TODO TASK: Operators cannot be overloaded in Java:
  boolean operator <(URL that)
  {
	if (_path.compareTo(that._path) < 0)
	{
	  return true;
	}
	return false;
  }
//#endif

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public int hashCode()
  {
	return _path.hashCode();
  }

  public final Override public boolean equals(Object obj)
  {
	if (this == obj)
	{
	  return true;
	}
	if (obj == null)
	{
	  return false;
	}
	if (getClass() != obj.getClass())
	{
	  return false;
	}
	final URL other = (URL) obj;
	if (_path.equals(other._path))
	{
	  return true;
	}
	return false;
  }
//#endif

}
