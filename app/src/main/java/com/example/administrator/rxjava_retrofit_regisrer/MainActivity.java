package com.example.administrator.rxjava_retrofit_regisrer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private final static String TAG="RxJava";
    private String Head_url="http://fy.iciba.com/";

    Observable<Translation_1> observable1;
    // 定义Observable接口类型的网络请求对象
    Observable<Translation_2> observable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Observable.create()
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Head_url)
                .addConverterFactory(GsonConverterFactory.create())//支持GSON解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//支持RxJava
                .build();
        //创建网络请求接口实例
        final GetRequest_Interface request=retrofit.create(GetRequest_Interface.class);
        //采用Observable<...>对2个网络请求封装
        observable1=request.getCall();
        observable2=request.getCall_2();

        observable1.subscribeOn(Schedulers.io())
                //处理网络1
                    .doOnNext(new Consumer<Translation_1>() {
                        @Override
                        public void accept(Translation_1 result) throws Exception {
                            Log.d(TAG,"第一次网络请求成功");
//                            Log.e(TAG,result.getContent().getOut());
                            result.getContent().showOut();
                        }
                    }).observeOn(Schedulers.io())
                .flatMap(new Function<Translation_1, ObservableSource<Translation_2>>() {
                    //作变换，即作嵌套网络请求
                    @Override
                    public ObservableSource<Translation_2> apply(Translation_1 result) throws Exception {
                        // 将网络请求1转换成网络请求2，即发送网络请求2
                        return observable2;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Translation_2>() {
                    @Override
                    public void accept(Translation_2 result2) throws Exception {
                        Log.d(TAG,"第二次网络请求成功\n");
//                        Log.e(TAG,result2.getContent().getOut());
                        result2.getContent().showOut();
                    }
                },new Consumer<Throwable>(){

            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("登录失败");
            }
        });
    }
}
