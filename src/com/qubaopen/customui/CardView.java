package com.qubaopen.customui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.qubaopen.R;
import com.qubaopen.customui.CardItemView.OnCardChangeListener;
import com.qubaopen.domain.MapData;
import com.qubaopen.settings.PhoneHelper;

public class CardView extends LinearLayout {
	private static ArrayList<Integer> colorList;
	private List<MapData> mapList;

	private int windowHeight = PhoneHelper.getPhoneHeight();
	private int myHeight = (int) (windowHeight * 0.1);

	public CardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public CardView(Context context) {
		this(context, null);
	}

	static {

		colorList = new ArrayList<Integer>();
		colorList.add(Color.parseColor("#42aaff"));
		colorList.add(Color.parseColor("#db3c41"));
		colorList.add(Color.parseColor("#c671fb"));
		colorList.add(Color.parseColor("#fb8371"));
		colorList.add(Color.parseColor("#4aae32"));
		colorList.add(Color.parseColor("#ecaa5b"));
		colorList.add(Color.parseColor("#f3542d"));
	}

	private void initView(Context context) {
		setOrientation(VERTICAL);
	}

	public List<MapData> getMapList() {
		return mapList;
	}

	public void setMapList(List<MapData> mapList) {

		this.mapList = mapList;
		updateView();
	}

	private void updateView() {
		removeAllViews();
		if (mapList != null && mapList.size() > 0) {
			for (int i = 0; i < mapList.size(); i++) {
				MapData mapData = mapList.get(i);
				if (mapData != null) {
					Builder builder = new Builder();
					final CardItemView cardItemView = builder.build(getContext());
					Log.i("mapdata", "setMapData..." + mapData);
					cardItemView.setMapData(mapData,
							colorList.get(i % colorList.size()));
					LinearLayout.LayoutParams cardItemLp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
//					cardItemLp.height = myHeight;
					if (i == 0) {
						cardItemLp.topMargin = 0;
					} else {
						cardItemLp.topMargin = (int) (-myHeight * 0.2);
						Log.i("22222222", (-myHeight * 0.2)+"");
					}

					cardItemView.setLayoutParams(cardItemLp);
					final int position = i;

					cardItemView.setAnimatorListener(animatorListener);

					if (mapData.getMapDataGraphicsType() != null) {
						if (mapData.getMapDataGraphicsType().equals("5")) {
							getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
								@Override
								public boolean onPreDraw() {
									// TODO Auto-generated method stub
									getViewTreeObserver().removeOnPreDrawListener(this);
									cardItemView.openCard(true);
									return false;
								}
							});
						}
					}

					cardItemView
							.setOnCardChangeListener(new OnCardChangeListener() {

								@Override
								public void onChanged(boolean isExpand) {
									Log.i("mapdata", "position:..." + position);
									// 关闭没选中的卡片
									for (int j = 0; j < getChildCount(); j++) {
										if ((isExpand && position != j)
												|| !isExpand) {
											CardItemView closeView = (CardItemView) getChildAt(j);
											if (closeView != null) {
												closeView.closeCard(true);
											}
										}
									}
								}
							});

					addView(cardItemView);
				}
			}
		}
	}

	private AnimatorListener animatorListener;

	public AnimatorListener getAnimatorListener() {
		return animatorListener;
	}

	public void setAnimatorListener(AnimatorListener animatorListener) {
		this.animatorListener = animatorListener;
	}

	class Builder {
		private CardItemView cardItemView;

		public CardItemView build(Context context) {
			cardItemView = (CardItemView) LayoutInflater.from(context).inflate(
					R.layout.fragment_xinlimap_card_item, null);
			return cardItemView;
		}
	}

}
