package com.example.MyTranslator.Transl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Request
{
    String text;
    String lang;

    public Request(String text, String lang)
    {
        this.text = text;
        this.lang = lang;
    }

    String format = "plain";

    String key = "trnsl.1.1.20200409T070900Z.543eb1f10ccf94ae.c2f85e3688e9da7d851a923f2fd862c4892eab6c";

    public byte[] toByteArray() throws UnsupportedEncodingException
    {
        if(text == null || text.equals("")) throw new IllegalArgumentException("Не введён текс!");
        String data = "format=" + format + "&key=" + key + "&text=" + URLEncoder.encode(text, StandardCharsets.UTF_8.toString()) + "&lang=" + lang;
        return data.getBytes();
    }
}