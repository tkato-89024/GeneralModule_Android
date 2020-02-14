/*
 GeneralModule_Android Cipherer

 Copyright (c) 2019 tkato

 This software is released under the MIT License.
 http://opensource.org/licenses/mit-license.php
 */
package jp.co.model.tkato.general_module.cursor;

import android.content.Context;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import jp.co.model.tkato.general_module.query.ContentResolverQueryOrganizer;
import jp.co.model.tkato.general_module.query.IQueryOrganizer;

public class AudioGenresCursorer extends Cursorer implements IContentResolverCursorer {

    public interface IForEach {
        void syncRun(Context context, final String genre);
    }

    public static AudioGenresCursorer create(final int audioId) {
        return new AudioGenresCursorer(
            new ContentResolverQueryOrganizer(
                MediaStore.Audio.Genres.getContentUriForAudioId("external", audioId),
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

    public AudioGenresCursorer() {
        super();
    }

    public AudioGenresCursorer(@Nullable IQueryOrganizer organizer) {
        super(organizer);
    }

    public int getCountNewCursor() {
        return super.getCountNewCursor(new ContentResolverQueryOrganizer(
            getOrganizer().getUri(),
            new String[] { MediaStore.Audio.Genres._ID },
            getOrganizer().getSelection(),
            getOrganizer().getSelectionArgs(),
            getOrganizer().getOrderBy()
        ));
    }

    @Override
    public AudioGenresCursorer query() {
        return (AudioGenresCursorer) super.query();
    }

    @Override
    public AudioGenresCursorer query(long length, long offset) {
        return (AudioGenresCursorer) super.query(length, offset);
    }

    public void forEach(@Nullable final IForEach action) {
        forEach(null, action);
    }

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
                final String genre = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.NAME));

                action.syncRun(
                    activityContext,
                     genre
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
