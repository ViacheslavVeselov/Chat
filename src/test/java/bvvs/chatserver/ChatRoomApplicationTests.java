package bvvs.chatserver;

import bvvs.chatserver.models.ChatMessage;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Log4j2
class ChatRoomApplicationTests {
	@LocalServerPort
	private Integer port;

	private WebSocketStompClient webSocketStompClient;

	private StompSession session;

	@BeforeEach
	public void setup() throws ExecutionException, InterruptedException {
		String wsUrl = "ws://127.0.0.1:" + port + "/chat";
		this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(
				List.of(new WebSocketTransport(new StandardWebSocketClient()))));
		session = webSocketStompClient.connect(wsUrl, new MyStompSessionHandler()).get();
	}

	@AfterEach
	public void tearDown() throws Exception {
		session.disconnect();
		webSocketStompClient.stop();
	}

	@Test
	void verifyUserMessageReceived() throws ExecutionException, InterruptedException, TimeoutException {
		BlockingQueue<ChatMessage> blockingQueue = new ArrayBlockingQueue(1);

		webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

		session.subscribe("/user/queue/messages21363ffb-fa5c-45f4-be30-6d732ca593c9", new StompSessionHandlerAdapter() {
			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				blockingQueue.add((ChatMessage) payload);
			}
		});

		session.send("/app/direct/21363ffb-fa5c-45f4-be30-6d732ca593c9", new ChatMessage().withText("text"));

		assertEquals(new ChatMessage().getText(), blockingQueue.poll(1, SECONDS).getText());
	}

	static class MyStompSessionHandler extends StompSessionHandlerAdapter {
		@Override
		public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
			log.info("Stomp client is connected");
			super.afterConnected(session, connectedHeaders);
		}

		@Override
		public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
			log.info("Exception: " + exception);
			super.handleException(session, command, headers, payload, exception);
		}
	}
}
