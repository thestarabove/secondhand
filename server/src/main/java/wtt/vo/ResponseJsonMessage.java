package wtt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wtt.utils.ResponseJsonStatus;

@Data

public class ResponseJsonMessage {
    private static final long serialVersionUID=-7127875856370230011L;
    private int status=20000;
    private String message;
    private Object data;

    public ResponseJsonMessage () {
    }

    public ResponseJsonMessage (int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseJsonMessage (int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

 public static ResponseJsonMessage ok(){return  new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(), (Object) null);
    }

    @Override
    public String toString() {
        return "ResponseJsonMessage{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
