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
	
	public static BufferedImage BilFileToBufferedImage(String filename, int rows, int columns){
		Path p = FileSystems.getDefault().getPath("", filename);
		try {
			byte [] fileData = Files.readAllBytes(p);
			ShortBuffer buffer = ByteBuffer.wrap(fileData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
						
			BufferedImage image = new BufferedImage(columns,rows,BufferedImage.TYPE_INT_ARGB);
			for (int i=0; i<rows; i++) for (int j=0;j<columns; j++){
				short data = buffer.get(i*columns + j);
				
				//TODO: I'm not considering negative values by the moment. This should be changed.
				if (data < 0) data = 0;
				
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
		
		public MaxMinBufferedImage(BufferedImage image, short min, short max){
			_image = image;
			_min = min;
			_max = max;
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
				
				//TODO: I'm not considering negative values by the moment. This should be changed.
				if (data < 0) data = 0;
				
				image.setRGB(j, i,((255<<24)|(0<<16)|data));
			}
			
			short max = buffer.get(rows*columns);
			short min = buffer.get(rows*columns + 1);
			
			buffer.clear();
			buffer = null;
			
			return new MaxMinBufferedImage(image,min,max);
		}
		catch (Exception E){return null;}
	}
	
	public static void BufferedImageToBilFile(BufferedImage image, String filename, int width, int height){
		ByteBuffer bytebuffer = ByteBuffer.allocate(width*height*2).order(ByteOrder.LITTLE_ENDIAN);
		ShortBuffer buffer = bytebuffer.asShortBuffer();

		for (int i=0; i<width; i++) for (int j=0;j<height; j++){

			short data = (short) ((image.getRGB(j,i)) & 0x0000FFFF);
			buffer.put(i*height + j, data);
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
	
	public static void BufferedImageToBilFileMaxMin(BufferedImage image, String filename, int width, int height, short max, short min){
		/**
		 * This function alters the normal BIL format to add a tile max and min after the elevation matrix.
		 * This should be noted when developing a pyramid client.
		 */
		
		final int dim = (width*height*2) + 4;
		ByteBuffer bytebuffer = ByteBuffer.allocate(dim).order(ByteOrder.LITTLE_ENDIAN);
		ShortBuffer buffer = bytebuffer.asShortBuffer();

		for (int i=0; i<width; i++) for (int j=0;j<height; j++){

			short data = (short) ((image.getRGB(j,i)) & 0x0000FFFF);
			buffer.put(i*height + j, data);
		}
		
		buffer.put(width*height,max);
		buffer.put((width*height)+1,min);
		
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
