package org.glob3.mobile.specific;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.glob3.mobile.generated.IStringUtils;

public class StringUtils_WebGL extends IStringUtils {

	@Override
	public String createString(byte[] data, int length) {
		try {
			return new String(data, "UTF8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	@Override
	public ArrayList<String> splitLines(String String) {
		String lines[] = String.split("\\r?\\n");
		ArrayList<String> l = new ArrayList<String>();
		for(int i = 0; i < lines.length; i++) {
			l.add(lines[i]);
		}
		
		return l;
	}

	@Override
	public boolean beginsWith(String String, String prefix) {
		return String.startsWith(prefix);
	}

	@Override
	public int indexOf(String String, String search) {
		return String.indexOf(search);
	}

	@Override
	public String substring(String String, int beginIndex, int endIndex) {
		return String.substring(beginIndex, endIndex);
	}

	@Override
	public String rtrim(String String) {
		int index = String.length()-1;
		while(index > 0 && String.charAt(index) == ' ') {
			index--;
		}
		return String.substring(0, index);
	}

	@Override
	public String ltrim(String String) {
		int index = 0;
		while(index < String.length() && String.charAt(index) == ' ') {
			index++;
		}
		return String.substring(index, String.length());
	}

}
