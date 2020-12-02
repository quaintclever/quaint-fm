package com.github.quaintclever.test.mq.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * desc: 消息dto
 * </p>
 *
 * @author quaint
 * @since 02 December 2020
 */
@Data
public class MessageDto {

    private String messageId;
    private String messageData;
    private LocalDateTime createTime;

}
