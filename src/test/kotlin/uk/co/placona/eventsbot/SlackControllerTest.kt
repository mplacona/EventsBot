package uk.co.placona.eventsbot

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import uk.co.placona.eventsbot.hawkeye.HawkeyeRepository

@RunWith(SpringRunner::class)
class SlackControllerTest {

    private lateinit var mockMvc: MockMvc
    private lateinit var slackController: SlackController

    @MockK
    private
    lateinit var hawkeyeRepositoryMock: HawkeyeRepository

    @Before
    fun setup() {
        System.setProperty("HAWKEYE_USERNAME", "username")
        System.setProperty("HAWKEYE_PASSWORD", "password")
        System.setProperty("VERIFICATION_TOKEN", "verification-token")

        MockKAnnotations.init(this)
        slackController = SlackController(hawkeyeRepositoryMock)
        mockMvc = MockMvcBuilders.standaloneSetup(slackController).build()
    }

    @Test
    @Throws(Exception::class)
    fun contextLoads(){
        assertThat(slackController).isNotNull()
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnDefaultMessage() {
        this.mockMvc.perform(get("/health")).andDo(print()).andExpect(status().isOk)
                .andExpect(content().string(containsString("Still alive")))
    }
}