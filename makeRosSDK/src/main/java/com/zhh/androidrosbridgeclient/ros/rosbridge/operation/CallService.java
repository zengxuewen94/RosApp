/**
 * Copyright (c) 2014 Jilk Systems, Inc.
 * 
 * This file is part of the Java ROSBridge Client.
 *
 * The Java ROSBridge Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Java ROSBridge Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Java ROSBridge Client.  If not, see http://www.gnu.org/licenses/.
 * 
 */
package com.zhh.androidrosbridgeclient.ros.rosbridge.operation;

import com.zhh.androidrosbridgeclient.ros.message.MessageType;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.rosbridge.indication.*;

@MessageType(string = "call_service")
public class CallService extends Operation {
    @Indicator public String service;
    @Indicated public Message args;
    public Integer fragment_size; // use Integer for optional items
    public String compression;
    public String type;
    public String response_type;
    public CallService() {}

    public CallService(String service, Message args,String type,String response_type) {
        this.service = service;
        this.args = args;
        this.type = type;
        this.response_type = response_type;
    }
}
