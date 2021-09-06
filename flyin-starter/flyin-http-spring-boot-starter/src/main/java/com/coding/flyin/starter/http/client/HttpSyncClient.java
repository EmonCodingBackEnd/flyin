package com.coding.flyin.starter.http.client;

import com.coding.flyin.cmp.common.regex.RegexSupport;
import com.coding.flyin.cmp.common.regex.result.UrlParamRegexResult;
import com.coding.flyin.cmp.common.regex.result.UrlRegexResult;
import com.coding.flyin.starter.http.exception.HttpException;
import com.coding.flyin.starter.http.property.HttpMethod;
import com.coding.flyin.starter.http.support.HttpSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http同步执行客户端.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180607 07:48</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 0.1.0
 */
@Slf4j
public abstract class HttpSyncClient {

    /**
     * 该方法返回字符串格式的应答，因此会占用内存，不适用于应答内容超过1MB的请求.
     *
     * <p>特殊说明：paramMap与paramString二选一
     *
     * @param client - 自定义的同步HttpClient
     * @param httpMethod - 请求方法，参见{@linkplain HttpMethod}
     * @param url - 请求地址
     * @param paramMap - <key,value>格式的请求参数，仅限于POST、PUT、PATCH、DELETE方法
     * @param paramString - 字符串格式的请求参数，仅限于POST、PUT、PATCH、DELETE方法，适用于json、xml等请求参数
     * @param timeout - 超时时间，-1表示永不超时，小于-1会被重置为默认值，单位：毫秒
     * @param headers - 请求头
     * @param charset - 请求体编码
     * @param context - 上下文
     * @throws IOException -
     */
    public static String execute(
            HttpClient client,
            HttpMethod httpMethod,
            String url,
            Map<String, String> paramMap,
            String paramString,
            HttpContext context,
            int timeout,
            Header[] headers,
            Charset charset,
            Object... objects)
            throws IOException {
        Logger logInner =
                objects.length > 0 && objects[0] instanceof Logger ? (Logger) objects[0] : log;
        return execute(
                client,
                httpMethod,
                url,
                paramMap,
                paramString,
                context,
                timeout,
                headers,
                charset,
                new AbstractResponseHandler<String>() {
                    @Override
                    public String handleEntity(HttpEntity entity) throws IOException {
                        return EntityUtils.toString(entity, charset);
                    }
                },
                logInner);
    }

    /**
     * 无返回结果调用，应答数据会输出到给定的输出流，该方法不会自动关闭输出流.
     *
     * <p>特殊说明：paramMap与paramString二选一
     *
     * @param client - 自定义的同步HttpClient
     * @param httpMethod - 请求方法，参见{@linkplain HttpMethod}
     * @param url - 请求地址
     * @param paramMap - <key,value>格式的请求参数，仅限于POST、PUT、PATCH、DELETE方法
     * @param paramString - 字符串格式的请求参数，仅限于POST、PUT、PATCH、DELETE方法，适用于json、xml等请求参数
     * @param context - 上下文
     * @param timeout - 超时时间，-1表示永不超时，小于-1会被重置为默认值，单位：毫秒
     * @param headers - 请求头
     * @param charset - 请求体编码
     * @param outputStream - 存放应答的输出流
     * @throws IOException -
     */
    public static void execute(
            HttpClient client,
            HttpMethod httpMethod,
            String url,
            Map<String, String> paramMap,
            String paramString,
            HttpContext context,
            int timeout,
            Header[] headers,
            Charset charset,
            OutputStream outputStream,
            Object... objects)
            throws IOException {
        Logger logInner =
                objects.length > 0 && objects[0] instanceof Logger ? (Logger) objects[0] : log;
        execute(
                client,
                httpMethod,
                url,
                paramMap,
                paramString,
                context,
                timeout,
                headers,
                charset,
                new AbstractResponseHandler<String>() {

                    @Override
                    public String handleEntity(HttpEntity entity) throws IOException {
                        entity.writeTo(outputStream);
                        if (entity.isChunked()) {
                            logInner.info(
                                    "【Http】isStreaming={},isChunked={},isRepeatable={},Transfer-Encoding: chunked类型暂不支持应答内容大小的估算",
                                    entity.isStreaming(),
                                    entity.isChunked(),
                                    entity.isRepeatable());
                        } else {
                            logInner.info(
                                    "【Http】isStreaming={},isChunked={},isRepeatable={},应答内容大小={}",
                                    entity.isStreaming(),
                                    entity.isChunked(),
                                    entity.isRepeatable(),
                                    HttpSupport.getNetContentSize(entity.getContentLength()));
                        }
                        return null;
                    }
                },
                logInner);
    }

