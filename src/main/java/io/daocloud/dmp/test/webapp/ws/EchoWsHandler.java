package io.daocloud.dmp.test.webapp.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class EchoWsHandler implements WebSocketHandler {
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session
                .send(session.receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .map(session::textMessage)
                );
    }
}
