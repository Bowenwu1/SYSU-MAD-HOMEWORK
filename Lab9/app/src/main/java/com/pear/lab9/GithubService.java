package com.pear.lab9;

import java.util.List;
import rx.Observable;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by bowenwu on 2017/12/15.
 */

public class GithubService {
   private interface Api {
       @GET("/users/{name}")
       Observable<User> getUser(@Path("name") String username);

       @GET("/users/{name}/repos")
       Observable<List<Repository>> getRepository(@Path("name") String username);
   }

   final public static String baseURL = "https://api.github.com/";

   public GithubService() {}

   private Api mService = ServiceFactory.createRetrofit(baseURL).create(Api.class);

   public void subscribeUser(String username, Subscriber<User> userSubscriber) {
       mService.getUser(username)
               .subscribeOn(Schedulers.newThread())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(userSubscriber);
   }

   public void subscribeRepository(String username, Subscriber<List<Repository>> listSubscriber) {
       mService.getRepository(username)
               .subscribeOn(Schedulers.newThread())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(listSubscriber);
   }
}
