package org.glob3.mobile.generated; 
public class CartoCSSSymbolizer
{
  private java.util.ArrayList<String> _selectors = new java.util.ArrayList<String>();
  private java.util.ArrayList<CartoCSSVariable> _variables = new java.util.ArrayList<CartoCSSVariable>();
  private java.util.ArrayList<CartoCSSVariable> _properties = new java.util.ArrayList<CartoCSSVariable>();
  private java.util.ArrayList<CartoCSSSymbolizer> _children = new java.util.ArrayList<CartoCSSSymbolizer>();

  private CartoCSSSymbolizer _parent;
  private void setParent(CartoCSSSymbolizer parent)
  {
    if (parent == null)
    {
      ILogger.instance().logError("Can't set a null parent");
    }
    else
    {
      if (_parent != null)
      {
        ILogger.instance().logError("parent already set");
      }
      else
      {
        _parent = parent;
      }
    }
  }

  private int getDepth()
  {
    return (_parent == null) ? 0 : _parent.getDepth()+1;
  }

  private void indent(IStringBuilder isb, int delta)
  {
    final int depth = getDepth() + delta;
    for (int i = 0; i < depth; i++)
    {
      isb.addString("   ");
    }
  }

  private void buildSelectorsDescription(IStringBuilder isb, int delta)
  {
    if (!_selectors.isEmpty())
    {
      isb.addString("\n");
      indent(isb, 1 + delta);
      isb.addString("selectors=");
      final int childrenSize = _selectors.size();
      for (int i = 0; i < childrenSize; i++)
      {
        isb.addString("\n");
        indent(isb, 2 + delta);
        isb.addString(_selectors.get(i));
      }
    }
  }
  private void buildVariablesDescription(IStringBuilder isb, int delta)
  {
    if (!_variables.isEmpty())
    {
      isb.addString("\n");
      indent(isb, 1 + delta);
      isb.addString("variables=");
      final int variablesSize = _variables.size();
      for (int i = 0; i < variablesSize; i++)
      {
        CartoCSSVariable variable = _variables.get(i);
        isb.addString("\n");
        indent(isb, 2 + delta);
        isb.addString(variable._name);
        isb.addString(":");
        isb.addString(variable._value);
      }
    }
  }
  private void buildPropertiesDescription(IStringBuilder isb, int delta)
  {
    if (!_properties.isEmpty())
    {
      isb.addString("\n");
      indent(isb, 1 + delta);
      isb.addString("properties=");
      final int propertiesSize = _properties.size();
      for (int i = 0; i < propertiesSize; i++)
      {
        CartoCSSVariable property = _properties.get(i);
        isb.addString("\n");
        indent(isb, 2 + delta);
        isb.addString(property._name);
        isb.addString(":");
        isb.addString(property._value);
      }
    }
  }
  private void buildChildrenDescription(IStringBuilder isb, int delta)
  {
    if (!_children.isEmpty())
    {
      isb.addString("\n");
      indent(isb, 1 + delta);
      isb.addString("children=");
      final int childrenSize = _children.size();
      for (int i = 0; i < childrenSize; i++)
      {
        isb.addString(_children.get(i).description(true, 1 + delta));
      }
    }
  }

  public CartoCSSSymbolizer()
  {
     _parent = null;
  }

  public CartoCSSSymbolizer(java.util.ArrayList<String> selectors)
  {
     _selectors = selectors;
     _parent = null;
  }

  public void dispose()
  {
  
    final int variablesSize = _variables.size();
    for (int i = 0; i < variablesSize; i++)
    {
      CartoCSSVariable variable = _variables.get(i);
      if (variable != null)
         variable.dispose();
    }
  
    final int propertiesSize = _properties.size();
    for (int i = 0; i < propertiesSize; i++)
    {
      CartoCSSVariable property = _properties.get(i);
      if (property != null)
         property.dispose();
    }
  
    final int childrenSize = _children.size();
    for (int i = 0; i < childrenSize; i++)
    {
      CartoCSSSymbolizer child = _children.get(i);
      if (child != null)
         child.dispose();
    }
  }

  public final void setVariableDeclaration(String name, String value)
  {
    final IStringUtils su = IStringUtils.instance();
    final String n = su.trim(name);
    final String v = su.trim(value);
    _variables.add(new CartoCSSVariable(n, v));
  }

  public final void setProperty(String name, String value)
  {
    final IStringUtils su = IStringUtils.instance();
    final String n = su.trim(name);
    final String v = su.trim(value);
    _properties.add(new CartoCSSVariable(n, v));
  }

  public final void addSymbolizer(CartoCSSSymbolizer symbolizer)
  {
    if (symbolizer != null)
    {
      _children.add(symbolizer);
      symbolizer.setParent(this);
    }
  }

  public final String description(boolean detailed)
  {
     return description(detailed, 0);
  }
  public final String description()
  {
     return description(false, 0);
  }
  public final String description(boolean detailed, int delta)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    if (detailed)
    {
      isb.addString("\n");
      indent(isb, delta);
      isb.addString("[CartoCSSSymbolizer");
  
      buildSelectorsDescription(isb, delta);
      buildVariablesDescription(isb, delta);
      buildPropertiesDescription(isb, delta);
      buildChildrenDescription(isb, delta);
  
      isb.addString("\n");
      indent(isb, delta);
      isb.addString("]");
    }
    else
    {
      isb.addString("[CartoCSSSymbolizer ");
      isb.addString(" depth=");
      isb.addInt(getDepth());
      isb.addString(", variables=");
      isb.addInt(_variables.size());
      isb.addString(", properties=");
      isb.addInt(_properties.size());
      isb.addString(", children=");
      isb.addInt(_children.size());
      isb.addString("]");
    }
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

}