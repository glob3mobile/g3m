package org.glob3.mobile.generated; 
public class TaitBryanAngles
{
   public TaitBryanAngles(Angle heading, Angle pitch, Angle roll)
   {
      _heading = heading;
      _pitch = pitch;
      _roll = roll;
   }
   public TaitBryanAngles fromRadians(double heading, double pitch, double roll)
   {
     return new TaitBryanAngles(Angle.fromRadians(heading), Angle.fromRadians(pitch), Angle.fromRadians(roll));
   }
   public TaitBryanAngles fromDegrees(double heading, double pitch, double roll)
   {
     return new TaitBryanAngles(Angle.fromDegrees(heading), Angle.fromDegrees(pitch), Angle.fromDegrees(roll));
   }
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