# Spring security学习

## 最简单的配置

-   引入相关依赖后启动项目

-   在浏览器访问请求，将弹出认证窗口，认证账号用户名默认为user，密码为启动后在控制台输出的一个随机字符串

-   修改默认账号: 在 `application.yml`中配置如下账号信息

    ```yaml
    spring:
      security:
        user:
          name: dragonhht
          password: 123
    ```
    
## 表单认证

-   继承类`org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter`,重写方法`configure(HttpSecurity http)`

-   配置拦截及登录

```java
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                // 指定跳转的登录页
                .loginPage("/toLogin")
                // 指定登录请求的处理url
                .loginProcessingUrl("/login")
                // 登录成功时的处理逻辑
                .successHandler((request, response, authentication) -> {
                    // 登录成功时调用
                    /*response.setContentType("application/json;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.write("{\"code\": 200, \"message\": \"OK\", {\"name\": \"user\", \"id\": \"123\"}}");*/
                })
                // 登录失败时
                .failureHandler((request, response, exception) -> {
                    // 登录失败时调用
                })
                // 使登录不设置权限
                .permitAll()
                .and()
                .csrf().disable();
    }
}
```

## 权限拦截

-   针对不同的URL配置不同角色的用户，如

```
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            // 只有ADMIN角色才能访问/admin/**
            .antMatchers("/admin/**").hasAnyRole("ADMIN")
            // 只有USER角色才能访问/user/**
            .antMatchers("/user/**").hasAnyRole("USER")
            .antMatchers("/app/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin();
}
```

### 认证方式

-   内存中获取用户配置，配置如下

    -   使用InMemoryUserDetailsManager，他是UserDetailsService的一个实现类，可将用户信息存入内存中
    
    -   因Spring security5中新增加了加密方式，所以使用BCryptPasswordEncoder对密码进行加密

```java
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 只有ADMIN角色才能访问/admin/**
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                // 只有USER角色才能访问/user/**
                .antMatchers("/user/**").hasAnyRole("USER")
                .antMatchers("/app/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    protected UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                .password(new BCryptPasswordEncoder().encode("123"))
                .roles("USER")
                .build());

        manager.createUser(User.withUsername("admin")
                .password(new BCryptPasswordEncoder().encode("123"))
                .roles("ADMIN")
                .build());
        return manager;
    }
}
```

-   使用默认数据库模型

    -   Spring Security提供了一种基于JDBC方式获取用户信息的类`JdbcUserDetailsManager`
    
    -   使用JdbcUserDetailsManager时，Spring Security提供了一种默认的表结构，其SQL在`org/springframework/security/core/userdetails/jdbc/users.ddl`中
    
    -   如下配置
    
        -   首先通过`manager.setDataSource(dataSource);`配置数据源
        
        -   当调用`createUser`方法时，Spring Security会将创建的用户信息存入默认的表`user`和`authorities`中。
    
    ```java
    @EnableWebSecurity
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
        @Autowired
        private DataSource dataSource;
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    // 只有ADMIN角色才能访问/admin/**
                    .antMatchers("/admin/**").hasAnyRole("ADMIN")
                    // 只有USER角色才能访问/user/**
                    .antMatchers("/user/**").hasAnyRole("USER")
                    .antMatchers("/app/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin();
        }
    
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService())
                    .passwordEncoder(new BCryptPasswordEncoder());
        }
    
        /**
         * 基于默认数据库模型
         * @return
         */
        protected UserDetailsService userDetailsService() {
            JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
            // 配置数据源
            manager.setDataSource(dataSource);
            // 当user用户不存在时自动创建默认的user用户
            if (!manager.userExists("user")) {
                manager.createUser(User.withUsername("user")
                        .password(new BCryptPasswordEncoder().encode("123"))
                        .roles("USER")
                        .build());
            }
            // 当admin用户不存在时自动创建默认的admin用户
            if (!manager.userExists("admin")) {
                manager.createUser(User.withUsername("admin")
                        .password(new BCryptPasswordEncoder().encode("123"))
                        .roles("ADMIN")
                        .build());
            }
            return manager;
        }
    }
    ```
    
