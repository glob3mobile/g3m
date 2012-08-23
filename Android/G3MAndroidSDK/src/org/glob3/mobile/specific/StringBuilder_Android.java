package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IStringBuilder;

public class StringBuilder_Android extends IStringBuilder {
	
	String res = "";

	@Override
	protected IStringBuilder getNewInstance() {
		return new StringBuilder_Android();
	}

	@Override
	public IStringBuilder add(double d) {
		res += d;
		return this;
	}

	@Override
	public IStringBuilder add(String s) {
		res += s;
		return this;
	}

	@Override
	public IStringBuilder addBool(boolean b) {
		res += b;
		return this;
	}

	@Override
	public String getString() {
		return res;
	}

}
