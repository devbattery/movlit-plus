package movlit.be.auth.application.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Slf4j
@NoArgsConstructor
public class MyMemberDetails implements UserDetails, OAuth2User {

    // 공통
    @Getter
    private MemberId memberId;

    // 로컬 로그인
    @Getter
    private Member member;

    // 소셜 로그인
    private Map<String, Object> attributes;

    public MyMemberDetails(Member member) {
        this(member, Collections.emptyMap());
    }

    public MyMemberDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.memberId = member.getMemberId();
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(member.getRole()));
    }

    @Override
    public String getPassword() {
        return member.getMemberId().getValue();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public String getName() {
        return member.getNickname();
    }

    // 버전 문제로 기존 default 메서드 추가
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}