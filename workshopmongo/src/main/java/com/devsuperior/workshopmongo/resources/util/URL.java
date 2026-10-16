package com.devsuperior.workshopmongo.resources.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static java.nio.charset.StandardCharsets.UTF_8;

public class URL {

    public static String decodeParam(String text) {
        try {
            return URLDecoder.decode(text, UTF_8);
        } catch (Exception e) {
            return "";
        }
    }
}
