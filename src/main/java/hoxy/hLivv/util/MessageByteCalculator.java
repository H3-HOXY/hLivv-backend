package hoxy.hLivv.util;

public class MessageByteCalculator {
    public static int byteCalc(String message) {
//        String message = "안녕하세요 저는 lee입니다.";
        int totalBytes = 0;

        for (char ch : message.toCharArray()) {
            // 한글 범위를 확인하여 한글인 경우 2바이트를, 그렇지 않으면 1바이트를 더한다.
            if (isKorean(ch)) {
                totalBytes += 2; // 한글인 경우
            } else {
                totalBytes += 1; // ASCII 문자인 경우
            }
        }
        return totalBytes;
    }



    private static boolean isKorean(char ch) {
        return (ch >= '\uAC00' && ch <= '\uD7A3') || (ch >= '\u1100' && ch <= '\u11FF') || (ch >= '\u3130' && ch <= '\u318F');
    }
}