-   使用自定义数据库模型

    -   创建自定义[用户实体](./src/main/java/com/github/dragonhht/security/entity/User.java), 实现接口`org.springframework.security.core.userdetails.UserDetails`
    
    -   实现[自定义UserDetailsService](./src/main/java/com/github/dragonhht/security/service/MyUserDetailsService.java)，该类实现接口`org.springframework.security.core.userdetails.UserDetailsService`中的`loadUserByUsername`方法,具体如下
    
    ```java
    @Service
    public class MyUserDetailsService implements UserDetailsService {
    
        @Autowired
        private UserRepository userRepository;
    
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            User user = userRepository.findUserByUserName(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户不存在");
            }
            // 设置用户角色
            user.setAuthorities(getUserAuthorities(user.getRoles()));
            return user;
        }
    
        /**
         * 解析数据库中保存的角色
         * @param roles
         * @return
         */
        private List<GrantedAuthority> getUserAuthorities(String roles) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            String[] roleArr = roles.split(";");
            for (String role : roleArr) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            return authorities;
        }
    }
    ```
    
    -   配置加密方式
    
    ```java
    @EnableWebSecurity
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private MyUserDetailsService myUserDetailsService;
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    // 只有ADMIN角色才能访问/admin/**
                    .antMatchers("/admin/**").hasAnyRole("ADMIN")
                    // 只有USER角色才能访问/user/**
                    .antMatchers("/user/**").hasAnyRole("USER")
                    .antMatchers("/app/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin();
        }
    
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(myUserDetailsService)
                    .passwordEncoder(new BCryptPasswordEncoder());
        }
    }
    ```

### 通过自定义过滤器实现验证码校验

-   配置kaptcha生成并获取图片验证码，具体代码如下清单

    -   [配置kaptcha](./src/main/java/com/github/dragonhht/security/config/CaptchaConfig.java)
    
    -   [生成并获取图片验证码](./src/main/java/com/github/dragonhht/security/controller/CaptchaController.java)
    
    -   实现自定义Spring Security过滤器([验证码校验](./src/main/java/com/github/dragonhht/security/filter/VerificationCodeFilter.java)，继承OncePerRequestFilter确保请求只通过一次该过滤器)
    
    -   将自定义过滤器添加到Spring Security过滤器中,如下代码中的`http.addFilterBefore(new VerificationCodeFilter(), UsernamePasswordAuthenticationFilter.class);`
    
    ```
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 只有ADMIN角色才能访问/admin/**
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                // 只有USER角色才能访问/user/**
                .antMatchers("/user/**").hasAnyRole("USER")
                .antMatchers("/app/**", "/captcha.jpg").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                // 指定跳转的登录页
                .loginPage("/toLogin")
                // 指定登录请求的处理url
                .loginProcessingUrl("/login")
                .permitAll()
                .and()
                .csrf().disable();
        // 在UsernamePasswordAuthenticationFilter前添加验证码校验过滤器
        http.addFilterBefore(new VerificationCodeFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    ```
    
### 通过自定义认证实现验证码校验

   -    实现[自定义`WebAuthenticationDetails`](./src/main/java/com/github/dragonhht/security/details/MyWebAuthenticationDetails.java),从而在自定义认证中可获取除账号信息以外的信息(验证码)
   
   -    为自定义`WebAuthenticationDetails`创建[自定义`AuthenticationDetailsSource`](./src/main/java/com/github/dragonhht/security/details/MyWebAuthenticationDetailsSource.java)
   
   -    实现[自定义`AuthenticationProvider`](./src/main/java/com/github/dragonhht/security/provider/MyAuthenticationProvider.java)(此处继承`DaoAuthenticationProvider`)完成自定义认证逻辑
   
   -    配置相关信息如下:
        -   通过`.authenticationDetailsSource(myWebAuthenticationDetailsSource)`设置自定义的`WebAuthenticationDetails`
        
        -   通过`auth.authenticationProvider(authenticationProvider);`配置自定义认证的`AuthenticationProvider`
   
   ```java
    @EnableWebSecurity
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
        @Autowired
        private MyUserDetailsService myUserDetailsService;
        @Autowired
        private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> myWebAuthenticationDetailsSource;
        @Autowired
        private MyAuthenticationProvider authenticationProvider;
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    // 只有ADMIN角色才能访问/admin/**
                    .antMatchers("/admin/**").hasAnyRole("ADMIN")
                    // 只有USER角色才能访问/user/**
                    .antMatchers("/user/**").hasAnyRole("USER")
                    .antMatchers("/app/**", "/captcha.jpg").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    // 使用自定义AuthenticationDetailsSource
                    .authenticationDetailsSource(myWebAuthenticationDetailsSource)
                    // 指定跳转的登录页
                    .loginPage("/toLogin")
                    // 指定登录请求的处理url
                    .loginProcessingUrl("/login")
                    .permitAll()
                    .and()
                    .csrf().disable();
        }
    
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            // 使用自定义Provider
            auth.authenticationProvider(authenticationProvider);
        }
    
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
   ```

