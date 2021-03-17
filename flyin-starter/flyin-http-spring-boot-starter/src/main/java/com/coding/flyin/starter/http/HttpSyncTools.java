package com.coding.flyin.starter.http;

import com.coding.flyin.starter.http.client.HttpSyncClient;
import com.coding.flyin.starter.http.property.HttpMethod;
import com.coding.flyin.starter.http.support.HeaderSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Http工具.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180607 12:07</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 0.1.0
 */
@Slf4j
public class HttpSyncTools {

    private CloseableHttpClient httpSyncClient;

    public HttpSyncTools(CloseableHttpClient httpSyncClient) {
        this.httpSyncClient = httpSyncClient;
    }

    // ==============================GET-Beg==============================

    public String doGet(String url) throws IOException {
        return doGet(url, new HashMap<>());
    }

    public String doGet(String url, int timeout) throws IOException {
        return doGet(url, new HashMap<>(), timeout);
    }

    public String doGet(String url, Map<String, String> paramMap) throws IOException {
        return doGet(url, paramMap, Integer.MIN_VALUE);
    }

    public String doGet(String url, Map<String, String> paramMap, int timeout) throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.GET,
                url,
                paramMap,
                null,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    public String doGet(String url, String paramString) throws IOException {
        return doGet(url, paramString, Integer.MIN_VALUE);
    }

    public String doGet(String url, String paramString, int timeout) throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.GET,
                url,
                null,
                paramString,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    // ==============================GET-End==============================

    // ==============================HEAD-Beg==============================

    public String doHead(String url) throws IOException {
        return doHead(url, new HashMap<>());
    }

    public String doHead(String url, int timeout) throws IOException {
        return doHead(url, new HashMap<>(), timeout);
    }

    public String doHead(String url, Map<String, String> paramMap) throws IOException {
        return doHead(url, paramMap, Integer.MIN_VALUE);
    }

    public String doHead(String url, Map<String, String> paramMap, int timeout) throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.HEAD,
                url,
                paramMap,
                null,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    public String doHead(String url, String paramString) throws IOException {
        return doHead(url, paramString, Integer.MIN_VALUE);
    }

    public String doHead(String url, String paramString, int timeout) throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.HEAD,
                url,
                null,
                paramString,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    // ==============================HEAD-End==============================

    // ==============================POST-Beg==============================

    // ----------------------------------------Segment-1-Beg-----------------------------------------
    public String doPost(String url) throws IOException {
        return doPost(url, new HashMap<>());
    }

    public String doPost(String url, int timeout) throws IOException {
        return doPost(url, new HashMap<>(), timeout);
    }

    public String doPost(String url, Map<String, String> paramMap) throws IOException {
        return doPost(url, paramMap, Integer.MIN_VALUE);
    }

    public String doPost(String url, Map<String, String> paramMap, int timeout) throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.POST,
                url,
                paramMap,
                null,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    public String doPost(String url, String paramString) throws IOException {
        return doPost(url, paramString, Integer.MIN_VALUE);
    }

    public String doPost(String url, String paramString, int timeout) throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.POST,
                url,
                null,
                paramString,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    public String doPostJson(String url, String paramString) throws IOException {
        return doPostJson(url, paramString, Integer.MIN_VALUE);
    }

