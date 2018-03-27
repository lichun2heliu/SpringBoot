package com.kotei.core.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/1
 * 修改历史：
 * 1. [2018/3/1]创建文件
 * 统一返回类
 * @author chunl
 */
@Getter
@Setter
public class ResultEntity<T> {

    /**
     * 返回枚举值内部类
     */
    public enum ResultEnum {
        @ApiModelProperty(value="0-成功",name="SUCCESS",required=false)
        SUCCESS(0, "成功"),
        @ApiModelProperty(value="100-失败",name="FAILED",required=false)
        FAILED(100, "失败"),
        @ApiModelProperty(value="101-参数错误",name="PARAMETER_ERROR",required=false)
        PARAMETER_ERROR(101, "参数错误"),
        @ApiModelProperty(value="102-程序内部错误",name="INTERNAL_ERROR",required=false)
        INTERNAL_ERROR(102, "程序内部错误"),
        @ApiModelProperty(value="103-输入信息重复",name="PRECONDITION_FAILED",required=false)
        PRECONDITION_FAILED(103,"输入信息重复");

        /**
         * code值
         */
        private int code;

        /**
         * msg错误消息
         */
        private String message;


        ResultEnum(int code, String message) {
            this.code = code;
            this.message = message;
        }


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

    }

    /**
     * 返回状态
     */
    @ApiModelProperty(value="返回状态",name="resultStatus",required=false)
    private ResultEnum resultStatus;

    /**
     * 如果失败，返回消息
     */
    @ApiModelProperty(value="如果失败，返回消息",name="message",required=false)
    private String message;

    /**
     * 返回data数据
     */
    @ApiModelProperty(value="返回data数据",name="data",required=false)
    public T data;
    /**
     * 成功
     *
     * @return
     */
    public static ResultEntity success() {
        return new ResultEntity(ResultEnum.SUCCESS);
    }

    /**
     * 失败
     *
     * @param message 返回错误信息
     * @return
     */
    public static ResultEntity fail(String message) {
        return new ResultEntity(ResultEnum.FAILED, message);
    }

    public ResultEntity(ResultEnum resultStatus) {
        this.resultStatus = resultStatus;
    }

    public ResultEntity(ResultEnum resultStatus, String message) {
        this.resultStatus = resultStatus;
        this.message = message;
    }

//    public ResultEnum getResultStatus() {
//        return resultStatus;
//    }
//
//    public void setResultStatus(ResultEnum resultStatus) {
//        this.resultStatus = resultStatus;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public T getData() {
//        return data;
//    }
//
//    public void setData(T data) {
//        this.data = data;
//    }
}
