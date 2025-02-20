package movlit.be.common.util;

import java.util.Map;
import movlit.be.auth.application.service.GoogleOAuth2UserInfo;
import movlit.be.auth.application.service.KakaoOAuth2UserInfo;
import movlit.be.auth.application.service.NaverOAuth2UserInfo;
import movlit.be.auth.application.service.OAuth2UserInfo;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String provider, Map<String, Object> attributes) {
        return switch (provider.toLowerCase()) {
            case "google" -> new GoogleOAuth2UserInfo(attributes);
            case "naver" -> new NaverOAuth2UserInfo(attributes);
            case "kakao" -> new KakaoOAuth2UserInfo(attributes);
            default -> throw new OAuth2AuthenticationException("올바른 OAuth 형식이 아닙니다.");
        };
    }

}
