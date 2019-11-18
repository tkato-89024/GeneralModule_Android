package jp.co.model.tkato.general_module.cipher;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CiphererInstrumentedTest {

    private final String password     = "iognkd";
    private final String decodeResult = "fags";
    private final String encodeResult = "tP4qWs7eTCtP/H2g8T/O2A==";

    // Context of the app under test.
    // Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    // assertEquals("jp.co.model.tkato.general_module.test", appContext.getPackageName());

    // Cipherer 内で使用されている Base64 は UnitTest では null しか返さないため
    // InstrumentedTest で行う

    @Test
    public void test_encode() {

        final String encode = Cipherer.encode(password, decodeResult);

        assertEquals(encodeResult, encode);
    }

    @Test
    public void test_decode() {

        final String decode = Cipherer.decode(password, encodeResult);

        assertEquals(decodeResult, decode);
    }
}