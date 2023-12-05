package com.shopping2.status;

import lombok.Data;

@Data
public class Message {

    private StatusEnum status;
    private String message;
    private Object data;

    public Message(StringBuilder sb) {
        this.status = StatusEnum.BAD_REQUEST;
        this.message = null;
        this.data = sb;
    }

    public Message(StatusEnum statusEnum, String message, Object data) {
        this.status = statusEnum;
        this.message = message;
        this.data = data;
    }

}
