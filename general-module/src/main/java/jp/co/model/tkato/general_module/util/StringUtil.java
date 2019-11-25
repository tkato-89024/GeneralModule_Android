/*
 GeneralModule_Android StringUtil

 Copyright (c) 2019 tkato

 This software is released under the MIT License.
 http://opensource.org/licenses/mit-license.php
 */
package jp.co.model.tkato.general_module.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class StringUtil {

    // region check

    /**
     * null、空文字のチェック
     * @param val 対象文字列
     * @return val が null か空文字の場合 true
     */
    public static boolean isEmpty(@Nullable final String val) {
        return null == val || val.isEmpty();
    }

    /**
     * 文字列の比較
     * @param lhs 左辺
     * @param rhs 右辺
     * @return 比較結果
     */
    public static boolean equals(@Nullable final String lhs, @Nullable final String rhs) {
        if (null != lhs) {
            return lhs.equals(rhs);
        }
        return null == rhs;
    }

    /**
     * 文字列の比較（null 同士の比較は false として返す）
     * @param lhs 左辺
     * @param rhs 右辺
     * @return 比較結果
     */
    public static boolean equalsIgnoreNull(@Nullable final String lhs, @Nullable final String rhs) {
        if (null != lhs) {
            return lhs.equals(rhs);
        }
        return false;
    }

    /**
     * 文字列が 0~9 かどうかの確認
     * @param value 文字列
     * @return 0~9 以外の文字が含まれていれば false
     */
    public static boolean isDigit(@Nullable String value) {
        if (isEmpty(value)) {
            return false;
        }
        for (char aChar : value.toCharArray()) {
            if (!Character.isDigit(aChar)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 数字かどうかのチェック
     * Double に変換できるかどうかで判断
     * @param val 文字列
     * @return 変換できなければ false
     */
    public static boolean isNumeric(@Nullable String val) {
        if (isEmpty(val)) {
            return false;
        }
        return val.matches("-?\\d+(\\.\\d+)?");
    }

    // endregion check

    // region convert

    // region array/list

    @SuppressWarnings("ConstantConditions")
    @NonNull
    public static String[] toArray(@NonNull List<String> list) {
        if (null == list) {
            return null;
        }
        // https://stackoverflow.com/questions/174093/toarraynew-myclass0-or-toarraynew-myclassmylist-size
        return list.toArray(new String[0]);
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @NonNull
    public static List<String> toList(@NonNull String[] array){
        if (null == array) {
            return null;
        }
        // LinkedList がないと、remove 時に java.lang.UnsupportedOperationException
        return new ArrayList(Arrays.asList(array));
    }

    // endregion array/list

    // region split

    /**
     * 文字列を配列に分割する
     * 分割に使用する文字列は ","
     * @param str 分割対象の文字列
     * @return 分割した結果
     */
    @NonNull
    public static String[] split(@NonNull final String str) {
        return split(str, ",");
    }

    /**
     * 文字列を配列に分割する
     * @param str 分割対象の文字列
     * @param regex 分割に使用する文字列
     * @return 分割した結果
     */
    @SuppressWarnings("ConstantConditions")
    @NonNull
    public static String[] split(@NonNull final String str, @NonNull final String regex) {
        if (isEmpty(str)) {
            return new String[] {};
        }

        // 空文字だと１文字区切りになるので null だけ対応する

        // ただし Java7, Java8 で挙動が変化するらしい
        // - Java 7 -> [, A, B, C]
        // - Java 8 -> [A, B, C]

        return str.split(null == regex ? "," : regex);
    }

    /**
     * 文字列を List に分割する
     * 分割に使用する文字列は ","
     * @param str 分割対象の文字列
     * @return 分割した結果
     */
    @NonNull
    public static List<String> splitList(@NonNull final String str) {
        return splitList(str, ",");
    }

    /**
     * 文字列を List に分割する
     * @param str 分割対象の文字列
     * @param regex 分割に使用する文字列
     * @return 分割した結果
     */
    @SuppressWarnings("ConstantConditions")
    @NonNull
    public static List<String> splitList(@NonNull final String str, @NonNull final String regex) {
        if (isEmpty(str)) {
            return toList(new String[] {});
        }

        final String regex_ = null == regex ? "," : regex;

        return toList(str.split(regex_));
    }

    // endregion split

    // region join

    /**
     * 配列を結合して文字列にする
     * 結合に使用する文字列は ","
     * @param strs 結合対象の配列
     * @return 結合した結果
     */
    @NonNull
    public static String join(@NonNull final String[] strs) {
        return join(strs, ",");
    }

    /**
     * 配列を結合して文字列にする
     * @param strs 結合対象の配列
     * @param val 結合に使用する文字列
     * @return 結合した結果
     */
    @SuppressWarnings("ConstantConditions")
    @NonNull
    public static String join(@NonNull final String[] strs, @NonNull final String val) {
        if (null == strs || 1 > strs.length) {
            return "";
        }

        final StringBuilder builder = new StringBuilder();

        final String val_ = null == val ? "," : val;
        final int    len  = strs.length - 1;

        for (int i = 0; i <= len; i++) {
            builder.append(strs[i]);
            if (i != len) {
                builder.append(val_);
            }
        }
        return builder.toString();
    }

    /**
     * List を結合して文字列にする
     * 結合に使用する文字列は ","
     * @param strs 結合対象の List
     * @return 結合した結果
     */
    @NonNull
    public static String join(@NonNull final List<String> strs) {
        return join(strs, ",");
    }

    /**
     * List を結合して文字列にする
     * @param strs 結合対象の List
     * @param val 結合に使用する文字列
     * @return 結合した結果
     */
    @SuppressWarnings("ConstantConditions")
    @NonNull
    public static String join(@NonNull final List<String> strs, @NonNull final String val) {
        if (null == strs || 1 > strs.size()) {
            return "";
        }

        final StringBuilder builder = new StringBuilder();

        final String val_ = null == val ? "," : val;
        final int    len  = strs.size() - 1;

        for (int i = 0; i <= len; i++) {
            builder.append(strs.get(i));
            if (i != len) {
                builder.append(val_);
            }
        }
        return builder.toString();
    }

    // endregion join

    // endregion convert

    private StringUtil() {}
}
