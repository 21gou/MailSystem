package patterns;

import oop.Message;

/**
 * Defines methods for encoding the body of messages
 */
public interface EncodeStrategy {
    /**
     * Encode messages with a certain strategy
     * @param body
     * @return
     */
    public String encode(String body);

    /**
     * Decode messages with a certain strategy
     * @param body
     * @return
     */
    public String decode(String body);
}
