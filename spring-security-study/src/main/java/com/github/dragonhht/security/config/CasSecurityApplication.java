package com.github.dragonhht.security.config;

import com.github.dragonhht.security.service.MyUserDetailsService;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.Arrays;

/**
 * CAS.
 *
 * @author: dragonhht
 * @Date: 2019-11-22
 */
@Configuration
@Order(1)
public class CasSecurityApplication {

    @Value("${cas.server.prefix}")
    private String casServerPrefix;
    @Value("${cas.server.login}")
    private String casServerLogin;
    @Value("${cas.server.logout}")
    private String casServerLogout;
    @Value("${cas.client.login}")
    private String casClientLogin;
    @Value("${cas.client.logout.url}")
    private String casClientLogout;
    @Value(("${cas.client.logout.relative}"))
    private String casClientLogoutRelative;
    @Value("${cas.user.inmemory}")
    private String casUserInmemory;

    /**
     *配置CAS Client属性
     * @return
     */
    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties properties = new ServiceProperties();
        properties.setService(casClientLogin);
        // 是否关闭单点登录，默认为false
        properties.setSendRenew(false);
        return properties;
    }

    @Bean
    @Primary
    public AuthenticationEntryPoint authenticationEntryPoint(ServiceProperties serviceProperties) {
        CasAuthenticationEntryPoint authenticationEntryPoint = new CasAuthenticationEntryPoint();
        // CAS Server认证的登录地址
        authenticationEntryPoint.setLoginUrl(casServerLogin);
        authenticationEntryPoint.setServiceProperties(serviceProperties);
        return authenticationEntryPoint;
    }

    /**
     * ticket校验
     * @return
     */
    @Bean
    public TicketValidator ticketValidator() {
        return new Cas20ProxyTicketValidator(casServerPrefix);
    }

    /**
     * CAS验证处理逻辑
     * @param serviceProperties
     * @param ticketValidator
     * @param userDetailsService
     * @return
     */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider(ServiceProperties serviceProperties,
                                                               TicketValidator ticketValidator,
                                                               MyUserDetailsService userDetailsService) {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setServiceProperties(serviceProperties);
        provider.setTicketValidator(ticketValidator);
        provider.setUserDetailsService(userDetailsService);
        provider.setKey("dragon");
        return provider;
    }

    /**
     * CAS验证过滤器
     * @param serviceProperties
     * @param provider
     * @return
     */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter(ServiceProperties serviceProperties,
                                                           AuthenticationProvider provider) {
        CasAuthenticationFilter filter = new CasAuthenticationFilter();
        filter.setServiceProperties(serviceProperties);
        filter.setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
        return filter;
    }

    /**
     * 接收CAS服务发出的注销请求
     * @return
     */
    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter filter = new SingleSignOutFilter();
        filter.setCasServerUrlPrefix(casServerPrefix);
        filter.setIgnoreInitConfiguration(true);
        return filter;
    }

    /**
     * 将注销请求转发到CAS Server
     * @return
     */
    @Bean
    public LogoutFilter logoutFilter() {
        LogoutFilter filter = new LogoutFilter(casServerLogout, new SecurityContextLogoutHandler());
        filter.setFilterProcessesUrl(casClientLogoutRelative);
        return filter;
    }
}
