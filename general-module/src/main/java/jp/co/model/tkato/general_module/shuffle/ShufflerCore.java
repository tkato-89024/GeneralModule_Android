package jp.co.model.tkato.general_module.shuffle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jp.co.model.tkato.general_module.log.Logger;

public class ShufflerCore implements Shuffler {

    private Random random = new Random(System.currentTimeMillis());

    private int _pos     = -1;
    private int _nextTmp = -1;
    private int _prevTmp = -1;
    private int _min     = 0;
    private int _max     = 0;
    private int _len     = 0;

    private List<Integer> _base  = new ArrayList<>();
    private List<Integer> _stack = new ArrayList<>();

    @Override
    public int getMin() {
        return _min;
    }

    @Override
    public int getMax() {
        return _max;
    }

    @Override
    public int getLength() {
        return _len;
    }

    @Override
    public int getPosition() {
        return _pos;
    }

    @Override
    public int getNextPosition() {

        int pos = _pos;

        if (0 > pos) {
            return pos;
        }

        pos += 1;
        if (pos >= _len) {
            pos = 0;
        }
        return pos;
    }

    @Override
    public int getPrevPosition() {

        int pos = _pos;

        if (0 > pos) {
            return pos;
        }

        pos -= 1;
        if (pos <= 0) {
            pos = _len - 1;
        }
        return pos;
    }

    @Override
    public int getCurrentIndex() {

        if (0 > _pos) {
            return -1;
        }

        return _stack.get(getPosition());
    }

    @Override
    public int getNextIndex() {

        if (0 > _pos) {
            return -1;
        }

        int pos = _pos;
        pos += 1;
        Logger.v("pos = " + pos + ", len = " + _len);
        if (pos >= _len) {
            return _nextTmp;
        }
        return _stack.get(pos);
    }

    @Override
    public int getPrevIndex() {

        if (0 > _pos) {
            return -1;
        }

        int pos = _pos;
        pos -= 1;
        if (pos < 0) {
            Logger.v("_prevTmp = " + _prevTmp);
            return _prevTmp;
        }
        return _stack.get(pos);
    }

    @Override
    public int getFirstIndex() {

        if (0 > _pos || 0 >= _stack.size()) {
            return -1;
        }

        return _stack.get(0);
    }

    @Override
    public int getLastIndex() {

        if (0 > _pos) {
            return -1;
        }

        final int size = _stack.size();
        if (0 >= size) {
            return -1;
        }

        return _stack.get(size-1);
    }

    @Override
    public void setPosition(int position) {

        if (position < 0 || _len <= position) {
            Logger.w("fail: out of range"
                + "\n" + "position = " + position
                + "\n" + "length   = " + _len
            );
            return;
        }

        _pos = position;
    }

    /**
     * 指定した index を元に、position を設定する
     * @param index 検索に使用する index
     * @return 設定された position の取得
     */
    @Override
    public int setPositionByIndex(int index) {
        if (   0 > index
            || index < _min || _max <= index
        ) {
            Logger.w("fail: out of range"
                + "\n" + "index = " + index
                + "\n" + "min = " + _min
                + "\n" + "max = " + _max
            );
            return _pos;
        }
        _pos = _stack.indexOf(index);
        return _pos;
    }

    /**
     * 次の position に移動する。<br/>
     * 指定した position が length を超えた場合、新しい乱数リストを生成して<br/>
     * position はリスト最前列 (0) に戻る。
     * @return リストが更新された場合 true
     */
    @Override
    public boolean next() {

        if (0 > _pos) {
            return false;
        }

        int pos = _pos;
        pos += 1;
        if (pos >= _len) {
            pos = 0;
            setup(_nextTmp, pos);
            return true;
        }
        _pos = pos;
        // return _stack.get(_pos);
        return false;
    }

    /**
     * 前の position に移動する。<br/>
     * 指定した position が 0 を下回った場合、新しい乱数リストを生成して<br/>
     * position はリスト最後尾（length - 1) に進む。
     * @return リストが更新された場合 true
     */
    @Override
    public boolean prev() {

        if (0 > _pos) {
            return false;
        }

        int pos = _pos;
        pos -= 1;
        if (pos < 0) {
            Logger.v("_prevTmp = " + _prevTmp);
            pos = _len - 1;
            setup(_prevTmp, pos);
            return true;
        }
        _pos = pos;
        // return _stack.get(_pos);
        return false;
    }

    /**
     * 乱数リストの作成
     * min = 20, limit = 30 の指定で、20 ~ 29 の乱数のリストを作成する
     * 値が正しくない場合、リスト削除のみ行われる
     * @param initIndex position = 0 の時の index の値
     * @param min index の最低値
     * @param limit index の限界値
     * @return initialize に成功した場合 true
     */
    // @SuppressWarnings("all") // Condition covered by subsequent conditions
    @Override
    public boolean initialize(int initIndex, int min, int limit) {

        Logger.d(""
            + "\n" + "initIndex = " + initIndex
            + "\n" + "min       = " + min
            + "\n" + "limit     = " + limit
        );

        clear();

        // リスト０
        if (   0 >  initIndex
            || 0 >  min
            || min >= limit
            || initIndex < min
            || initIndex >= limit
        ) {
            Logger.w("fail: list zero");
            return false;
        }

        _pos     = 0;
        _nextTmp = -1;
        _prevTmp = -1;
        _min     = min;
        _max     = limit - 1;
        _len     = limit - _min;

        // min >= 0
        // max >= 0
        // len >= 1

        // シャッフル前のインデックスリスト作成
        for (int i = _min, l = _max; i <= l; i++) {
            _base.add(i);
        }

        setup(initIndex, _pos);

        Logger.d(""
            + "\n" + "size    = " + _base.size()
            + "\n" + "pos     = " + _pos
            + "\n" + "min     = " + _min
            + "\n" + "max     = " + _max
            + "\n" + "len     = " + _len
        );

        return true;
    }

    @Override
    public void clear() {
        _pos     = -1;
        _nextTmp = -1;
        _prevTmp = -1;
        _min     = 0;
        _max     = 0;
        _len     = 0;
        _base.clear();
        _stack.clear();
    }


    // region private

    private void setup(int initIndex, int position) {

        _stack = new ArrayList<>(_base);

        Collections.shuffle(_stack, random);

        int i = _stack.indexOf(initIndex);
        int e = _stack.remove(i);
        _stack.add(position, e);

        Logger.v("i = " + i);
        Logger.v("e = " + e);

        _nextTmp = random(_min, _max);
        _prevTmp = random(_min, _max);
    }

    // min <= max の範囲でランダム値生成
    @SuppressWarnings("UnnecessaryLocalVariable") // Local variable is redundant
    private int random(int min, int max) {
        final int rng = max + 1 - min;
        final int res = random.nextInt(rng);
        final int ajt = res + min;
        return ajt;
    }

    // private int random(int ignore, int min, int max) {
    //     final int result = random(min, max);
    //     if (result == ignore) {
    //         return random(ignore, min, max);
    //     }
    //     return result;
    // }

    // endregion private
}
