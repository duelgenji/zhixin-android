package com.zhixin.activity;

import com.zhixin.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentStatePagerSupport extends Fragment implements
		View.OnClickListener {
	static final int NUM_ITEMS = 2;

	private static Activity mainActivity;

	private MyAdapter mAdapter;

	private ViewPager mPager;

	private static TextView txtPageTitle;

	private static WebView webView;

	private Intent intent;

	private RelativeLayout layoutPicture;

	private static ImageButton btnSwitchChart;

	private int currentType = 0;

	private static FragmentStatePagerSupport _this;

	private static ImageButton leftBorder, rightBorder, bodyBorder, headBorder,
			eyeBorder, leftInside, rightInside, bodyInside, headInside,
			btnSwitchAnimal;

	private static boolean canTouch;

	private static int tempColor;

	private static ImageView activeView;

	private static int ANIMAL_BIRD = 0, ANIMAL_LION = 1, ANIMAL_PIG = 2,
			ANIMAL_MONKEY = 3;

	private static int current_animal;

	private static Bitmap bitmap_left, bitmap_right, bitmap_body, bitmap_head;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mainActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pager, container, false);
		_this = this;
		mAdapter = new MyAdapter(getChildFragmentManager());

		mPager = (ViewPager) view.findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		txtPageTitle = (TextView) view.findViewById(R.id.title_of_the_page);

		// Watch for button clicks.
		Button button = (Button) view.findViewById(R.id.goto_first);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mPager.setCurrentItem(0);
			}
		});
		button = (Button) view.findViewById(R.id.goto_last);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mPager.setCurrentItem(NUM_ITEMS - 1);
			}
		});

		return view;
	}

	public static class MyAdapter extends FragmentStatePagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			return ArrayListFragment.newInstance(position);
		}
		
		
		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			// TODO Auto-generated method stub
			super.setPrimaryItem(container, position, object);
			txtPageTitle.setText("第"+(position+1)+"个");
		}
	}

	public static class ArrayListFragment extends ListFragment {
		int mNum;
		
		

		/**
		 * Create a new instance of CountingFragment, providing "num" as an
		 * argument.
		 */
		static ArrayListFragment newInstance(int num) {
			ArrayListFragment f = new ArrayListFragment();

			// Supply num input as an argument.
			Bundle args = new Bundle();
			args.putInt("num", num);
			f.setArguments(args);

			return f;
		}

		/**
		 * When creating, retrieve this instance's number from its arguments.
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mNum = getArguments() != null ? getArguments().getInt("num") : 1;
		}

		/**
		 * The Fragment's UI is just a simple text view showing its instance
		 * number.
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View view;

			if (mNum == 0) {

				view = inflater.inflate(R.layout.fragment_xinlimap_character, container,
						false);

//
//				btnSwitchChart = (ImageButton) view
//						.findViewById(R.id.btn_switch_chart);
				btnSwitchChart.setOnClickListener(_this);

//				webView = (WebView) view.findViewById(R.id.webView);
				WebSettings settings = webView.getSettings();
				// settings.setUseWideViewPort(true);
				// settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
				// settings.setSupportZoom(true);
				// settings.setBuiltInZoomControls(true);
				settings.setJavaScriptEnabled(true);
				// settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
				// settings.setLoadWithOverviewMode(true);
				webView.setWebViewClient(new WebViewClient() {

					@Override
					public void onReceivedError(WebView view, int errorCode,
							String description, String failingUrl) {
						// TODO Auto-generated method stub
						super.onReceivedError(view, errorCode, description,
								failingUrl);
						Toast.makeText(mainActivity, "没网" + errorCode, 3)
								.show();
					}

					@Override
					public boolean shouldOverrideUrlLoading(WebView view,
							String url) {

						view.loadUrl(url); // 在当前的webview中跳转到新的url

						return true;
					}
				});
				webView.loadUrl("http://10.0.0.88/hc.html");
				// webView.loadUrl("http://www.hcharts.cn/demo/index.php?p=10");

			} else if (mNum == 1) {
				view = inflater.inflate(R.layout.fragment_xinlimap_animal,
						container, false);

				leftBorder = (ImageButton) view
						.findViewById(R.id.btn_left_border);
				rightBorder = (ImageButton) view
						.findViewById(R.id.btn_right_border);
				bodyBorder = (ImageButton) view
						.findViewById(R.id.btn_body_border);
				headBorder = (ImageButton) view
						.findViewById(R.id.btn_head_border);
				eyeBorder = (ImageButton) view.findViewById(R.id.btn_eye);
				leftInside = (ImageButton) view
						.findViewById(R.id.btn_left_inside);
				rightInside = (ImageButton) view
						.findViewById(R.id.btn_right_inside);
				headInside = (ImageButton) view
						.findViewById(R.id.btn_head_inside);
				bodyInside = (ImageButton) view
						.findViewById(R.id.btn_body_inside);

				btnSwitchAnimal = (ImageButton) view
						.findViewById(R.id.btn_switch_animal);
				btnSwitchAnimal.setOnClickListener(_this);

				current_animal = ANIMAL_BIRD;
				bitmap_left = ((BitmapDrawable) (leftInside.getDrawable()))
						.getBitmap(); // 設定觸控螢幕監聽
				bitmap_right = ((BitmapDrawable) (rightInside.getDrawable()))
						.getBitmap(); // 設定觸控螢幕監聽
				bitmap_body = ((BitmapDrawable) (bodyInside.getDrawable()))
						.getBitmap(); // 設定觸控螢幕監聽
				bitmap_head = ((BitmapDrawable) (headInside.getDrawable()))
						.getBitmap(); // 設定觸控螢幕監聽

				canTouch = true;
				tempColor = 0;
				eyeBorder.setOnTouchListener(new View.OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_DOWN
								&& canTouch) {
							canTouch = false;
							int point_x = (int) event.getX();
							int point_y = (int) event.getY();
							if (point_x < bitmap_left.getWidth()
									&& point_y < bitmap_left.getHeight()
									&& bitmap_left.getPixel(point_x, point_y) != 0) {
								activeView = leftInside;
								Log.i("login", "left");
								// leftBorder.getDrawable().setColorFilter(new
								// LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
								tempColor = bitmap_left.getPixel(
										(int) (event.getX()),
										((int) event.getY()));
								activeView.getDrawable().setColorFilter(
										0xffff0000, PorterDuff.Mode.SRC_IN);
								// Toast.makeText(mainActivity, "点到l",
								// 3).show();
								return true;
							} else if (point_x < bitmap_right.getWidth()
									&& point_y < bitmap_right.getHeight()
									&& bitmap_right.getPixel(point_x, point_y) != 0) {
								Log.i("login", "right");
								activeView = rightInside;
								tempColor = bitmap_right.getPixel(
										(int) (event.getX()),
										((int) event.getY()));
								activeView.getDrawable().setColorFilter(
										0xffff0000, PorterDuff.Mode.SRC_IN);
								// Toast.makeText(mainActivity, "点到r",
								// 3).show();
								return true;
							} else if (point_x < bitmap_body.getWidth()
									&& point_y < bitmap_body.getHeight()
									&& bitmap_body.getPixel(point_x, point_y) != 0) {
								Log.i("login", "body");
								activeView = bodyInside;
								tempColor = bitmap_right.getPixel(
										(int) (event.getX()),
										((int) event.getY()));
								activeView.getDrawable().setColorFilter(
										0xffff0000, PorterDuff.Mode.SRC_IN);
								// Toast.makeText(mainActivity, "点到r",
								// 3).show();
								return true;
							} else if (point_x < bitmap_head.getWidth()
									&& point_y < bitmap_head.getHeight()
									&& bitmap_head.getPixel(point_x, point_y) != 0) {
								Log.i("login", "head");
								activeView = headInside;
								tempColor = bitmap_right.getPixel(
										(int) (event.getX()),
										((int) event.getY()));
								activeView.getDrawable().setColorFilter(
										0xffff0000, PorterDuff.Mode.SRC_IN);
								// Toast.makeText(mainActivity, "点到r",
								// 3).show();
								return true;
							} else {
								Log.i("login", "mei");
								// Toast.makeText(mainActivity, "nothing",
								// 3).show();
							}
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							canTouch = true;
							if (activeView != null) {
								activeView.getDrawable().setColorFilter(
										tempColor, PorterDuff.Mode.DST);
								activeView = null;
							}

						}

						return true;
					};
				});

			} else {
				view = inflater.inflate(R.layout.fragment_pager_list,
						container, false);
				View tv = view.findViewById(R.id.text);
				((TextView) tv).setText("Fragment #" + mNum);
			}

			return view;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			setListAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1));
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.i("login", "Item clicked: " + id);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.btn_switch_chart:
//			switchChart();
//			break;
		case R.id.btn_switch_animal:
			this.swtichAnimal();
			Toast.makeText(mainActivity, "switch", 3).show();
			break;
		default:
			break;
		}
	}

	private void switchChart() {

		if (currentType < 3) {
			webView.loadUrl("javascript:switchChart(" + (++currentType) + ")");
		} else if (currentType == 3) {
			currentType = 4;
			webView.loadUrl("http://10.0.0.88/hs.html");
			webView.loadUrl("javascript:switchChart(0)");

		} else if (currentType == 4) {
			currentType = 0;
			webView.loadUrl("http://10.0.0.88/hc.html");
			webView.loadUrl("javascript:switchChart(" + currentType + ")");

		}
	}

	private void swtichAnimal() {

		if (current_animal == ANIMAL_BIRD) {
			leftBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_left_border));
			rightBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_right_border));
			bodyBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_body_border));
			headBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_head_border));
			eyeBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_eye_border));
			leftInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_left_inside));
			rightInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_right_inside));
			headInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_head_inside));
			bodyInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_body_inside));
			current_animal = ANIMAL_LION;
		} else if (current_animal == ANIMAL_LION) {
			leftBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_left_border));
			rightBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_right_border));
			bodyBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_body_border));
			headBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_head_border));
			eyeBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_eye_border));
			leftInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_left_inside));
			rightInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_right_inside));
			headInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_head_inside));
			bodyInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_body_inside));
			current_animal = ANIMAL_PIG;
		} else if (current_animal == ANIMAL_PIG) {

			rightBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_right_border));
			bodyBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_body_border));
			headBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_head_border));
			eyeBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_eye_border));
			rightInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_right_inside));
			headInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_head_inside));
			bodyInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_body_inside));
			leftBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_left_border));
			leftInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_left_inside));
			leftBorder.bringToFront();
			leftInside.bringToFront();
			eyeBorder.bringToFront();
			current_animal = ANIMAL_MONKEY;
		} else if (current_animal == ANIMAL_MONKEY) {
			leftBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_left_border));
			rightBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_right_border));
			bodyBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_body_border));
			headBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_head_border));
			eyeBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_eye_border));
			leftInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_left_inside));
			rightInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_right_inside));
			headInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_head_inside));
			bodyInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_body_inside));
			current_animal = ANIMAL_BIRD;
		}

		bitmap_left = ((BitmapDrawable) (leftInside.getDrawable())).getBitmap(); // 設定觸控螢幕監聽
		bitmap_right = ((BitmapDrawable) (rightInside.getDrawable()))
				.getBitmap(); // 設定觸控螢幕監聽
		bitmap_body = ((BitmapDrawable) (bodyInside.getDrawable())).getBitmap(); // 設定觸控螢幕監聽
		bitmap_head = ((BitmapDrawable) (headInside.getDrawable())).getBitmap(); // 設定觸控螢幕監聽
	}

}