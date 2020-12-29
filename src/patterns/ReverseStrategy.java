package patterns;

/**
 * Encode message by reversing the body
 */
public class ReverseStrategy implements EncodeStrategy {
    @Override
    public String decode(String body) {
        return new StringBuilder(body).reverse().toString();
    }

    @Override
    public String encode(String body) {
        return decode(body);
    }
}
