package au.ambition.portfoliowtcalc.domain.service;

import java.util.List;
import java.util.Map;

import au.ambition.portfoliowtcalc.domain.model.FundDTO;
import au.ambition.portfoliowtcalc.exception.InputValidationException;

/**
 * @author Amol Kshirsagar
 *
 */
public interface ProcessInput {
	public Map<String, Object> createDTOsFromInput(String text) throws InputValidationException;
	public void arrangeFundMarketValues(List<FundDTO> fundDtos);
	public Map<String, List<String>> getBaseFundMarketValue();
	public void calculateAndPrintfundWeightWithinPortfolio(Map<String, List<String>> marketValueMap);
}
