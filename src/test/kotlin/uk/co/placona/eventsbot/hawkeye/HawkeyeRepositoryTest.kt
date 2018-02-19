package uk.co.placona.eventsbot.hawkeye

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Observable
import org.apache.log4j.Logger
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import uk.co.placona.eventsbot.EventsBotApp
import uk.co.placona.eventsbot.models.*

@RunWith(SpringRunner::class)
class HawkeyeRepositoryTest {

    private val log = Logger.getLogger(HawkeyeRepositoryTest::class.simpleName)

    @MockK
    private
    lateinit var mockHawkeyeService: HawkeyeService

    @MockK
    private
    lateinit var mockEventsBotApp: EventsBotApp

    private lateinit var hawkeyeRepository: HawkeyeRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        hawkeyeRepository = HawkeyeRepository(mockHawkeyeService)
        System.setProperty("HAWKEYE_USERNAME", "username")
        System.setProperty("HAWKEYE_PASSWORD", "password")
        System.setProperty("VERIFICATION_TOKEN", "verification-token")
    }

    @Test
    @Throws(Exception::class)
    fun `when countries are requested, should call client and return response`(){
        val response = HawkeyeCountryResponse(1, EmbeddedCountry(listOf(Country(1, "test"))))
        every { mockHawkeyeService
                .getCountries(System.getProperty("HAWKEYE_PASSWORD"))
        } returns Observable.just(response)

        val testObserver = hawkeyeRepository.getCountries().test()

        testObserver.awaitTerminalEvent()
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        val listResult = testObserver.values()[0]
        assertThat(listResult.size, `is`(1))
        assertTrue(listResult.containsKey("test"))

    }

    @Test
    @Throws(Exception::class)
    fun `when tags are requested, should call client and return response`(){
        val response = HawkeyeTagResponse(1, EmbeddedTag(listOf(Tag(1, "test"))))
        every { mockHawkeyeService
                .getTags(System.getProperty("HAWKEYE_PASSWORD"))
        } returns Observable.just(response)

        val testObserver = hawkeyeRepository.getTags().test()

        testObserver.awaitTerminalEvent()
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        val listResult = testObserver.values()[0]
        assertThat(listResult.size, `is`(1))
        assertTrue(listResult.containsKey("test"))
    }

    @Test
    @Throws(Exception::class)
    fun `when checking if a tag exists return tag id`(){
        val response = HawkeyeTagResponse(1, EmbeddedTag(listOf(Tag(1, "test"))))
        every { mockHawkeyeService
                .getTags(System.getProperty("HAWKEYE_PASSWORD"))
        } returns Observable.just(response)

        assertThat(hawkeyeRepository.checkIsTag("test"), `is`(1))
    }

    @Test
    @Throws(Exception::class)
    fun `when checking if invalid tag exists return default value`(){
        val response = HawkeyeTagResponse(1, EmbeddedTag(listOf(Tag(1, "test"))))
        every { mockHawkeyeService
                .getTags(System.getProperty("HAWKEYE_PASSWORD"))
        } returns Observable.just(response)

        assertThat(hawkeyeRepository.checkIsTag("invalid-tag"), `is`(0))
    }

    @Test
    @Throws(Exception::class)
    fun `when checking if a country exists return country id`(){
        val response = HawkeyeCountryResponse(1, EmbeddedCountry(listOf(Country(1, "test"))))
        every { mockHawkeyeService
                .getCountries(System.getProperty("HAWKEYE_PASSWORD"))
        } returns Observable.just(response)

        assertThat(hawkeyeRepository.checkIsCountry("test"), `is`(1))
    }

    @Test
    @Throws(Exception::class)
    fun `when checking if invalid country exists return default value`(){
        val response = HawkeyeCountryResponse(1, EmbeddedCountry(listOf(Country(1, "test"))))
        every { mockHawkeyeService
                .getCountries(System.getProperty("HAWKEYE_PASSWORD"))
        } returns Observable.just(response)

        assertThat(hawkeyeRepository.checkIsCountry("invalid-country"), `is`(0))
    }
}