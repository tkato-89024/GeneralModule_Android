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

@SuppressWarnings("all")
public class ImageCursorer extends Cursorer implements IContentResolverCursorer {

    public interface IForEach {
        void syncRun(Context context, final int id, final String title, final String path, final String mime, final long size, final long width, final long height);
    }

    public static ImageCursorer create() {
        return new ImageCursorer(
            new ContentResolverQueryOrganizer(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
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

    public ImageCursorer() {
        super();
    }

    public ImageCursorer(@Nullable IQueryOrganizer organizer) {
        super(organizer);
    }

    public int getCountNewCursor() {
        return super.getCountNewCursor(new ContentResolverQueryOrganizer(
            getOrganizer().getUri(),
            new String[] { MediaStore.Images.Media._ID },
            getOrganizer().getSelection(),
            getOrganizer().getSelectionArgs(),
            getOrganizer().getOrderBy()
        ));
    }

    @Override
    public ImageCursorer query() {
        return (ImageCursorer) super.query();
    }

    @Override
    public ImageCursorer query(long length, long offset) {
        return (ImageCursorer) super.query(length, offset);
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
                final int    id     = cursor.getInt   (cursor.getColumnIndex       (MediaStore.Images.Media._ID));
                final String title  = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE));
                final String path   = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                final String mime   = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));
                final long   size   = cursor.getLong  (cursor.getColumnIndex       (MediaStore.Images.Media.SIZE));
                final long   width  = cursor.getLong  (cursor.getColumnIndex       (MediaStore.Images.Media.WIDTH));
                final long   height = cursor.getLong  (cursor.getColumnIndex       (MediaStore.Images.Media.HEIGHT));

                action.syncRun(
                    activityContext,
                    id,
                    title,
                    path,
                    mime,
                    size,
                    width,
                    height
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
