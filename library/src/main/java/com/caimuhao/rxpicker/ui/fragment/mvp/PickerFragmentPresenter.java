package com.caimuhao.rxpicker.ui.fragment.mvp;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Toast;
import com.caimuhao.rxpicker.bean.ImageFolder;
import com.caimuhao.rxpicker.bean.ImageItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Smile
 * @time 2017/4/19  上午10:36
 * @desc ${TODD}
 */
public class PickerFragmentPresenter extends PickerFragmentContract.Presenter {

  /**
   * Media attribute.
   */
  private static final String[] STORE_IMAGES = {
      MediaStore.Images.Media._ID, // image id.
      MediaStore.Images.Media.DATA, // image absolute path.
      MediaStore.Images.Media.DISPLAY_NAME, // image name.
      MediaStore.Images.Media.DATE_ADDED, // The time to be added to the library.
      MediaStore.Images.Media.BUCKET_ID, // folder id.
      MediaStore.Images.Media.BUCKET_DISPLAY_NAME // folder name.
  };

  @Override public void start() {

  }

  /**
   * Scan the list of pictures in the library.
   */
  private Observable<List<ImageFolder>> getPhotoAlbum(final Context context) {

    return Observable.unsafeCreate(new Observable.OnSubscribe<List<ImageFolder>>() {
      @Override public void call(Subscriber<? super List<ImageFolder>> subscriber) {

        Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(),
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);
        Map<String, ImageFolder> albumFolderMap = new HashMap<>();

        ImageFolder allImageImageFolder = new ImageFolder();
        allImageImageFolder.setChecked(true);
        allImageImageFolder.setName("全部相册");

        while (cursor.moveToNext()) {
          int imageId = cursor.getInt(0);
          String imagePath = cursor.getString(1);
          String imageName = cursor.getString(2);
          long addTime = cursor.getLong(3);

          int bucketId = cursor.getInt(4);
          String bucketName = cursor.getString(5);

          ImageItem ImageItem = new ImageItem(imageId, imagePath, imageName, addTime);
          allImageImageFolder.addPhoto(ImageItem);

          ImageFolder imageFolder = albumFolderMap.get(bucketName);
          if (imageFolder != null) {
            imageFolder.addPhoto(ImageItem);
          } else {
            imageFolder = new ImageFolder(bucketId, bucketName);
            imageFolder.addPhoto(ImageItem);

            albumFolderMap.put(bucketName, imageFolder);
          }
        }
        cursor.close();
        List<ImageFolder> imageFolders = new ArrayList<>();

        Collections.sort(allImageImageFolder.getImages());
        imageFolders.add(allImageImageFolder);

        for (Map.Entry<String, ImageFolder> folderEntry : albumFolderMap.entrySet()) {
          ImageFolder imageFolder = folderEntry.getValue();
          Collections.sort(imageFolder.getImages());
          imageFolders.add(imageFolder);
        }
        subscriber.onNext(imageFolders);
        subscriber.onCompleted();
      }
    });
  }

  @Override public void loadAllImage(final Context context) {
    getPhotoAlbum(context).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Action0() {
          @Override public void call() {
            view.showWaitDialog();
          }
        })
        .doOnTerminate(new Action0() {
          @Override public void call() {
            view.hideWaitDialog();
          }
        })
        .subscribe(new Action1<List<ImageFolder>>() {
          @Override public void call(List<ImageFolder> imageFolders) {
            view.showAllImage(imageFolders);
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            Toast.makeText(context, "获取图片失败!", Toast.LENGTH_SHORT).show();
          }
        });
  }
}
