package com.zhixin.customui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.IntEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zhixin.R;
import com.zhixin.domain.MapData;
import com.zhixin.settings.CrossSystemMap;
import com.zhixin.settings.MyApplication;
import com.zhixin.settings.PhoneHelper;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.ShareUtil;

public class CardItemView extends LinearLayout {

	private LinearLayout titleLayout;
	private ImageView cardItemLeft;
	private TextView cardItemTitle;
	private ImageView cardItemRight;
	private ImageView cardItemImg;
	private WebView cardItemWebView;
	private RelativeLayout cardItemEpq;
	private TextView cardItemTips;
	private LinearLayout nsLayout;
	private TextView cardItemName;
	private TextView cardItemScore;
	private TextView cardItemContent;
	private Button cardItemShare;
	private int endHeight;

	private String contentStr;

	private boolean isExpand = false;
	private MapData mapData;
	private OnCardChangeListener onCardChangeListener;
	// private boolean isRunning;
	private AnimatorListener animatorListener;

	private List<Integer> epqList;
	private ImageView epqListItem;
	private int epqLevel = 0;
	private int epqResource = 0;

	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	private int windowWidth = PhoneHelper.getPhoneWIDTH();
	private int windowHeight = PhoneHelper.getPhoneHeight();
	private int myWidth = (int) (windowWidth * 0.1);
	private int myHeight = (int) (windowHeight * 0.1);
	private int imgWidth = (int) (windowWidth * 0.7);
	private int imgHeight = (int) (windowWidth * 0.5);
	private int epqHeight = (int) (windowWidth * 0.8);

