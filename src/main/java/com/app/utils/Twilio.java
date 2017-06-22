package com.app.utils;

import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.CallCreator;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by dhruv.suri on 19/06/17.
 */
public class Twilio {

    // Find your Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "ACaeeb3058cb3d6165dd420ff6b39ca115";
    public static final String AUTH_TOKEN = "545ccd9e8263433884342fed6d1cc273";

    public static void main(String[] args) {
        TwilioRestClient client = new TwilioRestClient.Builder(ACCOUNT_SID, AUTH_TOKEN).build();
        CallCreator callCreator = null;
        try {
            callCreator = new CallCreator(new PhoneNumber("+919052000970"), new PhoneNumber("+12057379064"), new URI("http://google.com"));
            callCreator.create(client);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
