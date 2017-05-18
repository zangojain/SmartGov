package com.oracle.fintech.smartgov.multichain;

public class MultichainTestParameter {

	public static void stringIsNotNullOrEmpty(String name, String string) throws MultichainException{
		if (string != null) {
			if (string.isEmpty()) {
				throw new MultichainException(name, "is null or empty.");
			}
		} else {
			throw new MultichainException(name, "is null or empty.");
		}
	}
	
	public static void arrayIsNotNullOrEmpty(String name, Object[] array) throws MultichainException{
		if(array != null){
			if(array.length<=0){
				throw new MultichainException(name, "size is 0.");
			}
		}else{
			throw new MultichainException(name, "is null.");
		}
	}
	
	public static void arrayNotContainNullOrEmptyValues(String name,Object[] array) throws MultichainException{
		for (Object object : array) {
			if(object==null){
				throw new MultichainException(name, "array contain null value.");			
			}else if(object.getClass()==String.class){
				if(((String)object).isEmpty()){
					throw new MultichainException(name, "array contain empty string.");				}
			}
		}
	}
	
	public static void floatArrayContainNullOrNegativeValue(String name, float[]array) throws MultichainException{
		for (float f : array) {
			if(f<0){
				throw new MultichainException(name, "array contain negative value.");
			}else if(f ==0){
				throw new MultichainException(name, "array contain null values.");
			}
		}
	}
	
	public static void intArrayContainNullOrNegativeValue(String name, int[]array) throws MultichainException{
		for (int i : array) {
			if(i<0){
				throw new MultichainException(name, "array contain negative value.");
			}else if(i ==0){
				throw new MultichainException(name, "array contain null values.");
			}
		}
	}
	
	public static void intValueIsPositive(String name,int value) throws MultichainException{
		if(value < 0){
			throw new MultichainException(name	, "is negative.");
		}else if (value ==0){
			throw new MultichainException(name, "is null.");
		}
	}
	
	public static void floatValueIsPositive(String name,float value) throws MultichainException{
		if(value < 0){
			throw new MultichainException(name	, "is negative.");
		}else if (value ==0){
			throw new MultichainException(name, "is null.");
		}
	}
	
}
