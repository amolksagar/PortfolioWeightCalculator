package au.ambition.portfoliowtcalc;

import java.util.List;
import java.util.Map;

import au.ambition.portfoliowtcalc.constants.Constants;
import au.ambition.portfoliowtcalc.domain.model.FundDTO;
import au.ambition.portfoliowtcalc.domain.service.ProcessInput;
import au.ambition.portfoliowtcalc.domain.service.ProcessInputImpl;
import au.ambition.portfoliowtcalc.exception.InputValidationException;
import au.ambition.portfoliowtcalc.util.CommonUtility;

/**
 * @author Amol Kshirsagar
 *
 */
public class PortfolioWtCalcLauncher {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InputValidationException {
		if(CommonUtility.isEmpty(args) || args.length>1){
			throw new InputValidationException(Constants.MSG_NO_INPUT_PROVIDED);
		}
		ProcessInput processInput = new ProcessInputImpl();
		Map<String, Object> processedResult = processInput.createDTOsFromInput(args[0]);
		
		List<FundDTO> fundDTOs = (List<FundDTO>) processedResult.get(Constants.STR_FUND_IDENTIFIER);
		processInput.arrangeFundMarketValues(fundDTOs);
		
		Map<String, List<String>> marketValueMap = processInput.getBaseFundMarketValue();
		processInput.calculateAndPrintfundWeightWithinPortfolio(marketValueMap);
	}
}
