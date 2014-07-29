package com.zhixin.settings;

import java.util.HashMap;

public class ErrHashMap {
	private static final HashMap<String, String> errhashMap = new HashMap<String, String>();

	static {
		insertErrCode();
	}

	private static void insertErrCode() {
        errhashMap.put("err001", "没有登录");
        errhashMap.put("err002", "密码输入不符合规范，8-30位且必须包含字母");
        errhashMap.put("err003", "手机号不符合规范");
        errhashMap.put("err004", "用户不存在");
        errhashMap.put("err005", "用户没激活");
        errhashMap.put("err006", "用户已激活,不能再输入邀请码");
        errhashMap.put("err007", "密码错误");
        errhashMap.put("err008", "输入验证码有误");
        errhashMap.put("err009", "输入验证码错误次数过多,请明天再试,如有问题请联系我们");
        errhashMap.put("err010", "您输入的邀请码为空");
        errhashMap.put("err011", "你输入的邀请码错误");
        errhashMap.put("err012", "你输入的邀请码错误(不能邀请自己)");
        errhashMap.put("err020", "旧密码错误");

        errhashMap.put("err101", "缺少必要的参数");
        errhashMap.put("err102", "参数值超出范围或值非法");
        errhashMap.put("err103", "系统无记录");
        errhashMap.put("err104", "检测到数据量异常,如有问题请联系官方人员");
        errhashMap.put("err105", "输入内容不能为空");

        errhashMap.put("err201", " 没有该问卷，或者该问卷已经下架");
        errhashMap.put("err202", "用户做过该问卷");
        errhashMap.put("err203", "用户还没做过该问卷");
        errhashMap.put("err204", "没有更多的问卷了");
        errhashMap.put("err205", "不符合答该问卷的条件");
        errhashMap.put("err206", "该问卷已经过期");
        errhashMap.put("err207", "答题人数满了");

        errhashMap.put("err301", "积分不足");
        errhashMap.put("err302", "金币不足");
        errhashMap.put("err303", "所填数字不能为小于0");
        errhashMap.put("err304", "超过每日积分获取最大限额");
        errhashMap.put("err305", "超过每日金币获取最大限额");
        errhashMap.put("err310", "用户身没通过身份证验证");
        errhashMap.put("err311", "银行卡账号格式不正确");
        errhashMap.put("err312", "没有验证码或者验证码失效，请重新申请验证码");
        errhashMap.put("err313", "验证码错误");
        errhashMap.put("err314", "查不到提现记录");
        errhashMap.put("err315", "交易已经结束");
        errhashMap.put("err316", "没有更多提现记录了");

        errhashMap.put("err402", "输入的内容有误");
        errhashMap.put("err403", "有人使用了该昵称");
        errhashMap.put("err405", "昵称不能超过14位（英文1位 中文2位）");
        errhashMap.put("err410", "地址超过10个,不能继续添加");
        errhashMap.put("err411", "地址不存在或者已经被删除");
        errhashMap.put("err412", "该收货地址不属于当前用户");
        errhashMap.put("err421", "身份证格式不正确");
        errhashMap.put("err422", "身份证姓名不匹配");
        errhashMap.put("err423", "每个月只能修改一次身份验证信息");
        errhashMap.put("err424", "验证信息提交错误次数超过限制,如有问题请联系我们.");
        errhashMap.put("err425", "该身份验证已经被使用,如非本人操作请联系官方人员.");
        errhashMap.put("err426", "您已绑定此身份验证,请勿重复提交.");


        errhashMap.put("err500", "你们已经是好友了");
        errhashMap.put("err501", "已经请求添加过好友，请勿重复添加");
        errhashMap.put("err502", "已经确认过好友，请勿重复确认");
        errhashMap.put("err503", "目标不能是自己");
        errhashMap.put("err504", "未找到对方的请求记录");
        errhashMap.put("err505", "不是好友关系");
        errhashMap.put("err506", "此人已经是您的宝宝");
        errhashMap.put("err507", "此人不是您的宝宝");
        errhashMap.put("err508", "宝宝被购买的时间间隔没到");
        errhashMap.put("err509", "已到宝宝数量限制");
        errhashMap.put("err510", "今天已经打赏过该宝宝");
        errhashMap.put("err520", "没有更多");

        errhashMap.put("err601", "参与过该话题");
        errhashMap.put("err602", "没有参与过该话题");
        errhashMap.put("err603", "未找到话题，可能已经下架或者被删除");

        errhashMap.put("err701", "现在没有活动");
        errhashMap.put("err702", "不能再参加该活动了");
        errhashMap.put("err703", "还没参加过该活动");
        errhashMap.put("err704", "兑换奖品库存不足");
        errhashMap.put("err705", "兑换奖品超出限制");
        errhashMap.put("err706", "没有找到记录");
        errhashMap.put("err707", "没有奖品信息");
        errhashMap.put("errFFF", "没有可用的网络连接");


	}

	
	public static String getErrMessage(String errcode){
		return errhashMap.get(errcode);
		
	}
}
