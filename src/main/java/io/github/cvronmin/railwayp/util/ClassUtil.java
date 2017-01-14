package io.github.cvronmin.railwayp.util;

import java.util.Arrays;

public class ClassUtil {
	public static Class<?> getClassFromDescriptor(String descriptor) {
		if(descriptor.isEmpty()) return null;
		if (descriptor.equalsIgnoreCase("I"))
			return int.class;

		if (descriptor.equalsIgnoreCase("B"))
			return byte.class;

		if (descriptor.equalsIgnoreCase("C"))
			return char.class;

		if (descriptor.equalsIgnoreCase("D"))
			return double.class;

		if (descriptor.equalsIgnoreCase("F"))
			return float.class;

		if (descriptor.equalsIgnoreCase("J"))
			return long.class;

		if (descriptor.equalsIgnoreCase("S"))
			return short.class;

		if (descriptor.equalsIgnoreCase("Z"))
			return boolean.class;
		
		if(descriptor.startsWith("L") && descriptor.endsWith(";")){
			descriptor = descriptor.substring(1, descriptor.length() - 1);
			try {
				return Class.forName(descriptor);
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
		return null;
	}
}
