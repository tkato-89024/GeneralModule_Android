package jp.co.model.tkato.general_module.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class StringUtilTest {

    // region check

    @SuppressWarnings("ConstantConditions")
    @Test
    public void test_isEmpty() {

        assertTrue(StringUtil.isEmpty(null));
        assertTrue(StringUtil.isEmpty(""));

        assertFalse(StringUtil.isEmpty("Aiueo"));
        assertFalse(StringUtil.isEmpty("ほげ"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void test_equal() {

        assertTrue(StringUtil.equals(null, null));
        assertTrue(StringUtil.equals("", ""));
        assertTrue(StringUtil.equals("Aiueo", "Aiueo"));
        final String hoge = "ほげ";
        assertTrue(StringUtil.equals("ほげ", hoge));
        assertTrue(StringUtil.equals(hoge, "ほげ"));


        assertFalse(StringUtil.equals(null, ""));
        assertFalse(StringUtil.equals("", null));
        assertFalse(StringUtil.equals("Aiueo", "Kakikukeko"));
        assertFalse(StringUtil.equals("ふが", hoge));
        assertFalse(StringUtil.equals(hoge, "ふが"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void test_equalsIgnoreNull() {

        assertTrue(StringUtil.equalsIgnoreNull("", ""));
        assertTrue(StringUtil.equalsIgnoreNull("Aiueo", "Aiueo"));
        final String hoge = "ほげ";
        assertTrue(StringUtil.equalsIgnoreNull("ほげ", hoge));
        assertTrue(StringUtil.equalsIgnoreNull(hoge, "ほげ"));


        assertFalse(StringUtil.equalsIgnoreNull(null, null));

        assertFalse(StringUtil.equalsIgnoreNull(null, ""));
        assertFalse(StringUtil.equalsIgnoreNull("", null));
        assertFalse(StringUtil.equalsIgnoreNull("Aiueo", "Kakikukeko"));
        assertFalse(StringUtil.equalsIgnoreNull("ふが", hoge));
        assertFalse(StringUtil.equalsIgnoreNull(hoge, "ふが"));
    }

    @Test
    public void test_isDigit() {

        assertTrue(StringUtil.isDigit("12345"));
        assertTrue(StringUtil.isDigit("098765"));

        assertFalse(StringUtil.isDigit(""));
        assertFalse(StringUtil.isDigit("-1234"));
        assertFalse(StringUtil.isDigit("+1234"));
        assertFalse(StringUtil.isDigit("1.234"));
    }

    @Test
    public void test_isNumeric() {

        assertTrue(StringUtil.isNumeric("12345"));
        assertTrue(StringUtil.isNumeric("098765"));
        assertTrue(StringUtil.isNumeric("-1234"));
        assertTrue(StringUtil.isNumeric("1.234"));

        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("+1234"));
    }

    // endregion check

    // region convert

    // region array/list

    @SuppressWarnings("all")
    @Test
    public void test_Nullチェック_toArray_toList() {

        final String[] stringArray = null;
        assertNull(StringUtil.toList(stringArray));
        assertNull(ArrayUtil.toList(stringArray));

        final List<String> stringList = null;
        assertNull(StringUtil.toArray(stringList));
        assertNull(ArrayUtil.toList(stringArray));
    }

    @Test
    public void test_toArray() {

        final List<String> strList = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
            add("4");
        }};

        final String[] resultValue = new String[] {"1", "2", "3", "4"};

        assertArrayEquals(resultValue, StringUtil.toArray(strList));

        try {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void test_toList() {

        final String[] strs = new String[] {"1", "2", "3", "4"};

        final List<String> resultValue = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
            add("4");
        }};

        final List<String> list = StringUtil.toList(strs);

        assertEquals(resultValue, list);
    }

    @SuppressWarnings("all")
    @Test
    public void test_toList_addとremoveで例外が発生しないかどうか確認() {

        final String[] strs = new String[] {"1", "2", "3", "4"};

        final List<String> list = StringUtil.toList(strs);

        try {
            list.add("check add");
            list.remove(0);
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    // endregion array/list

    // region split

    @Test
    public void test_split() {

        final String[] emptyValue = new String[] {};

        final String emp = "";
        assertArrayEquals(emptyValue, StringUtil.split(emp));

        final String[] resultValue = new String[] {"1", "2", "3", "4"};

        final String str1 = "1,2,3,4";
        //noinspection ConstantConditions
        assertArrayEquals(resultValue, StringUtil.split(str1,null));
        assertArrayEquals(resultValue, StringUtil.split(str1));

        final String str2 = "1:2:3:4";
        assertArrayEquals(resultValue, StringUtil.split(str2, ":"));

        final String str3 = "1, 2, 3, 4";
        assertArrayEquals(resultValue, StringUtil.split(str3, ", "));
    }

    @Test
    public void test_splitList() {

        final List<String> emptyValue = new ArrayList<String>() {{
        }};

        final String emp = "";
        assertEquals(emptyValue, StringUtil.splitList(emp));

        final List<String> resultValue = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
            add("4");
        }};

        final String str1 = "1,2,3,4";
        assertEquals(resultValue, StringUtil.splitList(str1));
        //noinspection ConstantConditions
        assertEquals(resultValue, StringUtil.splitList(str1,null));

        final String str2 = "1:2:3:4";
        assertEquals(resultValue, StringUtil.splitList(str2, ":"));

        final String str3 = "1, 2, 3, 4";
        assertEquals(resultValue, StringUtil.splitList(str3, ", "));
    }

    // endregion split

    // region join

    @Test
    public void test_join() {

        //null
        final String[] emp =  null;

        final List<String> empList = null;

        final String empValue = "";

        //noinspection ConstantConditions
        assertEquals(empValue, StringUtil.join(emp));
        //noinspection ConstantConditions
        assertEquals(empValue, StringUtil.join(empList));

        //strs.size < 1
        final String[] minus = new String[0];

        final List<String> minusList = new ArrayList<>(0);

        assertEquals(empValue, StringUtil.join(minus));
        assertEquals(empValue, StringUtil.join(minusList));

        //any value
        final String[] strs = new String[] {"1", "2", "3", "4"};

        final List<String> strList = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
            add("4");
        }};

        final String resultValue1 = "1,2,3,4";

        assertEquals(resultValue1, StringUtil.join(strs));
        assertEquals(resultValue1, StringUtil.join(strList));

        //noinspection ConstantConditions
        assertEquals(resultValue1, StringUtil.join(strs,null));
        //noinspection ConstantConditions
        assertEquals(resultValue1, StringUtil.join(strList,null));


        final String resultValue2 = "1:2:3:4";

        assertEquals(resultValue2, StringUtil.join(strs, ":"));
        assertEquals(resultValue2, StringUtil.join(strList, ":"));


        final String resultValue3 = "1, 2, 3, 4";

        assertEquals(resultValue3, StringUtil.join(strs, ", "));
        assertEquals(resultValue3, StringUtil.join(strList, ", "));
    }

    // endregion join

    // endregion convert
}