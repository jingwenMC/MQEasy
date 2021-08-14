package top.jingwenmc.mqeasy.api.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonMessage<T> {
    private String plugin;
    private String id;
    private MessageType messageType;
    /**
     * Where the message from, must be a server id
     */
    private String from;
    /**
     * Where the message to, can be a player or a server id
     */
    private String to;
    private T body;
}
