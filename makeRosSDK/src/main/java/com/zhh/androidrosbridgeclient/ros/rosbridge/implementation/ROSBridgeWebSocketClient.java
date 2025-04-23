/**
 * Copyright (c) 2014 Jilk Systems, Inc.
 * <p>
 * This file is part of the Java ROSBridge Client.
 * <p>
 * The Java ROSBridge Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * The Java ROSBridge Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with the Java ROSBridge Client.  If not, see http://www.gnu.org/licenses/.
 */
package com.zhh.androidrosbridgeclient.ros.rosbridge.implementation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.zhh.androidrosbridgeclient.ros.rosbridge.FullMessageHandler;
import com.zhh.androidrosbridgeclient.ros.rosbridge.operation.Operation;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.rosbridge.operation.Publish;
import com.zhh.androidrosbridgeclient.ros.rosbridge.operation.ServiceResponse;
import com.zhh.androidrosbridgeclient.ros.ROSClient;

import org.java_websocket.framing.CloseFrame;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.net.Socket;
import java.util.zip.GZIPInputStream;


public class ROSBridgeWebSocketClient extends WebSocketClient {

    private Registry<Class> classes;
    private Registry<FullMessageHandler> handlers;
    private boolean debug;
    private ROSClient.ConnectionStatusListener listener;
    public static final String DEFAULT_GZIP_CHARSET = "UTF-8";

    ROSBridgeWebSocketClient(URI serverURI) {
        super(serverURI);
        classes = new Registry<Class>();
        handlers = new Registry<FullMessageHandler>();
        Operation.initialize(classes);  // note, this ensures that the Message Map is initialized too
        listener = null;
    }

    public static ROSBridgeWebSocketClient create(String URIString) {
        ROSBridgeWebSocketClient client = null;
        try {
            URI uri = new URI(URIString);
            client = new ROSBridgeWebSocketClient(uri);
        }
        catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        return client;
    }

    public void setListener(ROSClient.ConnectionStatusListener listener) {
        this.listener = listener;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if (listener != null) {
            listener.onConnect();
        }
    }

    public static String unCompress(byte[] bytes, String charset) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        ByteArrayInputStream byteArrayInputStream = null;
        GZIPInputStream gzipInputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gzipInputStream.read(buffer)) >= 0) {
                byteArrayOutputStream.write(buffer, 0, n);
            }
            //gzipInputStream.transferTo(byteArrayOutputStream);
            closeQuietly(byteArrayInputStream);
            closeQuietly(gzipInputStream);
            closeQuietly(byteArrayOutputStream);
            return byteArrayOutputStream.toString(charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeQuietly(byteArrayInputStream);
        closeQuietly(gzipInputStream);
        closeQuietly(byteArrayOutputStream);
        return null;
    }
    public static String unCompress(byte[] bytes) {
        return unCompress(bytes, DEFAULT_GZIP_CHARSET);
    }
    public static Boolean isCompressed(byte[] bytes) {
        return bytes.length > 2 && (((bytes[0] & 0xFF) == 0x78 &&
                ((bytes[1] & 0xFF) == 0x9C || (bytes[1] & 0xFF) == 0x01 ||
                        (bytes[1] & 0xFF) == 0xDA || (bytes[1] & 0xFF) == 0x5E)) ||
                ((bytes[0] & 0xFF) == 0x1F && (bytes[1] & 0xFF) == 0x8B));
    }
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception unused) {
            }
        }
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        byte[] raw_message_bytes = new byte[bytes.remaining()];;
        bytes.get(raw_message_bytes);
        String decompressed_data = null;
        if(isCompressed(raw_message_bytes)) {
            decompressed_data = unCompress(raw_message_bytes);
            if (decompressed_data == null) {
                return;
            }
        } else {
            decompressed_data = new String(raw_message_bytes);
        }
        onMessage(decompressed_data);
    }


    @Override
    public void onMessage(String message) {
        if (debug) {
            System.out.println("<ROS " + message);
        }
        //System.out.println("ROSBridgeWebSocketClient.onMessage (message): " + message);
        Operation operation = Operation.toOperation(message, classes);
        //System.out.println("ROSBridgeWebSocketClient.onMessage (operation): ");
        //operation.print();

        FullMessageHandler handler = null;
        Message msg = null;
        if (operation instanceof Publish) {
            Publish p = (Publish) operation;
            handler = handlers.lookup(Publish.class, p.topic);
            msg = p.msg;
        }
        else if (operation instanceof ServiceResponse) {
            ServiceResponse r = ((ServiceResponse) operation);
            handler = handlers.lookup(ServiceResponse.class, r.service);
            msg = r.values;
        }
        // later we will add clauses for Fragment, PNG, and Status. When rosbridge has it, we'll have one for service requests.

        // need to handle "result: null" possibility for ROSBridge service responses
        // this is probably some sort of call to the operation for "validation." Do it
        // as part of error handling.


        if (handler != null) {
            handler.onMessage(operation.id, msg);
        } else if (debug) {
            System.out.print("No handler: id# " + operation.id + ", ");
            if (operation instanceof Publish) {
                System.out.println("Publish " + ((Publish) operation).topic);
            } else if (operation instanceof ServiceResponse) {
                System.out.println("Service Response " + ((ServiceResponse) operation).service);
            }
            //operation.print();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (listener != null) {
            boolean normal = (remote || (code == CloseFrame.NORMAL));
            listener.onDisconnect(normal, reason, code);
        }
    }

    @Override
    public void onError(Exception ex) {
        if (listener != null) {
            listener.onError(ex);
        } else {
            ex.printStackTrace();
        }
    }

    // There is a bug in V1.2 of java_websockets that seems to appear only in Android, specifically,
    //    it does not shut down the thread and starts using gobs of RAM (symptom is frequent garbage collection).
    //    This method goes into the WebSocketClient object and hard-closes the socket, which causes the thread
    //    to exit (note, just interrupting the thread does not work).
    @Override
    public void closeBlocking() throws InterruptedException {
        super.closeBlocking();
        try {
            Field channelField = this.getClass().getSuperclass().getDeclaredField("channel");
            channelField.setAccessible(true);
            SocketChannel channel = (SocketChannel) channelField.get(this);
            if (channel != null && channel.isOpen()) {
                Socket socket = channel.socket();
                if (socket != null) {
                    socket.close();
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Exception in Websocket close hack.");
            ex.printStackTrace();
        }
    }


    public void send(Operation operation) {
        String json = operation.toJSON();
        if (debug) {
            System.out.println("ROS> " + json);
        }
        send(json);
    }

    public void register(Class<? extends Operation> c,
                         String s,
                         Class<? extends Message> m,
                         FullMessageHandler h) {
        Message.register(m, classes.get(Message.class));
        classes.register(c, s, m);
        if (h != null) {
            handlers.register(c, s, h);
        }
    }

    public void unregister(Class<? extends Operation> c, String s) {
        handlers.unregister(c, s);
        // Note that there is no concept of unregistering a class - it can get replaced is all
    }

    public Class<? extends Message> getRegisteredMessage(String messageString) {
        return classes.lookup(Message.class, messageString);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
