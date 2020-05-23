package com.oem.nms.websocket.listen;

import com.oem.nms.common.mq.entity.WebsocketMessage;
import com.oem.nms.common.mq.util.MqConstants;
import com.oem.nms.common.util.JsonUtil;
import com.oem.nms.websocket.util.WebsocketConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

/**
 * @author Seaway John
 */
@Slf4j
@Component
@RabbitListener(
        bindings = @QueueBinding(
                exchange = @Exchange(value = MqConstants.MQ_EXCHANGE_WEBSOCKET, type = ExchangeTypes.DIRECT, autoDelete = "false"),
                key = MqConstants.MQ_ROUTING_KEY_WEBSOCKET_SNMP_TRAP,
                value = @Queue(value = MqConstants.MQ_QUEUE_WEBSOCKET_SNMP_TRAP, autoDelete = "false")
        )
)
public class SnmpTrapMqReceiver {

    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public SnmpTrapMqReceiver(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitHandler
    public void handle(WebsocketMessage websocketMessage) {
        log.info("Receive snmp trap mq {}", websocketMessage);

        messagingTemplate.convertAndSend(WebsocketConstants.WEBSOCKET_TOPIC_SNMP_TRAP, JsonUtil.toJson(websocketMessage));
    }

}
