package jp.co.model.tkato.general_module.shuffle;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import jp.co.model.tkato.general_module.log.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// LogCat で値を確認したいため、InstrumentedTest
@RunWith(AndroidJUnit4.class)
public class ShufflerCoreTest {

    // 生成される値のランダム性によって、テストによっては通らない場合も考えられるので、
    // 何回テストを行うか指定
    private static final int TEST_COUNT = 100;

    // テストに使用する値
    private static final int TEST_SHUFFLE_MIN_0 = 0;
    private static final int TEST_SHUFFLE_MIN   = 20;
    private static final int TEST_SHUFFLE_LIMIT = 30;


    private Shuffler shuffler;

    @SuppressWarnings("ConstantConditions")
    @Before
    public void setUp() throws Exception {

        // テストに使用する値の前提条件
        assertTrue((TEST_SHUFFLE_LIMIT - TEST_SHUFFLE_MIN) >= 10);

        Logger.initialize(true);
        shuffler = new ShufflerCore();
    }

    @After
    public void tearDown() throws Exception {
        shuffler.clear();
    }

    // region getter系

    @SuppressWarnings("all")
    @Test
    public void test_getter系_getMin_getMax_getLength_case1_正常() {

        final int min0  = TEST_SHUFFLE_MIN_0;
        final int min   = TEST_SHUFFLE_MIN;
        final int limit = TEST_SHUFFLE_LIMIT;

        assertTrue(shuffler.initialize( min0,       min0, limit));validationEqual_min_max_length(min0, (limit - 1), limit);
        assertTrue(shuffler.initialize((min0  + 5), min0, limit));validationEqual_min_max_length(min0, (limit - 1), limit);
        assertTrue(shuffler.initialize((limit - 1), min0, limit));validationEqual_min_max_length(min0, (limit - 1), limit);
        assertTrue(shuffler.initialize( min,        min,  limit));validationEqual_min_max_length(min,  (limit - 1), (limit - min));
        assertTrue(shuffler.initialize((min   + 5), min,  limit));validationEqual_min_max_length(min,  (limit - 1), (limit - min));
        assertTrue(shuffler.initialize((limit - 1), min,  limit));validationEqual_min_max_length(min,  (limit - 1), (limit - min));
    }

    @SuppressWarnings("SameParameterValue") // Actual value of parameter 'X' is always 'Y'
    private void validationEqual_min_max_length(int min, int max, int len) throws AssertionError {
        assertEquals(min, shuffler.getMin());
        assertEquals(max, shuffler.getMax());
        assertEquals(len, shuffler.getLength());
    }

    @SuppressWarnings("all")
    @Test
    public void test_getter系_getMin_getMax_getLength_case2_異常() {

        final int minus1 = -1;
        final int min0   = TEST_SHUFFLE_MIN_0;
        final int min    = TEST_SHUFFLE_MIN;
        final int limit  = TEST_SHUFFLE_LIMIT;

        assertFalse(shuffler.initialize( minus1,     min0,        limit));  // initIndex == -1
        assertFalse(shuffler.initialize( min0,       minus1,      limit));  // min       == -1
        assertFalse(shuffler.initialize( min0,       min0,        minus1)); // limit     == -1
        assertFalse(shuffler.initialize( min0,       limit,       limit));  // min <= limit
        assertFalse(shuffler.initialize( min0,       (limit + 1), limit));  // min <= limit
        assertFalse(shuffler.initialize((min - 5),   min,         limit));  // initIndex <  min
        assertFalse(shuffler.initialize( limit,      min,         limit));  // initIndex >= max
        assertFalse(shuffler.initialize((limit + 1), min,         limit));  // initIndex >= max
    }

    // endregion getter系

    // region getPosition系

    @Test
    public void test_getPosition_getNextPosition_getPrevPosition() {

        final int min0  = TEST_SHUFFLE_MIN_0;
        final int min   = TEST_SHUFFLE_MIN;
        final int limit = TEST_SHUFFLE_LIMIT;

        assertTrue(shuffler.initialize( min0,       min0, limit)); validationEqual_position(0, 1, (limit - 1));
        assertTrue(shuffler.initialize((min0  + 5), min0, limit)); validationEqual_position(0, 1, (limit - 1));
        assertTrue(shuffler.initialize((limit - 1), min0, limit)); validationEqual_position(0, 1, (limit - 1));
        assertTrue(shuffler.initialize( min,        min,  limit)); validationEqual_position(0, 1, (limit - min - 1));
        assertTrue(shuffler.initialize((min   + 5), min,  limit)); validationEqual_position(0, 1, (limit - min - 1));
        assertTrue(shuffler.initialize((limit - 1), min,  limit)); validationEqual_position(0, 1, (limit - min - 1));
    }

    @SuppressWarnings("SameParameterValue") // Actual value of parameter 'X' is always 'Y'
    private void validationEqual_position(int current, int next, int prev) throws AssertionError {
        assertEquals(current, shuffler.getPosition());
        assertEquals(next,    shuffler.getNextPosition());
        assertEquals(prev,    shuffler.getPrevPosition());
    }

