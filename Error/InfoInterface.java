package com.yang.bioDPointObject.Error;

/**
 * 
* @Title: BaseErrorInfoInterface
* @Description: 基础接口
*  自定义的错误描述枚举类需实现该接口
* @Version:1.0.0  
* @author pancm
*/
public interface InfoInterface {
    /** 状态码
     * @return*/
	 int getResultCode();

	/** 状态描述
	 * @return
	 */
	 String getResultMsg();
}
