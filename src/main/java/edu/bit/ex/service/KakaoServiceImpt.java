package edu.bit.ex.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import edu.bit.ex.vo.KakaoAuth;
import edu.bit.ex.vo.KakaoProfile;

@Service
public class KakaoServiceImpt {

	private final static String K_CLIENT_ID = "5e5475f98de7e76956c75cd2bf4de2f7";
	private final static String K_REDIRECT_URI = "http://localhost:9595/ex/kakaologin";

	public static String getAuthorizationUrl() {
		String kakaoUrl = "https://kauth.kakao.com/oauth/authorize?" + "client_id=" + K_CLIENT_ID + "&redirect_uri="
				+ K_REDIRECT_URI + "&response_type=code";
		return kakaoUrl;
	}

	private final static String K_TOKEN_URI = "https://kauth.kakao.com/oauth/token";

	public KakaoAuth getKakaoTokenInfo(String code) {

		RestTemplate restTemplate = new RestTemplate();
		
		// Set header : Content-type: application/x-www-form-urlencoded
		HttpHeaders headers = new HttpHeaders();
		// headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		// Set parameter
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", K_CLIENT_ID);
		params.add("redirect_uri", K_REDIRECT_URI);
		params.add("code", code);

		// Set http entity
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(K_TOKEN_URI, kakaoTokenRequest, String.class);

		System.out.println(response.getBody());
		System.out.println(response.getStatusCodeValue());

		Gson gson = new Gson();
		// Gson ,Json Simple, ObjectMapper
		if (response.getStatusCode() == HttpStatus.OK) {
			return gson.fromJson(response.getBody(), KakaoAuth.class);
		}

		return null;

	}

	private final static String K_PROFILE_URI = "https://kapi.kakao.com/v2/user/me";

	// https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
	public KakaoProfile getKakaoProfile(String accessToken) {

		RestTemplate restTemplate = new RestTemplate();// http ��û�� �����ϰ� ���� �� �ִ� Ŭ����

		// Set header : Content-type: application/x-www-form-urlencoded
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer " + accessToken);

		// Set http entity
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
		Gson gson = new Gson();
		try {
			// Request profile
			ResponseEntity<String> response = restTemplate.postForEntity(K_PROFILE_URI, request, String.class);

			if (response.getStatusCode() == HttpStatus.OK)
				System.out.println(response.getBody());
			KakaoProfile profile = gson.fromJson(response.getBody(), KakaoProfile.class);
			System.out.println(profile);
			return profile;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