    // endregion getPosition系

    // region index系

    @Test
    public void test_getCurrentIndex_getNextIndex_getPrevIndex_getFirstIndex_getLastIndex__setPosition() {

        final int min0  = TEST_SHUFFLE_MIN_0;
        final int min   = TEST_SHUFFLE_MIN;
        final int limit = TEST_SHUFFLE_LIMIT;

        assertTrue(shuffler.initialize( min0,       min0, limit)); assertEquals( min0,       shuffler.getCurrentIndex()); validationEqual_indexRange(shuffler);
        assertTrue(shuffler.initialize((min0  + 5), min0, limit)); assertEquals((min0  + 5), shuffler.getCurrentIndex()); validationEqual_indexRange(shuffler);
        assertTrue(shuffler.initialize((limit - 1), min0, limit)); assertEquals((limit - 1), shuffler.getCurrentIndex()); validationEqual_indexRange(shuffler);
        assertTrue(shuffler.initialize( min,        min,  limit)); assertEquals( min,        shuffler.getCurrentIndex()); validationEqual_indexRange(shuffler);
        assertTrue(shuffler.initialize((min   + 5), min,  limit)); assertEquals((min   + 5), shuffler.getCurrentIndex()); validationEqual_indexRange(shuffler);
        assertTrue(shuffler.initialize((limit - 1), min,  limit)); assertEquals((limit - 1), shuffler.getCurrentIndex()); validationEqual_indexRange(shuffler);
    }

    private void validationEqual_indexRange(Shuffler shuffler) throws AssertionError {

        for (int cnt = 0; cnt < TEST_COUNT; cnt++) {
            for (int i = 0, len = shuffler.getLength(); i < len; i++) {

                shuffler.setPosition(i);

                final int index = shuffler.getCurrentIndex();
                final int next  = shuffler.getNextIndex();
                final int prev  = shuffler.getPrevIndex();

                Logger.v("validationEqual_indexRange: position = " + shuffler.getPosition());
                Logger.v("validationEqual_indexRange: index = " + index + ", next = " + next + ", prev = " + prev);

                assertTrue(shuffler.getMin() <= index);
                assertTrue(index <= shuffler.getMax());
                assertTrue(shuffler.getMin() <= next);
                assertTrue(next  <= shuffler.getMax());
                assertTrue(shuffler.getMin() <= prev);
                assertTrue(prev  <= shuffler.getMax());

                assertFalse(index < shuffler.getMin() || shuffler.getMax() < index);
                assertFalse(next  < shuffler.getMin() || shuffler.getMax() < next);
                assertFalse(prev  < shuffler.getMin() || shuffler.getMax() < prev);

                // リストをまたぐ場合、インデックスが同数になる場合がある
                if (0 == i) {
                    assertTrue(index != next);
                    // assertTrue(index != prev);
                    // assertTrue(next  != prev);

                    assertEquals(shuffler.getFirstIndex(), index);

                } else if (len-1 == i) {
                    // assertTrue(index != next);
                    assertTrue(index != prev);
                    // assertTrue(next  != prev);

                    assertEquals(shuffler.getLastIndex(), index);

                } else {
                    assertTrue(index != next);
                    assertTrue(index != prev);
                    assertTrue(next  != prev);
                }
            }
        }
    }

    // endregion index系

    // region setPositionByIndex

    public void test_setPositionByIndex() {

        final int min0  = TEST_SHUFFLE_MIN_0;
        final int min   = TEST_SHUFFLE_MIN;
        final int limit = TEST_SHUFFLE_LIMIT;

        assertTrue(shuffler.initialize( min0,       min0, limit)); validationEqual_setPositionByIndex(shuffler);
        assertTrue(shuffler.initialize((min0  + 5), min0, limit)); validationEqual_setPositionByIndex(shuffler);
        assertTrue(shuffler.initialize((limit - 1), min0, limit)); validationEqual_setPositionByIndex(shuffler);
        assertTrue(shuffler.initialize( min,        min,  limit)); validationEqual_setPositionByIndex(shuffler);
        assertTrue(shuffler.initialize((min   + 5), min,  limit)); validationEqual_setPositionByIndex(shuffler);
        assertTrue(shuffler.initialize((limit - 1), min,  limit)); validationEqual_setPositionByIndex(shuffler);
    }