### 会话管理

#### 防御会话固定攻击

-   Spring Security内置了一个`sessionManagement`，提供了四种防御固定攻击的策略

    1.  `none`: 不做任何变动，登录后沿用旧的session
    
    2.  `newSession`: 登录之后创建一个新的session
    
    3.  `migrateSession`: 登录之后创建一个新的session，并将旧的session中的数据复制过来
    
    4.  `changeSessionId`: 不创建新的会话，而是使用由Servlet容器提供的会话固定保护
   
-   配置代码如下

```
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .formLogin()
            .and()
            // 设置防御固定攻击的策略
            .sessionManagement()
            .sessionFixation()
            .newSession()
            .and()
            .csrf().disable();
}
```

#### 会话过期

-   配置会话过期时跳转至某个URL
    
    ```
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .formLogin()
                .and()
                // 设置防御固定攻击的策略
                .sessionManagement()
                .sessionFixation()
                .newSession()
                // 过期时跳转
                .invalidSessionUrl("/session/invalid")
                .and()
                .csrf().disable();
    }
    ```

-   完全自定义过期策略

    -   通过实现`org.springframework.security.web.session.InvalidSessionStrategy`接口实现session过期时的执行逻辑,[如下所示](./src/main/java/com/github/dragonhht/security/strategy/MyInvalidSessionStrategy.java)
    
        ```java
        public class MyInvalidSessionStrategy implements InvalidSessionStrategy {
            @Override
            public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("Session已过期");
            }
        }
        ```

    -   配置Session失效策略
    
    ```
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .formLogin()
                .and()
                // 设置防御固定攻击的策略
                .sessionManagement()
                .sessionFixation()
                .newSession()
                // 配置Session失效策略
                .invalidSessionStrategy(new MyInvalidSessionStrategy())
                .and()
                .csrf().disable();
    }
    ```
    
#### 会话并发控制

-   配置统一用户session并发数

    -   使用`maximumSessions`可设置最大并发数
    
    -   使用`maxSessionsPreventsLogin`可设置到达最大并发数后，新的session的行为。为`true`时，将阻止新的session的创建；为`false`则新的session将会被创建，旧的则会被踢掉

    ```
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 只有ADMIN角色才能访问/admin/**
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                // 只有USER角色才能访问/user/**
                .antMatchers("/user/**").hasAnyRole("USER")
                .antMatchers("/app/**", "/captcha.jpg").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                // 设置migrateSession策略
                .sessionManagement()
                .sessionFixation()
                .newSession()
                .invalidSessionStrategy(new MyInvalidSessionStrategy())
                // 配置最大会话数
                .maximumSessions(1)
                // 到达最大会话数后不踢掉旧会话，而是阻止新会话建立
                .maxSessionsPreventsLogin(true);
    }
    ```

-   将类`org.springframework.security.web.session.HttpSessionEventPublisher`注册到IOC容器后，可将session的java事件转化为Spring事件。这样Spring Security便可监听session的创建和销毁

    ```
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
    ```

-   使用自定义数据库模型时需重写[用户实体](./src/main/java/com/github/dragonhht/security/entity/User.java)的`hashCode()`和`equals(Object obj)`方法

### 加密

-   声明PasswordEncoder

    ```
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }   
    ```

### 跨域

-   开启CORS

    -   使用`cors()`开启CORS

```
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            // 只有ADMIN角色才能访问/admin/**
            .antMatchers("/admin/**").hasAnyRole("ADMIN")
            // 只有USER角色才能访问/user/**
            .antMatchers("/user/**").hasAnyRole("USER")
            .antMatchers("/app/**", "/captcha.jpg").permitAll()
            .anyRequest().authenticated()
            .and()
            // 开启CORS
            .cors()
            .and()
            .formLogin()
            .and()
            // 设置migrateSession策略
            .sessionManagement()
            .sessionFixation()
            .newSession()
            //.invalidSessionUrl("/session/invalid")
            .invalidSessionStrategy(new MyInvalidSessionStrategy())
            // 配置最大会话数
            .maximumSessions(1)
            // 到达最大会话数后不踢掉旧会话，而是阻止新会话建立
            .maxSessionsPreventsLogin(true);
}
```

-   配置CORS

```
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    // 设置允许的跨域站点
    configuration.setAllowedOrigins(Arrays.asList("http://localhost"));
    // 允许使用的方法
    configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
    // 允许携带凭证
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    // 对所有URL生效
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```
