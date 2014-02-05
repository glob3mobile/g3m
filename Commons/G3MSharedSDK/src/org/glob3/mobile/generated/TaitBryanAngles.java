package org.glob3.mobile.generated; 
public class TaitBryanAngles
{
   public String description()
   {
   
     IStringBuilder isb = IStringBuilder.newStringBuilder();
     isb.addString("(TaitBryanAngles Heading= ");
     isb.addDouble(_heading._degrees);
     isb.addString(", Pitch= ");
     isb.addDouble(_pitch._degrees);
     isb.addString(", Roll= ");
     isb.addDouble(_roll._degrees);
     isb.addString(")");
     final String s = isb.getString();
     if (isb != null)
        isb.dispose();
     return s;
   
   }
}