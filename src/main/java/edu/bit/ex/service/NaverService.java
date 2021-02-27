package edu.bit.ex.service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

@Service
public class NaverService {
	private final static String CLIENT_ID = "Yr2flc6oKdlo0Bf0AAjs";
	private final static String CLIENT_SECRET = "acukMmPl30";
	private final static String REDIRECT_URI = "http://localhost:9595/ex/navercallback";

	private final static String PROFILE_API_URL = "https://openapi.naver.com/v1/nid/me";

	public String generateState() {
		// 난수 생성
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

	public String getAuthorizationUrl(HttpSession session) {
		String state = generateState();
		session.setAttribute("state", state);

		OAuth20Service oauthService = new ServiceBuilder().apiKey(CLIENT_ID).apiSecret(CLIENT_SECRET)
				.callback(REDIRECT_URI).state(state).build(NaverLoginApi.instance());

		return oauthService.getAuthorizationUrl();

	}

	public OAuth2AccessToken getAccessToken(HttpSession session, String code, String state) throws IOException {
		String sessionState = (String) session.getAttribute("state");
		if (StringUtils.pathEquals(sessionState, state)) {
			OAuth20Service oauthService = new ServiceBuilder().apiKey(CLIENT_ID).apiSecret(CLIENT_SECRET)
					.callback(REDIRECT_URI).state(state).build(NaverLoginApi.instance());

			OAuth2AccessToken accessToken = oauthService.getAccessToken(code);
			return accessToken;
		}
		return null;
	}

	/* Access Token을 이용하여 네이버 사용자 프로필 API를 호출 */
	public String getUserProfile(OAuth2AccessToken oauthToken) throws IOException{

      OAuth20Service oauthService =new ServiceBuilder()
            .apiKey(CLIENT_ID)
            .apiSecret(CLIENT_SECRET)
            .callback(REDIRECT_URI).build(NaverLoginApi.instance());

      OAuthRequest request = new OAuthRequest(Verb.GET, PROFILE_API_URL, oauthService);
      oauthService.signRequest(oauthToken, request);
      Response response = request.send();
      
      return response.getBody();
	}
}
