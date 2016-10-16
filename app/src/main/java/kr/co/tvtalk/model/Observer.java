package kr.co.tvtalk.model;

/**
 * Created by kwongiho on 2016. 10. 14..
 */

public interface Observer<T> {
    boolean register(T newClient);
    void notiyObserver(boolean isVisible);
}
