package com.yanhuanxy.multifuncommon.enums.multcompertition;

/**
 * socket 信息类型枚举
 * @author yym
 */
public enum MessageTypeEnums {
	ERROR(201,"出错"),
	ADD_USER(1, "建立连接"),
	RE_ADD_USER(2, "重新加入"),
	CHOOSE_USER(3, "匹配队友"),
	INIT_ROOM(4, "初始化房间"),
	DO_ANSWER(5, "答题"),
	ANSWER_END(6,"答题已结束");

	private Integer code;
	private String value;

	MessageTypeEnums(Integer code, String value) {
		this.code = code;
		this.value = value;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 校验code是否存在
	 * @param code
	 * @return
	 */
	public static boolean exist(Integer code) {
		MessageTypeEnums[] values = MessageTypeEnums.values();
		for (MessageTypeEnums messageTypeEnums : values) {
			if (messageTypeEnums.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}

}
