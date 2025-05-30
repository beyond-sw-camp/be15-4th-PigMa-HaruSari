package com.pigma.harusari.common.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoUserInfo {

    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakao_account;

    @Getter
    @Builder
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter
        @Builder
        public static class Profile {
            private String nickname;
        }
    }
}
