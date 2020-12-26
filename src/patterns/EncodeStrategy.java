package patterns;

import oop.Message;

public interface EncodeStrategy {
    public String encode(String body);
    public String decode(String body);
}
