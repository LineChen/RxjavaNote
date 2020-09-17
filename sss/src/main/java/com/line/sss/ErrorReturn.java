package com.line.sss;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by chenliu on 2020/9/17.
 */
public class ErrorReturn {

    public static void main(String[] p) {
        ErrorReturn errorReturn = new ErrorReturn();

        Observable<String> getNameApi = errorReturn.getName();
        Observable<List<String>> getHobbiesApi = errorReturn.getHobbies();
        Observable<String> nameApiWithErrorReturn = getNameApi.onErrorReturn(new Function<Throwable, String>() {
            @Override
            public String apply(Throwable throwable) throws Exception {
                return "nameApiWithErrorReturn";
            }
        });
        Observable<String> errorName = getNameApi.onErrorReturnItem("ErrorName");

        Disposable subscribe = Observable.zip(nameApiWithErrorReturn, getHobbiesApi, new BiFunction<String, List<String>, Boolean>() {
            @Override
            public Boolean apply(String s, List<String> strings) throws Exception {
                System.out.println("name is :" + s);
                System.out.println("hobbies are :" + Arrays.toString(strings.toArray()));
                return true;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                System.out.println("zip result: " + aBoolean);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.err.println("zip error: " + throwable.getMessage());
            }
        });

    }


    Observable<String> getName() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                e.onNext("Jack");
//                e.onComplete();
                e.onError(new Throwable("error."));
            }
        });
    }

    Observable<List<String>> getHobbies() {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                List<String> hobbies = Arrays.asList("篮球", "羽毛球", "足球");
                e.onNext(hobbies);
                e.onComplete();
            }
        });
    }
}
