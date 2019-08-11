package com.vote.util;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

public class EmojiFilter{

    public static String filterEmoji(String source) {
        if (source == null) {
            return source;
        }
        Pattern emoji = compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\ue000-\uefff]",
                UNICODE_CASE | CASE_INSENSITIVE);
        Matcher emojiMatcher = emoji.matcher(source);
        if (emojiMatcher.find()) {
            source = emojiMatcher.replaceAll("*");
            return source;
        }
        return source;
    }

    public static boolean isNotEmojiCharacter(char codePoint)
    {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
    /**
     * 过滤emoji 或者 其他非文字类型的字符
     * @param source
     * @return
     */
    public static String filterEmoji2(String source)
    {
        if(StringUtils.isEmpty(source)){
            return "*";
        }
        int len = source.length();
        StringBuilder buf = new StringBuilder(len);
        for (int i = 0; i < len; i++)
        {
            char codePoint = source.charAt(i);
            if (isNotEmojiCharacter(codePoint))
            {
                buf.append(codePoint);
            } else{

                buf.append("*");

            }
        }
        return buf.toString();
    }
}