    private void validationEqual_setPositionByIndex(Shuffler shuffler) throws AssertionError {

        for (int cnt = 0; cnt < TEST_COUNT; cnt++) {
            for (int i = 0, len = shuffler.getLength(); i < len; i++) {

                shuffler.setPositionByIndex(i);

                final int index = shuffler.getCurrentIndex();
                final int next = shuffler.getNextIndex();
                final int prev = shuffler.getPrevIndex();

                Logger.v("validationEqual_setPositionByIndex: position = " + shuffler.getPosition());
                Logger.v("validationEqual_setPositionByIndex: index = " + index + ", next = " + next + ", prev = " + prev);

                assertTrue(shuffler.getMin() <= index);
                assertTrue(index <= shuffler.getMax());
                assertTrue(shuffler.getMin() <= next);
                assertTrue(next <= shuffler.getMax());
                assertTrue(shuffler.getMin() <= prev);
                assertTrue(prev <= shuffler.getMax());

                assertFalse(index < shuffler.getMin() || shuffler.getMax() < index);
                assertFalse(next < shuffler.getMin() || shuffler.getMax() < next);
                assertFalse(prev < shuffler.getMin() || shuffler.getMax() < prev);

                // リストをまたぐ場合、インデックスが同数になる場合がある
                if (0 == i) {
                    assertTrue(index != next);
                    // assertTrue(index != prev);
                    // assertTrue(next  != prev);

                } else if (len - 1 == i) {
                    // assertTrue(index != next);
                    assertTrue(index != prev);
                    // assertTrue(next  != prev);

                } else {
                    assertTrue(index != next);
                    assertTrue(index != prev);
                    assertTrue(next  != prev);

                    assertEquals(i, index);
                }
            }
        }
    }

    // endregion setPositionByIndex

    // region next prev

    @Test
    public void test_nextPrevIndex() {

        final int min0  = TEST_SHUFFLE_MIN_0;
        final int min   = TEST_SHUFFLE_MIN;
        final int limit = TEST_SHUFFLE_LIMIT;

        assertTrue(shuffler.initialize( min0,       min0, limit)); validationEqual_nextPrevIndex(shuffler);
        assertTrue(shuffler.initialize((min0  + 5), min0, limit)); validationEqual_nextPrevIndex(shuffler);
        assertTrue(shuffler.initialize((limit - 1), min0, limit)); validationEqual_nextPrevIndex(shuffler);
        assertTrue(shuffler.initialize( min,        min,  limit)); validationEqual_nextPrevIndex(shuffler);
        assertTrue(shuffler.initialize((min   + 5), min,  limit)); validationEqual_nextPrevIndex(shuffler);
        assertTrue(shuffler.initialize((limit - 1), min,  limit)); validationEqual_nextPrevIndex(shuffler);
    }

    private void validationEqual_nextPrevIndex(Shuffler shuffler) throws AssertionError {

        for (int cnt = 0; cnt < TEST_COUNT; cnt++) {
            for (int i = 0, len = shuffler.getLength(); i < len; i++) {

                shuffler.setPosition(i);

                final int index = shuffler.getCurrentIndex();
                final int next = shuffler.getNextIndex();
                final int prev = shuffler.getPrevIndex();

                Logger.v("validationEqual_nextPrevIndex: ==========================================");
                Logger.v("validationEqual_nextPrevIndex: position = " + shuffler.getPosition());
                Logger.v("validationEqual_nextPrevIndex: index = " + index + ", next = " + next + ", prev = " + prev);

                // リストをまたぐ場合、インデックスが同数になる場合がある
                if (0 == i) {
                    assertTrue(index != next);
                    // assertTrue(index != prev);
                    // assertTrue(next  != prev);

                    assertFalse(shuffler.next());
                    assertEquals(next, shuffler.getCurrentIndex());
                    assertEquals(index, shuffler.getPrevIndex());
                    assertFalse(shuffler.prev());
                    assertEquals(index, shuffler.getCurrentIndex());
                    assertEquals(next, shuffler.getNextIndex());
                    assertEquals(prev, shuffler.getPrevIndex());

                } else if (len - 1 == i) {
                    // assertTrue(index != next);
                    assertTrue(index != prev);
                    // assertTrue(next  != prev);

                    assertFalse(shuffler.prev());
                    assertEquals(prev, shuffler.getCurrentIndex());
                    assertEquals(index, shuffler.getNextIndex());
                    assertFalse(shuffler.next());
                    assertEquals(index, shuffler.getCurrentIndex());
                    assertEquals(next, shuffler.getNextIndex());
                    assertEquals(prev, shuffler.getPrevIndex());

                } else {
                    assertTrue(index != next);
                    assertTrue(index != prev);
                    assertTrue(next  != prev);

                    // リストをまたがない限り、リストは更新されない

                    assertFalse(shuffler.next());
                    assertEquals(next,  shuffler.getCurrentIndex());
                    assertEquals(index, shuffler.getPrevIndex());
                    assertFalse(shuffler.prev());
                    assertEquals(index, shuffler.getCurrentIndex());
                    assertEquals(next,  shuffler.getNextIndex());
                    assertEquals(prev,  shuffler.getPrevIndex());
                    assertFalse(shuffler.prev());
                    assertEquals(prev,  shuffler.getCurrentIndex());
                    assertEquals(index, shuffler.getNextIndex());
                    assertFalse(shuffler.next());
                    assertEquals(index, shuffler.getCurrentIndex());
                    assertEquals(next,  shuffler.getNextIndex());
                    assertEquals(prev,  shuffler.getPrevIndex());
                }
            }
        }
    }

    // endregion next prev
}