    /**
     * 基本请求方法，内部使用.
     *
     * <p>特殊说明：paramMap与paramString二选一
     *
     * @param client - 自定义的同步HttpClient
     * @param httpMethod - 请求方法，参见{@linkplain HttpMethod}
     * @param url - 请求地址
     * @param paramMap - <key,value>格式的请求参数，仅限于POST、PUT、PATCH、DELETE方法
     * @param paramString - 字符串格式的请求参数，仅限于POST、PUT、PATCH、DELETE方法，适用于json、xml等请求参数
     * @param context - 上下文
     * @param timeout - 超时时间，-1表示永不超时，小于-1会被重置为默认值，单位：毫秒
     * @param headers - 请求头
     * @param charset - 请求体编码
     * @param responseHandler - 应答处理器，用来处理应答数据
     * @param <T> - 处理后的应答数据返回类型
     * @return - 应答结果
     * @throws IOException -
     */
    public static <T> T execute(
            HttpClient client,
            HttpMethod httpMethod,
            String url,
            Map<String, String> paramMap,
            String paramString,
            HttpContext context,
            int timeout,
            Header[] headers,
            Charset charset,
            ResponseHandler<T> responseHandler,
            Object... objects)
            throws IOException {
        Logger logInner =
                objects.length > 0 && objects[0] instanceof Logger ? (Logger) objects[0] : log;
        Assert.notNull(url, "url must be not null");
        if (!CollectionUtils.isEmpty(paramMap) && !StringUtils.isEmpty(paramString)) {
            logInner.error(
                    "【Http】{}请求paramMap={}与paramString={}不能同时存在",
                    httpMethod.name(),
                    paramMap,
                    paramString);
            throw new HttpException("【Http】请求paramMap与paramString不能同时存在");
        }

        String uri;
        String param;
        List<NameValuePair> pairList = new ArrayList<>();

        boolean isXmlOrJson = false;

        UrlRegexResult urlRegexResult = RegexSupport.matchUrl(url);
        if (!urlRegexResult.isMatched()) {
            logInner.error("【Http】{}请求不合法url={}", httpMethod.name(), url);
            throw new HttpException("【Http】url不合法");
        }
        uri = urlRegexResult.getUri();
        String urlParam = urlRegexResult.getParam();
        if (!StringUtils.isEmpty(urlParam)) {
            List<NameValuePair> paramList =
                    HttpSupport.convertToPairList(HttpSupport.buildParams(urlParam));
            pairList.addAll(paramList);
        }

        if (CollectionUtils.isEmpty(paramMap) && StringUtils.isEmpty(paramString)) {
            param = urlParam;
        } else if (!CollectionUtils.isEmpty(paramMap) && StringUtils.isEmpty(paramString)) {
            List<NameValuePair> paramList = HttpSupport.convertToPairList(paramMap);
            pairList.addAll(paramList);
            param = URLEncodedUtils.format(pairList, charset);
        } else {
            UrlParamRegexResult urlParamRegexResult = RegexSupport.matchUrlParam(paramString);
            if (urlParamRegexResult.isMatched()) {
                List<NameValuePair> paramList =
                        HttpSupport.convertToPairList(
                                HttpSupport.buildParams(urlParamRegexResult.getParam()));
                pairList.addAll(paramList);
                param = URLEncodedUtils.format(pairList, charset);
            } else {
                isXmlOrJson = true;
                param = paramString;
            }
        }

        HttpRequestBase httpRequest = HttpMethod.getHttpRequest(url, httpMethod);
        httpRequest.setHeaders(headers);

        // 实现了接口HttpEntityEnclosingRequest的类，可以支持设置Entity
        if (HttpEntityEnclosingRequest.class.isAssignableFrom(httpRequest.getClass())) {
            if (isXmlOrJson) {
                ((HttpEntityEnclosingRequestBase) httpRequest)
                        .setEntity(new StringEntity(paramString, charset));
            } else {
                ((HttpEntityEnclosingRequestBase) httpRequest)
                        .setEntity(new UrlEncodedFormEntity(pairList, charset));
            }
        } else {
            try {
                if (!StringUtils.isEmpty(param)) {
                    httpRequest.setURI(new URI(String.format("%s?%s", uri, param)));
                }
            } catch (URISyntaxException e) {
                logInner.error("【Http】请求地址解析错误", e);
                throw new HttpException(e);
            }
        }
        if (timeout != Integer.MIN_VALUE) {
            if (timeout < -1) {
                timeout = 5000;
            }
            RequestConfig requestConfig =
                    RequestConfig.custom()
                            .setConnectTimeout(timeout)
                            .setSocketTimeout(timeout)
                            .setConnectionRequestTimeout(timeout)
                            .build();
            httpRequest.setConfig(requestConfig);
        }

        logInner.info("【Http】{}请求url={},params={}", httpMethod.name(), uri, param);

        return execute(client, httpRequest, charset, responseHandler, context, logInner);
    }

    private static <T> T execute(
            HttpClient client,
            HttpRequestBase httpRequest,
            Charset charset,
            ResponseHandler<T> responseHandler,
            HttpContext context,
            Object... objects)
            throws IOException {
        Logger logInner =
                objects.length > 0 && objects[0] instanceof Logger ? (Logger) objects[0] : log;
        T result = client.execute(httpRequest, responseHandler, context);
        if (result != null) {
            if (Long.class.isAssignableFrom(result.getClass())
                    || Integer.class.isAssignableFrom(result.getClass())) {
                logInner.info("【Http】应答内容大小={}", HttpSupport.getNetContentSize((Long) result));
            } else {
                if (logInner.isDebugEnabled()) {
                    logInner.debug(
                            "【Http】应答内容大小={},应答内容={}",
                            HttpSupport.getNetContentSize(
                                    result.toString().getBytes(charset).length),
                            result);
                } else {
                    logInner.info(
                            "【Http】应答内容大小={}",
                            HttpSupport.getNetContentSize(
                                    result.toString().getBytes(charset).length));
                }
            }
        }
        return result;
    }
}
