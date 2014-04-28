package com.cog.lang;
public class Value {

    public static Value VOID = new Value(new Object());
    public static Value BREAK = new Value(new Object());

    final Object value;

    public Value(Object value) {
        this.value = value;
    }

	public Integer asInteger() {
		
		if(value instanceof Integer) {
			return (Integer)value;
		}
		else if(value instanceof String) {
			return Integer.parseInt((String)value);
		}
		else {
			throw new TypeConversionException(Integer.class, value);
		}
	}

    public Boolean asBoolean() {
        return (Boolean)value;
    }

    public Double asDouble() {
		if(value instanceof Integer) {
			return ((Integer)value).doubleValue();
		}
		else if (value instanceof Double) {
			return (Double)value;
		}
		else {
			throw new TypeConversionException(Double.class, value);
		}
    }

    public String asString() {
        return String.valueOf(value);
    }

    public boolean isString() {
        return value instanceof String;
    }

    public boolean isDouble() {
        return value instanceof Double;
    }
    
    public boolean isInteger() {
        return value instanceof Integer;
    }

    public boolean isNumber() {
        return isInteger() || isDouble();
    }

    @Override
    public int hashCode() {

        if(value == null) {
            return 0;
        }

        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if(value == o) {
            return true;
        }

        if(value == null || o == null || o.getClass() != value.getClass()) {
            return false;
        }

        Value that = (Value)o;

        return this.value.equals(that.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

	public Number asNumber() {
		return (Number)value;
	}

	public Value add(Value right) {
		int intValue = isInteger() ? asInteger() : 0;
		double doubleValue = isDouble() ? asDouble() : 0.0;
		
		int rIntValue = right.isInteger() ? right.asInteger() : 0;
		double rDoubleValue = right.isDouble() ? right.asDouble() : 0.0;
		
		Value v;
		if(isDouble()) {
			v=new Value(doubleValue + rIntValue + rDoubleValue);
		}
		else if(isInteger() && right.isInteger()) {
			v=new Value(intValue + rIntValue);
		}
		else {
			throw new TypeConversionException(Integer.class, right.value);
		}
		return v;
	}
	public Value subtract(Value right) {
		int intValue = isInteger() ? asInteger() : 0;
		double doubleValue = isDouble() ? asDouble() : 0.0;
		
		int rIntValue = right.isInteger() ? right.asInteger() : 0;
		double rDoubleValue = right.isDouble() ? right.asDouble() : 0.0;
		
		Value v;
		if(isDouble()) {
			v=new Value(doubleValue - rIntValue - rDoubleValue);
		}
		else if(isInteger() && right.isInteger()) {
			v=new Value(intValue - rIntValue);
		}
		else {
			throw new TypeConversionException(Integer.class, right.value);
		}
		return v;
	}
	public Value multiply(Value right) {
		int intValue = isInteger() ? asInteger() : 0;
		double doubleValue = isDouble() ? asDouble() : 0.0;
		
		int rIntValue = right.isInteger() ? right.asInteger() : 0;
		double rDoubleValue = right.isDouble() ? right.asDouble() : 0.0;
		
		Value v;
		if(isDouble() && right.isDouble()) {
			v=new Value(doubleValue * rDoubleValue);
		}
		else if(isDouble() && right.isInteger()) {
			v=new Value(doubleValue * rIntValue);
		}
		else if(isInteger() && right.isInteger()) {
			v=new Value(intValue * rIntValue);
		}
		else {
			v=new Value(intValue * rDoubleValue);
		}
		return v;
	}
	public Value divide(Value right) {
		int intValue = isInteger() ? asInteger() : 0;
		double doubleValue = isDouble() ? asDouble() : 0.0;
		
		int rIntValue = right.isInteger() ? right.asInteger() : 0;
		double rDoubleValue = right.isDouble() ? right.asDouble() : 0.0;
		
		Value v;
		if(isDouble() && right.isDouble()) {
			v=new Value(doubleValue / rDoubleValue);
		}
		else if(isDouble() && right.isInteger()) {
			v=new Value(doubleValue / rIntValue);
		}
		else if(isInteger() && right.isInteger()) {
			v=new Value(intValue / rIntValue);
		}
		else {
			v=new Value(intValue / rDoubleValue);
		}
		return v;
	}
	public Value mod(Value right) {
		int intValue = isInteger() ? asInteger() : 0;
		int rIntValue = right.isInteger() ? right.asInteger() : 0;
		
		Value v;
		if(isInteger() && right.isInteger()) {
			v=new Value(intValue % rIntValue);
		}
		else {
			throw new TypeConversionException(value.getClass(), right.value);
		}
		return v;
	}

	public Value GTEQ(Value right) {
		int intValue = isInteger() ? asInteger() : 0;
		double doubleValue = isDouble() ? asDouble() : 0.0;
		
		int rIntValue = right.isInteger() ? right.asInteger() : 0;
		double rDoubleValue = right.isDouble() ? right.asDouble() : 0.0;
		
		Value v;
		if(isDouble() && right.isDouble()) {
			v=new Value(doubleValue >= rDoubleValue);
		}
		else if(isDouble() && right.isInteger()) {
			v=new Value(doubleValue >= rIntValue);
		}
		else if(isInteger() && right.isInteger()) {
			v=new Value(intValue >= rIntValue);
		}
		else {
			v=new Value(intValue >= rDoubleValue);
		}
		return v;
	}
	public Value LTEQ(Value right) {
		int intValue = isInteger() ? asInteger() : 0;
		double doubleValue = isDouble() ? asDouble() : 0.0;
		
		int rIntValue = right.isInteger() ? right.asInteger() : 0;
		double rDoubleValue = right.isDouble() ? right.asDouble() : 0.0;
		
		Value v;
		if(isDouble() && right.isDouble()) {
			v=new Value(doubleValue <= rDoubleValue);
		}
		else if(isDouble() && right.isInteger()) {
			v=new Value(doubleValue <= rIntValue);
		}
		else if(isInteger() && right.isInteger()) {
			v=new Value(intValue <= rIntValue);
		}
		else {
			v=new Value(intValue <= rDoubleValue);
		}
		return v;
	}
	public Value LT(Value right) {
		int intValue = isInteger() ? asInteger() : 0;
		double doubleValue = isDouble() ? asDouble() : 0.0;
		
		int rIntValue = right.isInteger() ? right.asInteger() : 0;
		double rDoubleValue = right.isDouble() ? right.asDouble() : 0.0;
		
		Value v;
		if(isDouble() && right.isDouble()) {
			v=new Value(doubleValue < rDoubleValue);
		}
		else if(isDouble() && right.isInteger()) {
			v=new Value(doubleValue < rIntValue);
		}
		else if(isInteger() && right.isInteger()) {
			v=new Value(intValue < rIntValue);
		}
		else {
			v=new Value(intValue < rDoubleValue);
		}
		return v;
	}
	
	public Value GT(Value right) {
		int intValue = isInteger() ? asInteger() : 0;
		double doubleValue = isDouble() ? asDouble() : 0.0;
		
		int rIntValue = right.isInteger() ? right.asInteger() : 0;
		double rDoubleValue = right.isDouble() ? right.asDouble() : 0.0;
		
		Value v;
		if(isDouble() && right.isDouble()) {
			v=new Value(doubleValue > rDoubleValue);
		}
		else if(isDouble() && right.isInteger()) {
			v=new Value(doubleValue > rIntValue);
		}
		else if(isInteger() && right.isInteger()) {
			v=new Value(intValue > rIntValue);
		}
		else {
			v=new Value(intValue > rDoubleValue);
		}
		return v;
	}

	public Value eq(Value right) {
		int intValue = isInteger() ? asInteger() : 0;
		double doubleValue = isDouble() ? asDouble() : 0.0;
		
		int rIntValue = right.isInteger() ? right.asInteger() : 0;
		double rDoubleValue = right.isDouble() ? right.asDouble() : 0.0;
		
		Value v;
		if(isDouble() && right.isDouble()) {
			v=new Value(doubleValue == rDoubleValue);
		}
		else if(isDouble() && right.isInteger()) {
			v=new Value(doubleValue == rIntValue);
		}
		else if(isInteger() && right.isInteger()) {
			v=new Value(intValue == rIntValue);
		}
		else {
			v=new Value(intValue == rDoubleValue);
		}
		return v;
	}
	public Value neq(Value right) {
		int intValue = isInteger() ? asInteger() : 0;
		double doubleValue = isDouble() ? asDouble() : 0.0;
		
		int rIntValue = right.isInteger() ? right.asInteger() : 0;
		double rDoubleValue = right.isDouble() ? right.asDouble() : 0.0;
		
		Value v;
		if(isDouble() && right.isDouble()) {
			v=new Value(doubleValue != rDoubleValue);
		}
		else if(isDouble() && right.isInteger()) {
			v=new Value(doubleValue != rIntValue);
		}
		else if(isInteger() && right.isInteger()) {
			v=new Value(intValue != rIntValue);
		}
		else {
			v=new Value(intValue != rDoubleValue);
		}
		return v;
	}
	
}

