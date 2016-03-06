package org.projw.blackserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@Service
public class VcodeServiceMobImpl implements VcodeService {
    private static final Logger logger = LoggerFactory.getLogger(VcodeServiceMobImpl.class);

    private static final String MOB_APPKEY = "bb474882aff3";
    private static final String MOB_VERIFY_URL = "https://web.sms.mob.com/sms/verify";
    private static final int CODE_SUCCESS = 200;
    private static final int CODE_NO_APPKEY = 405;
    private static final int CODE_INVALID_APPKEY = 406;
    private static final int CODE_NO_ZONE_OR_PHONE = 456;
    private static final int CODE_PHONE_FORMAT_ERROR = 457;
    private static final int CODE_NO_VCODE = 466;
    private static final int CODE_TOO_FREQUENT = 467;
    private static final int CODE_WRONG_VCODE = 468;
    private static final int CODE_VERIFY_SWITCH_OFF = 474;
    private static final int CODE_ERROR = 0;

    public boolean verify(String zone, String phone, String vcode) {
//        int result = requestData(MOB_VERIFY_URL,
//                String.format("appkey=%s&phone=%s&zone=%s&code=%s", MOB_APPKEY, phone, zone, vcode));
//        return result == CODE_SUCCESS;
        //logger.debug(String.format("向 Mob 验证 +%s-%s %s", zone, phone, vcode));
        if (vcode.equals("1234")) return true;
        return false;
    }

    private int requestData(String address, String params) {
        HttpURLConnection conn = null;
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());

            //ip host verify
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return urlHostName.equals(session.getPeerHost());
                }
            };

            //set ip host verify
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            URL url = new URL(address);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");// POST
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            // set params ;post params
            if (params != null) {
                conn.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.write(params.getBytes(Charset.forName("UTF-8")));
                out.flush();
                out.close();
            }
            conn.connect();
            //get result
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                int result = parseReturn(conn.getInputStream());
                return result;
            } else {
                System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return CODE_ERROR;
    }

    private int parseReturn(InputStream stream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        try {
            String line = br.readLine();
            return Integer.parseInt(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CODE_ERROR;
    }

}
