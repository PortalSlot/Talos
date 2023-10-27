package fr.konoashi.talos.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class ServerWebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/inToServer");//From the proxy
        registry.addHandler(webSocketHandler2(), "/outToProxy");//In from Face to go to the proxy back
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new GoToServerWS();
    }

    @Bean
    public WebSocketHandler webSocketHandler2() {
        return new BackToProxyWS();
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(1024*1024);
        container.setMaxBinaryMessageBufferSize(1024*1024);
        return container;
    }
}
