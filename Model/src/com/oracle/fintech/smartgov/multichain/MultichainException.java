package com.oracle.fintech.smartgov.multichain;

public class MultichainException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8184535545216565397L;

	public MultichainException(String object, String reason){
		System.out.println("Error "+ object +" "+reason);
	}
}
