package com.line.sss;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by chenliu on 2020/6/15.
 */
public class Te {

    public static void main(String[] args) {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("ErrorHandler : " + throwable.getMessage());
            }
        });

        new Rx().gt()
//                .flatMap(new Function<Boolean, ObservableSource<Boolean>>() {
//                    @Override
//                    public ObservableSource<Boolean> apply(Boolean aBoolean) throws Exception {
//                        return Observable.just(aBoolean);
//                    }
//                })
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean aBoolean) throws Exception {
//
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        System.out.println("accept:" + throwable.getMessage());
//                    }
//                });
                .flatMap(new Function<Boolean, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Boolean aBoolean) throws Exception {
                        return Observable.just(String.valueOf(aBoolean));
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String aBoolean) throws Exception {

                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("doOnError:" + throwable.getMessage());
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        System.out.println("doOnSubscribe:");
                    }
                })
                .subscribe();
    }


    public static class Rx {
        public Observable<Boolean> gt() {
            return new MyOb()
                    .flatMap(new Function<Boolean, ObservableSource<Boolean>>() {
                        @Override
                        public ObservableSource<Boolean> apply(Boolean aBoolean) throws Exception {
                            return Observable.just(aBoolean);
                        }
                    });


//            return Observable.create(new ObservableOnSubscribe<Boolean>() {
//                @Override
//                public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
//                    e.onError(new Throwable("just error."));
//                }
//            });
        }


        public class MyOb extends Observable<Boolean> implements Disposable {

            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
                observer.onSubscribe(this);
                observer.onError(new Throwable("just error!!!"));
            }

            @Override
            public void dispose() {

            }

            @Override
            public boolean isDisposed() {
                return false;
            }
        }


    }

}
