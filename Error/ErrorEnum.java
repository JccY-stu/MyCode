package com.yang.bioDPointObject.Error;



/**
 * 
 * @Title: CommonEnum
 * @Description: 公用描述枚举类
 * @Version:1.0.0
 * @author pancm
 */
public enum ErrorEnum implements InfoInterface {
	// 数据操作错误定义
	NOT_FOUND_USER(801,"该用户名不存在!"),
	ALREADY_EXIT_USER(802,"该用户名已经存在！请重新选择！")
	;

	/** 错误码 */
	private int resultCode;

	/** 错误描述 */
	private String resultMsg;

	ErrorEnum(int resultCode, String resultMsg) {
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

	@Override
	public int getResultCode() {
		return resultCode;
	}

	@Override
	public String getResultMsg() {
		return resultMsg;
	}

}
