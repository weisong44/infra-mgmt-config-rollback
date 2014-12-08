package util;

import models.Context;

public class ContextHolder {

	public static final ThreadLocal<Context> holder = new ThreadLocal<>();
	
	static public Context get() {
		Context ctx = holder.get();
		if(ctx == null) {
			ctx = new Context();
			holder.set(ctx);
		}
		return ctx;
	}

}
