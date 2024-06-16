package dev.masterflomaster1.sjc;

import java.net.URL;

public class ResourceHandler {

    public static URL get(String path) {
        return ResourceHandler.class.getResource(path);
    }

}
