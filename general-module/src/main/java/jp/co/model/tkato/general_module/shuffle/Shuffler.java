package jp.co.model.tkato.general_module.shuffle;

public interface Shuffler {

    int getMin();
    int getMax();
    int getLength();

    int getPosition();
    int getNextPosition();
    int getPrevPosition();

    int getCurrentIndex();
    int getNextIndex();
    int getPrevIndex();
    int getFirstIndex();
    int getLastIndex();


    boolean initialize(int initIndex, int min, int limit);

    void clear();

    void setPosition(int position);

    @SuppressWarnings("UnusedReturnValue")
    int setPositionByIndex(int index);

    boolean next();
    boolean prev();
}
