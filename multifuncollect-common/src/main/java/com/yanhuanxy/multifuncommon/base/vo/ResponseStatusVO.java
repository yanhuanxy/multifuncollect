package com.yanhuanxy.multifuncommon.base.vo;


/**
 * 响应状态
 * @author hxl
 */
public class ResponseStatusVO {

	/**
	 * 请求状态（true：成功；false：失败）
	 */
	private boolean flag;
	/**
	 * 状态码
	 */
	private String code;

	/**
	 * 描述信息
	 */
	private String message;
	
	public ResponseStatusVO() {
	}
	
	public ResponseStatusVO(boolean flag, String code, String message) {
		this.flag = flag;
		this.code = code;
		this.message = message;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
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
