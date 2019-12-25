package jp.co.model.tkato.general_module.log.factory;

import jp.co.model.tkato.general_module.log.tree.DebugTree;
import jp.co.model.tkato.general_module.log.tree.ReleaseTree;
import timber.log.Timber;

public class TreeFactory implements ITreeFactory {

    private boolean isDebug;

    @SuppressWarnings("WeakerAccess")
    public boolean isDebug() {
        return isDebug;
    }

    public TreeFactory(boolean isDebug) {
        this.isDebug = isDebug;
    }

    @Override
    public Timber.Tree create() {
        if (isDebug()) {
            return new DebugTree();
        } else {
            return new ReleaseTree();
        }
    }
}
