/*
 GeneralModule_Android ArrayUtil

 Copyright (c) 2019 tkato

 This software is released under the MIT License.
 http://opensource.org/licenses/mit-license.php
 */

package jp.co.model.tkato.general_module.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ArrayUtil {

    // region convert to

    // ジェネリクスは基本的に new が不可能
    @SuppressWarnings("ConstantConditions")
    @NonNull
    public static <T> T[] toArray(@NonNull final List<T> list, @NonNull final T[] newArray) {
        if (null == list || null == newArray) {
            // Timber.w("toArray fail: list null = " + (null == list) + ", newArray null = " + (null == newArray));
            return null;
        }
        return list.toArray(newArray);
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    public static int[] toIntArray(@NonNull final List<Integer> list) {
        if (null == list) {
            return null;
        }
        final int[] values = new int[list.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = list.get(i);
        }
        return values;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    public static byte[] toByteArray(@NonNull final List<Byte> list) {
        if (null == list) {
            return null;
        }
        final byte[] values = new byte[list.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = list.get(i);
        }
        return values;
    }

    @SuppressWarnings("all") // Call to 'asList' with only one argument
    @NonNull
    public static List<Integer> toList(@Nullable final int[] array) {
        if (null == array) {
            return null;
        }

        final int       len    = array.length;
        final Integer[] array_ = new Integer[len];
        for (int i = 0; i < len; i++) {
            array_[i] = array[i];
        }
        return new ArrayList(Arrays.asList(array_));
    }

    @SuppressWarnings("all")
    @NonNull
    public static List<Byte> toList(@Nullable final byte[] array) {
        if (null == array) {
            return null;
        }

        final int    len    = array.length;
        final Byte[] array_ = new Byte[len];
        for (int i = 0; i < len; i++) {
            array_[i] = array[i];
        }
        return new ArrayList(Arrays.asList(array_));
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"}) // Unchecked call to
    public static <T> List<T> toList(@NonNull final T[] array) {
        if (null == array) {
            // Timber.w("toList fail: array null");
            return null;
        }
        // remove 時に java.lang.UnsupportedOperationException
        // return Arrays.asList(array);
        return new ArrayList(Arrays.asList(array));
    }

    // endregion convert to

    // region sort

    @SuppressWarnings({"ConstantConditions", "UnusedReturnValue"}) // Return value of the method is never used
    public static <T> boolean reverse(@NonNull final List<T> list) {
        if (null == list) {
            return false;
        }
        Collections.reverse(list);
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    public static <T> List<T> reverseToList(@NonNull final T[] array) {
        if (null == array) {
            return null;
        }
        final List<T> list = new ArrayList<>(Arrays.asList(array));
        Collections.reverse(list);
        return list;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    public static List<Integer> reverseToList(@NonNull final int[] array) {
        if (null == array) {
            return null;
        }
        final List<Integer> list = new ArrayList<>(array.length);
        for (int i = array.length - 1; i >= 0; i--) {
            list.add(array[i]);
        }
        return list;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    public static List<Byte> reverseToList(@NonNull final byte[] array) {
        if (null == array) {
            return null;
        }
        final List<Byte> list = new ArrayList<>(array.length);
        for (int i = array.length - 1; i >= 0; i--) {
            list.add(array[i]);
        }
        return list;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    public static int[] reverseToArray(@NonNull final int[] array) {
        if (null == array) {
            return null;
        }
        final int[] list = new int[array.length];
        for (int i = array.length - 1, cnt = 0; i >= 0; i--, cnt++) {
            list[cnt] = array[i];
        }
        return list;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    public static byte[] reverseToArray(@NonNull final byte[] array) {
        if (null == array) {
            return null;
        }
        final byte[] list = new byte[array.length];
        for (int i = array.length - 1, cnt = 0; i >= 0; i--, cnt++) {
            list[cnt] = array[i];
        }
        return list;
    }

    // endregion sort

    private ArrayUtil() {}
}
