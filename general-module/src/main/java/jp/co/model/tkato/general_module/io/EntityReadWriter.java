package jp.co.model.tkato.general_module.io;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Jackson を利用して、Entity クラスを SharedPreferences で保存・読込する
 * 同じクラスのデータを複数保存することはできない
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class EntityReadWriter {

    private static final Object lock = new Object();

    // ActivityContext を付与するとリークするので、AppContext を使う
    @SuppressLint("StaticFieldLeak") // Do not place Android context classes in static fields; this is a memory leak
    private static EntityReadWriter instance;

    public static EntityReadWriter instance(@NonNull Context appContext) {

        synchronized (lock) {
            if (null == instance) {
                instance = new EntityReadWriter(appContext.getApplicationContext());
            }
            return instance;
        }
    }

    @NonNull
    protected Context context;

    public EntityReadWriter(@NonNull Context appContext) {
        context = appContext;
    }

    // region method

    @Nullable
    public <T> T bind(@NonNull Class<T> clazz) {
        try {
            final String raw = getSharedPreferences(context).getString(clazz.toString(), null);
            // Timber.d("bind: " + raw);
            if (null != raw) {
                return new ObjectMapper().readValue(raw, clazz);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public <T> T bind(@NonNull Class<T> clazz, @NonNull Callable<T> callable) {
        final T result = bind(clazz);
        if (null != result) {
            return result;
        }

        try {
            return callable.call();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("ApplySharedPref") // Consider using instead; commit writes its data to presistent storage immediately.
    public void allClear() {
        final SharedPreferences        pref   = getSharedPreferences(context);
        final SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    // endregion method

    // region method chain

    /**
     * 既に引数と同じクラスのデータが保存されていれば put しない
     * @param object 保存データ
     * @return 自身
     */
    @SuppressWarnings("unchecked")
    public <T> Editor putOnce(@NonNull T object) {
        // Timber.i("putOnce: " + object);
        if (null == bind((Class<T>) object.getClass())) {
            return new Editor(context).put(object);
        }
        return new Editor(context);
    }

    public <T> Editor putOnce(@NonNull Class<T> clazz, @NonNull Callable<T> callable) {
        // ジェネリクスから型取得が実質不可能（要実装になってしまうため）
        // Timber.i("putOnce: " + clazz);
        if (null == bind(clazz)) {
            try {
                return new Editor(context).put(callable.call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new Editor(context);
    }

    public <T> Editor put(@NonNull T object) {
        return new Editor(context).put(object);
    }

    public <T> Editor clear(@NonNull Class<T> clazz) {
        return new Editor(context).clear(clazz);
    }

    // endregion method chain

    // region inner class Editor

    /**
     * put、remove をメソッドチェーンでまとめて commit するためのクラス
     */
    public class Editor {

        @NonNull
        private SharedPreferences sp;

        @NonNull
        private SharedPreferences.Editor editor;

        @SuppressWarnings("CommitPrefEdits")
        protected Editor(@NonNull Context context) {
            sp     = getSharedPreferences(context);
            editor = sp.edit();
        }

        @NonNull
        public <T> Editor putOnce(@NonNull T object) {
            // Timber.i("putOnce: " + object);
            if (null == sp.getString(object.getClass().toString(), null)) {
                return put(object);
            }
            return this;
        }

        public <T> Editor putOnce(@NonNull Class<T> clazz, @NonNull Callable<T> callable) {
            if (null == sp.getString(clazz.toString(), null)) {
                try {
                    return put(callable.call());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return this;
        }

        @SuppressWarnings("ConstantConditions")
        @NonNull
        public <T> Editor put(@NonNull T object) {
            try {
                // Timber.d("put: " + object);
                if (null != object) {
                    editor.putString(object.getClass().toString(), new ObjectMapper().writeValueAsString(object));
                }

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return this;
        }

        @SuppressWarnings("ConstantConditions")
        @NonNull
        public <T> Editor clear(@NonNull Class<T> clazz) {
            // Timber.d("clear: " + clazz);
            if (null != clazz) {
                editor.remove(clazz.toString());
            }
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        @NonNull
        public Editor commit() {
            final boolean result = editor.commit();
            // Timber.d("commit result: " + result);
            return this;
        }
    }

    // endsegion inner class Editor

    @NonNull
    private SharedPreferences getSharedPreferences(@NonNull final Context context) {
        return context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
    }
}
