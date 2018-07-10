package au.ambition.portfoliowtcalc.util;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import au.ambition.portfoliowtcalc.domain.model.FundDTO;

/**
 * @author Amol Kshirsagar
 *
 */
public class CommonUtility {
	public static boolean isEmpty(Collection<?> collection) {
	    return collection == null || collection.isEmpty();
	}
	public static boolean isEmpty(String[] array) {
	    return (array == null || array.length==0);
	}
	
	public static boolean isNumeric(String str) {
	  try  {
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe) { 
	    return false;  
	  }  
	  return true;  
	}
	
	public static Optional<FundDTO> containsBaseFund(final List<FundDTO> list, final String fundName){
	    return list.stream().filter(o -> o.getBaseFund().equals(fundName)).findFirst();
	}
}
