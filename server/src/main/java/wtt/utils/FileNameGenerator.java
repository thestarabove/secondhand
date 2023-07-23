package wtt.utils;

import java.util.UUID;

public class FileNameGenerator {
    public static String generateFileName() {
        UUID uuid=UUID.randomUUID();
        String name =uuid.randomUUID().toString().replace("-", "");
        return name.substring(0, 18);
    }

    }