package org.glob3.mobile.generated; 
//
//  G3MCSceneDescription.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/26/13.
//
//

//
//  G3MCSceneDescription.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/26/13.
//
//



public class G3MCSceneDescription
{
  private final String _id;
  private final String _user;
  private final String _name;
  private final String _description;
  private final String _iconURL;
  private final java.util.ArrayList<String> _tags;


  public G3MCSceneDescription(String id, String user, String name, String description, String iconURL, java.util.ArrayList<String> tags)
  {
     _id = id;
     _user = user;
     _name = name;
     _description = description;
     _iconURL = iconURL;
     _tags = tags;

  }

  public final String getId()
  {
    return _id;
  }

  public final String getUser()
  {
    return _user;
  }

  public final String getName()
  {
    return _name;
  }

  public final String getDescription()
  {
    return _description;
  }

  public final String getIconURL()
  {
    return _iconURL;
  }

  public final java.util.ArrayList<String> getTags()
  {
    java.util.ArrayList<String> result = new java.util.ArrayList<String>();
    final int size = _tags.size();
    for (int i = 0; i < size; i++)
    {
      result.add(_tags[i]);
    }
    return result;
  }

  public void dispose()
  {

  }

  public final String description()
  {
    //return "TODO: G3MCSceneDescription::description()";
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    //isb->addDouble(_degrees);
    isb.addString("[Scene id=");
    isb.addString(_id);
    isb.addString(", user=");
    isb.addString(_user);
    isb.addString(", name=");
    isb.addString(_name);
    isb.addString(", description=");
    isb.addString(_description);
    isb.addString(", iconURL=");
    isb.addString(_iconURL);
  
    isb.addString(", tags=[");
    final int tagsSize = _tags.size();
    for (int i = 0; i < tagsSize; i++)
    {
      if (i > 0)
      {
        isb.addString(", ");
      }
      isb.addString(_tags[i]);
    }
    isb.addString("]");
  
    isb.addString("]");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

}