package me.zohar.runscore.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BizError {

	app监控回调失败找不到对应通道金额的订单("1014", "app监控回调失败,找不到对应通道金额的订单"),

	账号密钥无效("1014", "账号密钥无效"),

	谷歌验证码不正确("1014", "谷歌验证码不正确"),

	请绑定谷歌验证器("1014", "请绑定谷歌验证器"),

	额度不足("1015", "额度不足"),

	只有未确认超时取消的订单才能补单("1000", "只有未确认超时取消的订单才能补单"),

	订单状态为未确认超时取消才能转为确认已支付("1015", "订单状态为未确认超时取消才能转为确认已支付"),

	订单状态为未确认超时取消才能手动取消冻结余额("1015", "订单状态为未确认超时取消才能手动取消冻结余额"),

	系统已自动取消冻结余额("1000", "该订单系统已自动取消冻结余额，无需手动取消"),

	接单太快了("1000", "接单太快了,请稍后再操作"),

	发起申诉太快了("1000", "发起申诉太快了,请稍后再操作"),

	无权修改电子钱包("999", "无权修改电子钱包"),

	无权修改银行卡("999", "无权修改银行卡"),

	请登录("999", "请登录"),

	参数异常("1000", "参数异常"),

	业务异常("1000", "业务异常"),

	账户余额余额不足("1006", "账户余额余额不足"),

	用户名已存在("1014", "用户名已存在"),

	无权查看投注记录("1015", "无权查看投注记录"),

	旧的登录密码不正确("1016", "旧的登录密码不正确"),

	旧的资金密码不正确("1017", "旧的资金密码不正确"),

	资金密码不正确("1017", "资金密码不正确"),

	签名不正确("1018", "签名不正确"),

	发起订单失败通道不存在("1018", "发起订单失败,通道不存在"),

	发起订单失败通道维护中("1018", "发起订单失败,通道维护中"),

	发起订单失败该通道未开通("1018", "发起订单失败,该通道未开通"),

	发起订单失败该通道已关闭("1018", "发起订单失败,该通道已关闭"),

	充值订单不存在("1019", "充值订单不存在"),

	只有待支付状态的充值订单才能取消("1019", "只有待支付状态的充值订单才能取消"),

	发起支付异常("1021", "发起支付异常"),

	存款时间不能为空("1019", "存款时间不能为空"),

	存款人姓名不能为空("1019", "存款人姓名不能为空"),

	你有充值订单未处理完("1019", "你有充值订单未处理完,请等处理完再次提交"),

	银行卡未绑定无法进行提现("1014", "银行卡未绑定无法进行提现"),

	电子钱包未绑定无法进行提现("1014", "电子钱包未绑定无法进行提现"),

	只有状态为发起提现的记录才能审核通过("1014", "只有状态为发起提现的记录才能审核通过"),

	只有状态为发起提现或审核通过的记录才能进行审核不通过操作("1014", "只有状态为发起提现或审核通过的记录才能进行审核不通过操作"),

	只有状态为审核通过的记录才能进行确认到帐操作("1014", "只有状态为审核通过的记录才能进行确认到帐操作"),

	邀请码不存在或已失效("1014", "邀请码不存在或已失效"),

	未开放注册功能("1014", "未开放注册功能"),

	字典项code不能重复("1000", "字典项code不能重复"),

	邀请人不存在("1014", "邀请人不存在"),

	邀请注册功能已关闭("1014", "邀请注册功能已关闭"),

	不能设置重复的通道("1014", "不能设置重复的通道"),

	未知通道不能进行下级开户操作("1014", "未知通道,不能进行下级开户操作"),

	无权查看数据("1015", "无权查看数据"),

	无权删除数据("1015", "无权删除数据"),

	商户未接入("1015", "商户未接入"),

	商户账号已被禁用("1015", "商户账号已被禁用"),

	只有已支付的订单才能进行异步通知("1015", "只有已支付的订单才能进行异步通知"),

	系统维护中不能发起订单("1015", "系统维护中,不能发起订单"),

	系统维护中不能接单("1015", "系统维护中,不能接单"),

	金额格式不正确("1015", "金额格式不正确"),

	金额不能小于或等于0("1015", "金额不能小于或等于0"),

	只有状态为正在接单才能进行接单("1015", "只有状态为正在接单才能进行接单"),

	商户订单不存在("1015", "商户订单不存在"),

	商户订单号不能重复("1015", "商户订单号不能重复"),

	不支持该支付类型("1015", "不支持该支付类型"),

	订单已被接或已取消("1015", "订单已被接或已取消"),

	只有状态为取消订单退款的订单才能更改状态为已支付("1015", "只有状态为取消订单退款的订单才能更改状态为已支付"),

	接单通道未开通("1015", "接单通道未开通,请联系客服"),

	接单通道已禁用("1015", "接单通道已禁用,请联系客服"),

	接单通道已关闭("1015", "接单通道已关闭,请重新开启"),

	账户余额不足无法接单("1015", "账户余额不足无法接单"),

	订单状态为已接单才能转为确认已支付("1015", "订单状态为已接单才能转为确认已支付"),

	订单状态为申述中才能转为确认已支付("1015", "订单状态为申述中才能转为确认已支付"),

	账户余额不足无法转为确认已支付("1015", "账户余额不足无法转为确认已支付"),

	无权确认订单("1015", "无权确认订单"),

	演示模式无法执行该操作("1015", "演示模式,无法执行该操作"),

	无权获取平台订单收款码信息("1015", "无权获取平台订单收款码信息"),

	无权更新商户订单状态为商户已确认支付("1015", "无权更新商户订单状态为商户已确认支付"),

	订单状态为已接单才能转为平台已确认支付("1015", "订单状态为已接单才能转为平台已确认支付"),

	只有等待接单状态的商户订单才能取消("1019", "只有等待接单状态的商户订单才能取消"),

	只有已支付状态的商户订单才能强制取消("1019", "只有已支付状态的商户订单才能强制取消"),

	找不到所属账号无法新增收款码("1019", "找不到所属账号无法新增收款码"),

	订单状态为已接单或平台已确认支付才能申请取消订单("1015", "订单状态为已接单或平台已确认支付才能申请取消订单"),

	无权取消订单("1015", "无权取消订单"),

	只有申请取消订单状态的平台订单才能转为未支付取消订单("1019", "只有申请取消订单状态的平台订单才能转为未支付取消订单"),

	只能上传图片类型的收款码("1015", "只能上传图片类型的收款码"),

	只能上传图片类型的身份证("1015", "只能上传图片类型的身份证"),

	未设置收款码无法接单("1015", "未设置收款码无法接单"),

	无法接单找不到对应金额的收款码("1015", "无法接单,找不到对应金额的收款码"),

	实际支付金额须小于收款金额("1015", "实际支付金额须小于收款金额"),

	实际支付金额须大于收款金额("1015", "实际支付金额须大于收款金额"),

	账户余额不足无法手工减账户余额("1014", "账户余额不足,无法手工减账户余额"),

	账户余额不足("1014", "账户余额不足"),

	不能开启或关闭该账号("1014", "不能开启或关闭该账号"),

	可提现金额不足("1014", "可提现金额不足"),

	关联账号不存在("1014", "关联账号不存在"),

	账号已关联其他商户("1014", "账号已关联其他商户,无法重复关联"),

	只能关联商户类型的账号("1014", "只能关联商户类型的账号"),

	用户名已使用("1014", "用户名已使用,请使用另外的用户名"),

	商户号已使用("1014", "商户号已使用,请使用另外的商户号"),

	商户名称已使用("1014", "商户名称已使用,请使用另外的商户名称"),

	收款通道已使用("1014", "收款通道已使用,请使用另外的用通道code"),

	该订单存在未处理的申诉记录不能重复发起("1015", "该订单存在未处理的申诉记录,不能重复发起"),

	申诉类型不合法("1015", "申诉类型不合法"),

	无权撤销申诉("1015", "无权撤销申诉"),

	当前申诉已完结无法更改处理方式("1015", "当前申诉已完结无法更改处理方式"),

	该申诉类型的处理方式不能是改为实际支付金额("1015", "该申诉类型的处理方式不能是改为实际支付金额"),

	该申诉类型的处理方式不能是取消订单("1015", "该申诉类型的处理方式不能是取消订单"),

	只有申诉中或已接单的商户订单才能进行取消订单退款操作("1019", "只有申诉中或已接单的商户订单才能进行取消订单退款操作"),

	当前申诉已完结("1015", "当前申诉已完结"),

	不是申诉发起方无权撤销申诉("1015", "不是申诉发起方,无权撤销申诉"),

	用户已提供截图无法撤销申诉("1015", "用户已提供截图,无法撤销申诉"),

	商户已提供截图无法撤销申诉("1015", "商户已提供截图,无法撤销申诉"),

	无权上传截图("1015", "无权上传截图"),

	只有待处理的申诉记录才能上传截图("1015", "只有待处理的申诉记录才能上传截图"),

	已有截图不能重复上传("1015", "已有截图不能重复上传"),

	不能设置重复的返点("1000", "不能设置重复的返点"),

	该返点已存在("1000", "该返点已存在"),

	不能设置重复的接单通道("1000", "不能设置重复的接单通道"),

	通道限额范围无效("1000", "通道限额范围无效"),

	只有管理员或代理才能进行代理开户操作("1000", "只有管理员或代理才能进行代理开户操作"),

	只有代理才能进行下级开户操作("1000", "只有代理才能进行下级开户操作"),

	开户类型只能是代理或会员("1000", "开户类型只能是代理或会员"),

	开户类型只能是会员("1000", "开户类型只能是会员"),

	下级账号的返点不能大于上级账号("1000", "下级账号的返点不能大于上级账号"),

	邀请码配额不足("1000", "邀请码配额不足"),

	该返点未设置("1000", "该返点未设置"),

	已达到接单数量上限("1015", "已达到接单数量上限,请先处理待审核的订单"),

	禁止接重复单("1015", "禁止接重复单"),

	禁止接重复金额的订单("1015", "禁止接重复金额的订单,请先处理"),

	同一收款方式禁止接相同金额的订单("1015", "同一收款方式禁止接相同金额的订单"),

	同一码禁止接相同金额的订单("1015", "同一码禁止接相同金额的订单"),

	没有可接单的收款码("1015", "没有可接单的收款码，请上传收款码"),

	未达到接单账户余额最低要求("1015", "未达到接单账户余额最低要求,请先充值"),

	用户名不存在("1014", "用户名不存在"),

	你的账号已被禁用("1014", "你的账号已被禁用"),

	不是上级账号无权查看该账号及下级的接单记录("1014", "不是上级账号无权查看该账号及下级的接单记录"),

	不是上级账号无权查看该账号及下级的账号信息("1014", "不是上级账号无权查看该账号及下级的账号信息"),

	不是上级账号无权查看该账号及下级的帐变日志("1014", "不是上级账号无权查看该账号及下级的帐变日志"),

	不是上级账号无权查看该账号及下级的充值记录("1014", "不是上级账号无权查看该账号及下级的充值记录"),

	不是上级账号无权查看该账号及下级的提现记录("1014", "不是上级账号无权查看该账号及下级的提现记录"),

	无权修改收款码("1000", "无权修改收款码"),

	无权修改商户银行卡信息("1000", "无权修改商户银行卡信息"),

	银行卡信息异常("1000", "银行卡信息异常"),

	只有状态为审核中或审核通过的记录才能进行审核不通过操作("1014", "只有状态为审核中或审核通过的记录才能进行审核不通过操作"),

	只有状态为审核中的记录才能审核通过操作("1014", "只有状态为审核中的记录才能审核通过操作"),

	未设置接单通道无法接单("1015", "未设置接单通道无法接单,请联系客服"),

	无权调整该账号的返点("1000", "无权调整该账号的返点"),

	无权修改商户订单备注("1000", "无权修改商户订单备注"),;

	private String code;

	private String msg;

}
