package esqeee.xieqing.com.eeeeee.widget.viewPager;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import esqeee.xieqing.com.eeeeee.R;

public class LazyViewPager extends ViewPager {

	private static final float DEFAULT_OFFSET = 0.5f;

	private LazyPagerAdapter mLazyPagerAdapter;
	private float mInitLazyItemOffset = DEFAULT_OFFSET;

	public LazyViewPager(Context context) {
		super(context);
	}

	public LazyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LazyViewPager);
		setInitLazyItemOffset(a.getFloat(R.styleable.LazyViewPager_init_lazy_item_offset, DEFAULT_OFFSET));
		a.recycle();
	}
	boolean isScroll = true;
	public void setScroll(boolean scroll) {
		isScroll = scroll;
	}

	/**
	 * 是否拦截
	 * 拦截:会走到自己的onTouchEvent方法里面来
	 * 不拦截:事件传递给子孩子
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// return false;//可行,不拦截事件,
		// return true;//不行,孩子无法处理事件
		//return super.onInterceptTouchEvent(ev);//不行,会有细微移动
		if (isScroll){
			return super.onInterceptTouchEvent(ev);
		}else{
			return false;
		}
	}
	/**
	 * 是否消费事件
	 * 消费:事件就结束
	 * 不消费:往父控件传
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//return false;// 可行,不消费,传给父控件
		//return true;// 可行,消费,拦截事件
		//super.onTouchEvent(ev); //不行,
		//虽然onInterceptTouchEvent中拦截了,
		//但是如果viewpage里面子控件不是viewgroup,还是会调用这个方法.
		if (isScroll){
			return super.onTouchEvent(ev);
		}else {
			return true;// 可行,消费,拦截事件
		}
	}
    /**
     * change the initLazyItemOffset
     * @param initLazyItemOffset set mInitLazyItemOffset if {@code 0 < initLazyItemOffset <= 1}
     */
	public void setInitLazyItemOffset(float initLazyItemOffset) {
		if (initLazyItemOffset > 0 && initLazyItemOffset <= 1) {
		    mInitLazyItemOffset = initLazyItemOffset;
        }
	}

	@Override
	public void setAdapter(PagerAdapter adapter) {
		super.setAdapter(adapter);
        mLazyPagerAdapter = adapter != null && adapter instanceof LazyPagerAdapter ? (LazyPagerAdapter) adapter : null;
	}

	@Override
	protected void onPageScrolled(int position, float offset, int offsetPixels) {
		if (mLazyPagerAdapter != null) {
			if (getCurrentItem() == position) {
				int lazyPosition = position + 1;
				if (offset >= mInitLazyItemOffset && mLazyPagerAdapter.isLazyItem(lazyPosition)) {
                    mLazyPagerAdapter.startUpdate(this);
                    mLazyPagerAdapter.addLazyItem(this, lazyPosition);
                    mLazyPagerAdapter.finishUpdate(this);
				}
			} else if (getCurrentItem() > position) {
				int lazyPosition = position;
				if (1 - offset >= mInitLazyItemOffset && mLazyPagerAdapter.isLazyItem(lazyPosition)) {
                    mLazyPagerAdapter.startUpdate(this);
                    mLazyPagerAdapter.addLazyItem(this, lazyPosition);
                    mLazyPagerAdapter.finishUpdate(this);
				}
			}
		}
		super.onPageScrolled(position, offset, offsetPixels);
	}
}