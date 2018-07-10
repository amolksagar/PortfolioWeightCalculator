package au.ambition.portfoliowtcalc.util;

import java.util.List;

import java.util.function.Predicate;
import au.ambition.portfoliowtcalc.domain.model.FundDTO;

/**
 * @author Amol Kshirsagar
 *
 */
public class FundPredicate
{
	public static Predicate<FundDTO> isParentNode(List<String> children) {
	    return p -> (!children.contains(p.getParentFund()));
	}
	
	public static Predicate<FundDTO> isChildNode(List<String> children) {
	    return p -> (children.contains(p.getParentFund()));
	}
}  