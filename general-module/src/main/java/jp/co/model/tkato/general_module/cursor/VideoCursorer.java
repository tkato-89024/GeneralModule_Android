package jp.co.model.tkato.general_module.cursor;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import jp.co.model.tkato.general_module.log.Logger;
import jp.co.model.tkato.general_module.query.ContentResolverQueryOrganizer;
import jp.co.model.tkato.general_module.query.IQueryOrganizer;

public class VideoCursorer extends Cursorer implements IContentResolverCursorer {

    public interface IForEach {
        void syncRun(Context context, final int id, final String title, final String artist, final String path, final String mime, final long size, final long duration, final long width, final long height);
    }

    public static VideoCursorer create() {
        return new VideoCursorer(
            new ContentResolverQueryOrganizer(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
            )
        );
    }

    @NonNull
    public ContentResolverQueryOrganizer getOrganizer() {
        return (ContentResolverQueryOrganizer) organizer;
    }

    @NonNull
    public void setOrganizer(ContentResolverQueryOrganizer organizer) {
        this.organizer = organizer;
    }

    public VideoCursorer() {
        super();
    }

    public VideoCursorer(@Nullable IQueryOrganizer organizer) {
        super(organizer);
    }

    public int getCountNewCursor() {
        return super.getCountNewCursor(new ContentResolverQueryOrganizer(
            getOrganizer().getUri(),
            new String[] { MediaStore.Video.Media._ID },
            getOrganizer().getSelection(),
            getOrganizer().getSelectionArgs(),
            getOrganizer().getOrderBy()
        ));
    }

    @Override
    public VideoCursorer query() {
        return (VideoCursorer) super.query();
    }

    @Override
    public VideoCursorer query(long length, long offset) {
        return (VideoCursorer) super.query(length, offset);
    }

    public void forEach(@Nullable final Context activityContext, @Nullable final IForEach action) {
        forEach(activityContext, false, action);
    }

    public void forEach(@Nullable final IForEach action) {
        forEach(null, false, action);
    }

    public void forEach(final boolean isReadVideoSize, @Nullable final IForEach action) {
        forEach(null, isReadVideoSize, action);
    }

    public void forEach(@Nullable final Context activityContext, final boolean isReadVideoSize, @Nullable final IForEach action) {
        if (null == action) {
            Logger.w("forEach fail: null action");
            return;
        } else if (null == cursor) {
            Logger.w("forEach fail: null cursor");
            return;
        } else if (0 == cursor.getCount()) {
            Logger.w("forEach fail: cursor count 0");
            return;
        }
        try {
            if (!cursor.moveToFirst()) {
                Logger.w("forEach fail: moveToFirst");
                return;
            }
            do {
                final int    id       = cursor.getInt   (cursor.getColumnIndex       (MediaStore.Video.Media._ID));
                final String title    = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                final String artist   = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                final String path     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                final String mime     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                final long   size     = cursor.getLong  (cursor.getColumnIndex       (MediaStore.Video.Media.SIZE));
                final long   duration = cursor.getLong  (cursor.getColumnIndex       (MediaStore.Video.Media.DURATION));
                long         width    = cursor.getLong  (cursor.getColumnIndex       (MediaStore.Video.Media.WIDTH));
                long         height   = cursor.getLong  (cursor.getColumnIndex       (MediaStore.Video.Media.HEIGHT));

                if (isReadVideoSize && (0 == width || 0 == height)) {
                    final MediaPlayer mp = new MediaPlayer();
                    mp.setDataSource(path);
                    mp.prepare();
                    // カメラムービーは width x height が取れない
                    // MediaPlayer で読み込むと取得可能になる
                    // prepare にかかる時間
                    // 動画 00:04 => 38  ms
                    // 動画 16:58 => 523 ms
                    width  = mp.getVideoWidth();
                    height = mp.getVideoHeight();
                }

                action.syncRun(
                    activityContext,
                    id,
                    title,
                    artist,
                    path,
                    mime,
                    size,
                    duration,
                    width,
                    height
                );

            } while (cursor.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (null != cursor) { try { cursor.close(); } catch (Exception e) {} }
        }
    }
}
