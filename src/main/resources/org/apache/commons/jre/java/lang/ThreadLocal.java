package java.lang;

import java.util.WeakHashMap;

public class ThreadLocal<T> {
	
    private WeakHashMap<Object, Object> map;

    /**
     * Creates a thread local variable.
     */
    public ThreadLocal() {
    	this.map = new WeakHashMap<Object, Object>();
    }

    @SuppressWarnings("unchecked")
	public T get() {
    	return (T) map;
    }

    /**
     * Sets the current thread's copy of this thread-local variable
     * to the specified value.  Most subclasses will have no need to
     * override this method, relying solely on the {@link #initialValue}
     * method to set the values of thread-locals.
     *
     * @param value the value to be stored in the current thread's copy of
     *        this thread-local.
     */
    @SuppressWarnings("unchecked")
	public void set(T value) {
    	this.map = (WeakHashMap<Object, Object>) value;
    }

     public void remove() {
    	 // nothing to do.
     }
}
