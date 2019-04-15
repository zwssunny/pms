package com.foresight.pms.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class ShiroConfig {

	// @Value("${adminPath}")
	private String adminPath;

	// 注入自定义的realm，告诉shiro如何获取用户信息来做登录或权限控制
	@Bean
	public Realm realm() {
		// return new SystemAuthorizingRealm();
		return new AuthorizingRealm() {

			@Override
			protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
					throws AuthenticationException {
				// TODO Auto-generated method stub
				SimpleAuthenticationInfo info = new SimpleAuthenticationInfo();
				return info;
			}

			@Override
			protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
				// TODO Auto-generated method stub
				SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
				return info;
			}
		};
	}

	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
		/**
		 * setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
		 * 在@Controller注解的类的方法中加入@RequiresRole注解，会导致该方法无法映射请求，导致返回404。 加入这项配置能解决这个bug
		 */
		creator.setUsePrefix(true);
		return creator;
	}

	@Bean
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(realm());
		return securityManager;
	}

	/**
	 * 这里统一做鉴权，即判断哪些请求路径需要用户登录，哪些请求路径不需要用户登录。 这里只做鉴权，不做权限控制，因为权限用注解来做。
	 * 
	 * @return
	 */
	@Bean
	public ShiroFilterChainDefinition shiroFilterChainDefinition() {
		DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();
		chain.addPathDefinition("/static/**", "anon");
		chain.addPathDefinition("/userfiles/**", "anon");
		chain.addPathDefinition(adminPath + "/sys/user/infoCareStatus", "anon");
		chain.addPathDefinition(adminPath + "/sys/user/validateLoginName", "anon");
		chain.addPathDefinition(adminPath + "/sys/user/validateMobile", "anon");
		chain.addPathDefinition(adminPath + "/sys/user/validateMobileExist", "anon");
		chain.addPathDefinition(adminPath + "/sys/user/resetPassword", "anon");
		chain.addPathDefinition(adminPath + "/sys/register", "anon");
		chain.addPathDefinition(adminPath + "/sys/register/registerUser", "anon");
		chain.addPathDefinition(adminPath + "/sys/register/getRegisterCode", "anon");
		chain.addPathDefinition(adminPath + "/sys/register/validateMobileCode", "anon");
		chain.addPathDefinition(adminPath + "/soft/sysVersion/getAndroidVer", "anon");
		chain.addPathDefinition(adminPath + "/soft/sysVersion/getIosVer", "anon");
		chain.addPathDefinition(adminPath + "/cas", "cas");
		chain.addPathDefinition(adminPath + "/login", "authc");
		chain.addPathDefinition(adminPath + "/logout", "anon");
		chain.addPathDefinition(adminPath + "/**	", "user");
		chain.addPathDefinition("/act/rest/service/editor/**", "perms[act:model:edit]");
		chain.addPathDefinition("/act/rest/service/model/**", "perms[act:model:edit]");
		chain.addPathDefinition("/act/rest/service/**", "user");
		chain.addPathDefinition("/ReportServer/**", "user");
		return chain;
	}
}