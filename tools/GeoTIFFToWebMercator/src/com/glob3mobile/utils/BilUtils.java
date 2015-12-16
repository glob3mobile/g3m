package com.glob3mobile.utils;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class is supposed to work with 1-band, int16 little-endian BIL files.
 * Rows and columns should be included as parameters, since we do not process .hdr files
 * (Worldwind .bil files don't provide us with them)
 * Other configurations need to be implemented.
 */

public class BilUtils {
	
	public final static short LOWER_EARTH_LIMIT = 11200;
	public final static short NODATAVALUE = 15000;
	
	public static BufferedImage BilFileToBufferedImage(String filename, int rows, int columns){
		Path p = FileSystems.getDefault().getPath("", filename);
		try {
			byte [] fileData = Files.readAllBytes(p);
			ShortBuffer buffer = ByteBuffer.wrap(fileData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
						
			BufferedImage image = new BufferedImage(columns,rows,BufferedImage.TYPE_INT_ARGB);
			for (int i=0; i<rows; i++) for (int j=0;j<columns; j++){
				short data = buffer.get(i*columns + j);
				
				//This function will be used only on the original data records.
				//Applying rescale so we can use image ops while saving under
				//0 heights ...
				
				data += LOWER_EARTH_LIMIT;
				
				image.setRGB(j, i,((255<<24)|(0<<16)|data));
			}
			
			buffer.clear();
			buffer = null;
			
			return image;
		}
		catch (Exception E){return null;}
	}
	
	public static class MaxMinBufferedImage {
		public BufferedImage _image;
		public short _min, _max;
		public short _childrenData, _similarity;
		
		public MaxMinBufferedImage(BufferedImage image, short min, short max,
				short childrenData, short similarity){
			_image = image;
			_min = min;
			_max = max;
			_childrenData = childrenData;
			_similarity = similarity;
		}
	}
	
	public static MaxMinBufferedImage BilFileMaxMinToBufferedImage(String filename, int rows, int columns){
		Path p = FileSystems.getDefault().getPath("", filename);
		try {
			byte [] fileData = Files.readAllBytes(p);
			ShortBuffer buffer = ByteBuffer.wrap(fileData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
						
			BufferedImage image = new BufferedImage(columns,rows,BufferedImage.TYPE_INT_ARGB);
			for (int i=0; i<rows; i++) for (int j=0;j<columns; j++){
				short data = buffer.get(i*columns + j);
				
				//Considering our own NODATAS and under zero heights.
				if (data == NODATAVALUE)
					image.setRGB(j,i,0);
				else {
					data += LOWER_EARTH_LIMIT;
					image.setRGB(j, i,((255<<24)|(0<<16)|data));
				}
			}
			
			short max = buffer.get(rows*columns);
			short min = buffer.get(rows*columns + 1);
			short childrenData = buffer.get(rows*columns + 2);
			short similarity = buffer.get(rows*columns + 3);
			
			buffer.clear();
			buffer = null;
			
			return new MaxMinBufferedImage(image,min,max,childrenData,similarity);
		}
		catch (Exception E){return null;}
	}
	
	public static void BufferedImageToBilFile(BufferedImage image, String filename, int width, int height){
		ByteBuffer bytebuffer = ByteBuffer.allocate(width*height*2).order(ByteOrder.LITTLE_ENDIAN);
		ShortBuffer buffer = bytebuffer.asShortBuffer();

		for (int i=0; i<width; i++) for (int j=0;j<height; j++){

			short data = (short) ((image.getRGB(j,i)) & 0x0000FFFF);
			if (data == 0)
				buffer.put(i*height + j, NODATAVALUE);
			else
				buffer.put(i*height + j, (short) (data - LOWER_EARTH_LIMIT));
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			FileChannel out = fos.getChannel();
			out.write(bytebuffer);
			out.close();
			fos.close();
		}
		catch (Exception E){
			System.out.println("Bil File write failure");
		}
	}
	
	public static void BufferedImageToBilFileMaxMin(BufferedImage image, String filename, 
			int width, int height, short max, short min, short childrenData, short similarity){
		/**
		 * This function alters the normal BIL format to add a tile max and min after the elevation matrix.
		 * This should be noted when developing a pyramid client.
		 */
		
		final int dim = (width*height*2) + 8;
		ByteBuffer bytebuffer = ByteBuffer.allocate(dim).order(ByteOrder.LITTLE_ENDIAN);
		ShortBuffer buffer = bytebuffer.asShortBuffer();

		for (int i=0; i<width; i++) for (int j=0;j<height; j++){

			short data = (short) ((image.getRGB(j,i)) & 0x0000FFFF);
			if (data == 0)
				buffer.put(i*height + j, NODATAVALUE);
			else
				buffer.put(i*height + j, (short) (data - LOWER_EARTH_LIMIT));
		}
		
		buffer.put(width*height,max);
		buffer.put((width*height)+1,min);
		buffer.put(width*height + 2,childrenData);
		buffer.put(width*height + 3,similarity);
		
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			FileChannel out = fos.getChannel();
			out.write(bytebuffer);
			out.close();
			fos.close();
		}
		catch (Exception E){
			System.out.println("BilMaxMin File write failure");
		}
	}
	
	public static void saveRedimBilFile(short dims, BufferedImage image, String filename, 
			short max, short min, short childrenData, short similarity){
		
		int width = dims, height = dims;
		
		final int dim = (width*height*2) + 10;
		ByteBuffer bytebuffer = ByteBuffer.allocate(dim).order(ByteOrder.LITTLE_ENDIAN);
		ShortBuffer buffer = bytebuffer.asShortBuffer();

		for (int i=0; i<width; i++) for (int j=0;j<height; j++){

			short data = (short) ((image.getRGB(j,i)) & 0x0000FFFF);
			if (data == 0)
				buffer.put((i*height + j)+1, NODATAVALUE);
			else
				buffer.put((i*height + j)+1, (short) (data - LOWER_EARTH_LIMIT));
		}
		
		buffer.put(0,dims);
		
		buffer.put(width*height + 1,max);
		buffer.put((width*height)+2,min);
		buffer.put(width*height + 3,childrenData);
		buffer.put(width*height + 4,similarity);
		
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			FileChannel out = fos.getChannel();
			out.write(bytebuffer);
			out.close();
			fos.close();
		}
		catch (Exception E){
			System.out.println("BilMaxMin File write failure");
		}
	}

}
