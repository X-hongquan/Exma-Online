package com.chq.util.impl;// This file is auto-generated, don't edit it. Thanks.


import cn.hutool.core.util.RandomUtil;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;

import com.aliyun.sdk.service.dysmsapi20170525.models.*;
import com.aliyun.sdk.service.dysmsapi20170525.*;
import com.chq.pojo.dto.MsgDto;
import com.chq.util.Sender;
import com.google.gson.Gson;

import darabonba.core.client.ClientOverrideConfiguration;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component(value = "sms")
public class SendSms implements Sender {


    @Resource
    private StringRedisTemplate stringRedisTemplate;

   private static final String KEY="LTAI5tCP5mqHfMusx2qgbzUw";
   private static final String PWD="Wi1epMS4T1N8QJrzO0gbEArEqi6SCx";

   private StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
            .accessKeyId("LTAI5tCgiPiGzZ1i6Nhcn5Dn")
            .accessKeySecret("dVCqftz8dLe7nZFJbqIQSh6fobHGDI")
            .build());

  private AsyncClient client = AsyncClient.builder()
            .region("cn-shenzhen")
            .credentialsProvider(provider)
            .overrideConfiguration(
                    ClientOverrideConfiguration.create()
                            .setEndpointOverride("dysmsapi.aliyuncs.com")
            )
            .build();




    @Override
    public boolean sendAuthCode(MsgDto msgDto)  {
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName("阿里云短信测试")
                .templateCode("SMS_154950909")
                .phoneNumbers(msgDto.getReceiver())
                .templateParam("{\"code\":\""+ msgDto.getContent()+"\"}")
                .build();
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        SendSmsResponse resp = null;
        try {
            resp = response.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println(new Gson().toJson(resp));
        client.close();
        return true;
    }

    @Override
    public boolean sendNotice(MsgDto msgDto) {
        return false;
    }
}