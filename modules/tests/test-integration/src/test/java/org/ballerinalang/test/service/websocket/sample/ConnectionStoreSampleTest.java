package org.ballerinalang.test.service.websocket.sample;

import org.ballerinalang.test.util.HttpClientRequest;
import org.ballerinalang.test.util.TestConstant;
import org.ballerinalang.test.util.websocket.WebSocketClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Test all the scenarios of WebSocket connection store.
 */
public class ConnectionStoreSampleTest extends WebSocketIntegrationTest {

    private final int threadSleepTime = 500;
    private final int clientCount = 5;
    private final WebSocketClient[] webSocketClients = new WebSocketClient[clientCount];

    {
        for (int i = 0; i < 5; i++) {
            webSocketClients[i] = new WebSocketClient("ws://localhost:9090/store/ws");
        }
    }

    @Test(priority = 0)
    public void testSendTextToStoredClient() throws IOException, InterruptedException, URISyntaxException {
        handshakeAllClients(webSocketClients);
        String sentText = "hello ";
        Map<String, String> headers = new HashMap<>();
        HttpClientRequest.doExecute(getServiceURLHttp("storeInfo/0"), TestConstant.POST, sentText + "0", headers);
        HttpClientRequest.doExecute(getServiceURLHttp("storeInfo/1"), TestConstant.POST, sentText + "1", headers);
        HttpClientRequest.doExecute(getServiceURLHttp("storeInfo/2"), TestConstant.POST, sentText + "2", headers);
        HttpClientRequest.doExecute(getServiceURLHttp("storeInfo/3"), TestConstant.POST, sentText + "3", headers);
        HttpClientRequest.doExecute(getServiceURLHttp("storeInfo/4"), TestConstant.POST, sentText + "4", headers);
        Thread.sleep(threadSleepTime);
        for (int i = 0; i < clientCount; i++) {
            Assert.assertEquals(webSocketClients[i].getTextReceived(), sentText + i);
        }
    }

    @Test(priority = 1)
    public void testRemoveStoredClient() throws IOException, InterruptedException, URISyntaxException {
        HttpClientRequest.doExecute(getServiceURLHttp("storeInfo/rm/0"), TestConstant.GET);
        Thread.sleep(threadSleepTime);
        Map<String, String> headers = new HashMap<>();
        HttpClientRequest.doExecute(getServiceURLHttp("storeInfo/0"), TestConstant.POST, "You are out", headers);
        Assert.assertEquals(webSocketClients[0].getTextReceived(), null);
    }

    @Test(priority = 2)
    public void testCloseConnection() throws IOException, InterruptedException {
        HttpClientRequest.doExecute(getServiceURLHttp("storeInfo/close/1"), TestConstant.GET);
        Assert.assertFalse(webSocketClients[1].isOpen());
        shutDownAllClients(webSocketClients);
    }
}
