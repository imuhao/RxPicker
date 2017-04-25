## RxPicker

基于 RxJava 的 Android 图片选择器.

## 特征

1. 与 RxJava 结合,支持响应式得到选择图片结果
2. 兼容 Android 7.0
3. 自定义 `ImageLoader`


## 预览图

<image src="./image/1.png" width="200px"/> <image src="./image/2.png" width="200px"/> 


## 使用

1.添加 gradle 引用

```
compile 'com.caimuhao:rxpicker:1.1.4'
```

2.继承 `RxPickerImageLoader` 创建自定义的图片加载

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

3.初始化 `RxPicker`

```
RxPicker.init(new GlideImageLoader());
```

4.使用

- 图片单选
```
RxPicker.of().start(this).subscribe(new Consumer<List<ImageItem>>() {
        @Override public void accept(@NonNull List<ImageItem> imageItems) throws Exception {
          //得到结果
        }
      });
```

- 图片多选

```
RxPicker.of()
          .single(false)
          .camera(true)
          .limit(3,9)
          .start(this)
          .subscribe(new Consumer<List<ImageItem>>() {
            @Override public void accept(@NonNull List<ImageItem> imageItems) throws Exception {
              //得到结果
            }
          });
```