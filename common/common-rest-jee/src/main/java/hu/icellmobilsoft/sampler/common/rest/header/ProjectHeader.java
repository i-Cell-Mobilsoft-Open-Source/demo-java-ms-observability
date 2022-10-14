/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 i-Cell Mobilsoft Zrt.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package hu.icellmobilsoft.sampler.common.rest.header;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.enterprise.inject.Vetoed;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.StringUtils;

import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.sampler.dto.constant.IHttpHeaderConstants;

/**
 * Project header class
 * 
 * @author imre.scheffer
 * @since 0.1.0
 */
@Vetoed
public class ProjectHeader implements IHttpHeaderConstants {

    /**
     * Http port separator
     */
    private static final String HOST_PORT_SEPARATOR = ":";
    /**
     * Empty value
     */
    public static final String EMPTY_VALUE = "empty";
    /**
     * Http header "For" key
     */
    private static final String FORWARDED_FOR_TAG = "for=";
    /**
     * Http port max length for validation
     */
    private static final int HOST_PORT_MAX_LENGTH = 15;

    /**
     * Session token value
     */
    private String sessionToken;
    /**
     * Refresh token value
     */
    private String refreshToken;
    /**
     * Host value
     */
    private String host;
    /**
     * Source value
     */
    private String source;
    /**
     * Language value
     */
    private String language;
    /**
     * Forwarder value
     */
    private String forwarded;
    /**
     * Forwarded for host value
     */
    private String forwardedForHost;
    /**
     * Forwarded for port value
     */
    private String forwardedForPort;

    /**
     * Read http header into bean fields
     * 
     * @param headers
     *            http header
     * @return header bean with filled fields
     */
    public static ProjectHeader readHeaders(HttpHeaders headers) {
        if (headers == null) {
            return null;
        }
        return readHeaders(headers.getRequestHeaders());
    }

    /**
     * Read http header into bean fields
     * 
     * @param headerMap
     *            http header values map
     * @return header bean with filled fields
     */
    public static ProjectHeader readHeaders(MultivaluedMap<String, String> headerMap) {
        ProjectHeader projectHeader = new ProjectHeader();
        if (headerMap != null) {

            String host = getHeaderValue(headerMap, HEADER_HOST, false);
            if (StringUtils.isBlank(host)) {
                host = getHeaderValue(headerMap, HEADER_XHOST, false);
            }
            projectHeader.setHost(host);

            projectHeader.setLanguage(getHeaderValue(headerMap, HEADER_LANGUAGE, false));
            projectHeader.setSessionToken(getHeaderValue(headerMap, HEADER_SESSION_TOKEN, true));
            projectHeader.setRefreshToken(getHeaderValue(headerMap, HEADER_REFRESH_TOKEN, false));
            projectHeader.setSource(getHeaderValue(headerMap, HEADER_SOURCE, false));
            projectHeader.setForwarded(getHeaderValue(headerMap, HEADER_FORWARDED, false));
        }
        return projectHeader;
    }

    private void handleForwardedHeader(String headerValue) {
        String host = null;
        String port = null;
        String hostPort = headerValue;
        if (StringUtils.isNotBlank(headerValue) && StringUtils.contains(headerValue, FORWARDED_FOR_TAG)) {
            String[] forwarded = StringUtils.split(headerValue, ";");
            Optional<String> o = Arrays.stream(forwarded).filter(s -> StringUtils.startsWithIgnoreCase(StringUtils.trim(s), FORWARDED_FOR_TAG))
                    .findFirst();
            if (o.isPresent()) {
                hostPort = StringUtils.substringAfter(o.get(), FORWARDED_FOR_TAG);
            }
        }
        if (StringUtils.contains(hostPort, HOST_PORT_SEPARATOR)) {
            host = StringUtils.trim(StringUtils.substringBefore(hostPort, HOST_PORT_SEPARATOR));
            port = StringUtils.trim(StringUtils.substringAfter(hostPort, HOST_PORT_SEPARATOR));
        } else {
            host = StringUtils.trim(hostPort);
        }
        setForwardedForHost(StringUtils.defaultString(StringUtils.left(host, HOST_PORT_MAX_LENGTH), EMPTY_VALUE));
        setForwardedForPort(StringUtils.defaultString(StringUtils.left(port, HOST_PORT_MAX_LENGTH), EMPTY_VALUE));
    }

    /**
     * Getting one value from header
     * 
     * @param headers
     *            http header
     * @param key
     *            key of searching value
     * @param required
     *            is http header value mandatory? If true then print it to log
     * @return value from http header
     */
    public static String getHeaderValue(HttpHeaders headers, String key, boolean required) {
        if (headers == null || key == null) {
            return null;
        }
        return getHeaderValue(headers.getRequestHeaders(), key, required);
    }

    /**
     * Getting one value from header
     * 
     * @param headerMap
     *            http header values map
     * @param key
     *            key of searching value
     * @param required
     *            is http header value mandatory? If true then print it to log
     * @return value from http header
     */
    public static String getHeaderValue(MultivaluedMap<String, String> headerMap, String key, boolean required) {
        Logger log = Logger.getLogger(ProjectHeader.class);
        try {
            if (headerMap == null) {
                return null;
            }

            List<String> values = headerMap.get(key);
            if (values == null || values.isEmpty()) {
                String msg = "Request header doesnt contain (" + key + ") key";
                if (required) {
                    log.warn(msg);
                } else {
                    log.debug(msg);
                }
                return null;
            } else {
                return values.get(0);
            }
        } catch (Exception e) {
            log.warn("Error in getHeaderValue(" + key + ")", e);
            return null;
        }
    }

    /**
     * Get session token
     * 
     * @return session token
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * Set session token
     * 
     * @param sessionToken
     *            token
     */
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    /**
     * Get refresh token
     * 
     * @return refresh token
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Set refresh token
     * 
     * @param refreshToken
     *            token
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Get host
     * 
     * @return host
     */
    public String getHost() {
        return host;
    }

    /**
     * Set host
     * 
     * @param host
     *            host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Get source
     * 
     * @return source
     */
    public String getSource() {
        return source;
    }

    /**
     * Set source
     * 
     * @param source
     *            source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Get language
     * 
     * @return language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Set language
     * 
     * @param language
     *            language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Get forwarded header
     * 
     * @return forwarded header value
     */
    public String getForwarded() {
        return forwarded;
    }

    /**
     * Set forwarded header value
     * 
     * @param forwarded
     *            forwarded value
     */
    public void setForwarded(String forwarded) {
        this.forwarded = forwarded;
    }

    /**
     * Get forwarded header host value
     * 
     * @return forwarded host value
     */
    public String getForwardedForHost() {
        if (forwardedForHost == null && forwarded != null) {
            handleForwardedHeader(forwarded);
        }
        return forwardedForHost;
    }

    /**
     * Set forwarded for host value
     * 
     * @param forwardedForHost
     *            host value
     */
    public void setForwardedForHost(String forwardedForHost) {
        this.forwardedForHost = forwardedForHost;
    }

    /**
     * Get forwarded header port value
     * 
     * @return forwarded port value
     */
    public String getForwardedForPort() {
        if (forwardedForPort == null && forwarded != null) {
            handleForwardedHeader(forwarded);
        }
        return forwardedForPort;
    }

    /**
     * Set forwarded for port value
     * 
     * @param forwardedForPort
     *            port value
     */
    public void setForwardedForPort(String forwardedForPort) {
        this.forwardedForPort = forwardedForPort;
    }
}
