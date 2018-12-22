package bkdev.android.base_mvp.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.Lifecycle.State;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.app.FragmentManager;

import java.util.Stack;



public class SafeTransaction implements LifecycleObserver {
    private final Lifecycle mLifecycle;
    private final FragmentManager mFragmentManager;
    private final Stack<TransitionHandler> mFragmentTransitionsStack;

    public static SafeTransaction createInstance(Lifecycle lifecycle, FragmentManager fragmentManager) {
        return new SafeTransaction(lifecycle, fragmentManager);
    }

    private SafeTransaction(Lifecycle lifecycle, FragmentManager fragmentManager) {
        this.mLifecycle = lifecycle;
        this.mFragmentManager = fragmentManager;
        this.mFragmentTransitionsStack = new Stack();
    }

    private void onTransitionRegistered(TransitionHandler transition) {
        this.mFragmentTransitionsStack.add(transition);
        if (this.mLifecycle.getCurrentState().isAtLeast(State.RESUMED)) {
            this.doTransactions();
        }
    }

    @OnLifecycleEvent(Event.ON_RESUME)
    void onReadyToDoPendingTransactions() {
        this.doTransactions();
    }

    private synchronized void doTransactions() {
        while (this.mFragmentTransitionsStack.size() != 0) {
            TransitionHandler transition = this.mFragmentTransitionsStack.pop();
            transition.onTransitionAvailable(this.mFragmentManager);
        }
    }

    public void registerTransition(TransitionHandler transitionHandler) {
        this.onTransitionRegistered(transitionHandler);
    }

    @FunctionalInterface
    public interface TransitionHandler {
        void onTransitionAvailable(FragmentManager fragmentManager);
    }
}
