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
        JSONArray aData = jbo.getJSONArray("aData");
        JSONObject jboInA;

        long userId= CurrentUserHelper.getCurrentUserId();
        UserAddress userAddress;
        AddressDao addressDao=new AddressDao();
        String iSfId;
        String iCsId;
        String iDqId;
        String sSfmc;
        String sCsmc;
        String sDqmc;
        deleteAllAddress();
        for (int i = 0; i < aData.length(); i++) {
            jboInA = aData.getJSONObject(i);
            userAddress = new UserAddress();
            userAddress.setUserId(userId);
            userAddress.setDzId(jboInA.getInt("iDzId"));
            userAddress.setName(jboInA.getString("sName"));
            iSfId=!jboInA.has("iSfId")?"":jboInA.getString("iSfId");
            iCsId=!jboInA.has("iCsId")?"":jboInA.getString("iCsId");
            iDqId=!jboInA.has("iDqId")?"":jboInA.getString("iDqId");

            if(addressDao.getSfmc(iSfId)!=null){
                sSfmc= addressDao.getSfmc(iSfId).getMc();
                userAddress.setSfmc(sSfmc);
            }
            if(addressDao.getCsmc(iCsId)!=null){
                sCsmc= addressDao.getCsmc(iCsId).getMc();
                userAddress.setCsmc(sCsmc);
            }
            if(addressDao.getDqmc(iDqId)!=null){
                sDqmc= addressDao.getDqmc(iDqId).getMc();
                userAddress.setDqmc(sDqmc);
            }
            userAddress.setSfId(iSfId);
            userAddress.setCsId(iCsId);
            userAddress.setDqId(iDqId);
            userAddress.setAddress(jboInA.getString("sAddress"));
            userAddress.setPostCode(jboInA.getString("sPostCode"));
            userAddress.setIsDefault(jboInA.getInt("isDefault"));
            userAddress.setPhone(jboInA.getString("sPhone"));


            DbManager.getDatabase().save(userAddress);
        }
        
    }

    public void saveUserAddressOne(JSONObject jbo) throws JSONException {

        int iDzId=jbo.getInt("iDzId");


        UserAddress userAddress=
                DbManager.getDatabase().findUniqueBySql(UserAddress.class,"select * from user_address where dzId='"+ iDzId+"';");

        userAddress.setName(jbo.getString("sName"));
        String iSfId;
        String iCsId;
        String iDqId;
        String sSfmc;
        String sCsmc;
        String sDqmc;
        iSfId=!jbo.has("iSfId")?"":jbo.getString("iSfId");
        iCsId=!jbo.has("iCsId")?"":jbo.getString("iCsId");
        iDqId=!jbo.has("iDqId")?"":jbo.getString("iDqId");
        sSfmc=!jbo.has("sSfmc")?"":jbo.getString("sSfmc");
        sCsmc=!jbo.has("sCsmc")?"":jbo.getString("sCsmc");
        sDqmc=!jbo.has("sDqmc")?"":jbo.getString("sDqmc");

        userAddress.setSfId(iSfId);
        userAddress.setCsId(iCsId);
        userAddress.setDqId(iDqId);
        userAddress.setSfmc(sSfmc);
        userAddress.setCsmc(sCsmc);
        userAddress.setDqmc(sDqmc);
        userAddress.setAddress(jbo.getString("sAddress"));
        userAddress.setPostCode(jbo.getString("sPostCode"));
        if(jbo.has("isDefault")){
            setNoDefault();
            userAddress.setIsDefault(jbo.getInt("isDefault"));
        }
        userAddress.setPhone(jbo.getString("sPhone"));

        DbManager.getDatabase().update(userAddress);

    }

    public void clearData() {
        deleteAllAddress();
    }

    private void deleteAllAddress() {
        if (DbManager.getDatabase().tableExists(UserAddress.class)) {
            String sql = "delete from user_address where memberId="
                    + CurrentUserHelper.getCurrentUserId();
            DbManager.getDatabase().exeCustomerSql(sql);
        }
    }

    private void setNoDefault() {
        if (DbManager.getDatabase().tableExists(UserAddress.class)) {
            String sql = "update user_address set isDefault='0' where memberId="
                    + CurrentUserHelper.getCurrentUserId();
            DbManager.getDatabase().exeCustomerSql(sql);
        }
    }
}
