package com.eiisys.ipcc.bean;


import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;


/**
 * web层通用返回对象
 * @author 
 *
 */
@SuppressWarnings("rawtypes")
@Getter
@Setter
public  class GenericResponse implements Serializable {
	
	
	/**
	 * Serial version UID for the class to uniquely identify the object during serialization process
	 */
	public static final long serialVersionUID = 1L;
	
	/**
	 * An array that contains the actual objects
	 */
	private Collection rows;
	
	/**
	 * An Map that contains the actual objects
	 */
	private Map mapData;
	
	/**
	 * An Object that contains the actual value
	 */
	private Object data;
	
	/**
	 * A String containing response code.
	 */
	private String code;
	
	/**
	 * An int that contains the count of all records
	 */
	private int count;
	
	/**
	 * A String containing message.
	 */
	private String message;

	/** 成功返回 true, 失败返回false */
	private boolean result = false;

	public GenericResponse() {
	}

	public GenericResponse(Collection rows, String code, int count, String message, Map mapData) {
		this.rows = rows;
		this.code = code;
		this.count = count;
		this.message = message;
		this.mapData = mapData;
	}

	public GenericResponse(Collection rows, String code, int count, String message) {
		this.rows = rows;
		this.code = code;
		this.count = count;
		this.message = message;
	}

	public GenericResponse(Map mapData, String code, int count, String message) {
		this.mapData = mapData;
		this.code = code;
		this.count = count;
		this.message = message;
	}
}
