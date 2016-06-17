package com.kiwi.auready_ver2.rest_service;

import com.kiwi.auready_ver2.login.LoginContract;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Response;

import static org.mockito.Mockito.verify;

/**
 * Created by kiwi on 6/14/16.
 */
public class SignupServiceTest {

    private MockWebServer server;
    @Mock
    private LoginContract.UserActionsListener mLoginPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        server = new MockWebServer();
        server.start();
        RestServiceConstants.BASE_URL = server.url("/").toString();
    }

    @Test
    public void signupSuccessResponse() throws Exception {

        ISignupService signupService = ServiceGenerator.createService(ISignupService.class);

        // Create the signupInfo stub
        String email = "dd@gmail.com";
        String password = "123";
        SignupInfo signupInfo = new SignupInfo(email, password);

        // Actual Test
        Call<SignupResponse> signupCall = signupService.signupLocal(signupInfo);
        Response<SignupResponse> signupResponse = signupCall.execute();

        // Asserting response
        Assert.assertTrue(signupResponse.isSuccessful());
        Assert.assertEquals(email, signupResponse.body().getEmail());
    }
    @Test
    public void whenSignupFail_showFailMessage() throws Exception {

        String email = "dd@gmail.com";
        String password = "123";
        //mLoginPresenter.onRequestSignup(email, password);

        int statusCode = 400;
        // if received response 400 code,
        server.enqueue(new MockResponse()
        .setResponseCode(statusCode));

        verify(mLoginPresenter).onSignupFail();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}
