package io.github.cvronmin.railwayp.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyUnlimitedInteger implements IUnlistedProperty<Integer> {
    private final String name;
    private final Predicate<Integer> validator;
    
    public PropertyUnlimitedInteger(String name) {
		this(name, Predicates.<Integer>alwaysTrue());
	}
    public PropertyUnlimitedInteger(String name, Predicate<Integer> predicate) {
		this.name = name;
		this.validator = predicate;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public boolean isValid(Integer value) {
		// TODO Auto-generated method stub
		return validator.apply(value);
	}

	@Override
	public Class<Integer> getType() {
		// TODO Auto-generated method stub
		return Integer.class;
	}

	@Override
	public String valueToString(Integer value) {
		// TODO Auto-generated method stub
		return value.toString();
	}

}
