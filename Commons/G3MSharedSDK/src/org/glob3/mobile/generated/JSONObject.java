

package org.glob3.mobile.generated;

//
// JSONObject.cpp
// G3MiOSSDK
//
// Created by Oliver Koehler on 01/10/12.
// Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
// JSONObject.hpp
// G3MiOSSDK
//
// Created by Oliver Koehler on 01/10/12.
// Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


// C++ TO JAVA CONVERTER NOTE: Java has no need of forward class
// declarations:
// class IStringBuilder;

public class JSONObject
    extends
      JSONBaseObject {
  private final java.util.HashMap<String, JSONBaseObject> _entries = new java.util.HashMap<String, JSONBaseObject>();


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: void putKeyAndValueDescription(const String& key,
  // IStringBuilder *isb) const
  private void putKeyAndValueDescription(final String key,
                                         final IStringBuilder isb) {
    isb.addString("\"");
    isb.addString(key);
    isb.addString("\":");
    // TODO: IN CORE
    isb.addString((get(key) == null) ? "null" : get(key).description());
  }


  @Override
  public void dispose() {
    _entries.clear();

  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: const JSONObject* asObject() const
  @Override
  public final JSONObject asObject() {
    return this;
  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: const JSONBaseObject* get(const String& key) const
  public final JSONBaseObject get(final String key) {

    return _entries.get(key);
  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: const JSONObject* getAsObject(const String& key) const
  public final JSONObject getAsObject(final String key) {
    final JSONBaseObject object = get(key);
    return (object == null) ? null : object.asObject();
  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: const JSONArray* getAsArray(const String& key) const
  public final JSONArray getAsArray(final String key) {
    final JSONBaseObject object = get(key);
    return (object == null) ? null : object.asArray();
  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: const JSONBoolean* getAsBoolean(const String& key) const
  public final JSONBoolean getAsBoolean(final String key) {
    final JSONBaseObject object = get(key);
    return (object == null) ? null : object.asBoolean();
  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: const JSONNumber* getAsNumber(const String& key) const
  public final JSONNumber getAsNumber(final String key) {
    final JSONBaseObject object = get(key);
    return (object == null) ? null : object.asNumber();
  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: const JSONString* getAsString(const String& key) const
  public final JSONString getAsString(final String key) {
    final JSONBaseObject object = get(key);
    return (object == null) ? null : object.asString();
  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: boolean getAsBoolean(const String& key, boolean
  // defaultValue) const
  public final boolean getAsBoolean(final String key,
                                    final boolean defaultValue) {
    final JSONBoolean jsBool = getAsBoolean(key);
    return (jsBool == null) ? defaultValue : jsBool.value();
  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: double getAsNumber(const String& key, double
  // defaultValue) const
  public final double getAsNumber(final String key,
                                  final double defaultValue) {
    final JSONNumber jsNumber = getAsNumber(key);
    return (jsNumber == null) ? defaultValue : jsNumber.value();
  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: const String getAsString(const String& key, const
  // String& defaultValue) const
  public final String getAsString(final String key,
                                  final String defaultValue) {
    final JSONString jsString = getAsString(key);
    return (jsString == null) ? defaultValue : jsString.value();
  }


  public final void put(final String key,
                        final JSONBaseObject object) {
    _entries.put(key, object);
  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: int size() const
  public final int size() {
    return _entries.size();
  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: java.util.ArrayList<String> keys() const
  public final java.util.ArrayList<String> keys() {
    // C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    // #if JAVA_CODE
    return new java.util.ArrayList<String>(_entries.keySet());
    // #endif
  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: const String description() const
  @Override
  public final String description() {
    final IStringBuilder isb = IStringBuilder.newStringBuilder();

    isb.addString("{");

    final java.util.ArrayList<String> keys = this.keys();

    final int keysCount = keys.size();
    if (keysCount > 0) {
      putKeyAndValueDescription(keys.get(0), isb);
      for (int i = 1; i < keysCount; i++) {
        isb.addString(", ");
        putKeyAndValueDescription(keys.get(i), isb);
      }
    }

    isb.addString("}");

    final String s = isb.getString();
    if (isb != null) {
      isb.dispose();
    }
    return s;
  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: JSONObject* deepCopy() const
  @Override
  public final JSONObject deepCopy() {
    final JSONObject result = new JSONObject();

    final java.util.ArrayList<String> keys = this.keys();

    final int keysCount = keys.size();
    for (int i = 0; i < keysCount; i++) {
      final String key = keys.get(i);
      result.put(key, JSONBaseObject.deepCopy(get(key)));
    }

    return result;
  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: void acceptVisitor(JSONVisitor* visitor) const
  @Override
  public final void acceptVisitor(final JSONVisitor visitor) {
    visitor.visitObjectBeforeChildren(this);

    final java.util.ArrayList<String> keys = this.keys();

    final int keysCount = keys.size();
    for (int i = 0; i < keysCount; i++) {
      if (i != 0) {
        visitor.visitObjectInBetweenChildren(this);
      }
      final String key = keys.get(i);
      visitor.visitObjectBeforeChild(this, key);
      final JSONBaseObject child = get(key);
      child.acceptVisitor(visitor);
    }

    visitor.visitObjectAfterChildren(this);
  }

}
