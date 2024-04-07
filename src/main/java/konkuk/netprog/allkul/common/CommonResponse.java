package konkuk.netprog.allkul.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class CommonResponse {
    private boolean isSuccess;
    HttpStatus status;
    private String msg;
    private List object;

    public CommonResponse(boolean isSuccess, String msg) {
        this.isSuccess = isSuccess;
        this.msg = msg;
    }

    public CommonResponse(boolean isSuccess, HttpStatus status, String msg) {
        this.isSuccess = isSuccess;
        this.status = status;
        this.msg = msg;
    }

    public CommonResponse(boolean isSuccess, HttpStatus status, String msg, List object) {
        this.isSuccess = isSuccess;
        this.status = status;
        this.msg = msg;
        this.object = object;
    }
}