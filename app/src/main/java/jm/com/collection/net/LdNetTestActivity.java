package jm.com.collection.net;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jm.com.collection.AppConstant;
import jm.com.collection.R;
import jm.com.collection.utils.EncryptUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class LdNetTestActivity extends AppCompatActivity {

    private static final String TAG ="LdNetTestActivity";
    @Bind(R.id.btn_get_department)
    Button btnGetDepartment;
    @Bind(R.id.tv_show_department)
    TextView tvShowDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ld_net_test);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_get_department)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_get_department:
                //getDepartmentOne();
               // getDepartmentTwo();
               // getDepartmentRxjava();
               // getAppLoginInfo();
                getTempToldForPaadm();
                break;
        }
    }



    /**
     * 单纯的使用Retrofit请求数据的方式
     */
    private void getDepartmentOne() {
        //创建请求的body
        String json="{\"request\":\"hjFgVB/JB112Mv0xdtreUQ==\"}";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetDepartmentsApi getDepartmentsApi = retrofit.create(GetDepartmentsApi.class);
        Call<ResponseBean> departmentList = getDepartmentsApi.getDepartmentList(requestBody);
        departmentList.enqueue(new Callback<ResponseBean>() {
            @Override
            public void onResponse(Call<ResponseBean> call, Response<ResponseBean> response) {
                String str = response.body().getResponse();
                String result= EncryptUtil.Decrypt(str, AppConstant.ENCRYPTKEY);
                tvShowDepartment.setText(result);
            }

            @Override
            public void onFailure(Call<ResponseBean> call, Throwable t) {
                tvShowDepartment.setText("加载失败:"+t.getMessage());
            }
        });

    }

    /**
     * 对Retrofit封装，添加HttpInterceptor拦截添加头信息
     */
    private void getDepartmentTwo(){
        Retrofit retrofit = HttpClient.getInstance();
        Map map=new HashMap();
        map.put("key","");
        RequestBody requestBody = HttpClient.getRequestBody(map);
        GetDepartmentsApi2 getDepartmentsApi2 = retrofit.create(GetDepartmentsApi2.class);
        getDepartmentsApi2.getDepartmentList(requestBody).enqueue(new Callback<ResponseBean>() {
            @Override
            public void onResponse(Call<ResponseBean> call, Response<ResponseBean> response) {
                String str = response.body().getResponse();
                String result=EncryptUtil.Decrypt(str, AppConstant.ENCRYPTKEY);
                tvShowDepartment.setText(result);
            }
            @Override
            public void onFailure(Call<ResponseBean> call, Throwable t) {
                tvShowDepartment.setText("加载失败:"+t.getMessage());
            }
        });

    }

    /**
     * Retrofit于Rxjava1.x.x版本结合使用获取数据
     */
    private  void getDepartmentRxjava(){
        Subscriber subscriber=new Subscriber<List<Map>>() {
            @Override
            public void onNext(List list) {
                tvShowDepartment.setText("数组大小："+list.size());
            }
            @Override
            public void onCompleted() {
                Log.i(TAG,"onComplete被调用了"+Thread.currentThread().getName());
            }
            @Override
            public void onError(Throwable t) {
             tvShowDepartment.setText(t.getMessage());
            }
        };

        Retrofit retrofit=HttpClient.getInstance();
        Map map=new HashMap();
        map.put("key","");
        GetDepartmentsApi3 getDepartmentsApi3 = retrofit.create(GetDepartmentsApi3.class);
        getDepartmentsApi3.getDepartmentList(HttpClient.getRequestBody(map))
                .map(FuncToTable.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private void getAppLoginInfo(){
        Subscriber subscriber=new Subscriber<List<Map>>() {
            @Override
            public void onNext(List list) {
                tvShowDepartment.setText("数组大小："+list.size());
            }
            @Override
            public void onCompleted() {
                Log.i(TAG,"onComplete被调用了"+Thread.currentThread().getName());
            }
            @Override
            public void onError(Throwable t) {
                tvShowDepartment.setText(t.getMessage());
            }
        };
        Retrofit retrofit=HttpClient.getInstance();
        Map map=new HashMap();
        map.put("AppCode", "bin");
        map.put("AppPwd", "wxm123");
        map.put("System", "Android");
        GetDepartmentsApi3 getDepartmentsApi3 = retrofit.create(GetDepartmentsApi3.class);
        getDepartmentsApi3.getAppLoginInfo(HttpClient.getRequestBody(map))
                .map(FuncToTable.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * Rxjava的map与flatMap配合使用，通过第一个接口获取paadm再通过第二个接口获取下一个接口信息的信息
     */
    private void getTempToldForPaadm() {
        Subscriber subscriber = new Subscriber<List<Map>>(){

            @Override
            public void onNext(List<Map> maps) {
             tvShowDepartment.setText(GsonUtil.parseJson(maps).toString());
            }
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }


        };
        Retrofit retrofit = HttpClient.getInstance();
        final Map map=new HashMap();
        map.put("IDCard","");
        map.put("DJH","1");
        map.put("MZFlag","2");//1,2
        final getServiceForPaadmApi serviceApi = retrofit.create(getServiceForPaadmApi.class);
        serviceApi.getPaadmList(HttpClient.getRequestBody(map))
                .map(FuncToOther.getInstance())
                .flatMap(new Func1<List<Map>, Observable<ResponseBean>>() {
                    @Override
                    public Observable<ResponseBean> call(List<Map> mapList) {
                        int size = mapList.size();
                        Map map1=new HashMap();
                        map1.put("PAADM",mapList.get(0).get("PAADM"));
                        map1.put("ToldType","临时医嘱");
                        return  serviceApi.getTempTold(HttpClient.getRequestBody(map1));
                    }
                })
                .map(FuncToResult.getInstance())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

}