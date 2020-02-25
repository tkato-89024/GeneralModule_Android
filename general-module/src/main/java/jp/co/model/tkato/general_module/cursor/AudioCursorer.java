package jp.co.model.tkato.general_module.cursor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import jp.co.model.tkato.general_module.query.ContentResolverQueryOrganizer;
import jp.co.model.tkato.general_module.query.IQueryOrganizer;

public class AudioCursorer extends Cursorer implements IContentResolverCursorer {

    public interface IForEach {
        void syncRun(Context context, final int id, final String title, final String artist, final String path, final String mime, final long size, final long duration, final String album, final int track);
    }

    public static AudioCursorer create() {
        return new AudioCursorer(
            new ContentResolverQueryOrganizer(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.AudioColumns.IS_MUSIC + "=? ",
                new String[] { "1" },
                null
            )
        );
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    public ContentResolverQueryOrganizer getOrganizer() {
        return (ContentResolverQueryOrganizer) organizer;
    }

    public void setOrganizer(ContentResolverQueryOrganizer organizer) {
        this.organizer = organizer;
    }

    public AudioCursorer() {
        super();
    }

    private AudioCursorer(@Nullable IQueryOrganizer organizer) {
        super(organizer);
    }

    public int getCountNewCursor() {
        return super.getCountNewCursor(new ContentResolverQueryOrganizer(
            getOrganizer().getUri(),
            new String[] { MediaStore.Audio.Media._ID },
            getOrganizer().getSelection(),
            getOrganizer().getSelectionArgs(),
            getOrganizer().getOrderBy()
        ));
    }

    @Override
    public AudioCursorer query() {
        return (AudioCursorer) super.query();
    }

    @Override
    public AudioCursorer query(long length, long offset) {
        return (AudioCursorer) super.query(length, offset);
    }

    public void forEach(@Nullable final IForEach action) {
        forEach(null, action);
    }

    @SuppressLint("InlinedApi")
    public void forEach(@Nullable final Context activityContext, @Nullable final IForEach action) {
        if (null == action) {
//            Timber.w("forEach fail: null action");
            return;
        } else if (null == cursor) {
//            Timber.w("forEach fail: null cursor");
            return;
        } else if (0 == cursor.getCount()) {
//            Timber.w("forEach fail: cursor count 0");
            return;
        }
        try {
            if (!cursor.moveToFirst()) {
//                Timber.w("forEach fail: moveToFirst");
                return;
            }
            do {
                final int    id       = cursor.getInt   (cursor.getColumnIndex       (MediaStore.Audio.Media._ID));
                final String title    = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                final String artist   = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                final String path     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                final String mime     = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
                final long   size     = cursor.getLong  (cursor.getColumnIndex       (MediaStore.Audio.Media.SIZE));
                final long   duration = cursor.getLong  (cursor.getColumnIndex       (MediaStore.Audio.Media.DURATION));
                final String album    = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                final int    track    = cursor.getInt   (cursor.getColumnIndex       (MediaStore.Audio.Media.TRACK));

                action.syncRun(
                    activityContext,
                    id,
                    title,
                    artist,
                    path,
                    mime,
                    size,
                    duration,
                    album,
                    track
                );

            } while (cursor.moveToNext());

        } catch (Exception e) {
//            Timber.w("forEach fail: " + e.getLocalizedMessage());
            e.printStackTrace();

        } finally {
            if (null != cursor) { try { cursor.close(); } catch (Exception e) {} }
        }
    }
}
