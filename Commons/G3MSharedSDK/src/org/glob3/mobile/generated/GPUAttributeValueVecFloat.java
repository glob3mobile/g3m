

package org.glob3.mobile.generated;

///////////

public class GPUAttributeValueVecFloat
         extends
            GPUAttributeValue {
   private final IFloatBuffer _buffer;
   private int                _timeStamp;


   public GPUAttributeValueVecFloat(final IFloatBuffer buffer,
                                    final int attributeSize,
                                    final int arrayElementSize,
                                    final int index,
                                    final int stride,
                                    final boolean normalized) {
      super(GLType.glFloat(), attributeSize, arrayElementSize, index, stride, normalized);
      _buffer = buffer;
      _timeStamp = buffer.timestamp();
   }


   @Override
   public final void setAttribute(final GL gl,
                                  final int id) {
      if (_index != 0) {
         //TODO: Change vertexAttribPointer
         ILogger.instance().logError("INDEX NO 0");
      }

      gl.vertexAttribPointer(id, _arrayElementSize, _normalized, _stride, _buffer);
   }


   @Override
   public final boolean isEqualsTo(final GPUAttributeValue v) {
      return (_buffer == ((GPUAttributeValueVecFloat) v)._buffer) && (_timeStamp == ((GPUAttributeValueVecFloat) v)._timeStamp)
             && (_type == v.getType()) && (_attributeSize == v.getAttributeSize()) && (_stride == v.getStride())
             && (_normalized == v.getNormalized());
   }


   @Override
   public final GPUAttributeValue shallowCopy() {
      final GPUAttributeValueVecFloat v = new GPUAttributeValueVecFloat(_buffer, _attributeSize, _arrayElementSize, _index,
               _stride, _normalized);
      v._timeStamp = _timeStamp;
      return v;
   }


   @Override
   public final String description() {

      final IStringBuilder isb = IStringBuilder.newStringBuilder();
      isb.addString("Attribute Value Float.");
      isb.addString(" ArrayElementSize:");
      isb.addInt(_arrayElementSize);
      isb.addString(" AttributeSize:");
      isb.addInt(_attributeSize);
      isb.addString(" Index:");
      isb.addInt(_index);
      isb.addString(" Stride:");
      isb.addInt(_stride);
      isb.addString(" Normalized:");
      isb.addBool(_normalized);

      final String s = isb.getString();
      if (isb != null) {
         isb.dispose();
      }
      return s;
   }

}
