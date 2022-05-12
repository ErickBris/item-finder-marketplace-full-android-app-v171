package com.libraries.html;


public class MGHtml {

    static String[] encode = {"&#10;", "&quot;", "&#36;", "&#39;", "&#47;", "&#92;", "&#94;", "&#96;" };
    static String[] decode = {"\n",    "\"",     "$",     "'",     "/",     "\\",    "^",     "`" };

    public static String decodeToPlainText(String text) {

        String str = text;
        for(int x = 0; x < encode.length; x++) {
            str = str.replace(encode[x], "");
        }
        for(int x = 0; x < decode.length; x++) {
            str = str.replace(decode[x], "");
        }
        return str;
    }

    public static String decodeText(String text) {

        if(encode.length != decode.length)
            throw new ArithmeticException("MGHtml - encode and decode array content and length must be the same");

        String str = text;
        for(int x = 0; x < encode.length; x++) {
            str = str.replace(encode[x], decode[x]);
        }
        return str;
    }

    public static String encodeText(String text) {

        if(encode.length != decode.length)
            throw new ArithmeticException("MGHtml - encode and decode array content and length must be the same");

        String str = text;
        for(int x = 0; x < encode.length; x++) {
            str = str.replace(decode[x], encode[x]);
        }
        return str;
    }
}
