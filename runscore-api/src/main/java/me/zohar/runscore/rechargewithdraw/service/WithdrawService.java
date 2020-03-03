package me.zohar.runscore.rechargewithdraw.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import me.zohar.runscore.common.exception.BizError;
import me.zohar.runscore.common.exception.BizException;
import me.zohar.runscore.common.valid.ParamValid;
import me.zohar.runscore.common.vo.PageResult;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.mastercontrol.domain.WithdrawSetting;
import me.zohar.runscore.mastercontrol.repo.WithdrawSettingRepo;
import me.zohar.runscore.rechargewithdraw.domain.WithdrawRecord;
import me.zohar.runscore.rechargewithdraw.param.LowerLevelWithdrawRecordQueryCondParam;
import me.zohar.runscore.rechargewithdraw.param.StartWithdrawParam;
import me.zohar.runscore.rechargewithdraw.param.WithdrawRecordQueryCondParam;
import me.zohar.runscore.rechargewithdraw.repo.WithdrawRecordRepo;
import me.zohar.runscore.rechargewithdraw.vo.WithdrawRecordVO;
import me.zohar.runscore.useraccount.domain.AccountChangeLog;
import me.zohar.runscore.useraccount.domain.BankCard;
import me.zohar.runscore.useraccount.domain.UserAccount;
import me.zohar.runscore.useraccount.domain.VirtualWallet;
import me.zohar.runscore.useraccount.repo.AccountChangeLogRepo;
import me.zohar.runscore.useraccount.repo.BankCardRepo;
import me.zohar.runscore.useraccount.repo.UserAccountRepo;
import me.zohar.runscore.useraccount.repo.VirtualWalletRepo;

@Service
public class WithdrawService {

	@Autowired
	private WithdrawRecordRepo withdrawRecordRepo;

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Autowired
	private AccountChangeLogRepo accountChangeLogRepo;

	@Autowired
	private WithdrawSettingRepo withdrawSettingRepo;

	@Autowired
	private BankCardRepo bankCardRepo;

	@Autowired
	private VirtualWalletRepo virtualWalletRepo;

	@Transactional(readOnly = true)
	public WithdrawRecordVO findWithdrawRecordById(@NotBlank String id) {
		WithdrawRecord withdrawRecord = withdrawRecordRepo.getOne(id);
		return WithdrawRecordVO.convertFor(withdrawRecord);
	}

	/**
	 * 审核通过
	 *
	 * @param id
	 */
	@ParamValid
	@Transactional
	public void approved(@NotBlank String id, String note) {
		WithdrawRecord withdrawRecord = withdrawRecordRepo.getOne(id);
		if (!Constant.提现记录状态_发起提现.equals(withdrawRecord.getState())) {
			throw new BizException(BizError.只有状态为发起提现的记录才能审核通过);
		}

		withdrawRecord.approved(note);
		withdrawRecordRepo.save(withdrawRecord);
	}

	/**
	 * 审核不通过
	 *
	 * @param id
	 */
	@ParamValid
	@Transactional
	public void notApproved(@NotBlank String id, String note) {
		WithdrawRecord withdrawRecord = withdrawRecordRepo.getOne(id);
		if (!(Constant.提现记录状态_发起提现.equals(withdrawRecord.getState())
				|| Constant.提现记录状态_审核通过.equals(withdrawRecord.getState()))) {
			throw new BizException(BizError.只有状态为发起提现或审核通过的记录才能进行审核不通过操作);
		}

		withdrawRecord.notApproved(note);
		withdrawRecordRepo.save(withdrawRecord);

		UserAccount userAccount = withdrawRecord.getUserAccount();
		double cashDeposit = NumberUtil.round(userAccount.getCashDeposit() + withdrawRecord.getWithdrawAmount(), 4)
				.doubleValue();
		userAccount.setCashDeposit(cashDeposit);
		userAccountRepo.save(userAccount);
		accountChangeLogRepo.save(AccountChangeLog.buildWithWithdrawNotApprovedRefund(userAccount, withdrawRecord));
	}

	/**
	 * 确认到帐
	 *
	 * @param id
	 */
	@ParamValid
	@Transactional
	public void confirmCredited(@NotBlank String id) {
		WithdrawRecord withdrawRecord = withdrawRecordRepo.getOne(id);
		if (!(Constant.提现记录状态_审核通过.equals(withdrawRecord.getState()))) {
			throw new BizException(BizError.只有状态为审核通过的记录才能进行确认到帐操作);
		}

		withdrawRecord.confirmCredited();
		withdrawRecordRepo.save(withdrawRecord);
	}

