package twino.util;

import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;

public class HttpHelper {

    private HttpHelper() {
    }

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";

    public static String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader(X_FORWARDED_FOR);
        if (!Strings.isNullOrEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",", 2)[0];
        }
        return request.getRemoteAddr();
    }
}
