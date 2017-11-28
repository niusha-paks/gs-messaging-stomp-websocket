package hello;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GreetingControllerTest {

    @Test
    public void itShouldRespondHelloMessageNameGivenMessage() throws Exception {
        //GIVEN
        GreetingController greetingController = new GreetingController();
        HelloMessage message = new HelloMessage();
        message.setName("Toto");

        //WHEN
        Greeting greeting = greetingController.greeting(message);

        //THEN
        assertEquals("Hello, Toto !", greeting.getContent());
    }

}
