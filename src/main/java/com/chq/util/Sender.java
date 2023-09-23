package com.chq.util;

import com.chq.pojo.dto.MsgDto;

import java.util.concurrent.ExecutionException;

public interface Sender {

    boolean sendAuthCode(MsgDto msgDto);

    boolean sendNotice(MsgDto msgDto);
}
