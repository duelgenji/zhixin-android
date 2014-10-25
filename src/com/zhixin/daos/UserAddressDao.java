package com.zhixin.daos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.database.DbManager;
import com.zhixin.domain.UserAddress;
import com.zhixin.settings.CurrentUserHelper;

/**
 * Created by duel on 14-3-26.
 */
public class UserAddressDao {

	public void saveUserAddress(JSONObject jbo) throws JSONException {
		JSONArray aData = jbo.getJSONArray("content");
		JSONObject jboInA;

		long userId = CurrentUserHelper.getCurrentUserId();
		UserAddress userAddress;
		AddressDao addressDao = new AddressDao();
		String iSfId = null;
		String iCsId = null;
		String iDqId = null;
		String sSfmc = null;
		String sCsmc = null;
		String sDqmc = null;
		String areaCode = null;

		deleteAllAddress();

		for (int i = 0; i < aData.length(); i++) {
			jboInA = aData.getJSONObject(i);
			userAddress = new UserAddress();
			userAddress.setUserId(userId);
			userAddress.setDzId(jboInA.getInt("id"));
			userAddress.setName(jboInA.getString("consignee"));

			iSfId = !jboInA.has("firstCode") ? "" : jboInA
					.getString("firstCode");
			iCsId = !jboInA.has("secondCode") ? "" : jboInA
					.getString("secondCode");
			iDqId = !jboInA.has("thirdCode") ? "" : jboInA
					.getString("thirdCode");

			if (jboInA.has("areaCode")) {
				areaCode = jboInA.getString("areaCode");
			} else {
				if (jboInA.has("firstCode")) {
					areaCode = jboInA.getString("firstCode");
					if (jboInA.has("secondCode")) {
						areaCode = jboInA.getString("secondCode");
						if (jboInA.has("thirdCode")) {
							areaCode = jboInA.getString("thirdCode");
						}
					}
				}
			}

			if (addressDao.getSfmc(iSfId) != null) {
				sSfmc = addressDao.getSfmc(iSfId).getMc();
				userAddress.setSfmc(sSfmc);
			}
			if (addressDao.getCsmc(iCsId) != null) {
				sCsmc = addressDao.getCsmc(iCsId).getMc();
				userAddress.setCsmc(sCsmc);
			}
			if (addressDao.getDqmc(iDqId) != null) {
				sDqmc = addressDao.getDqmc(iDqId).getMc();
				userAddress.setDqmc(sDqmc);
			}

			userAddress.setSfId(iSfId);
			userAddress.setCsId(iCsId);
			userAddress.setDqId(iDqId);
			userAddress.setAddress(jboInA.getString("detialAddress"));
			userAddress.setAreaCode(areaCode);
			userAddress.setPostCode(jboInA.getString("postCode"));
			if (jboInA.getBoolean("defaultAddress")) {
				userAddress.setIsDefault(1);
			} else {
				userAddress.setIsDefault(0);
			}

			userAddress.setPhone(jboInA.getString("phone"));

			DbManager.getDatabase().save(userAddress);
		}

	}

	public void saveSingleUserAddress(JSONObject jbo) throws JSONException {

		UserAddress userAddress = new UserAddress();

		if (jbo.has("id")) {
			userAddress.setDzId(jbo.getInt("id"));
		}

		if (jbo.has("userId")) {
			userAddress.setUserId(jbo.getLong("userId"));
		}

		setSameContent(jbo, userAddress);

		DbManager.getDatabase().save(userAddress);

	}

	private void setSameContent(JSONObject jbo, UserAddress userAddress) {

		String iSfId = null;
		String iCsId = null;
		String iDqId = null;
		String sSfmc = null;
		String sCsmc = null;
		String sDqmc = null;
		String areaCode = null;

		try {
			if (jbo.has("consignee")) {
				userAddress.setName(jbo.getString("consignee"));
			}

			if (jbo.has("phone")) {
				userAddress.setPhone(jbo.getString("phone"));
			}

			if (jbo.has("firstCode")) {
				iSfId = jbo.getString("firstCode");
				userAddress.setSfId(iSfId);
			}

			if (jbo.has("secondCode")) {
				iCsId = jbo.getString("secondCode");
				userAddress.setCsId(iCsId);
			}
			if (jbo.has("thirdCode")) {
				iDqId = jbo.getString("thirdCode");
				userAddress.setDqId(iDqId);
			}

			if (jbo.has("areaCode")) {
				areaCode = jbo.getString("areaCode");
				userAddress.setAreaCode(areaCode);
			} else if (jbo.has("firstCode")) {
				areaCode = jbo.getString("firstCode");
				if (jbo.has("secondCode")) {
					areaCode = jbo.getString("secondCode");
					if (jbo.has("thirdCode")) {
						areaCode = jbo.getString("thirdCode");
					}
				}
				userAddress.setAreaCode(areaCode);
			}

			AddressDao addressDao = new AddressDao();

			if (addressDao.getSfmc(iSfId) != null) {
				sSfmc = addressDao.getSfmc(iSfId).getMc();
				userAddress.setSfmc(sSfmc);
			}
			if (addressDao.getCsmc(iCsId) != null) {
				sCsmc = addressDao.getCsmc(iCsId).getMc();
				userAddress.setCsmc(sCsmc);
			}
			if (addressDao.getDqmc(iDqId) != null) {
				sDqmc = addressDao.getDqmc(iDqId).getMc();
				userAddress.setDqmc(sDqmc);
			}

			if (jbo.has("detialAddress")) {
				userAddress.setAddress(jbo.getString("detialAddress"));
			}

			if (jbo.has("postCode")) {
				userAddress.setPostCode(jbo.getString("postCode"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void updateSingleUserAddress(JSONObject jbo) throws JSONException {

		int iDzId = jbo.getInt("id");

		UserAddress userAddress = DbManager.getDatabase().findUniqueBySql(
				UserAddress.class,
				"select * from user_address where dzId='" + iDzId + "';");

		setSameContent(jbo, userAddress);
		
		if (jbo.has("defaultAddress")) {

			if (jbo.getBoolean("defaultAddress")) {
				setNoDefault();
				userAddress.setIsDefault(1);
				long userId = userAddress.getUserId();
				String defaultAddress = null;
				if (jbo.has("detialAddress")) {
					defaultAddress = jbo.getString("detialAddress");
				} else {
					defaultAddress = userAddress.getAddress();
				}
				UserInfoDao userInfoDao = new UserInfoDao();
				userInfoDao.saveUserInfoDefaultAddressById(userId,
						defaultAddress);
			} else {
				userAddress.setIsDefault(0);
			}
		}

		DbManager.getDatabase().update(userAddress);

	}

	public void clearData() {
		deleteAllAddress();
	}

	private void deleteAllAddress() {
		if (DbManager.getDatabase().tableExists(UserAddress.class)) {
			String sql = "delete from user_address where userId="
					+ CurrentUserHelper.getCurrentUserId();
			DbManager.getDatabase().exeCustomerSql(sql);
		}
	}

	public void deleteAddressById(int id) {
		if (DbManager.getDatabase().tableExists(UserAddress.class)) {
			String sql = "delete from user_address where dzId=" + id;
			DbManager.getDatabase().exeCustomerSql(sql);
		}
	}

	private void setNoDefault() {
		if (DbManager.getDatabase().tableExists(UserAddress.class)) {
			String sql = "update user_address set isDefault='0' where userId="
					+ CurrentUserHelper.getCurrentUserId();
			DbManager.getDatabase().exeCustomerSql(sql);
		}
	}
}
