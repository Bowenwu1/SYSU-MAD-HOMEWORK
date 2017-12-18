package com.pear.lab9;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by bowenwu on 2017/12/15.
 */

public interface GithubService {
    @GET("/users/{user}/repos")
    Observable<List<Repository>> listRepos(@Path("user") String user);

    @GET("/users/{user}")
    Observable<User> getUser(@Path("user") String user);

}
