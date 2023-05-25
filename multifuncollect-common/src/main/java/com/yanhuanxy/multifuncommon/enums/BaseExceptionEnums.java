package com.yanhuanxy.multifuncommon.enums;


import com.yanhuanxy.multifuncommon.constant.ResponseConst;

/**
 * 自定义异常枚举
 */
public enum BaseExceptionEnums {
	FAILURE(ResponseConst.CODE_FAILURE, ResponseConst.MSG_FAILURE); // 失败

	private String code;
	private String message;

	BaseExceptionEnums(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
