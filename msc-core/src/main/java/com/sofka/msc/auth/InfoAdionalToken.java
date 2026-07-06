package com.sofka.msc.auth;

import com.sofka.msc.entity.CustomerEntity;
import com.sofka.msc.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <b> Descripcion de la clase, interface o enumeracion. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Component
public class InfoAdionalToken implements TokenEnhancer {

	@Autowired
	private ICustomerService userService;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		CustomerEntity customer = userService.findCustomerByUsername(authentication.getName()).orElse(null);
		if (customer != null) {
			Map<String, Object> additionalInformation = new HashMap<>();
			additionalInformation.put("status", customer.getStatus());
			additionalInformation.put("name", customer.getPerson().getName());
			additionalInformation.put("lastname", customer.getPerson().getLastname());
			additionalInformation.put("username", customer.getUsername());
			additionalInformation.put("identification", customer.getPerson().getIdentification());

			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
		}
		return accessToken;
	}
}
