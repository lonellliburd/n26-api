import com.n26.api.Application;
import com.n26.api.Request;
import com.n26.api.TransactionManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Calendar;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class TransactionControllerTest {

    private HttpMessageConverter Jackson2HttpMessageConverter;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.Jackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(httpmc -> httpmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("Null", this.Jackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        TransactionManager.reset();
    }

    @Test
    public void testSendProperTimestamp() throws Exception{
        mockMvc.perform(post("/transactions")
                .content(this.json(new Request().setAmount(100).setTimestamp(System.currentTimeMillis())))
                .contentType(contentType))
                .andExpect(status().is(201));
    }

    @Test
    public void testSendExpiredTimestamp() throws Exception{
        mockMvc.perform(post("/transactions")
                .content(this.json(new Request().setAmount(100).setTimestamp(1531191965158L)))
                .contentType(contentType))
                .andExpect(status().is(204));
    }

    @Test
    public void testSendEmptyRequest() throws Exception{
        mockMvc.perform(post("/transactions")
                .content(this.json(null))
                .contentType(contentType))
                .andExpect(status().is(400));
    }

    @Test
    public void testGetStatistics() throws Exception {
        mockMvc.perform(post("/transactions")
                .content(this.json(new Request().setAmount(100).setTimestamp(System.currentTimeMillis())))
                .contentType(contentType))
                .andExpect(status().is(201));

        MockHttpServletResponse response = mockMvc.perform(get("/statistic")
                .contentType(contentType))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        assertEquals("{\"sum\":100.0,\"avg\":100.0,\"max\":100.0,\"min\":100.0,\"count\":1}",
                response.getContentAsString());
    }

    @Test
    public void testSubmitMultipleAndGetStatistic() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        mockMvc.perform(post("/transactions")
                .content(this.json(new Request().setAmount(100).setTimestamp(System.currentTimeMillis())))
                .contentType(contentType))
                .andExpect(status().is(201));

        mockMvc.perform(post("/transactions")
                .content(this.json(new Request().setAmount(200).setTimestamp(System.currentTimeMillis()+20000)))
                .contentType(contentType))
                .andExpect(status().is(201));

        mockMvc.perform(post("/transactions")
                .content(this.json(new Request().setAmount(300).setTimestamp(cal.getTimeInMillis())))
                .contentType(contentType))
                .andExpect(status().is(204));

        mockMvc.perform(post("/transactions")
                .content(this.json(new Request().setAmount(300).setTimestamp(System.currentTimeMillis()+40000)))
                .contentType(contentType))
                .andExpect(status().is(201));

        MockHttpServletResponse response = mockMvc.perform(get("/statistic")
                .contentType(contentType))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        assertEquals("{\"sum\":600.0,\"avg\":200.0,\"max\":300.0,\"min\":100.0,\"count\":3}",
                response.getContentAsString());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.Jackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}