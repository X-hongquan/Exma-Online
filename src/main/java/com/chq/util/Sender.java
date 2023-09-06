package com.chq.util;

import com.chq.pojo.dto.MsgDto;

public interface Sender {

    boolean sendAuthCode(MsgDto msgDto);

    boolean sendNotice(MsgDto msgDto);
}