    public String doPostJson(String url, String paramString, int timeout) throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.POST,
                url,
                null,
                paramString,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_JSON_UTF8_HEADERS,
                StandardCharsets.UTF_8);
    }

    public String doPostXml(String url, String paramString) throws IOException {
        return doPostXml(url, paramString, Integer.MIN_VALUE);
    }

    public String doPostXml(String url, String paramString, int timeout) throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.POST,
                url,
                null,
                paramString,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_XML_UTF8_HEADERS,
                StandardCharsets.UTF_8);
    }

    // ==============================POST-End==============================

    // ==============================PUT-Beg==============================

    public String doPut(String url) throws IOException {
        return doPut(url, new HashMap<>());
    }

    public String doPut(String url, int timeout) throws IOException {
        return doPut(url, new HashMap<>(), timeout);
    }

    public String doPut(String url, Map<String, String> paramMap) throws IOException {
        return doPut(url, paramMap, Integer.MIN_VALUE);
    }

    public String doPut(String url, Map<String, String> paramMap, int timeout) throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.PUT,
                url,
                paramMap,
                null,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    public String doPut(String url, String paramString) throws IOException {
        return doPut(url, paramString, Integer.MIN_VALUE);
    }

    public String doPut(String url, String paramString, int timeout) throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.PUT,
                url,
                null,
                paramString,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    // ==============================PUT-End==============================

    // ==============================PATCH-Beg==============================

    public String doPatch(String url) throws IOException {
        return doPatch(url, new HashMap<>());
    }

    public String doPatch(String url, int timeout) throws IOException {
        return doPatch(url, new HashMap<>(), timeout);
    }

    public String doPatch(String url, Map<String, String> paramMap) throws IOException {
        return doPatch(url, paramMap, Integer.MIN_VALUE);
    }

    public String doPatch(String url, Map<String, String> paramMap, int timeout)
            throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.PATCH,
                url,
                paramMap,
                null,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    public String doPatch(String url, String paramString) throws IOException {
        return doPatch(url, paramString, Integer.MIN_VALUE);
    }

    public String doPatch(String url, String paramString, int timeout) throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.PATCH,
                url,
                null,
                paramString,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    // ==============================PATCH-End==============================

    // ==============================DELETE-Beg==============================

    public String doDelete(String url) throws IOException {
        return doDelete(url, new HashMap<>());
    }

    public String doDelete(String url, int timeout) throws IOException {
        return doDelete(url, new HashMap<>(), timeout);
    }

    public String doDelete(String url, Map<String, String> paramMap) throws IOException {
        return doDelete(url, paramMap, Integer.MIN_VALUE);
    }

    public String doDelete(String url, Map<String, String> paramMap, int timeout)
            throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.DELETE,
                url,
                paramMap,
                null,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    public String doDelete(String url, String paramString) throws IOException {
        return doDelete(url, paramString, Integer.MIN_VALUE);
    }

    public String doDelete(String url, String paramString, int timeout) throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.DELETE,
                url,
                null,
                paramString,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    // ==============================DELETE-End==============================

    // ==============================OPTIONS-Beg==============================

    public String doOptions(String url) throws IOException {
        return doOptions(url, new HashMap<>());
    }

    public String doOptions(String url, int timeout) throws IOException {
        return doOptions(url, new HashMap<>(), timeout);
    }

    public String doOptions(String url, Map<String, String> paramMap) throws IOException {
        return doOptions(url, paramMap, Integer.MIN_VALUE);
    }

    public String doOptions(String url, Map<String, String> paramMap, int timeout)
            throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.OPTIONS,
                url,
                paramMap,
                null,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    public String doOptions(String url, String paramString) throws IOException {
        return doOptions(url, paramString, Integer.MIN_VALUE);
    }

    public String doOptions(String url, String paramString, int timeout) throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.OPTIONS,
                url,
                null,
                paramString,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    // ==============================OPTIONS-End==============================

    // ==============================TRACE-Beg==============================

    public String doTrace(String url) throws IOException {
        return doTrace(url, new HashMap<>());
    }

    public String doTrace(String url, int timeout) throws IOException {
        return doTrace(url, new HashMap<>(), timeout);
    }

    public String doTrace(String url, Map<String, String> paramMap) throws IOException {
        return doTrace(url, paramMap, Integer.MIN_VALUE);
    }

    public String doTrace(String url, Map<String, String> paramMap, int timeout)
            throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.TRACE,
                url,
                paramMap,
                null,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    public String doTrace(String url, String paramString) throws IOException {
        return doTrace(url, paramString, Integer.MIN_VALUE);
    }

    public String doTrace(String url, String paramString, int timeout) throws IOException {
        return HttpSyncClient.execute(
                this.httpSyncClient,
                HttpMethod.TRACE,
                url,
                null,
                paramString,
                null,
                timeout,
                HeaderSupport.KEEP_ALIVE_ENCODED_HEADERS,
                StandardCharsets.UTF_8);
    }

    // ==============================TRACE-End==============================

}
