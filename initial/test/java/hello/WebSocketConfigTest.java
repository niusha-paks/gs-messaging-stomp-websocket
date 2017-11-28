package hello;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.broker.AbstractBrokerMessageHandler;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.SimpleBrokerRegistration;
import org.springframework.messaging.support.AbstractMessageChannel;
import org.springframework.messaging.support.ExecutorSubscribableChannel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class WebSocketConfigTest {

    @Test
    @Ignore //TODO find a way to assert registeredPrefixes
    public void itShouldConfigureMessageBroker() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        //GIVEN
        WebSocketConfig webSocketConfig = new WebSocketConfig();
        MessageBrokerRegistry config = new MessageBrokerRegistry(new ExecutorSubscribableChannel(), new AbstractMessageChannel() {
            @Override
            protected boolean sendInternal(Message<?> message, long l) {
                return false;
            }
        });

        Method applicationDestinationPrefixes = MessageBrokerRegistry.class.getDeclaredMethod("getApplicationDestinationPrefixes", new Class[0]);
        applicationDestinationPrefixes.setAccessible(true);

        Field simpleBrokerRegistration = MessageBrokerRegistry.class.getDeclaredField("simpleBrokerRegistration");
        simpleBrokerRegistration.setAccessible(true);

        //WHEN
        webSocketConfig.configureMessageBroker(config);
        Collection<String> prefixes = (Collection<String>) applicationDestinationPrefixes.invoke(config);
        SimpleBrokerRegistration brokerRegistration = (SimpleBrokerRegistration) simpleBrokerRegistration.get(config);

        //AND
        Field destinationPrefixes = AbstractBrokerMessageHandler.class.getDeclaredField("destinationPrefixes");
        destinationPrefixes.setAccessible(true);
        List<String> registeredPrefixes = (List<String>) destinationPrefixes.get(brokerRegistration);

        //THEN
        assertTrue("registeredPrefixes collection should contains /topic", registeredPrefixes.contains("/topic"));
        assertTrue("prefixes collection should contains /app", prefixes.contains("/app"));
    }

}