	@Transactional(readOnly = true)
	public PageResult<WithdrawRecordVO> findTop5TodoWithdrawRecordByPage() {
		Specification<WithdrawRecord> spec = new Specification<WithdrawRecord>() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<WithdrawRecord> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(root.get("state"), Constant.提现记录状态_发起提现));
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<WithdrawRecord> result = withdrawRecordRepo.findAll(spec,
				PageRequest.of(0, 5, Sort.by(Sort.Order.desc("submitTime"))));
		PageResult<WithdrawRecordVO> pageResult = new PageResult<>(WithdrawRecordVO.convertFor(result.getContent()), 1,
				5, result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<WithdrawRecordVO> findWithdrawRecordByPage(WithdrawRecordQueryCondParam param) {
		Specification<WithdrawRecord> spec = new Specification<WithdrawRecord>() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<WithdrawRecord> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getOrderNo())) {
					predicates.add(builder.equal(root.get("orderNo"), param.getOrderNo()));
				}
				if (StrUtil.isNotBlank(param.getState())) {
					predicates.add(builder.equal(root.get("state"), param.getState()));
				}
				if (param.getSubmitStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.beginOfDay(param.getSubmitStartTime())));
				}
				if (param.getSubmitEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.endOfDay(param.getSubmitEndTime())));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<WithdrawRecord> result = withdrawRecordRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("submitTime"))));
		PageResult<WithdrawRecordVO> pageResult = new PageResult<>(WithdrawRecordVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@ParamValid
	@Transactional
	public void startWithdrawWithBankCard(StartWithdrawParam param) {
		BankCard bankCard = bankCardRepo.findByIdAndUserAccountId(param.getBankCardId(), param.getUserAccountId());
		if (bankCard == null) {
			throw new BizException(BizError.银行卡未绑定无法进行提现);
		}
		UserAccount userAccount = userAccountRepo.getOne(param.getUserAccountId());
		if (!new BCryptPasswordEncoder().matches(param.getMoneyPwd(), userAccount.getMoneyPwd())) {
			throw new BizException(BizError.资金密码不正确);
		}
		WithdrawSetting withdrawSetting = withdrawSettingRepo.findTopByOrderByLatelyUpdateTime();
		List<WithdrawRecord> withdrawRecords = withdrawRecordRepo
				.findByUserAccountIdAndSubmitTimeGreaterThanEqualAndSubmitTimeLessThanEqual(userAccount.getId(),
						DateUtil.beginOfDay(new Date()), DateUtil.endOfDay(new Date()));
		if (withdrawRecords.size() >= withdrawSetting.getEverydayWithdrawTimesUpperLimit()) {
			throw new BizException(BizError.业务异常.getCode(),
					"每日提现次数为" + withdrawSetting.getEverydayWithdrawTimesUpperLimit() + "次,你已达到上限");
		}
		if (param.getWithdrawAmount() < withdrawSetting.getWithdrawLowerLimit()) {
			throw new BizException(BizError.业务异常.getCode(),
					"最低提现金额不能小于" + new DecimalFormat("###################.###########")
							.format(withdrawSetting.getWithdrawLowerLimit()));
		}
		if (param.getWithdrawAmount() > withdrawSetting.getWithdrawUpperLimit()) {
			throw new BizException(BizError.业务异常.getCode(),
					"最高提现金额不能大于" + new DecimalFormat("###################.###########")
							.format(withdrawSetting.getWithdrawUpperLimit()));
		}
		double cashDeposit = NumberUtil.round(userAccount.getCashDeposit() - param.getWithdrawAmount(), 4)
				.doubleValue();
		if (cashDeposit < 0) {
			throw new BizException(BizError.账户余额余额不足);
		}

		WithdrawRecord withdrawRecord = param.convertToPo();
		withdrawRecord.setBankInfo(bankCard);
		withdrawRecordRepo.save(withdrawRecord);
		userAccount.setCashDeposit(cashDeposit);
		userAccountRepo.save(userAccount);
		accountChangeLogRepo.save(AccountChangeLog.buildWithStartWithdraw(userAccount, withdrawRecord));
	}

	@ParamValid
	@Transactional
	public void startWithdrawWithVirtualWallet(StartWithdrawParam param) {
		VirtualWallet virtualWallet = virtualWalletRepo.findByIdAndUserAccountId(param.getVirtualWalletId(),
				param.getUserAccountId());
		if (virtualWallet == null) {
			throw new BizException(BizError.电子钱包未绑定无法进行提现);
		}
		UserAccount userAccount = userAccountRepo.getOne(param.getUserAccountId());
		if (!new BCryptPasswordEncoder().matches(param.getMoneyPwd(), userAccount.getMoneyPwd())) {
			throw new BizException(BizError.资金密码不正确);
		}
		WithdrawSetting withdrawSetting = withdrawSettingRepo.findTopByOrderByLatelyUpdateTime();
		List<WithdrawRecord> withdrawRecords = withdrawRecordRepo
				.findByUserAccountIdAndSubmitTimeGreaterThanEqualAndSubmitTimeLessThanEqual(userAccount.getId(),
						DateUtil.beginOfDay(new Date()), DateUtil.endOfDay(new Date()));
		if (withdrawRecords.size() >= withdrawSetting.getEverydayWithdrawTimesUpperLimit()) {
			throw new BizException(BizError.业务异常.getCode(),
					"每日提现次数为" + withdrawSetting.getEverydayWithdrawTimesUpperLimit() + "次,你已达到上限");
		}
		if (param.getWithdrawAmount() < withdrawSetting.getWithdrawLowerLimit()) {
			throw new BizException(BizError.业务异常.getCode(),
					"最低提现金额不能小于" + new DecimalFormat("###################.###########")
							.format(withdrawSetting.getWithdrawLowerLimit()));
		}
		if (param.getWithdrawAmount() > withdrawSetting.getWithdrawUpperLimit()) {
			throw new BizException(BizError.业务异常.getCode(),
					"最高提现金额不能大于" + new DecimalFormat("###################.###########")
							.format(withdrawSetting.getWithdrawUpperLimit()));
		}
		double cashDeposit = NumberUtil.round(userAccount.getCashDeposit() - param.getWithdrawAmount(), 4)
				.doubleValue();
		if (cashDeposit < 0) {
			throw new BizException(BizError.账户余额余额不足);
		}

		WithdrawRecord withdrawRecord = param.convertToPo();
		withdrawRecord.setVirtualWalletInfo(virtualWallet);
		withdrawRecordRepo.save(withdrawRecord);
		userAccount.setCashDeposit(cashDeposit);
		userAccountRepo.save(userAccount);
		accountChangeLogRepo.save(AccountChangeLog.buildWithStartWithdraw(userAccount, withdrawRecord));
	}

	@Transactional(readOnly = true)
	public PageResult<WithdrawRecordVO> findLowerLevelWithdrawRecordByPage(
			LowerLevelWithdrawRecordQueryCondParam param) {
		UserAccount currentAccount = userAccountRepo.getOne(param.getCurrentAccountId());
		UserAccount lowerLevelAccount = currentAccount;
		if (StrUtil.isNotBlank(param.getUserName())) {
			lowerLevelAccount = userAccountRepo.findByUserNameAndDeletedFlagIsFalse(param.getUserName());
			if (lowerLevelAccount == null) {
				throw new BizException(BizError.用户名不存在);
			}
			// 说明该用户名对应的账号不是当前账号的下级账号
			if (!lowerLevelAccount.getAccountLevelPath().startsWith(currentAccount.getAccountLevelPath())) {
				throw new BizException(BizError.不是上级账号无权查看该账号及下级的提现记录);
			}
		}
		String lowerLevelAccountLevelPath = lowerLevelAccount.getAccountLevelPath();

		Specification<WithdrawRecord> spec = new Specification<WithdrawRecord>() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<WithdrawRecord> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.like(root.join("userAccount", JoinType.INNER).get("accountLevelPath"),
						lowerLevelAccountLevelPath + "%"));
				if (StrUtil.isNotEmpty(param.getAccountType())) {
					predicates.add(builder.equal(root.join("userAccount", JoinType.INNER).get("accountType"),
							param.getAccountType()));
				}
				if (StrUtil.isNotBlank(param.getState())) {
					predicates.add(builder.equal(root.get("state"), param.getState()));
				}
				if (param.getSubmitStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.beginOfDay(param.getSubmitStartTime())));
				}
				if (param.getSubmitEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.endOfDay(param.getSubmitEndTime())));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<WithdrawRecord> result = withdrawRecordRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("submitTime"))));
		PageResult<WithdrawRecordVO> pageResult = new PageResult<>(WithdrawRecordVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

}
