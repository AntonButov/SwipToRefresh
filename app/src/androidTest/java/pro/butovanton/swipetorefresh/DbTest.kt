package pro.butovanton.swipetorefresh


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pro.butovanton.swipetorefresh.App.Companion.app
import pro.butovanton.swipetorefresh.db.Data
import java.util.concurrent.CountDownLatch

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DbTest {

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    val dao = (app as App).getDB().getDao()

    @After
    fun delete() {
        dao.delete()
    }

    val testData = listOf(Data("testName1"),Data("testName2"))

    @Test
    fun dbTest() {
        val countDownLatch = CountDownLatch(1)
        Assert.assertNotNull(dao)
        dao.insert(testData)
        dao.getAll().subscribe { fromDB ->
            if (fromDB.size > 0)
            //   Assert.assertEquals(testData, fromDB)
            countDownLatch.countDown()
        }
        countDownLatch.await()
    }
}