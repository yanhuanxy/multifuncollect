package com.yanhuanxy.multifuncommon.enums.multcompertition;

/**
 * socket 用户状态
 * @author yym
 */
public enum CompetitionStatusEnums {
	IDLE(1, "空闲中"),
	IN_COMPARE(2, "竞赛中"),
	COMPARE_OVER(2, "竞赛完成");

	private Integer code;
	private String value;

	CompetitionStatusEnums(Integer code, String value) {
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
		CompetitionStatusEnums[] values = CompetitionStatusEnums.values();
		for (CompetitionStatusEnums messageTypeEnums : values) {
			if (messageTypeEnums.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 通过编码转换枚举
	 *
	 * @param code
	 */
	public static CompetitionStatusEnums getCompetitionStatus(Integer code) {
		for (CompetitionStatusEnums competitionStatusEnums : values()) {
			if (competitionStatusEnums.getCode().equals(code)) {
				return competitionStatusEnums;
			}
		}
		return IDLE;
	}

}
