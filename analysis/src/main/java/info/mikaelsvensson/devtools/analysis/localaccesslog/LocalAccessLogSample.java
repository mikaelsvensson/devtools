/*
 * Copyright 2014 Mikael Svensson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package info.mikaelsvensson.devtools.analysis.localaccesslog;

import info.mikaelsvensson.devtools.analysis.shared.AbstractSample;

import java.util.Date;

public class LocalAccessLogSample extends AbstractSample {
    private String ipAddress;
    private final String request;
    private final int statusCode;
    private final int responseLength;

    public LocalAccessLogSample(Date timeStamp, int responseTime, String ipAddress, String request, int statusCode, int responseLength) {
        super(timeStamp, responseTime);
        this.ipAddress = ipAddress;
        this.request = request;
        this.statusCode = statusCode;
        this.responseLength = responseLength;
    }

    public String getRequest() {
        return request;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public int getResponseLength() {
        return responseLength;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
