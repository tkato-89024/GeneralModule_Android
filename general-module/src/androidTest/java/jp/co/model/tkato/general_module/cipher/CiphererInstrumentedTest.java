package jp.co.model.tkato.general_module.cipher;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@SmallTest // 200 ms 未満で終わるようなテスト
@RunWith(AndroidJUnit4.class)
public class CiphererInstrumentedTest {

    // region member / setup / tearDown

    private final String password     = "iognkd";
    private final String decodeResult = "fags";
    private final String encodeResult = "tP4qWs7eTCtP/H2g8T/O2A==";

    // Context of the app under test.
    // Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    // assertEquals("jp.co.model.tkato.general_module.test", appContext.getPackageName());

    // Cipherer 内で使用されている Base64 は UnitTest では null しか返さないため
    // InstrumentedTest で行う

    // endregion member / setup / tearDown

    // region encode / decode

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

    // endregion encode / decode
}