	public CardItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(VERTICAL);
	}

	public CardItemView(Context context) {
		this(context, null);
	}

	public MapData getMapData() {
		return mapData;
	}

	public void setMapData(MapData mapData, int color) {
		this.mapData = mapData;
		initView(color);
	}

	public AnimatorListener getAnimatorListener() {
		return animatorListener;
	}

	public void setAnimatorListener(AnimatorListener animatorListener) {
		this.animatorListener = animatorListener;
	}

	private void initView(int color) {
		Log.i("init", windowWidth + ";" + windowHeight);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.interest_list_default_image)
				.showImageForEmptyUri(R.drawable.interest_list_default_image)
				.showImageOnFail(R.drawable.interest_list_default_image)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
		if (mapData == null) {
			return;
		}

		LinearLayout.LayoutParams imgleftLp = (LinearLayout.LayoutParams) cardItemLeft
				.getLayoutParams();
		imgleftLp.width = (int) (myWidth * 0.2);
		imgleftLp.height = (int) (myHeight * 0.75);
		cardItemLeft.setLayoutParams(imgleftLp);

		LinearLayout.LayoutParams titleLp = (LinearLayout.LayoutParams) cardItemTitle
				.getLayoutParams();
		titleLp.topMargin = (int) (myHeight * 0.15);
		cardItemTitle.setLayoutParams(titleLp);

		LinearLayout.LayoutParams arrowLp = (LinearLayout.LayoutParams) cardItemRight
				.getLayoutParams();
		arrowLp.rightMargin = (int) (myWidth * 0.5);
		arrowLp.topMargin = (int) (myHeight * 0.3);
		cardItemRight.setLayoutParams(arrowLp);

		LinearLayout.LayoutParams imgLp = (LinearLayout.LayoutParams) cardItemImg
				.getLayoutParams();
		imgLp.width = imgWidth;
		imgLp.height = imgHeight;
		cardItemImg.setLayoutParams(imgLp);

		LinearLayout.LayoutParams webLp = (LinearLayout.LayoutParams) cardItemWebView
				.getLayoutParams();
		webLp.height = imgHeight;
		cardItemWebView.setLayoutParams(webLp);

		LinearLayout.LayoutParams epqLp = (LinearLayout.LayoutParams) cardItemEpq
				.getLayoutParams();
		epqLp.height = epqHeight;
		cardItemEpq.setLayoutParams(epqLp);

		cardItemLeft.setBackgroundColor(color);
		cardItemRight.setBackgroundColor(color);
		cardItemRight.setImageResource(R.drawable.card_arrow_origin);

		if (mapData.getMapDataTitle() != null) {
			cardItemTitle.setText(mapData.getMapDataTitle());
			Log.i("mapdata", "carditemview...tilte" + mapData.getMapDataTitle());
		}

		if (mapData.isMapDataIsLock()) {
			cardItemImg.setVisibility(View.VISIBLE);
			cardItemWebView.setVisibility(View.GONE);
			cardItemEpq.setVisibility(View.GONE);
			cardItemTips.setVisibility(View.VISIBLE);
			nsLayout.setVisibility(View.GONE);
			cardItemContent.setVisibility(View.GONE);

			cardItemImg.setImageResource(R.drawable.ic_launcher);
			if (mapData.getMapDataTips() != null) {
				cardItemTips.setText(mapData.getMapDataTips());
			}
		} else {
			cardItemTips.setVisibility(View.GONE);
			nsLayout.setVisibility(View.VISIBLE);
			cardItemContent.setVisibility(View.VISIBLE);
			if (mapData.getMapDataPicPath() != null) {
				cardItemImg.setVisibility(View.VISIBLE);
				Log.i("mapdata",
						"carditemview...picpath" + mapData.getMapDataPicPath());
				String imgUrl = SettingValues.URL_PREFIX
						+ mapData.getMapDataPicPath();
				imageLoader.displayImage(imgUrl, cardItemImg, options,
						animateFirstListener);
			} else {
				cardItemImg.setVisibility(View.GONE);
			}

			if (mapData.getMapDataGraphicsType() != null) {
				if (mapData.getMapDataGraphicsType().equals("5")) {
					if (mapData.getMapDataPoint() != null) {
						cardItemWebView.setVisibility(View.GONE);
						cardItemEpq.setVisibility(View.VISIBLE);
						swtichCross(mapData.getMapDataPoint(),
								mapData.getMapDataLevel());
					} else {
						cardItemEpq.setVisibility(View.GONE);
					}

				} else {
					cardItemEpq.setVisibility(View.GONE);
					if (mapData.getMapDataChat() != null) {
						cardItemWebView.setVisibility(View.VISIBLE);
						setWebView();
					} else {
						cardItemWebView.setVisibility(View.GONE);
					}
				}
			} else {
				cardItemEpq.setVisibility(View.GONE);
				cardItemWebView.setVisibility(View.GONE);
			}

			if (mapData.getMapDataName() != null) {
				cardItemName.setText(mapData.getMapDataName());
			} else {
				cardItemName.setText("暂无名称");
			}
			if (mapData.getMapDataResultScore() != null) {
				cardItemScore.setText(mapData.getMapDataResultScore());
			} else {
				cardItemScore.setText("暂无得分");
			}
			if (mapData.getMapDataContent() != null) {
				// contentStr = mapData.getMapDataContent();
				Log.i("mapdata",
						"carditemview...content改前"
								+ mapData.getMapDataContent());
				contentStr = (mapData.getMapDataContent()).replaceAll("\\\\n",
						"\n");
				Log.i("mapdata", "carditemview...content改后" + contentStr);
				cardItemContent.setText(contentStr);
			} else {
				cardItemContent.setText("暂无内容");
			}
		}

		cardItemShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShareUtil.showShare(
						MyApplication.getAppContext().getString(
								R.string.share_title_sharesoft),
						MyApplication.getAppContext().getString(
								R.string.share_content_sharesoft));

			}
		});

		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						// 现在布局全部完成，可以获取到任何View组件的宽度、高度、左边、右边等信息
						endHeight = getMeasuredHeight();
						Log.i("open", mapData.getMapDataTitle()
								+ "inner getMeasuredHeight:..." + endHeight);
						setIntialHeight();

					}
				});

		requestLayout();
		// ViewGroup.LayoutParams lp = new
		// ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 180);
		// lp.height=180;
		// setLayoutParams(lp);

	}

	public void setIntialHeight() {
		android.view.ViewGroup.LayoutParams lp = getLayoutParams();
		lp.height = titleLayout.getHeight() + getPaddingBottom()
				+ getPaddingTop();
		setLayoutParams(lp);
	}

	//
	// private void setEndHeight(int h) {
	// this.endHeight = h;
	// }
	//
	// private int getEndHeight() {
	// return endHeight;
	// }

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		titleLayout = (LinearLayout) findViewById(R.id.layout_card_item_title);
		cardItemLeft = (ImageView) findViewById(R.id.layout_card_item_title_color_block);
		cardItemTitle = (TextView) findViewById(R.id.layout_card_item_title_content);
		cardItemRight = (ImageView) findViewById(R.id.layout_card_item_title_arrow);
		cardItemImg = (ImageView) findViewById(R.id.layout_card_item_lock_img);
		cardItemWebView = (WebView) findViewById(R.id.layout_card_item_webview);
		cardItemEpq = (RelativeLayout) findViewById(R.id.layout_card_item_epq_picture);
		cardItemTips = (TextView) findViewById(R.id.layout_card_item_lock_tips);
		nsLayout = (LinearLayout) findViewById(R.id.layout_card_item_ns);
		cardItemName = (TextView) findViewById(R.id.layout_card_item_name);
		cardItemScore = (TextView) findViewById(R.id.layout_card_item_score);
		cardItemContent = (TextView) findViewById(R.id.layout_card_item_content);
		cardItemShare = (Button) findViewById(R.id.btn_card_item_share);
		addListener();
	}

	/**
	 * 标题点击监听
	 */
	private void addListener() {
		titleLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 已经展开，关闭
				if (isExpand) {
					closeCard(true);
				} else {// 关闭状态，打开
					openCard(true);
				}
				performCardChange(isExpand);
			}
		});
	}

	/**
	 * 关闭卡片
	 * 
	 * @param isAnimate
	 */
	public void closeCard(boolean isAnimate) {
		// 改变箭头
		if (isExpand) {
			isExpand = false;
			rotateArrow(cardItemRight, true, 50);
			// int height = getHeight();
			Log.i("close", "before_close..." + endHeight);
			if (isAnimate) {
				int afterHeight = titleLayout.getHeight() + getPaddingBottom()
						+ getPaddingTop();
				Log.i("close", afterHeight + "");
				performAnimate(this, endHeight, afterHeight, 50);
			}
		}

	}

	/**
	 * 打开卡片
	 * 
	 * @param isAnimate
	 */
	// @SuppressLint("NewApi")
	public void openCard(final boolean isAnimate) {
		isExpand = true;
		// 改变箭头
		rotateArrow(cardItemRight, false, 50);

		int height = getHeight();

		Log.i("open", "height:" + height + ";endHeight:" + endHeight);
		performAnimate(this, height, endHeight, 50);

	}

	// 旋转箭头
	private void rotateArrow(ImageView imageView, boolean isOpen, int duration) {

		RotateAnimation operatingAnim;

		if (!isOpen) {
			operatingAnim = new RotateAnimation(0, 179,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		} else {
			operatingAnim = new RotateAnimation(180, 359,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		}
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		operatingAnim.setDuration(duration);
		operatingAnim.setFillAfter(true);
		imageView.startAnimation(operatingAnim);
	}

	private void performCardChange(boolean isExpand) {
		if (onCardChangeListener != null) {
			onCardChangeListener.onChanged(isExpand);
		}
	}

	// view 改变height动画
	private void performAnimate(final View target, final int start,
			final int end, Integer duration) {

		// isRunning = true;
		ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);

		valueAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
				if (animatorListener != null) {
					animatorListener.onAnimationStart(arg0);
				}
			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
				if (animatorListener != null) {
					animatorListener.onAnimationRepeat(arg0);
				}
			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				if (animatorListener != null) {
					animatorListener.onAnimationEnd(arg0);
				}
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
				if (animatorListener != null) {
					animatorListener.onAnimationCancel(arg0);
				}
			}
		});

		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			// 持有一个IntEvaluator对象，方便下面估值的时候使用
			private IntEvaluator mEvaluator = new IntEvaluator();

			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				// 获得当前动画的进度值，整型，1-100之间
				int currentValue = (Integer) animator.getAnimatedValue();
				// 计算当前进度占整个动画过程的比例，浮点型，0-1之间
				float fraction = (float) currentValue / 100000f;

				// 这里我偷懒了，不过有现成的干吗不用呢
				// 直接调用整型估值器通过比例计算出宽度，然后再设给Button
				ViewGroup.LayoutParams lp = target.getLayoutParams();
				lp.height = currentValue;
				target.setLayoutParams(lp);

				// mEvaluator.evaluate(fraction,
				// start, end);
				if (target.getLayoutParams().height == end) {
					target.setEnabled(true);
				}
			}

		});
		valueAnimator.setDuration(duration).start();
	}

	public OnCardChangeListener getOnCardChangeListener() {
		return onCardChangeListener;
	}

	public void setOnCardChangeListener(
			OnCardChangeListener onCardChangeListener) {
		this.onCardChangeListener = onCardChangeListener;
	}

	public interface OnCardChangeListener {
		public void onChanged(boolean isExpand);
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	// EPQ测试图
	private void swtichCross(JSONObject json, int level) {
		double x = 0;
		double y = 0;
		double rad = 0;
		boolean isTwo = false;
		Integer count = cardItemEpq.getChildCount();
		if (count > 2) {
			cardItemEpq.removeViewsInLayout(2, count - 2);
		}

		try {
			epqLevel = level;
			JSONObject point = new JSONObject();

			point = json;
			Log.i("epq", point + "");
			x = point.getDouble("E") - 50;
			y = point.getDouble("N") - 50;
			rad = Math.atan2(y, x);
			// epqPosition = (y / x);
			Log.i("epq", rad + "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String color = "r";
		int p = 0;
		int pb = 0;
		int ps = 0;
		if (epqLevel > 0) {

			if (rad == 0) {
				pb = 1;
				ps = 32;
				isTwo = true;
			} else {
				for (int i = 1; i < 33; i++) {
					double degree = 11.25 * ((2 * Math.PI) / 360);

					if (i <= 16) {
						if (rad < degree * (i) && rad > 0) {
							p = i;
							isTwo = false;
							break;
						} else if (rad == degree * (i)) {
							pb = i + 1;
							ps = i;
							isTwo = true;
						}
					} else {
						if (rad < degree * (i - 32)) {
							p = i;
							isTwo = false;
							break;
						} else if (rad == degree * (i - 32)) {
							ps = i;
							pb = i + 1;
							isTwo = true;
						}
					}

				}
			}
			Log.i("epq", "p" + p + "...pb" + pb + "...ps" + ps + "");
			epqList = new ArrayList<Integer>();
			switch (epqLevel) {
			case 1:
				for (int i = 0; i < 3; i++) {
					if (isTwo) {
						if (i == 0) {
							epqResource = CrossSystemMap.getErrMessage(color,
									pb);
							epqList.add(epqResource);
							epqResource = CrossSystemMap.getErrMessage(color,
									ps);
							epqList.add(epqResource);
						} else {
							color = "y";
							epqResource = CrossSystemMap.getErrMessage(color,
									(ps - i));
							epqList.add(epqResource);
							epqResource = CrossSystemMap.getErrMessage(color,
									(pb + i));
							epqList.add(epqResource);
						}
					} else {
						if (i == 0) {
							epqResource = CrossSystemMap
									.getErrMessage(color, p);
							epqList.add(epqResource);
						} else {
							color = "y";
							epqResource = CrossSystemMap.getErrMessage(color,
									(p - i));
							epqList.add(epqResource);
							epqResource = CrossSystemMap.getErrMessage(color,
									(p + i));
							epqList.add(epqResource);
						}

					}
				}
				break;
			case 2:
				for (int i = 0; i < 2; i++) {
					if (isTwo) {
						if (i == 0) {
							epqResource = CrossSystemMap.getErrMessage(color,
									pb);
							epqList.add(epqResource);
							epqResource = CrossSystemMap.getErrMessage(color,
									ps);
							epqList.add(epqResource);
						} else {
							color = "y";
							epqResource = CrossSystemMap.getErrMessage(color,
									(ps - i));
							epqList.add(epqResource);
							epqResource = CrossSystemMap.getErrMessage(color,
									(pb + i));
							epqList.add(epqResource);
						}
					} else {
						if (i == 0) {
							epqResource = CrossSystemMap
									.getErrMessage(color, p);
							Log.i("epq", "resource:" + epqResource
									+ "...color:" + color + "...p:" + p);
							epqList.add(epqResource);
						} else {
							color = "y";
							epqResource = CrossSystemMap.getErrMessage(color,
									(p - i));
							epqList.add(epqResource);
							epqResource = CrossSystemMap.getErrMessage(color,
									(p + i));
							epqList.add(epqResource);
						}

					}
				}
				break;
			case 3:
				if (isTwo) {
					epqResource = CrossSystemMap.getErrMessage(color, pb);
					epqList.add(epqResource);
					epqResource = CrossSystemMap.getErrMessage(color, ps);
					epqList.add(epqResource);
				} else {
					epqResource = CrossSystemMap.getErrMessage(color, p);
					epqList.add(epqResource);
				}
				break;
			default:
				break;
			}
		}
		setImg(epqList);
	}

	// EPQ测试图结果图片
	private void setImg(List<Integer> epqList) {
		for (int i = 0; i < epqList.size(); i++) {
			Log.i("epq", "resource" + "......" + epqList.size());
			epqListItem = new ImageView(getContext());
			Bitmap bm = BitmapFactory.decodeResource(MyApplication
					.getAppContext().getResources(), epqList.get(i));
			epqListItem.setImageBitmap(bm);
			Log.i("epq", "Bitmap" + "......" + bm);
			epqListItem.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			epqListItem.setScaleType(ImageView.ScaleType.FIT_CENTER);
			cardItemEpq.addView(epqListItem);
		}
	}

	// 加载WebView
	private void setWebView() {
		WebSettings settings = cardItemWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		cardItemWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(getContext(), "没网" + errorCode,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url); // 在当前的webview中跳转到新的url

				return true;
			}
		});
		// cardWebView.loadUrl("http://10.0.0.88/hs_android.html");
		cardItemWebView.loadUrl("http://10.0.0.88/hc.html"
				+ "?height=300&timestamp=" + new Date().getTime());

		cardItemWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				try {
					JSONObject mapParamsJson = new JSONObject();

					mapParamsJson.put("title", mapData.getMapDataTitle());
					mapParamsJson.put("mapMax", mapData.getMapDataMax());
					mapParamsJson.put("chartType",
							mapData.getMapDataGraphicsType());
					mapParamsJson.put("chart", mapData.getMapDataChat());

					String mapParams = mapParamsJson.toString();

					cardItemWebView.loadUrl("javascript:switchChart('"
							+ mapParams + "')");
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

		cardItemWebView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
				}
				return false;
			}
		});
	}

}
