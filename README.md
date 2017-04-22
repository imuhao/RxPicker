# RxPicker
The ImageSelect tool based on RxJava.

[中文文档](./README_CN.md)

## Feature

1. Combined with RxJava ,Supper observer pattern to get result
2. Supper single/multiple select image
3. Compatible with Android 7.0
4. Supper custom `RxPickerImageLoader`


## Preview

<image src="./image/1.png" width="200px"/> <image src="./image/2.png" width="200px"/> 

## Use Glide

1.Add gradle:

```
compile 'com.caimuhao:rxpicker:1.1.2'
```

2.Extends `RxPickerImageLoader` create custom  ImageLoader

```
public class GlideImageLoader implements RxPickerImageLoader {

  @Override public void display(ImageView imageView, String path, int width, int height) {
    Glide.with(imageView.getContext())
        .load(path)
        .error(R.drawable.ic_preview_image)
        .centerCrop()
        .override(width, height)
        .into(imageView);
  }
}
```

3.Initialize RxPicker

```
RxPicker.init(new GlideImageLoader());
```

4.Use

- Image Single

```
RxPicker.of().start(this).subscribe(new Consumer<List<ImageItem>>() {
        @Override public void accept(@NonNull List<ImageItem> imageItems) throws Exception {
          //Get the result
        }
      });
```

- Image multiple

```
RxPicker.of()
          .single(false)
          .camera(true)
          .limit(3)
          .start(this)
          .subscribe(new Consumer<List<ImageItem>>() {
            @Override public void accept(@NonNull List<ImageItem> imageItems) throws Exception {
              //Get the result
            }
          });
```