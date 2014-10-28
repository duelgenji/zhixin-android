package com.qubaopen.logic;

import java.util.List;

import com.qubaopen.daos.QuhuatiDao;
import com.qubaopen.domain.HuatiChoices;
import com.qubaopen.domain.QuHuati;

import android.os.AsyncTask;

public class HuatiDataObject extends AsyncTask<Void, Void, Void> {

	public interface HuatiLoadFinished {
		public void displayThings();

	}

	private int huatiId;
	private String huatiTitle;
	private List<HuatiChoices> choiceList;
	private int type;

	private HuatiLoadFinished mHuatiLoadFinished;

	private QuhuatiDao huatiDao;

	public HuatiDataObject(int id) {
		this.huatiId = id;
		huatiDao = new QuhuatiDao();
	}

	@Override
	protected Void doInBackground(Void... params) {
		QuHuati huati = huatiDao.getQuhuati(huatiId);
		huatiTitle = huati.getTitle();
		type = huati.getType();
		choiceList = huatiDao.getHuatiChoices(huatiId);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		mHuatiLoadFinished.displayThings();
	}

	public int getHuatiId() {
		return huatiId;
	}

	public String getHuatiTitle() {
		return huatiTitle;
	}

	public List<HuatiChoices> getChoiceList() {
		return choiceList;
	}

	public void setmHuatiLoadFinished(HuatiLoadFinished mHuatiLoadFinished) {
		this.mHuatiLoadFinished = mHuatiLoadFinished;
	}

	public int getType() {
		return type;
	}

	
	
}
