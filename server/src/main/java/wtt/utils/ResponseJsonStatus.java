package wtt.utils;

public enum ResponseJsonStatus {
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 操作失败
     */
    FAILURE(500, "操作失败"),
    /**
     * 请求错误
     */
    REQUEST_ERROR(100, "请求错误"),
    /**
     * 服务端程序错误
     */
    SERVER_INTERNAL_ERROR(501, "服务端内部错误"),
    /**
     * 服务类错误
     */
    SERVICE_EXCEPTION(600, "业务逻辑错误");
    /**
     * 状态码
     */
    private int status;
    /**
     * 信息描述
     */
    private String message;
    ResponseJsonStatus () {
    }
    ResponseJsonStatus (int status, String message) {
        this.status = status;
        this.message = message;
    }
    public int getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
}