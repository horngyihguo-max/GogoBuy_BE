package com.example.demo.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class NoticeDTO {
    private String msg;
    private Integer minutes;
    
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime time;

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public Integer getMinutes() { return minutes; }
    public void setMinutes(Integer minutes) { this.minutes = minutes; }
    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
}
