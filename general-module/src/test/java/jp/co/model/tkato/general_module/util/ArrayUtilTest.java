package jp.co.model.tkato.general_module.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class ArrayUtilTest {

    // region null check

    @SuppressWarnings("all")
    @Test
    public void test_Nullチェック_全メソッド() {

        assertNull(ArrayUtil.toIntArray(null));

        assertNull(ArrayUtil.toByteArray(null));

        assertNull(ArrayUtil.toArray(null, null));

        assertNull(ArrayUtil.toArray(new ArrayList<Integer>(), null));
        assertNull(ArrayUtil.toArray(null, new Integer[0]));

        assertNull(ArrayUtil.toArray(new ArrayList<Byte>(), null));
        assertNull(ArrayUtil.toArray(null, new Byte[0]));

        assertNull(ArrayUtil.toArray(new ArrayList<String>(), null));
        assertNull(ArrayUtil.toArray(null, new String[0]));

        final int[] intArray = null;
        assertNull(ArrayUtil.toList(intArray));
        assertNull(ArrayUtil.reverseToList(intArray));
        assertNull(ArrayUtil.reverseToArray(intArray));

        final byte[] byteArray = null;
        assertNull(ArrayUtil.toList(byteArray));
        assertNull(ArrayUtil.reverseToList(byteArray));
        assertNull(ArrayUtil.reverseToArray(byteArray));

        final Object[] objectArray = null;
        assertNull(ArrayUtil.toList(objectArray));
        assertNull(ArrayUtil.reverseToList(objectArray));

        // Null の場合 False が変える
        final List<Object> objectList = null;
        assertFalse(ArrayUtil.reverse(objectList));
    }

    // endregion null check

    // region toArray

    @Test
    public void test_toIntArray() {

        final List<Integer> list = new ArrayList<Integer>() {{
            add(0);
            add(1);
            add(2);
            add(3);
        }};

        final int[] resultValue = new int[] {
            0,
            1,
            2,
            3
        };

        assertArrayEquals(resultValue, ArrayUtil.toIntArray(list));
    }

    @Test
    public void test_toByteArray() {

        final List<Byte> list = new ArrayList<Byte>() {{
            add((byte) 0);
            add((byte) 1);
            add((byte) 2);
            add((byte) 3);
        }};

        final byte[] resultValue1 = new byte[] {
            0,
            1,
            2,
            3
        };

        assertArrayEquals(resultValue1, ArrayUtil.toByteArray(list));
    }

    @Test
    public void test_toArray_case1_IntegerArray() {

        final List<Integer> listInteger = new ArrayList<Integer>() {{
            add(0);
            add(1);
            add(2);
            add(3);
        }};

        final Integer[] resultValue1 = new Integer[] {
            0,
            1,
            2,
            3
        };

        assertArrayEquals(resultValue1, ArrayUtil.toArray(listInteger, new Integer[0]));
    }

    @Test
    public void test_toArray_case2_ByteArray() {

        final List<Byte> byteList = new ArrayList<Byte>() {{
            add((byte) 0);
            add((byte) 1);
            add((byte) 2);
            add((byte) 3);
        }};

        final Byte[] resultValue1 = new Byte[] {
            0,
            1,
            2,
            3
        };

        assertArrayEquals(resultValue1, ArrayUtil.toArray(byteList, new Byte[0]));
    }

    @Test
    public void test_toArray_case3_StringArray() {

        final List<String> stringList = new ArrayList<String>() {{
            add("0");
            add("1");
            add("2");
            add("3");
        }};

        final String[] resultValue1 = new String[] {
            "0",
            "1",
            "2",
            "3"
        };

        assertArrayEquals(resultValue1, ArrayUtil.toArray(stringList, new String[0]));
    }

    // endregion toArray

    // region toList

    @Test
    public void test_toList_IntList() {

        final int[] array = new int[] {
            0,
            1,
            2,
            3
        };
        final int len = array.length;

        final List<Integer> list = ArrayUtil.toList(array);
        for (int i = 0; i < len; i++) {
            final int     i_ = array[i];
            final Integer I  = list.get(i);
            assertEquals(i_, I.intValue());
            assertEquals(i_, I.byteValue());
        }

        for (int i = len - 1; 0 <= i; i--) {
            try {
                list.remove(i);
            } catch (Exception e) {
                fail();
            }
        }
    }

    @Test
    public void test_toList_ByteList() {

        final byte[] array = new byte[] {
            0,
            1,
            2,
            3
        };
        final int len = array.length;

        final List<Byte> list = ArrayUtil.toList(array);
        for (int i = 0; i < len; i++) {
            final byte b = array[i];
            final Byte B = list.get(i);
            assertEquals(b, B.intValue());
            assertEquals(b, B.byteValue());
        }

        for (int i = len - 1; 0 <= i; i--) {
            try {
                list.remove(i);
            } catch (Exception e) {
                fail();
            }
        }
    }

    @Test
    public void test_toList_StringList() {

        final String[] array = new String[] {
            "0",
            "1",
            "2",
            "3"
        };
        final int len = array.length;

        final List<String> list1 = ArrayUtil.toList(array);
        for (int i = 0; i < len; i++) {
            assertEquals(array[i], list1.get(i));
        }

        for (int i = len - 1; 0 <= i; i--) {
            try {
                list1.remove(i);
            } catch (Exception e) {
                fail();
            }
        }

        ////////

        final List<String> list2 = StringUtil.toList(array);
        for (int i = 0; i < len; i++) {
            assertEquals(array[i], list2.get(i));
        }

        for (int i = len - 1; 0 <= i; i--) {
            try {
                list2.remove(i);
            } catch (Exception e) {
                fail();
            }
        }
    }

    // endregion toList

    // region reverse

    @Test
    public void test_reverse_IntArray() {

        final int[] array = new int[] {
            0,
            1,
            2,
            3
        };
        final int len = array.length;

        final List<Integer> resultValue = new ArrayList<Integer>() {{
            add(3);
            add(2);
            add(1);
            add(0);
        }};

        final List<Integer> list = ArrayUtil.reverseToList(array);
        for (int i = 0; i < len; i++) {
            assertEquals(resultValue.get(i), list.get(i));
        }

        for (int i = len - 1; 0 <= i; i--) {
            try {
                list.remove(i);
            } catch (Exception e) {
                fail();
            }
        }
    }

    @Test
    public void test_reverse_ByteArray() {

        final byte[] array = new byte[] {
            0,
            1,
            2,
            3
        };
        final int len = array.length;

        final List<Byte> resultValue = new ArrayList<Byte>() {{
            add((byte) 3);
            add((byte) 2);
            add((byte) 1);
            add((byte) 0);
        }};

        final List<Byte> list = ArrayUtil.reverseToList(array);
        for (int i = 0; i < len; i++) {
            assertEquals(resultValue.get(i), list.get(i));
        }

        for (int i = len - 1; 0 <= i; i--) {
            try {
                list.remove(i);
            } catch (Exception e) {
                fail();
            }
        }
    }

    @Test
    public void test_reverse_StringArray() {

        final String[] array = new String[] {
            "0",
            "1",
            "2",
            "3"
        };
        final int len = array.length;

        final List<String> resultValue = new ArrayList<String>() {{
            add("3");
            add("2");
            add("1");
            add("0");
        }};

        final List<String> list = ArrayUtil.reverseToList(array);
        for (int i = 0; i < len; i++) {
            assertEquals(resultValue.get(i), list.get(i));
        }

        for (int i = len - 1; 0 <= i; i--) {
            try {
                list.remove(i);
            } catch (Exception e) {
                fail();
            }
        }
    }

    @SuppressWarnings("all")
    @Test
    public void test_reverse_list_Listの場合は直接操作するのでListを新たに生成しない() {

        final List<String> list = new ArrayList<String>() {{
            add("0");
            add("1");
            add("2");
            add("3");
        }};
        final int len = list.size();

        final List<String> resultValue = new ArrayList<String>() {{
            add("3");
            add("2");
            add("1");
            add("0");
        }};

        assertTrue(ArrayUtil.reverse(list));

        for (int i = 0; i < len; i++) {
            assertEquals(resultValue.get(i), list.get(i));
        }

        for (int i = len - 1; 0 <= i; i--) {
            try {
                list.remove(i);
            } catch (Exception e) {
                fail();
            }
        }
    }
    @Test
    public void test_reverse_ToArray_Int() {

        final int[] array = new int[] {
                0,
                1,
                2,
                3
        };
        final List<Integer> resultValue = new ArrayList<Integer>() {{
            add(3);
            add(2);
            add(1);
            add(0);
        }};

        final int len = array.length;
        final int[] list = ArrayUtil.reverseToArray(array);
        for (int i = 0; i < len; i++) {
            assertTrue(resultValue.get(i) == list[i]);
        }
    }
    @Test
    public void test_reverse_ToArray_Byte() {

        final byte[] array = new byte[] {
                0,
                1,
                2,
                3
        };
        final int len = array.length;

        final List<Byte> resultValue = new ArrayList<Byte>() {{
            add((byte) 3);
            add((byte) 2);
            add((byte) 1);
            add((byte) 0);
        }};
        final byte[] list = ArrayUtil.reverseToArray(array);
        for (int i = 0; i < len; i++) {
            assertTrue(resultValue.get(i) == list[i]);
        }
    }
    // endregion reverse
}