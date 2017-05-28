package mixturedd.robotcontroler;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

public class ThreadManager {

	private String tag = "ThreadManager";

	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	// We want at least 2 threads and at most 4 threads in the core pool,
	// preferring to have 1 less than the CPU count to avoid saturating
	// the CPU with background work
	private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
	private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
	private static final int KEEP_ALIVE_SECONDS = 30;

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "ThreadManagerTask #" + mCount.getAndIncrement());
		}
	};

	private static final BlockingQueue<Runnable> sPoolWorkQueue =
			new LinkedBlockingQueue<>(128);

	private MyHandler myHandler;

	private UiHandler uiHandler;

//	private ThreadPoolExecutor threadPoolExecutor;

	public static final Executor THREAD_POOL_EXECUTOR;

	static {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
				CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
				sPoolWorkQueue, sThreadFactory);
		threadPoolExecutor.allowCoreThreadTimeOut(true);
		THREAD_POOL_EXECUTOR = threadPoolExecutor;
	}

	// private HandlerThread handlerThread;

	private static volatile ThreadManager threadManager = new ThreadManager();

	private ThreadManager() {
	}

	public static ThreadManager getInstance() {

		if (null == threadManager) {

			synchronized (ThreadManager.class) {

				if (null == threadManager) {
					threadManager = new ThreadManager();
				}
			}

		}

		return threadManager;
	}

	/*******
	 * 插入耗时较多的操作，这个方法需要线程池
	 * 
	 * @param tagH
	 *            标签名
	 ********/
	public void putLongTime(final ThreadManagerCallBack threadManagerCallBack, final String tagH) {
	}

	/*******
	 * 插入耗时较少的操作，可以更新UI，同时threadManagerCallBack执行优先于threadManagerUiCallBack
	 * 
	 * @param threadManagerCallBack
	 *            刷新异步接口
	 * @param threadManagerUiCallBack
	 *            刷新UI
	 * @param tagH
	 *            标签名
	 * @param delayMillis
	 *            延时时间 毫秒
	 ********/
	public void putShortTimeUiDelayed(ThreadManagerCallBack threadManagerCallBack,
			ThreadManagerUiCallBack threadManagerUiCallBack, String tagH, long delayMillis) {
	}

	/*******
	 * 插入耗时较少的操作，可以更新UI，同时threadManagerCallBack执行优先于threadManagerUiCallBack
	 * 
	 * @param threadManagerCallBack
	 *            刷新异步接口
	 * @param threadManagerUiCallBack
	 *            刷新UI
	 * @param tagH
	 *            标签名
	 ********/
	public void putShortTimeUi(ThreadManagerCallBack threadManagerCallBack,
			ThreadManagerUiCallBack threadManagerUiCallBack, String tagH) {
	}

	/*******
	 * 插入耗时较少的操作，可以更新UI，同时threadManagerCallBack执行优先于threadManagerUiCallBack
	 * 
	 * @param threadManagerCallBack
	 *            刷新异步接口
	 * @param threadManagerUiCallBack
	 *            刷新UI
	 * @param tagH
	 *            标签名
	 * @param uptimeMillis
	 *            特定时间
	 ********/
	public void putShortTimeUiAtTime(ThreadManagerCallBack threadManagerCallBack,
			ThreadManagerUiCallBack threadManagerUiCallBack, String tagH, long uptimeMillis) {
	}

	/*******
	 * 插入耗时较少的操作，不可以更新UI
	 * 
	 * @param threadManagerCallBack
	 *            刷新异步接口
	 * @param tagH
	 *            标签名
	 * @param delayMillis
	 *            延时时间 毫秒
	 ********/
	public void putShortTimeDelayed(ThreadManagerCallBack threadManagerCallBack, String tagH, long delayMillis) {
	}

	/*******
	 * 插入耗时较少的操作，不可以更新UI
	 * 
	 * @param threadManagerCallBack
	 *            刷新异步接口
	 * @param tagH
	 *            标签名
	 ********/
	public void putShortTime(ThreadManagerCallBack threadManagerCallBack, String tagH) {
	}

	/*******
	 * 插入耗时较少的操作，不可以更新UI
	 * 
	 * @param threadManagerCallBack
	 *            刷新异步接口
	 * @param tagH
	 *            标签名
	 * @param uptimeMillis
	 *            特定时间
	 ********/
	public void putShortTimeAtTime(ThreadManagerCallBack threadManagerCallBack, String tagH, long uptimeMillis) {
	}

	/*********** 异步操作 ***************/
	private static class MyHandler extends Handler {

		public MyHandler() {
		}

		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
		}

	}

	/*********** UI操作 ***********/
	private static class UiHandler extends Handler {
		public UiHandler() {
		}

		@Override
		public void dispatchMessage(Message msg) {
		}
	}

	private class ThreadContent implements Parcelable {
		String tagH;
		int threadType;
		public static final int INCLUDE_UI = 1;
		public static final int INCLUDE_NO_UI = 0;
		ThreadManagerCallBack threadManagerCallBack;
		ThreadManagerUiCallBack threadManagerUiCallBack;

		private ThreadContent() {
		}

        private ThreadContent(Parcel in) {
            this.tagH = in.readString();
            this.threadType = in.readInt();
            this.threadManagerCallBack = (ThreadManagerCallBack) in.readSerializable();
            this.threadManagerUiCallBack = (ThreadManagerUiCallBack) in.readSerializable();
        }

		public int getThreadType() {
			return this.threadType;
		}

		public void setThreadType(int threadType) {
			this.threadType = threadType;
		}

		public ThreadManagerCallBack getThreadManagerCallBack() {
			return this.threadManagerCallBack;
		}

		public void setThreadManagerCallBack(ThreadManagerCallBack threadManagerCallBack) {
			this.threadManagerCallBack = threadManagerCallBack;
		}

		public ThreadManagerUiCallBack getThreadManagerUiCallBack() {
			return this.threadManagerUiCallBack;
		}

		public void setThreadManagerUiCallBack(ThreadManagerUiCallBack threadManagerUiCallBack) {
			this.threadManagerUiCallBack = threadManagerUiCallBack;
		}

		public String getTagH() {
			return this.tagH;
		}

		public void setTagH(String tagH) {
			this.tagH = tagH;
		}

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.tagH);
            dest.writeInt(this.threadType);
            dest.writeParcelable(this.threadManagerCallBack, flags);
            dest.writeParcelable(this.threadManagerUiCallBack, flags);
        }

        public final Creator<ThreadContent> CREATOR = new Creator<ThreadContent>() {
            public ThreadContent createFromParcel(Parcel source) {
                return new ThreadContent(source);
            }

            public ThreadContent[] newArray(int size) {
                return new ThreadContent[size];
            }
        };
    }

	public Executor getThreadPoolExecutor() {
		return THREAD_POOL_EXECUTOR;
	}

	public interface ThreadManagerCallBack extends Parcelable {
		void threadCallBack(ThreadState paramThreadState);
	}

	public interface ThreadManagerUiCallBack extends Parcelable {
		void threadUiCallBack(ThreadState paramThreadState);
	}

	public class ThreadState implements Parcelable {
		int FLAG;

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(this.FLAG);
		}

		public ThreadState() {
		}

		protected ThreadState(Parcel in) {
			this.FLAG = in.readInt();
		}

		public final Creator<ThreadState> CREATOR = new Creator<ThreadState>() {
			public ThreadState createFromParcel(Parcel source) {
				return new ThreadState(source);
			}

			public ThreadState[] newArray(int size) {
				return new ThreadState[size];
			}
		};
	}

}
