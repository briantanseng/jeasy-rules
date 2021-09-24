package jeasy.rules;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class Utils {
    public static Reader getReader(String string) {
        InputStream is = new ByteArrayInputStream( string.getBytes(StandardCharsets.UTF_8) );
        return new InputStreamReader(is);
    }
}
