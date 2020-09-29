package mufeezslc.annotation;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonSerializationProcessorTest {

    private Person person = new Person("mufiz","shaikh", "34", "South jordan");
    private JsonSerializationProcessor jp = new JsonSerializationProcessor();
    private String convertedJsonString = jp.convertToJson(person);

    @Test
    public void testAnnotation_hasExpectedValue(){
        String expectedValue = "{\"personAge\":\"34\",\"firstName\":\"Mufiz\",\"lastName\":\"Shaikh\"}";
        assertEquals(expectedValue, convertedJsonString);
    }

    @Test
    public void result_doesnot_have_address(){
        assertTrue("result should not have address as its not annotated with JsonElement", !convertedJsonString.contains("address"));
    }
}