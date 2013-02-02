package cz.nkp.differ.rest.auth;

import cz.nkp.differ.dao.UserDAOImpl;
import cz.nkp.differ.exceptions.UserDifferException;
import cz.nkp.differ.model.User;
import cz.nkp.differ.user.UserManager;
import java.util.Collections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author xrosecky
 */
public class DBAuthenticationManager implements AuthenticationProvider, ApplicationContextAware {

    @Autowired
    private UserManager userManager;

    public DBAuthenticationManager() {
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
	String password = (String) auth.getCredentials();
	String principal = (String) auth.getPrincipal();
	try {
	    User user = userManager.attemptLogin(principal, password);
	    return new UsernamePasswordAuthenticationToken(
		    auth.getPrincipal(),
		    auth.getCredentials(),
		    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
	} catch (UserDifferException ude) {
	    throw new BadCredentialsException("Bad username or password.");
	}
    }

    @Override
    public boolean supports(Class<?> type) {
	return type.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext appCtx) throws BeansException {
	
    }

}
