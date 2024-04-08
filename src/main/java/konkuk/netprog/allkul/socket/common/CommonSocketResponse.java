package konkuk.netprog.allkul.socket.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommonSocketResponse {

    private String status;
    private String msg;

    @Override
    public String toString(){
        return status + " : " + msg;
    }
}
