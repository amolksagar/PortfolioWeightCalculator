package au.ambition.portfoliowtcalc.domain.model;

import java.util.List;

/**
 * @author Amol Kshirsagar
 *
 */
public class FundDTO{
	private String baseFund;
	private String parentFund;
	private List<FundDTO> childrenFunds;
	private Double marketValue;
	private boolean isRoot;
	
    public FundDTO(String baseFund) {
    	this.baseFund = baseFund;
    }	
	
	public FundDTO() {
	}

	public String getParentFund() {
		return parentFund;
	}
	public void setParentFund(String parentFund) {
		this.parentFund = parentFund;
	}
	public List<FundDTO> getChildrenFunds() {
		return childrenFunds;
	}
	public void setChildrenFunds(List<FundDTO> childrenFunds) {
		this.childrenFunds = childrenFunds;
	}
	public Double getMarketValue() {
		return marketValue;
	}
	public void setMarketValue(Double marketValue) {
		this.marketValue = marketValue;
	}
	public boolean isRoot() {
		return isRoot;
	}
	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}
	public String getBaseFund() {
		return baseFund;
	}
	public void setBaseFund(String baseFund) {
		this.baseFund = baseFund;
	}
}
