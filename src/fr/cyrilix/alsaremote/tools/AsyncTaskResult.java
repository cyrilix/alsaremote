package fr.cyrilix.alsaremote.tools;

/**
 * Generic class for result of an asynchrone task
 * 
 * @author Cyrille Nofficial
 * @param <T> result type
 * 
 */
public class AsyncTaskResult<T> {
    private T result = null;
    private Exception error = null;

    /**
     * Constructor
     * 
     * @param result task result
     */
    public AsyncTaskResult(T result) {
        super();
        this.result = result;
    }

    /**
     * Constructor
     * 
     * @param error task error
     */
    public AsyncTaskResult(Exception error) {
        super();
        this.error = error;
    }

    /**
     * Return result
     * 
     * @return result of the task
     */
    public T getResult() {
        return result;
    }

    /**
     * Return exception error, <code>null</code> if AsyncTask finished without
     * error
     * 
     * @return task error
     */
    public Exception getError() {
        return error;
    }

    /**
     * Return <code>true</code> if task finished with error
     * 
     * @return <code>true</code> if task finished with error
     */
    public boolean hasError() {
        return null != error;
    }
}