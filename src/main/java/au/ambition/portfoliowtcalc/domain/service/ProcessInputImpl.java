package au.ambition.portfoliowtcalc.domain.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import au.ambition.portfoliowtcalc.constants.Constants;
import au.ambition.portfoliowtcalc.domain.model.FundDTO;
import au.ambition.portfoliowtcalc.exception.InputValidationException;
import au.ambition.portfoliowtcalc.util.CommonUtility;

/**
 * @author Amol Kshirsagar
 *
 */
public class ProcessInputImpl implements ProcessInput{
	Map<String, List<String>> baseFundMarketValue = new LinkedHashMap<>();
	@Override
	public Map<String,Object> createDTOsFromInput(String fileLocation) throws InputValidationException {
		File file = new File(fileLocation);
		List<FundDTO> fundDtos = new ArrayList<FundDTO>();
		Map<String,String> allChildren = new HashMap<String,String>();
	    try {
	        Scanner sc = new Scanner(file);
	        int lineNumber = 0;
	        while (sc.hasNextLine()) {
	            String line = sc.nextLine();
	            List<String> items = Arrays.asList(line.split(","));
	            if(!CommonUtility.isNumeric(items.get(items.size()-1))){
	            	throw new InputValidationException(String.format(Constants.MSG_NON_NUMERIC_MARKET_VALUE,Constants.STR_EMPTY+(lineNumber+1),items.get(0)));
	            }
	            if(!CommonUtility.isEmpty(items)){
	            	String baseFund = items.get(0);
	            	Optional<FundDTO> optionalFundDTO = CommonUtility.containsBaseFund(fundDtos,baseFund);
	            	boolean dtoWithBaseFundAlreadyExists=optionalFundDTO.isPresent();
	            	FundDTO fundDTO = optionalFundDTO.orElse(new FundDTO());
	            	fundDTO.setBaseFund(baseFund);
	            	List<String> children = items.subList(1, items.size()-1);
	            	List<FundDTO> childrenFundDTOs = new ArrayList<>();
	            	children.stream().forEach(c -> {
	            		FundDTO fund = new FundDTO(c);
	            		fund.setParentFund(items.get(0));
	            		fund.setMarketValue(Double.parseDouble(items.get(items.size()-1)));
	            		childrenFundDTOs.add(fund);
	            		allChildren.put(c,items.get(0));
	            	});
	            	if(!CommonUtility.isEmpty(fundDTO.getChildrenFunds())){
            			fundDTO.getChildrenFunds().addAll(childrenFundDTOs);
            		}
            		else{
            			fundDTO.setChildrenFunds(childrenFundDTOs);
            		}
	            	if(!dtoWithBaseFundAlreadyExists)
	            		fundDtos.add(fundDTO);
	            }
	            lineNumber++;
	        }
	        fundDtos.stream().forEach(f -> {
	        	String parent = allChildren.get(f.getBaseFund());
	        	if(parent!=null)
	        		f.setParentFund(parent);
	        	else{
	        		f.setRoot(true);
	        	}
	        });
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    Comparator<String> strcmp = Comparator.nullsFirst(Comparator.naturalOrder());
	    Comparator<FundDTO> cmp = Comparator.comparing(FundDTO::getParentFund, strcmp)
	                                       .thenComparing(FundDTO::getParentFund, strcmp);
	    Collections.sort(fundDtos, cmp);
	    Map<String,Object> result = new HashMap<String,Object>();
	    result.put(Constants.STR_FUND_IDENTIFIER, fundDtos);
	    result.put(Constants.STR_CHILDREN_IDENTIFIER, allChildren);
		return result;
	}

	@Override
	public void arrangeFundMarketValues(List<FundDTO> fundDtos) {
		for (FundDTO fundDTO : fundDtos) {
			if(fundDTO.getChildrenFunds()!=null){
				StringBuilder strBuilderValue = new StringBuilder();
				StringBuilder strBuilder = new StringBuilder();
				for(int i=0;i<fundDTO.getChildrenFunds().size();i++){
					if(i>0){
					strBuilder.append("+");
					}
					FundDTO childfundDTO = fundDTO.getChildrenFunds().get(i);
					List<FundDTO> checkedFundDTOs = checkIfChildIsSomeOnesParentAndReturnGrandChildren(childfundDTO,fundDtos);
					if(!CommonUtility.isEmpty(checkedFundDTOs)){
						int index = 0;
						for(FundDTO fund : checkedFundDTOs){
						if(index>0){
							strBuilder.append("+");
						}
						if(strBuilderValue.length()>0){
						strBuilderValue.append("+");
						}
						strBuilderValue.append(fund.getMarketValue());
						strBuilder.append(fund.getBaseFund());
						index++;
						}
					}
					else{
						if(i>0){
							strBuilderValue.append("+");
							}
						strBuilderValue.append(fundDTO.getChildrenFunds().get(i).getMarketValue());
						strBuilder.append(fundDTO.getChildrenFunds().get(i).getBaseFund());
					}
				};
				List<String> marketValueAndNumberOfChildren = new ArrayList<>();
				marketValueAndNumberOfChildren.add(strBuilderValue.toString());
				baseFundMarketValue.put(fundDTO.getBaseFund()+"="+strBuilder.toString(), marketValueAndNumberOfChildren);
				arrangeFundMarketValues(fundDTO.getChildrenFunds());
			}
		}
	}
	private List<FundDTO> checkIfChildIsSomeOnesParentAndReturnGrandChildren(FundDTO childfundDTO, List<FundDTO> fundDtos) {
		List<FundDTO> grandChildren = new ArrayList<>();
		for (FundDTO fundDTO : fundDtos) {
			if(childfundDTO.getBaseFund().equals(fundDTO.getParentFund())){
				grandChildren.add(fundDTO);
			}
			for(FundDTO childDTO : fundDTO.getChildrenFunds()){
				if(childfundDTO.getBaseFund().equals(childDTO.getParentFund())){
					grandChildren.add(childDTO);
				}
			}
		}
		return grandChildren;
	}

	public Map<String, List<String>> getBaseFundMarketValue() {
		return baseFundMarketValue;
	}
	public void setBaseFundMarketValue(Map<String, List<String>> baseFundMarketValue) {
		this.baseFundMarketValue = baseFundMarketValue;
	}

	@Override
	public void calculateAndPrintfundWeightWithinPortfolio(Map<String, List<String>> marketValueMap) {
		marketValueMap.forEach((k, v) -> {
			/*System.out.println("Fund : " + k +","+ "Value : " + v.get(0));*/
			List<String> lhsRhs = Arrays.asList(k.split("="));
			String lhs = lhsRhs.get(0);
			String rhs = lhsRhs.get(1);
			List<String> rhsElements = Arrays.asList(rhs.split("\\"+Constants.STR_ADDITION));
			List<String> marketValues = Arrays.asList(v.get(0).split("\\"+Constants.STR_ADDITION));
			Double sum = 0.0;
			for (int i = 0; i < marketValues.size(); i++) {
				sum+=Double.parseDouble(marketValues.get(i));
			}
			for(int i=0;i<rhsElements.size();i++){
				System.out.println(lhs+","+rhsElements.get(i)+","+(Double.parseDouble(marketValues.get(i)))/(sum));
			}
		});
	}
}
