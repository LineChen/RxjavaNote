package com.linechen.rxjavanote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        testSingle();
//
//        testSchedulers();

//        testInterval();

//        testBuffer();

//        testFlatMap();

//        testGroupBy();

//        testMap();

//        testDebounce();

//        testDistinct();

//        testElementAt();

//        testFilter();

//        testFirst();

//        testSample();

//        testSkip();

        testSkipLast();

    }



    /**
     * 创建操作
     */
    public void testSingle(){
        Single<Integer> single = Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> e) throws Exception {
                e.onSuccess(1);
            }
        });

        single.subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer integer) {
                Toast.makeText(MainActivity.this, "int" + integer, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {

            }
        });

    }

    public void testSubject(){
        AsyncSubject<Integer> asyncSubject = AsyncSubject.create();
        asyncSubject.onNext(1);
        asyncSubject.onNext(1);
        asyncSubject.onNext(1);
        asyncSubject.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    public void testSchedulers(){
        //普通调度器
        Schedulers.newThread().createWorker().schedule(new Runnable() {
            @Override
            public void run() {

            }
        });

        //延时调度器
        Schedulers.computation().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                Log.e("==", "schedule");
            }
        }, 3000, TimeUnit.MILLISECONDS);

        //延时周期调度器
        Schedulers.computation().createWorker().schedulePeriodically(new Runnable() {
            @Override
            public void run() {
                Log.e("==", "schedulePeriodically");
            }
        }, 1000, 500, TimeUnit.MILLISECONDS);
    }

    public void testCreate(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.e("===", "create:" + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> e) throws Exception {
                e.onSuccess(0);
            }
        }).subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer integer) {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void testDefer(){
        Observable.defer(new Callable<ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> call() throws Exception {
                return null;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void testInterval(){
        Observable.interval(1000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.e("===", "interval:" + aLong);
            }
        });
    }

    /**
     * 变换操作
     */

    public void testBuffer(){
        //发送到最后98这一组就结束了，99这一项没有发送
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 100; i++) {
                    e.onNext(i);
                }
            }
        }).buffer(3, 4).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                Log.e("====", "buffer:" + Arrays.toString(integers.toArray()));
            }
        });
    }

    public void testFlatMap(){
        List<String> titles = new ArrayList<>();
        titles.add("000");
        titles.add("111");
        titles.add("222");
        Observable.fromIterable(titles)
                .flatMap(new Function<String, ObservableSource<List<Integer>>>() {
                    @Override
                    public ObservableSource<List<Integer>> apply(String s) throws Exception {
                        char[] chars = s.toCharArray();
                        List<Integer> ints = new ArrayList<>(chars.length);
                        for (Character c : chars) {
                            ints.add((int)c);
                        }
                        return Observable.just(ints);
                    }
                }).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> cha) throws Exception {
                Log.e("====", Arrays.toString(cha.toArray()));
            }
        });
    }

    public void testGroupBy(){
        List<String> titles = new ArrayList<>();
        titles.add("000");
        titles.add("111");
        titles.add("022");
        Observable.fromIterable(titles)
                .groupBy(new Function<String, Character>() {
                    @Override
                    public Character apply(String s) throws Exception {
                        return s.charAt(0);
                    }
                }).subscribe(new Consumer<GroupedObservable<Character, String>>() {
            @Override
            public void accept(final GroupedObservable<Character, String> stringStringGroupedObservable) throws Exception {
                stringStringGroupedObservable.subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("===", stringStringGroupedObservable.getKey() + ":" + s);
                    }
                });
            }
        });
    }

    public void testMap(){
        Observable.just("1211")
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) throws Exception {
                        return Integer.parseInt(s);
                    }
                }).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e("===", "map:" + integer);
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                Log.e("===", "map:complete" );
            }
        }).subscribe();

        Observable.just(1)
                .cast(String.class)
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                    }
                });
    }

    /**
     * 过滤操作
     */

    public void testDebounce(){
       Observable.create(new ObservableOnSubscribe<Integer>() {
           @Override
           public void subscribe(ObservableEmitter<Integer> e) throws Exception {
               for (int i = 0; i < 20; i++) {
                   Thread.sleep(1000);
                   e.onNext(i);
               }
           }
       })
               .observeOn(AndroidSchedulers.mainThread())
               .debounce(2000, TimeUnit.MILLISECONDS, Schedulers.newThread())
               .doOnNext(new Consumer<Integer>() {
                   @Override
                   public void accept(Integer integer) throws Exception {
                       Log.e("====", "debounce:" + integer);
                   }
               }).subscribe();
    }

    public void testDistinct(){
        Observable.just(1, 2, 1, 3, 2, 3, 4, 1).distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e("====", "disdinct:" + integer);
                    }
                });
    }

    public void testElementAt(){
        Observable.just(0, 1, 2, 3, 4, 5)
                .elementAt(6, 0)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e("===", "elementAt:" + integer);
                    }
                });
    }

    public void testFilter(){
        Observable.just(1, 2, 3, 4, 5, 6)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 3;
                    }
                })
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e("===", "filter:" + integer);
                    }
                })
                .subscribe();

        Observable.just(1, 2, 3, "1-1",  "2-2", true, 4)
                .ofType(String.class)
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String integer) throws Exception {
                        Log.e("===", "ofType:" + integer);
                    }
                }).subscribe();
    }

    public void testFirst(){
        Observable.just(1, 2 ,3)
                .first(9)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e("===", "first:" +integer);
                    }
                });

        Observable.empty().first(1)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.e("==", "empty firsr:" + (int)o);
                    }
                });
    }

    int i = 0;
    public void testSample(){
        //每两秒拉取一次数据
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final ObservableEmitter<Integer> e) throws Exception {
                final Scheduler.Worker worker = Schedulers.newThread().createWorker();
                worker.schedulePeriodically(new Runnable() {
                    @Override
                    public void run() {
                        e.onNext(i++);
                        if(i == 50){
                            worker.dispose();
                        }
                    }
                }, 1, 1,  TimeUnit.SECONDS);
            }
        }).sample(2, TimeUnit.SECONDS)
        .doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, "sample:" + integer);
            }
        }).subscribe();
    }

    public void testSkip(){
        Observable.just(1, 2, 3, 4, 5)
                .skip(3)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "skip:" + integer);
                    }
                }).subscribe();
    }

    public void testSkipLast(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final ObservableEmitter<Integer> e) throws Exception {
                final Scheduler.Worker worker = Schedulers.newThread().createWorker();
                worker.schedulePeriodically(new Runnable() {
                    @Override
                    public void run() {
                        e.onNext(i++);
                        if(i == 50){
                            worker.dispose();
                        }
                    }
                }, 1, 1,  TimeUnit.SECONDS);
            }
        }).skipLast(40).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, "skipLast:" + integer);
            }
        }).subscribe();
    }
}
