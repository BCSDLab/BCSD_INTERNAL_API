package com.bcsdlab.internal.auth;


import java.util.Arrays;
import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.bcsdlab.internal.auth.exception.AuthException;
import com.bcsdlab.internal.auth.exception.AuthExceptionType;
import com.bcsdlab.internal.member.Member;
import com.bcsdlab.internal.member.MemberRepository;

import static java.util.Objects.requireNonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;
    private final AuthContext authContext;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Auth authAt = parameter.getParameterAnnotation(Auth.class);
        requireNonNull(authAt);
        List<Authority> permitStatus = Arrays.asList(authAt.permit());
        Member member = memberRepository.getById(authContext.getMemberId());
        if (permitStatus.contains(member.getAuthority())) {
            return member.getId();
        }
        throw new AuthException(AuthExceptionType.INVALID_TOKEN);
    }
}
