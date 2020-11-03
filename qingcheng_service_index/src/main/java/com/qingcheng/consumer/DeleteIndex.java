package com.qingcheng.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class DeleteIndex implements MessageListener {
    @Override
    public void onMessage(Message message) {

    }
}
