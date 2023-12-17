package com.chq.util;

import com.chq.pojo.dto.MsgDto;
import org.springframework.stereotype.Component;

@Component
public class SenderContext {


    public void strategySend(Sender sender,MsgDto dto) {
       sender.sendAuthCode(dto);
    }
}
