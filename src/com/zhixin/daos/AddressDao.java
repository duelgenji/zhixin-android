package com.zhixin.daos;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.zhixin.database.DbManager;
import com.zhixin.domain.AddressCs;
import com.zhixin.domain.AddressDq;
import com.zhixin.domain.AddressSf;

public class AddressDao {
	public List<AddressSf> getAddressSf() {
		return DbManager.getPublicDatabase().findAll(AddressSf.class);
	}


	public List<AddressCs> getAddressCs(String sfdm) {
		String sql = "sfdm='" + sfdm + "'";
		return DbManager.getPublicDatabase().findAllByWhere(AddressCs.class,
				sql);
	}

	public List<AddressDq> getAddressDq(String csdm) {
		String sql = "csdm='" + csdm + "'";
		return DbManager.getPublicDatabase().findAllByWhere(AddressDq.class,
				sql);

	}


    public AddressSf getSfmc(String sfdm) {
        return DbManager.getPublicDatabase().findUniqueByWhere(AddressSf.class," sfdm='" + sfdm + "'");
    }
    public AddressCs getCsmc(String csdm) {
        return DbManager.getPublicDatabase().findUniqueByWhere(AddressCs.class," csdm='"+csdm+"'");
    }
    public AddressDq getDqmc(String dqdm) {
        return DbManager.getPublicDatabase().findUniqueByWhere(AddressDq.class," dqdm='"+dqdm+"'");
    }


	public void saveAddress(JSONObject jbo, Context context)
			throws JSONException {
		JSONArray aSf = jbo.getJSONArray("aSf");
		JSONArray aCs = jbo.getJSONArray("aCs");
		JSONArray aDq = jbo.getJSONArray("aDq");
		JSONObject jboInA;

		AddressSf addressSf;
		AddressCs addressCs;
		AddressDq addressDq;

		 DbManager.getDatabase().deleteAll(AddressSf.class);
		 DbManager.getDatabase().deleteAll(AddressCs.class);
		 DbManager.getDatabase().deleteAll(AddressDq.class);


		for (int i = 0; i < aSf.length(); i++) {
			jboInA = aSf.getJSONObject(i);
			addressSf = new AddressSf();
			addressSf.setMc(jboInA.getString("sSfMc"));
			addressSf.setSfdm(jboInA.getString("iSfDm"));
			DbManager.getPublicDatabase().save(addressSf);
		}

		for (int i = 0; i < aCs.length(); i++) {
			jboInA = aCs.getJSONObject(i);
			addressCs = new AddressCs();
			addressCs.setMc(jboInA.getString("sCsMc"));
			addressCs.setCsdm(jboInA.getString("iCsDm"));
			addressCs.setSfdm(jboInA.getString("iSfDm"));
			DbManager.getPublicDatabase().save(addressCs);
		}

		for (int i = 0; i < aDq.length(); i++) {
			jboInA = aDq.getJSONObject(i);
			addressDq = new AddressDq();
			addressDq.setMc(jboInA.getString("sDqMc"));
			addressDq.setDqdm(jboInA.getString("iDqDm"));
			addressDq.setCsdm(jboInA.getString("iCsDm"));
			DbManager.getPublicDatabase().save(addressDq);
		}

	}

	private void deleteAll() {
		if (DbManager.getPublicDatabase().tableExists(AddressSf.class)) {
			String sql = "delete from address_sf where 1=1";
			DbManager.getPublicDatabase().exeCustomerSql(sql);
		}
		if (DbManager.getPublicDatabase().tableExists(AddressCs.class)) {
			String sql = "delete from address_cs where 1=1";
			DbManager.getPublicDatabase().exeCustomerSql(sql);
		}
		if (DbManager.getPublicDatabase().tableExists(AddressDq.class)) {
			String sql = "delete from address_dq where 1=1";
			DbManager.getPublicDatabase().exeCustomerSql(sql);
		}

	}
}
